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
    private static final String USER_TABLE = "User";
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
                                "email VARCHAR(255),\n" +
                                "movieId INT), \n" +
                                "PRIMARY KEY(email, movieId), \n" +
                                "FOREIGN KEY (email) REFERENCES " + USER_TABLE + "(email),\n" +
                                "FOREIGN KEY (email) REFERENCES " + MovieRepo.MOVIE_TABLE + "(id),\n" +
                                ");", WATCH_LIST_TABLE
                )
        );
    }

    @Override
    protected String getGetElementByIdStatement() {
        return String.format("SELECT* FROM %s a WHERE a.id = ?;", USER_TABLE);
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, String id) {
        try {
            st.setString(1, id);
        } catch (SQLException e) {
            //ignore
        }
    }

    @Override
    protected String getGetAllElementsStatement() {
        return String.format("SELECT * FROM %s;", USER_TABLE);
    }

    @Override
    protected User convertResultSetToDomainModel(ResultSet rs) {
        try {
            var newUser = new User(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("nickname"),
                    rs.getString("name"),
                    rs.getString("birthDate")
            );
            newUser.setRetriever(new Retriever());
            return newUser;
        } catch (Exception e) {
            //ignore
        }
        return null;
    }

    @Override
    protected ArrayList<User> convertResultSetToDomainModelList(ResultSet rs) {
        try {
            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(this.convertResultSetToDomainModel(rs));
            }
            return users;
        } catch (SQLException e) {
            //ignore
        }
        return null;
    }

    @Override
    public void addElement(User newObject) throws CustomException {
    }

    private String getAddToWatchListStatement() {
        return String.format("INSERT INTO %s (email, movieId)\n" +
                "VALUES (?, ?);", WATCH_LIST_TABLE);
    }

    private void fillAddToWatchListValues(PreparedStatement st, String userId, Integer movieId) {
        try {
            st.setString(1, userId);
            st.setString(2, movieId.toString());
        } catch (SQLException e) {
            //ignore
        }
    }

    private String getRemoveFromWatchListStatement() {
        return String.format("DELETE FROM %s\n" +
                "WHERE userId=? AND movieId=?;", WATCH_LIST_TABLE);
    }

    private void fillRemoveFromWatchListValues(PreparedStatement st, String userId, Integer movieId) {
        try {
            st.setString(1, userId);
            st.setString(2, movieId.toString());
        } catch (SQLException e) {
            //ignore
        }
    }


    public List<Movie> getWatchList(String userId) throws CustomException {
        return getElementById(userId).getWatchList();
    }

    public void addToWatchList(User user, Movie movie) throws AgeLimitException, SQLException {
        if (!user.isOlderThan(movie.getAgeLimit()))
            throw new AgeLimitException();

        executeUpdate(getAddToWatchListStatement(), List.of(user.getId(), movie.getId().toString()))

        user.addToWatchList(movie);
    }

    public void removeFromWatchList(User user, Integer movieId) throws MovieNotFoundException, SQLException {

        executeUpdate(getRemoveFromWatchListStatement(), List.of(user.getId(), movieId.toString()));

        user.removeFromWatchList(movieId);
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
