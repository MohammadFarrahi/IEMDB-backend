package ie.iemdb.repository;


import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.Actor;
import ie.iemdb.model.Comment;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;

import java.sql.SQLException;
import java.util.ArrayList;


public class Retriever {
    public ArrayList<Actor> getCastForMovie(int movieId) throws SQLException {
        return ActorRepo.getInstance().getCastForMovie(movieId);
    }

    public ArrayList<Movie> getMoviesForActor(int actorId) throws SQLException, ObjectNotFoundException {
        return MovieRepo.getInstance().getMoviesForActor(actorId);
    }

    public ArrayList<Movie> getWatchlistForUser(String username) throws SQLException {
        return MovieRepo.getInstance().getWatchlistForUser(username);
    }

    public Movie getMovieForComment(int commentId) throws SQLException {
        return MovieRepo.getInstance().getMovieForComment(commentId);
    }

    public User getUserForComment(int commentId) throws SQLException {
        return UserRepo.getInstance().getUserForComment(commentId);
    }

    public ArrayList<Comment> getCommentsForMovie(Integer movieId) throws SQLException {
        return CommentRepo.getInstance().getCommentsForMovie(movieId);
    }
}
