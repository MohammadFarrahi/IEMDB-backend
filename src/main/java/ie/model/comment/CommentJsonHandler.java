package ie.model.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.generic.model.JsonHandler;

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
    public String serialize(Comment object, Set<String> notIncludedFields) {
        return null;
    }

    @Override
    public String serialize(List<Comment> objects, Set<String> notIncludedFields) {
        return null;
    }
}
