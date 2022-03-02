package ie.model.film;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.generic.model.JsonHandler;


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
    public String serialize(Film object, Set<String> notIncludedFields) {
        return null;
    }

    @Override
    public String serialize(List<Film> objects, Set<String> notIncludedFields) {
        return null;
    }
}
