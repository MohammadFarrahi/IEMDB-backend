package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import ie.iemdb.exception.ActorAlreadyExistsException;
import ie.iemdb.exception.ActorNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Actor;
import ie.iemdb.repository.ActorRepo;

import java.time.format.DateTimeParseException;


public class ActorAPIConsumer extends APIConsumer {
    public ActorAPIConsumer(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected void loadRepo(JsonNode arrayNode) {
        try {
            var repo = ActorRepo.getInstance();
            for (var node : arrayNode) {
                try {
                    var newActor = makeNewActor(node);
                    repo.addElement(newActor);
                } catch (ActorAlreadyExistsException | DateTimeParseException e) {
                    //ignore
                }

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
