package ie.film;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ie.user.UserManager;
import java.util.ArrayList;
import ie.types.Constant;
import java.util.HashMap;
import java.util.HashSet;

public class FilmManager {
    private final HashMap<String, Film> filmMap;
    private final ObjectMapper objectJsonMapper;

    public FilmManager() {
        objectJsonMapper = new ObjectMapper();
        objectJsonMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        filmMap = new HashMap<>();
    }

    public Film addMovie(String data) throws Exception {
        String id = objectJsonMapper.readTree(data).get(Constant.Movie.ID).toString();

        if (filmMap.containsKey(id)) {
            var existingFilm = filmMap.get(id);
            objectJsonMapper.readerForUpdating(existingFilm).readValue(data);
            return existingFilm;
        }
        var newFilm = objectJsonMapper.readValue(data, Film.class);
        filmMap.put(id, newFilm);
        return newFilm;
    }

    public Film getFilm(String id) throws Exception {
        var film = filmMap.get(id);
        if (film != null) {
            return film;
        } else {
            throw new Exception("Movie Not Found");
        }
    }

    public void rateMovie(String jsonData, UserManager userManager) throws Exception {
        JsonNode rateJsonNode = objectJsonMapper.readTree(jsonData);
        ValidateVoteJson(rateJsonNode);
        ValidateVoteData(rateJsonNode, userManager);

        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        filmMap.get(filmId).updateFilmRating(userEmail, rate);
    }

    private void ValidateVoteData(JsonNode rateJsonNode, UserManager userManager) throws Exception {
        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        if (!userManager.isUserPresent(userEmail)) {
            throw new Exception("ne user with this id");
        }
        if (filmMap.get(filmId) == null) {
            throw new Exception("ne film with this id");
        }    // TODO: consider using getFilm method
        if (!(1 <= rate && rate <= 10)) {
            throw new Exception("invalid rate number");
        }
    }

    private void ValidateVoteJson(JsonNode rateJsonNode) throws Exception {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        rateJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        var voteJsonFieldNames = Constant.Rate.getSet();
        boolean exceptionFlag = (jsonFiledNames.size() != voteJsonFieldNames.size());
        exceptionFlag |= !(voteJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)));
        exceptionFlag |= !(rateJsonNode.get(Constant.Rate.M_ID).isInt() &&
                rateJsonNode.get(Constant.Rate.U_ID).isTextual() &&
                rateJsonNode.get(Constant.Rate.RATE).isInt());
        if (exceptionFlag) {
            throw new Exception("invalid input vote");
        }
    }
}
