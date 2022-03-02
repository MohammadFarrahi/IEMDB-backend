package ie.generic.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

public interface JsonHandler <T> {
    public T deserialize(String jsonData) throws JsonProcessingException;
    public List<T> deserializeList(String jsonData) throws JsonProcessingException;
    public String serialize(T object, Set<String> notIncludedFields);
    public String serialize(List<T> objects, Set<String> notIncludedFields); //TODO: make it not specific to any framework
}
