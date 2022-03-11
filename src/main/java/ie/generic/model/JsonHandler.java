package ie.generic.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;

public interface JsonHandler <T> {
    public T deserialize(String jsonData) throws JsonProcessingException;
    public List<T> deserializeList(String jsonData) throws JsonProcessingException;
    public String serialize(T object, Set<String> notIncludedFields);
    public String serialize(List<T> objects, Set<String> notIncludedFields);

    // TODO : make these methods, not framework-specific
    public static JsonNode getNodeOfObject(Object object) throws JsonProcessingException {
        return new ObjectMapper().convertValue(object, JsonNode.class);
    }
    public static JsonNode geNodeOfJson(String jsonData) throws JsonProcessingException {
        return new ObjectMapper().convertValue(jsonData, JsonNode.class);
    }
    private String getFieldValue(JsonNode node, String key) {
        return node.get(key).asText();
    }
}
