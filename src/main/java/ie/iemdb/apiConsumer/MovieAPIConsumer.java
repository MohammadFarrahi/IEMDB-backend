package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;
import ie.iemdb.repository.ActorRepo;
import ie.iemdb.repository.MovieRepo;

import java.util.ArrayList;

public class MovieAPIConsumer extends APIConsumer {

    MovieAPIConsumer(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected void loadRepo(JsonNode arrayNode) {
        try {
            var repo = MovieRepo.getInstance();
            for (var node : arrayNode) {
                var newMovie = makeNewMovie(node);
                repo.addElement(newMovie);
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private Movie makeNewMovie(JsonNode node) throws ObjectNotFoundException {
        String id = node.get("id").asText();
        String name = node.get("name").asText();
        String summary = node.get("summary").asText();
        String director = node.get("director").asText();
        String releaseDate = node.get("releaseDate").asText();
        Integer ageLimit = node.get("ageLimit").asInt();
        Integer duration = node.get("duration").asInt();
        Double imdbRate = node.get("imdbRate").asDouble();
        String coverImgUrl = node.get("coverImage").asText();
        String imgUrl = node.get("image").asText();

        ArrayList<Actor> cast = getCastsForMovie(getStringCollection(node.get("cast")));
        ArrayList<String> writers = getStringCollection(node.get("writers"));
        ArrayList<String> genres =  getStringCollection(node.get("genres"));

        return new Movie(
                id,
                name,
                summary,
                director,
                cast,
                releaseDate,
                writers,
                genres,
                ageLimit,
                duration,
                imdbRate,
                coverImgUrl,
                imgUrl);
    }

    private ArrayList<Actor> getCastsForMovie(ArrayList<String> ids) throws ObjectNotFoundException {
        var repo = ActorRepo.getInstance();
        return (ArrayList<Actor>) repo.getElementsById(ids);
    }

    private ArrayList<String> getStringCollection(JsonNode node) {
        ArrayList<String> result = new ArrayList<>();
        for (var item : node) {
            result.add(item.asText());
        }
        return result;
    }

    private ArrayList<Integer> getIntegerCollection(JsonNode node) {
        ArrayList<Integer> result = new ArrayList<>();
        for (var item : node) {
            result.add(item.asInt());
        }
        return result;
    }

}
