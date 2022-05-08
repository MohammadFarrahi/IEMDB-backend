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


}
