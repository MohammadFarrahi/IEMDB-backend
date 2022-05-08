package ie.iemdb.repository;


import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;

import java.util.ArrayList;


public class Retriever {
    public ArrayList<Actor> getCastForMovie(int movieId) {
        return ActorRepo.getInstance().getCastForMovie(movieId);
    }

    public ArrayList<Movie> getMoviesForActor(int actorId) {
        return MovieRepo.getInstance().getMoviesForActor(actorId);
    }

    public ArrayList<Movie> getWatchlistForUser(String username) {
        return MovieRepo.getInstance().getWatchlistForUser(username);
    }

    public Movie getMovieForComment(int commentId){
        return MovieRepo.getInstance().getMovieForComment(commentId);
    }

    public User getUserForComment(int commentId){
        return UserRepo.getInstance().getUserForComment(commentId);
    }
}
