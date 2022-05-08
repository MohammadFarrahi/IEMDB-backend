package ie.iemdb.repository;


import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class Retriever {
    public List<Actor> getCastForMovie(int movieId) {
        List<Actor> casts = new ArrayList<>();
        try {
            casts = ActorRepo.getInstance().getCastForMovie(movieId);
            return casts;
        } catch (Exception e) {
            //ignore
        }
        return casts;
    }

    public List<Movie> getMoviesForActor(int actorId) {
        List<Movie> movies = new ArrayList<>();
        try {
            movies = MovieRepo.getInstance().getMoviesForActor(actorId);
            return movies;
        } catch (Exception e) {
            //ignore
        }
        return movies;
    }

    public List<Movie> getWatchlistForUser(String username) {
        List<Movie> movies = new ArrayList<>();
        try {
            movies = MovieRepo.getInstance().getWatchlistForUser(username);
            return movies;
        } catch (Exception e) {
            //ignore
        }
        return movies;
    }


}
