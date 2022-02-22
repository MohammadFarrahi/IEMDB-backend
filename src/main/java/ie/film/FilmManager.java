package ie.film;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ie.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class FilmManager {
    private final HashMap<String, Film> filmMap;

    public FilmManager () {
        filmMap = new HashMap<>();
    }

    public void addMovie (String data) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String id = objectMapper.readTree(data).get("id").toString();

        if (filmMap.containsKey(id))
            throw new Exception("Duplicate");

        Film film = objectMapper.readValue(data, Film.class);
        filmMap.put(id, film);
    }

    public Film getFilm(String id) throws Exception {
        var film = filmMap.get(id);
        if (film != null) {
            return film;
        }
        else
            throw new Exception("Movie Not Found");
    }
}
