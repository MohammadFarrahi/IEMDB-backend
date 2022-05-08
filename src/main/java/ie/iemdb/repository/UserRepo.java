package ie.iemdb.repository;

import ie.iemdb.exception.AgeLimitException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.MovieNotFoundException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class UserRepo extends Repo<User, String> {
    private static UserRepo instance = null;

    public static User loggedInUser = null;

    public static UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    private UserRepo() {
        this.notFoundException = new UserNotFoundException();
    }

    @Override
    protected String getGetElementByIdStatement() {
        return null;
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, String id) {

    }

    @Override
    protected String getGetAllElementsStatement() {
        return null;
    }

    @Override
    protected User convertResultSetToDomainModel(ResultSet rs) {
        return null;
    }

    @Override
    protected ArrayList<User> convertResultSetToDomainModelList(ResultSet rs) {
        return null;
    }

    @Override
    public void addElement(User newObject) throws CustomException {
        var objectId = newObject.getId();
        if (isIdValid(objectId)) {
            throw this.notFoundException;
        }
        this.objectMap.put(objectId, newObject);
        return;
    }

    @Override
    public void updateElement(User newObject) throws CustomException {
        var objectId = newObject.getId();
        if (!isIdValid(objectId)) {
            throw this.notFoundException;
        }
        objectMap.put(objectId, newObject);
    }

    public List<Movie> getWatchList(String userId) throws CustomException {
        return getElementById(userId).getWatchList();
    }

    public void addToWatchList(User user, Movie movie) throws AgeLimitException {
        if (!user.isOlderThan(movie.getAgeLimit()))
            throw new AgeLimitException();

        user.addToWatchList(movie);
    }

    public void removeFromWatchList(User user, String movieId) throws MovieNotFoundException {
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
