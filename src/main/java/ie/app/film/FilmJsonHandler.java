package ie.app.film;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.app.actor.ActorManager;
import ie.app.comment.CommentManager;
import ie.generic.model.JsonHandler;
import ie.util.types.Constant;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilmJsonHandler implements JsonHandler<Film> {
    private final ObjectMapper mapper;
    public FilmJsonHandler() {
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    }

    @Override
    public Film deserialize(String jsonData) throws JsonProcessingException {
        return mapper.readValue(jsonData, Film.class);
    }
    @Override
    public List<Film> deserializeList(String jsonData) throws JsonProcessingException {
        return mapper.readValue(jsonData, new TypeReference<List<Film>>(){});
    }
    @Override
    public String serialize(Film object, Set<String> notIncludedFields) {
        try {
            var objectJsontNode = (ObjectNode) mapper.valueToTree(object);
            if(notIncludedFields != null) {
                objectJsontNode.remove(notIncludedFields);
            } else {
                var castJsonNode = mapper.readTree(ActorManager.getInstance().serializeElementList(object.getCast(), Constant.SER_MODE.SHORT));
                var commentJsonNode = mapper.readTree(CommentManager.getInstance().serializeElementList(object.getComments(), Constant.SER_MODE.SHORT));
                objectJsontNode.replace(Constant.Movie.CAST, castJsonNode);
                objectJsontNode.replace(Constant.Movie.COMMENTS, commentJsonNode);
            }
            return mapper.writeValueAsString(objectJsontNode);
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public String serialize(List<Film> objects, Set<String> notIncludedFields) {
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
