package ie.generic.model;

public interface JsonHandler <T> {
    public T deserialize(String jsonData);
    public String serialize(T object);
}
