package ie.model.film;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.Iemdb;
import java.util.ArrayList;

import ie.exception.*;
import ie.util.types.Constant;
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

    public String updateOrAddElement(String jsonData) throws JsonProcessingException, CustomException {
        var jsonNode = mapper.readTree(jsonData);
        var filmId = jsonNode.get(Constant.Movie.ID_S).asText();
        var cast = Iemdb.convertListToString(mapper.convertValue(mapper.readTree(jsonData).get(Constant.Movie.CAST), ArrayList.class));

        if (!database.modelListExists(cast, Constant.Model.ACTOR)) {
            throw new ActorNotFoundException();
        }
        if (isIdValid(filmId)) {
            updateElement(filmId, jsonData);
        } else {
            addElement(jsonData);
        }
        return filmId;
    }

    public String addElement(String jsonData) throws JsonProcessingException, CustomException {
        String filmId = mapper.readTree(jsonData).get(Constant.Movie.ID_S).asText();
        if (isIdValid(filmId)) {
            throw new MovieAlreadyExistsException();
        }
        var newFilm = mapper.readValue(jsonData, Film.class);
        filmMap.put(filmId, newFilm);
        return filmId;
    }

    public void updateElement(String id, String jsonData) throws JsonProcessingException, CustomException {
        if (!isIdValid(id)) {
            throw new MovieNotFoundException();
        }
        mapper.readerForUpdating(filmMap.get(id)).readValue(jsonData);
    }

    public void rateMovie(String jsonData) throws JsonProcessingException, CustomException {
        JsonNode rateJsonNode = mapper.readTree(jsonData);
        ValidateRateJson(rateJsonNode);
        ValidateRateData(rateJsonNode);

        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        filmMap.get(filmId).updateFilmRating(userEmail, rate);
    }

    public JsonNode getMovieByIdJson(String data) throws JsonProcessingException, CustomException  {
        var jsonNode = mapper.readTree(data);
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        if(jsonFiledNames.size() != 1 || !jsonFiledNames.get(0).equals(Constant.WatchList.M_ID)) {
            throw new InvalidCommandException();
        }
        return serializeElement(jsonNode.get(Constant.WatchList.M_ID).asText(), Constant.SER_MODE.LONG);
    }

    public JsonNode getMoviesByGenre(String data) throws JsonProcessingException, CustomException {
        var jsonNode = mapper.readTree(data);
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        if(jsonFiledNames.size() != 1 || !jsonFiledNames.get(0).equals("genre"))
            throw new InvalidCommandException();

        var genre = jsonNode.get("genre").asText();
        var filteredFilmsIds = filterElement(genre);
        return serializeElementList(filteredFilmsIds, Constant.SER_MODE.SHORT);
    }

    public Film getElement(String id) throws CustomException {
        if (filmMap.containsKey(id)) {
            return filmMap.get(id);
        }
        throw new MovieNotFoundException();
    }

    public ArrayList<String> filterElement(String genre) {
        try {
            ArrayList<String> filteredIdList = new ArrayList<>();
            for (var pair : filmMap.entrySet()) {
                if (pair.getValue().includeGenre(genre))
                    filteredIdList.add(pair.getKey());
            }
            return filteredIdList;
        } catch (Exception e) {
            return null;
        }
    }

    public JsonNode serializeElement(String filmId, Constant.SER_MODE mode) throws CustomException {
        var film = getElement(filmId);
        try {
            var filmJsonNode = (ObjectNode) mapper.valueToTree(film);

            if (mode == Constant.SER_MODE.LONG) {
                var castJsonNode = database.serializeElementList(film.getCast(), Constant.Model.ACTOR, Constant.SER_MODE.SHORT);
                var commentJsonNode = database.serializeElementList(film.getComments(), Constant.Model.COMMENT, Constant.SER_MODE.SHORT);

                filmJsonNode.replace(Constant.Movie.CAST, castJsonNode);
                filmJsonNode.replace(Constant.Movie.COMMENTS, commentJsonNode);
            }
            else {
                filmJsonNode.remove(Constant.Movie.REMOVABLE_SHORT_SER);
            }
            return filmJsonNode;
        } catch (Exception e) {
            return null;
        }
    }

    public JsonNode serializeElementList(ArrayList<String> filmIds, Constant.SER_MODE mode) throws CustomException {
        var filmJsonList = new ArrayList<JsonNode>();
        for(var id : filmIds) {
            filmJsonList.add(serializeElement(id, mode));
        }
        var jsonNode = mapper.valueToTree(filmJsonList);
        return jsonNode;
    }

    private void ValidateRateData(JsonNode rateJsonNode) throws CustomException {
        var userEmail = rateJsonNode.get(Constant.Rate.U_ID).asText();
        var filmId = rateJsonNode.get(Constant.Rate.M_ID).asText();
        var rate = rateJsonNode.get(Constant.Rate.RATE).asInt();
        if (!database.modelExists(userEmail, Constant.Model.USER)) {
            throw new UserNotFoundException();
        }
        if (!isIdValid(filmId)) {
            throw new MovieNotFoundException();
        }
    }

    private void ValidateRateJson(JsonNode rateJsonNode) throws CustomException {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        rateJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        var rateJsonFieldNames = Constant.Rate.getSet();
        boolean exceptionFlag = (jsonFiledNames.size() != rateJsonFieldNames.size());
        exceptionFlag |= !(rateJsonFieldNames.equals(new HashSet<>(jsonFiledNames)));
        exceptionFlag |= !(rateJsonNode.get(Constant.Rate.M_ID).isInt() &&
                rateJsonNode.get(Constant.Rate.U_ID).isTextual() &&
                rateJsonNode.get(Constant.Rate.RATE).isInt());
        if (exceptionFlag) {
            throw new InvalidCommandException();
        }
    }

    public boolean isIdValid(String id) {
        return filmMap.containsKey(id);
    }

    public boolean isIdListValid(ArrayList<String> ids) {
        for (var id : ids) {
            if (!filmMap.containsKey(id))
                return false;
        }
        return true;
    }

}
