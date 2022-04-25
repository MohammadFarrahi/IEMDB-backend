package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Actor;
import ie.iemdb.model.Movie;
import ie.iemdb.repository.ActorRepo;
import org.jsoup.Jsoup;


import ie.iemdb.util.types.Constant;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ActorAPIConsumer {

    public void importData() throws IOException {
        var jsonData = getJsonData();
        var nodeArray = getJsonNode(jsonData);


    }

    private String getJsonData() throws IOException {
        return Jsoup.connect(Constant.FetchApiUrl.BASE + Constant.FetchApiUrl.ACTOR).ignoreContentType(true).execute().body();
    }

    private JsonNode getJsonNode(String jsonData) throws JsonProcessingException {
        return new ObjectMapper().convertValue(jsonData, JsonNode.class);
    }

    private void loadRepo(JsonNode arrayNode) {
        try {
            var repo = ActorRepo.getInstance();
            for (var node : arrayNode) {
                var newActor = makeNewActor(node);
                repo.addElement(newActor);
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private Actor makeNewActor(JsonNode node) {
        String id = node.get("id").asText();
        String name = node.get("name").asText();
        String birthDate = node.get("birthDate").asText();
        String nationality = node.get("nationality").asText();
        String imgUrl = node.get("image").asText();
        return new Actor(
                id, name, birthDate, nationality, imgUrl
        );
    }
}
