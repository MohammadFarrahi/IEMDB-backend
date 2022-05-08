package ie.iemdb.repository;


import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class Retriever {
    public List<Actor> getCastForMovie(int movieId) {
        return ActorRepo.getInstance().getCastForMovie(movieId);
    }

    public List<Movie> getMoviesForActor(int actorId) {
        return MovieRepo.getInstance().getMoviesForActor(actorId);
    }

    public List<Movie> getWatchlistForUser(String username) {
        return MovieRepo.getInstance().getWatchlistForUser(username);
    }


}
