package ie.film;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.types.Constant;

import java.util.HashMap;

public class FilmManager {
    private final HashMap<String, Film> filmMap;

    public FilmManager () {
        filmMap = new HashMap<>();
    }

    public Film addMovie (String data) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String id = objectMapper.readTree(data).get(Constant.Movie.ID).toString();

        if (filmMap.containsKey(id)) {
            var existingFilm = filmMap.get(id);
            objectMapper.readerForUpdating(existingFilm).readValue(data);
            return existingFilm;
        }
        var newFilm = objectMapper.readValue(data, Film.class);
        filmMap.put(id, newFilm);
        return newFilm;
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
