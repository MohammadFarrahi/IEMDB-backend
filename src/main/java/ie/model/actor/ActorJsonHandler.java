package ie.model.actor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.generic.model.JsonHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActorJsonHandler implements JsonHandler<Actor> {
    private final ObjectMapper mapper;

    public ActorJsonHandler() {
        mapper = new ObjectMapper();
    }
    @Override
    public Actor deserialize(String jsonData) throws JsonProcessingException {
        return mapper.readValue(jsonData, Actor.class);
    }
    @Override
    public String serialize(Actor object, Set<String> notIncludedFields) {
        try {
            var objectNode = (ObjectNode) mapper.valueToTree(object);
            objectNode.remove(notIncludedFields);
            return mapper.writeValueAsString(objectNode);
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public String serialize(List<Actor> objects, Set<String> notIncludedFields) {
        var objectJsonList = new ArrayList<JsonNode>();
        try {
            for (var object : objects) {
                objectJsonList.add(mapper.readTree(serialize(object, notIncludedFields)));
            }
            return mapper.writeValueAsString(mapper.valueToTree(objectJsonList));
        } catch (Exception e) {
            return null;
        }
    }
}
