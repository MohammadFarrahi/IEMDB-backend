package ie.iemdb.repository;

import ie.iemdb.exception.*;
import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;
import ie.iemdb.util.types.Constant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.util.stream.Collectors;

public class MovieRepo extends Repo<Movie, Integer> {
    private static MovieRepo instance = null;
    public static final String CAST_TABLE = "Cast";
    public static final String MOVIE_TABLE = "Movie";
    public static final String GENRE_TABLE = "Genre";
    public static final String RATE_TABLE = "MovieRate";

    private void initMovieTable() {
        this.initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(id INTEGER," +
                                "\nname VARCHAR(225)," +
                                "\nsummary VARCHAR(225)," +
                                "\nreleaseDate VARCHAR(225)," +
                                "\ndirector VARCHAR(225)," +
                                "\nwriters VARCHAR(225)," +
                                "\nimdbRate FLOAT(24)," +
                                "\nduration INTEGER," +
                                "\nageLimit INTEGER," +
                                "\ncoverImgUrl VARCHAR(225)," +
                                "\nimgUrl VARCHAR(225)," +
                                "\nPRIMARY KEY(id));",
                        MOVIE_TABLE
                )
        );
    }

    private void initCastTable() {
        this.initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(movieId INTEGER," +
                                "\nactorId INTEGER," +
                                "\nFOREIGN KEY (actorId) REFERENCES " + ActorRepo.ACTOR_TABLE + "(id)," +
                                "\nFOREIGN KEY (movieId) REFERENCES " + MOVIE_TABLE + "(id)," +
                                "\nPRIMARY KEY(movieId, actorId));",
                        CAST_TABLE
                )
        );
    }

    private void initGenreTable() {
        this.initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(movieId INTEGER," +
                                "\ngenre VARCHAR(225)," +
                                "\nFOREIGN KEY (movieId) REFERENCES " + MOVIE_TABLE + "(id) ON DELETE CASCADE," +
                                "\nPRIMARY KEY(movieId, genre));",
                        GENRE_TABLE
                )
        );
    }

    private void initMovieRateTable() {
        this.initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "movieId INTEGER," +
                                "\nuserId VARCHAR(225)," +
                                "\nrate INT," +
                                "\nFOREIGN KEY (userId) REFERENCES " + UserRepo.USER_TABLE + "(email)," +
                                "\nFOREIGN KEY (movieId) REFERENCES " + MOVIE_TABLE + "(id)," +
                                "\nPRIMARY KEY(movieId, userId));",
                        RATE_TABLE
                )
        );
    }
    public static MovieRepo getInstance() {
        if (instance == null) {
            instance = new MovieRepo();
        }
        return instance;
    }

    private MovieRepo() {
        this.initCastTable();
        this.initGenreTable();
        this.initCastTable();
        this.initMovieRateTable();
        this.notFoundException = new MovieNotFoundException();
    }

    @Override
    protected String getGetElementByIdStatement() {
        return String.format("SELECT* FROM %s movie WHERE movie.id = ?;", MOVIE_TABLE);
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, Integer id) {
        try {
            st.setString(1, String.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getGetAllElementsStatement() {
        return String.format("SELECT * FROM %s;", MOVIE_TABLE);
    }

    private ArrayList<String> getMovieGenres(Integer movieId) throws SQLException {
        var genres = new ArrayList<String>();
        String sql = String.format("SELECT G.genre FROM %s G WHERE G.movieId = ?;", GENRE_TABLE);
        var res = executeQuery(sql, List.of(movieId.toString()));
        while (res.next()) {
            genres.add(res.getString("genre"));
        }
        return genres;
    }

    private HashMap<String, Integer> getUserRateMap(Integer movieId) throws SQLException {
        var hashMap = new HashMap<String, Integer>();
        String sql = String.format("SELECT userId, rate FROM %s WHERE movieId=?;", RATE_TABLE);
        var res = executeQuery(sql, List.of(movieId.toString()));
        while (res.next()) {
            hashMap.put(res.getString("userId"), res.getInt("rate"));
        }
        return hashMap;
    }

    @Override
    protected Movie convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        var movie = new Movie(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("summary"),
                rs.getString("director"),
                rs.getString("releaseDate"),
                new ArrayList<>(Arrays.asList(rs.getString("writers").split(","))),
                getMovieGenres(rs.getInt("id")),
                rs.getInt("ageLimit"),
                rs.getInt("duration"),
                rs.getDouble("imdbRate"),
                rs.getString("coverImgUrl"),
                rs.getString("imgUrl"),
                getUserRateMap(rs.getInt("id"))
        );
        movie.setRetriever(new Retriever());
        return movie;
    }

    @Override
    protected String getAddElementStatement() {
        return String.format("INSERT INTO %s\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", MOVIE_TABLE);
    }

    @Override
    protected ArrayList<Movie> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Movie> movies = new ArrayList<>();
        while (rs.next()) {
            movies.add(this.convertResultSetToDomainModel(rs));
        }
        return movies;
    }

    @Override
    public void addElement(Movie newObject) throws SQLException {
        var tupleMap = newObject.getDBTuple();
        executeUpdate(getAddElementStatement(), List.of(
                tupleMap.get("id"),
                tupleMap.get("name"),
                tupleMap.get("summary"),
                tupleMap.get("director"),
                tupleMap.get("writers"),
                tupleMap.get("releaseDate"),
                tupleMap.get("ageLimit"),
                tupleMap.get("duration"),
                tupleMap.get("imdbRate"),
                tupleMap.get("coverImgUrl"),
                tupleMap.get("imgUrl")
        ));
    }


    public ArrayList<Movie> filterElementsByGenre(String genre, List<Movie> elements) {
        try {
            ArrayList<Movie> filteredList = new ArrayList<>();
            for (var element : elements) {
                if (element.includeGenre(genre))
                    filteredList.add(element);
            }
            return filteredList;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Movie> filterElementsByYear(int from, int to, List<Movie> elements) throws CustomException {
        var filteredList = new ArrayList<Movie>();
        for (var element : elements) {
            if (!element.isCreatedBefore(from) && !element.isCreatedAfter(to)) {
                filteredList.add(element);
            }
        }
        return filteredList;
    }

    public ArrayList<Movie> getMoviesForActor(int actorId) throws SQLException, ObjectNotFoundException {
        String sql = String.format(
                "SELECT movieId\n" +
                        "FROM %s\n" +
                        "WHERE actorId=?;", CAST_TABLE
        );
        var res = executeQuery(sql, List.of(String.valueOf(actorId)));
        return convertResultSetToDomainModelList(res);
    }

    public ArrayList<Movie> getWatchlistForUser(String userId) throws SQLException {
        var movieIds = UserRepo.getInstance().getMovieIdsForUserWatchList(userId);
        try {
            return (ArrayList<Movie>) getElementsById(movieIds);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Movie getMovieForComment(int commentId) {
        return null;
    }

    public void rateMovie(Integer movieId, String userEmail, int rate) throws CustomException, SQLException {
        if (!UserRepo.getInstance().isIdValid(userEmail)) {
            throw new UserNotFoundException();
        }
        String sql = String.format(
                "INSERT INTO %s (movieId, userId, rate\n" +
                        "VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE\n" +
                        "rate=?;", RATE_TABLE);
        executeUpdate(sql, List.of(movieId.toString(), userEmail, String.valueOf(rate), String.valueOf(rate)));
        getElementById(movieId).updateMovieRating(userEmail, rate);
    }

    public List<Integer> getCastIdsForMovie(Integer movieId) {

    }
}
