package ie.iemdb.repository;

import ie.iemdb.exception.AgeLimitException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;

import kotlin.Pair;

import java.util.*;

public class UserRepo extends Repo<User> {
    private static UserRepo instance = null;

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

    public void addToWatchList(User user, Movie movie) throws CustomException {
        if (!user.isOlderThan(movie.getAgeLimit()))
            throw new AgeLimitException();

        user.addToWatchList(movie);
    }

    public void removeFromWatchList(User user, String movieId) throws CustomException {
        user.removeFromWatchList(movieId);
    }

}
