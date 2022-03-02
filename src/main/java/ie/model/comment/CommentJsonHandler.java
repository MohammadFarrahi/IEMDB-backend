package ie.model.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.generic.model.JsonHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommentJsonHandler implements JsonHandler<Comment> {
    private final ObjectMapper mapper;

    public CommentJsonHandler() {
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    }
    @Override
    public Comment deserialize(String jsonData) throws JsonProcessingException {
        return mapper.readValue(jsonData, Comment.class);
    }
    @Override
    public List<Comment> deserializeList(String jsonData) throws JsonProcessingException {
        return mapper.readValue(jsonData, new TypeReference<List<Comment>>(){});
    }
    @Override
    public String serialize(Comment object, Set<String> notIncludedFields) {
        try {
            var objectNode = (ObjectNode) mapper.valueToTree(object);
            objectNode.remove(notIncludedFields);
            return mapper.writeValueAsString(objectNode);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String serialize(List<Comment> objects, Set<String> notIncludedFields) {
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
