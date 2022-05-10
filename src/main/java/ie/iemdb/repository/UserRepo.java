package ie.iemdb.repository;

import ie.iemdb.exception.AgeLimitException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.MovieNotFoundException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserRepo extends Repo<User, String> {
    private static UserRepo instance = null;
    public static final String USER_TABLE = "User";
    private static final String WATCH_LIST_TABLE = "Watchlist";

    public static User loggedInUser = null;

    public static UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    private UserRepo() {
        initUserTable();
        initWatchlistTable();
        this.notFoundException = new UserNotFoundException();
    }

    private void initUserTable() {
        initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "email VARCHAR(255),\n" +
                                "password VARCHAR(255),\n" +
                                "nickname VARCHAR(255),\n" +
                                "name VARCHAR(255),\n" +
                                "birthDate VARCHAR(225),\n" +
                                "PRIMARY KEY(email)" +
                                ");", USER_TABLE
                )
        );
    }

    private void initWatchlistTable() {
        initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s( \n" +
                                "userId VARCHAR(255),\n" +
                                "movieId INT), \n" +
                                "PRIMARY KEY(userId, movieId), \n" +
                                "FOREIGN KEY (userId) REFERENCES " + USER_TABLE + "(email),\n" +
                                "FOREIGN KEY (movieId) REFERENCES " + MovieRepo.MOVIE_TABLE + "(id),\n" +
                                ");", WATCH_LIST_TABLE
                )
        );
    }

    @Override
    protected String getGetElementByIdStatement() {
        return String.format("SELECT* FROM %s a WHERE a.id = ?;", USER_TABLE);
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, String id) throws SQLException {
            st.setString(1, id);
    }

    @Override
    protected String getGetAllElementsStatement() {
        return String.format("SELECT * FROM %s;", USER_TABLE);
    }

    @Override
    protected User convertResultSetToDomainModel(ResultSet rs) throws SQLException, CustomException {
            var newUser = new User(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("nickname"),
                    rs.getString("name"),
                    rs.getString("birthDate")
            );
            newUser.setRetriever(new Retriever());
            return newUser;
    }

    @Override
    protected ArrayList<User> convertResultSetToDomainModelList(ResultSet rs) throws SQLException, CustomException {
            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(this.convertResultSetToDomainModel(rs));
            }
            return users;
    }
    @Override
    protected String getAddElementStatement() {
        return String.format("INSERT INTO %s\n" +
                "VALUES (?, ?, ?, ?, ?);", WATCH_LIST_TABLE);
    }

    @Override
    public void addElement(User newObject) throws SQLException {
        var tupleMap = newObject.getDBTuple();
        executeUpdate(getAddElementStatement(), List.of(
                tupleMap.get("email"),
                tupleMap.get("password"),
                tupleMap.get("nickname"),
                tupleMap.get("name"),
                tupleMap.get("birthDate")
        ));
    }

    private String getAddToWatchListStatement() {
        return String.format("INSERT INTO %s (email, movieId)\n" +
                "VALUES (?, ?);", WATCH_LIST_TABLE);
    }

    private String getRemoveFromWatchListStatement() {
        return String.format("DELETE FROM %s\n" +
                "WHERE userId=? AND movieId=?;", WATCH_LIST_TABLE);
    }


    public List<Movie> getWatchList(String userId) throws CustomException, SQLException {
        return getElementById(userId).getWatchList();
    }

    public void addToWatchList(User user, Movie movie) throws AgeLimitException, SQLException {
        if (!user.isOlderThan(movie.getAgeLimit()))
            throw new AgeLimitException();

        executeUpdate(getAddToWatchListStatement(), List.of(user.getId(), movie.getId().toString()));

        user.addToWatchList(movie);
    }

    public void removeFromWatchList(User user, Integer movieId) throws MovieNotFoundException, SQLException {

        executeUpdate(getRemoveFromWatchListStatement(), List.of(user.getId(), movieId.toString()));

        user.removeFromWatchList(movieId);
    }

    public List<Integer> getMovieIdsForUserWatchList(String userId) throws SQLException {
        List<Integer> list = new ArrayList<>();
        String sql = String.format(
                "SELECT movieId\n" +
                        "FROM %s\n" +
                        "WHERE email=?;", userId);
        var res = executeQuery(sql, List.of(userId));
        while(res.next()){
            list.add(res.getInt("movieId"));
        }
        return list;
    }

    public void loginUser(User user) {
        loggedInUser = user;
    }

    public void logoutUser() {
        loggedInUser = null;
    }

    public User getUserForComment(int commentId) {
        return null;
    }
}
