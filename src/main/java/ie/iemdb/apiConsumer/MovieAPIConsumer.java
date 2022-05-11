package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import ie.iemdb.exception.ActorNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.MovieAlreadyExistsException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;
import ie.iemdb.repository.ActorRepo;
import ie.iemdb.repository.MovieRepo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MovieAPIConsumer extends APIConsumer {

    public MovieAPIConsumer(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected void loadRepo(JsonNode arrayNode) {
        try {
            var repo = MovieRepo.getInstance();
            for (var node : arrayNode) {
                try{
                    var newMovie = makeNewMovie(node);
                    System.out.println(newMovie.getName());
                    repo.addElement(newMovie);
                }catch (ActorNotFoundException | SQLException e){
                    e.printStackTrace();
                }
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private Movie makeNewMovie(JsonNode node) throws ObjectNotFoundException, SQLException {
        Integer id = node.get("id").asInt();
        String name = node.get("name").asText();
        String summary = node.get("summary").asText();
        String director = node.get("director").asText();
        String releaseDate = LocalDate.parse(node.get("releaseDate").asText(), DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString();
        Integer ageLimit = node.get("ageLimit").asInt();
        Integer duration = node.get("duration").asInt();
        Double imdbRate = node.get("imdbRate").asDouble();
        String coverImgUrl = node.get("coverImage").asText();
        String imgUrl = node.get("image").asText();

        ArrayList<Actor> cast = getCastsForMovie(getIntegerCollection(node.get("cast")));
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

    private ArrayList<Actor> getCastsForMovie(ArrayList<Integer> ids) throws ObjectNotFoundException, SQLException {
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
