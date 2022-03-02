package ie.model.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.generic.model.JsonHandler;

import java.util.List;
import java.util.Set;

public class UserJsonHandler implements JsonHandler<User> {
    private final ObjectMapper mapper;

    public UserJsonHandler() {
        mapper = new ObjectMapper();
    }
    @Override
    public User deserialize(String jsonData) throws JsonProcessingException {
        return mapper.readValue(jsonData, User.class);
    }

    @Override
    public String serialize(User object, Set<String> notIncludedFields) {
        return null;
    }

    @Override
    public String serialize(List<User> objects, Set<String> notIncludedFields) {
        return null;
    }
}
