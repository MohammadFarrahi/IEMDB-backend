package ie.film;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ie.Iemdb;
import ie.actor.Actor;
import ie.user.UserManager;
import java.util.ArrayList;
import ie.types.Constant;
import java.util.HashMap;
import java.util.HashSet;

public class FilmManager {
    private final HashMap<String, Film> filmMap;
    private final ObjectMapper mapper;
    private final Iemdb database;

    public FilmManager(Iemdb database) {
        this.database = database;

        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        filmMap = new HashMap<>();
    }

    public String updateOrAddElement(String jsonData) throws Exception {
        var jsonNode = mapper.readTree(jsonData);
        var filmId = jsonNode.get(Constant.Movie.ID).asText();
        var cast = Iemdb.convertListToString(mapper.convertValue(mapper.readTree(jsonData).get(Constant.Movie.CAST), ArrayList.class));

        if(!database.modelListExists(cast, Constant.Model.ACTOR)) {
            throw new Exception("Actor not found");
        }
        if (isIdValid(filmId)) {
            updateElement(filmId, jsonData);
        }
        else {
            addElement(jsonData);
        }
        return filmId;
    }
    public String addElement(String jsonData) throws Exception {
        String filmId = mapper.readTree(jsonData).get(Constant.Movie.ID).asText();
        if (isIdValid(filmId)) {
            throw new Exception("movie already exist");
        }
        var newFilm = mapper.readValue(jsonData, Film.class);
        filmMap.put(filmId, newFilm);
        return filmId;
    }
    public void updateElement(String id, String jsonData) throws Exception {
        if (!isIdValid(id)) {
            throw new Exception("movie not found");
        }
        mapper.readerForUpdating(filmMap.get(id)).readValue(jsonData);
    }

    public Film getFilm(String id) throws Exception {
        var film = filmMap.get(id);
        if (film != null) {
            return film;
        } else {
            throw new Exception("Movie Not Found");
        }
    }

    public void rateMovie(String jsonData) throws Exception {
        JsonNode rateJsonNode = mapper.readTree(jsonData);
        ValidateRateJson(rateJsonNode);
        ValidateRateData(rateJsonNode);

        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        filmMap.get(filmId).updateFilmRating(userEmail, rate);
    }

    private void ValidateRateData(JsonNode rateJsonNode) throws Exception {
        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        if (!database.modelExists(userEmail, Constant.Model.USER)) {
            throw new Exception("ne user with this id");
        }
        if (!isIdValid(filmId)) {
            throw new Exception("no film with this id");
        }
    }

    private void ValidateRateJson(JsonNode rateJsonNode) throws Exception {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        rateJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        var rateJsonFieldNames = Constant.Rate.getSet();
        boolean exceptionFlag = (jsonFiledNames.size() != rateJsonFieldNames.size());
        exceptionFlag |= !(rateJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)));
        exceptionFlag |= !(rateJsonNode.get(Constant.Rate.M_ID).isInt() &&
                rateJsonNode.get(Constant.Rate.U_ID).isTextual() &&
                rateJsonNode.get(Constant.Rate.RATE).isInt());
        if (exceptionFlag) {
            throw new Exception("invalid input rate");
        }
    }

    public boolean isIdValid(String id){
        return filmMap.containsKey(id);
    }

    public boolean isIdListValid(ArrayList<String> ids) {
        for (var id : ids){
            if(!filmMap.containsKey(id))
                return false;
        }
        return true;
    }
}
