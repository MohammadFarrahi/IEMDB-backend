package ie.iemdb.repository;

import ie.iemdb.exception.*;
import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;
import ie.iemdb.util.types.Constant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.util.stream.Collectors;

public class MovieRepo extends Repo<Movie, Integer> {
    private static MovieRepo instance = null;
    public static final String CAST_TABLE = "Casts";
    public static final String MOVIE_TABLE = "Movie";
    public static final String GENRE_TABLE = "Genre";
    public static final String RATE_TABLE = "MovieRate";

    private void initMovieTable() {
        this.initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(id INTEGER," +
                                "\nname VARCHAR(225)," +
                                "\nsummary VARCHAR(1024)," +
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
                                "\nFOREIGN KEY (movieId) REFERENCES " + MOVIE_TABLE + "(id) ON DELETE CASCADE," +
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
        this.initMovieTable();
        this.initGenreTable();
        this.initCastTable();
        this.initMovieRateTable();
        UserRepo.getInstance().initWatchlistTable();
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
        var dbOutput = executeQuery(sql, List.of(movieId.toString()));
        var res = dbOutput.getFirst();
        while (res.next()) {
            genres.add(res.getString("genre"));
        }
        finishWithResultSet(dbOutput.getSecond());
        return genres;
    }

    private HashMap<String, Integer> getUserRateMap(Integer movieId) throws SQLException {
        var hashMap = new HashMap<String, Integer>();
        String sql = String.format("SELECT userId, rate FROM %s WHERE movieId=?;", RATE_TABLE);
        var dbOutput = executeQuery(sql, List.of(movieId.toString()));
        var res = dbOutput.getFirst();
        while (res.next()) {
            hashMap.put(res.getString("userId"), res.getInt("rate"));
        }
        finishWithResultSet(dbOutput.getSecond());
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
        return String.format("INSERT IGNORE INTO %s\n" +
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
                tupleMap.get("releaseDate"),
                tupleMap.get("director"),
                tupleMap.get("writers"),
                tupleMap.get("imdbRate"),
                tupleMap.get("duration"),
                tupleMap.get("ageLimit"),
                tupleMap.get("coverImgUrl"),
                tupleMap.get("imgUrl")
        ));
        System.out.println(tupleMap.get("name"));

        addMovieGenres(newObject.getId(), newObject.getGenres());
        addMovieCast(newObject.getId(), newObject.getCast());

    }

    private void addMovieCast(Integer movieId, ArrayList<Actor> cast) throws SQLException {
        var sql = String.format(
                "INSERT IGNORE INTO %s(movieId, actorId)\nVALUES (?,?);", CAST_TABLE
        );
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(sql);
        for(var actor : cast) {
            fillValues(st, List.of(movieId.toString(), actor.getId().toString()));
            st.executeUpdate();
        }
        st.close();
        con.close();

        System.out.println("cast added");
    }

    private void addMovieGenres(Integer movieId, ArrayList<String> genres) throws SQLException {
        var sql = String.format(
                "INSERT IGNORE INTO %s(movieId, genre)\nVALUES (?,?);", GENRE_TABLE
        );
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(sql);
        for(var genre : genres) {
            fillValues(st, List.of(movieId.toString(), genre));
            st.executeUpdate();
        }
        st.close();
        con.close();

        System.out.println("genres added");
    }


    public ArrayList<Movie> getFilteredElementsByGenre(String genre) throws SQLException {
        String sql = String.format("""
                        SELECT *
                        FROM %s
                        WHERE id IN (
                            Select movieId
                            FROM %s
                            WHERE genre=?);
                        """, MOVIE_TABLE, GENRE_TABLE);
        var res = executeQuery(sql, List.of(genre));
        var movies = convertResultSetToDomainModelList(res.getFirst());
        finishWithResultSet(res.getSecond());
        return movies;
    }

    public ArrayList<Movie> getFilteredElementsByName(String name) throws SQLException {
//        String sql = String.format("""
//                        SELECT *
//                        FROM %s
//                        WHERE name LIKE '%%%s%%';
//                        """, MOVIE_TABLE, name);
        String sql = String.format("""
                        SELECT *
                        FROM %s
                        WHERE name LIKE ?;
                        """, MOVIE_TABLE);
        var res = executeQuery(sql, List.of("%" + name + "%"));
        var movies = convertResultSetToDomainModelList(res.getFirst());
        finishWithResultSet(res.getSecond());
        return movies;
    }

    public ArrayList<Movie> getFilteredElementsByYear(int from, int to) throws SQLException {
        String sql = String.format("""
                        SELECT *
                        FROM %s
                        WHERE releaseDate>'%s-01-01' AND releaseDate<'%s-12-29';
                        """, MOVIE_TABLE, String.valueOf(from), String.valueOf(to));
        var res = executeQuery(sql, List.of());
        var movies = convertResultSetToDomainModelList(res.getFirst());
        finishWithResultSet(res.getSecond()) ;
        return movies;
    }

    public ArrayList<Movie> getMoviesForActor(int actorId) throws SQLException, ObjectNotFoundException {
        String sql = String.format(
                "SELECT movieId\n" +
                        "FROM %s\n" +
                        "WHERE actorId=?;", CAST_TABLE
        );
        var res = executeQuery(sql, List.of(String.valueOf(actorId)));
        var movieIds = new ArrayList<Integer>(); var rs = res.getFirst();
        while (rs.next()) {
            movieIds.add(rs.getInt("movieId"));
        }
        var movies = getElementsById(movieIds);
        finishWithResultSet(res.getSecond());
        return (ArrayList)movies;
    }

    public ArrayList<Movie> getWatchlistForUser(String userId) throws SQLException {
        var movieIds = UserRepo.getInstance().getMovieIdsForUserWatchList(userId);
        try {
            return (ArrayList<Movie>) getElementsById(movieIds);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Movie getMovieForComment(int commentId) throws SQLException {
        Integer movieId = CommentRepo.getInstance().getMovieIdForComment(commentId);
        try {
            return getElementById(movieId);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void rateMovie(Integer movieId, String userEmail, int rate) throws CustomException, SQLException {
        String sql = String.format(
                "INSERT INTO %s(movieId, userId, rate)\n" +
                        "VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE\n" +
                        "rate=?;", RATE_TABLE);
        executeUpdate(sql, List.of(movieId.toString(), userEmail, String.valueOf(rate), String.valueOf(rate)));

        System.out.println("rate added " + userEmail + " " +  String.valueOf(rate));
    }

    public List<Integer> getCastIdsForMovie(Integer movieId) throws SQLException {
        String sql = String.format(
                "SELECT actorId\n" +
                        "FROM %s\n" +
                        "WHERE movieId=?;", CAST_TABLE
        );
        var res = executeQuery(sql, List.of(String.valueOf(movieId)));
        List<Integer> castIds = new ArrayList<>(); var rs = res.getFirst();
        while (rs.next()) {
            castIds.add(rs.getInt("actorId"));
        }
        finishWithResultSet(res.getSecond());
        return castIds;
    }
}
