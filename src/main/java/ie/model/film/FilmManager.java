package ie.model.film;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.Iemdb;
import java.util.ArrayList;
import ie.exception.*;
import ie.generic.model.JsonHandler;
import ie.generic.model.Manager;
import ie.model.actor.ActorManager;
import ie.model.comment.CommentManager;
import ie.model.user.UserManager;
import ie.util.types.Constant;
import java.util.HashSet;

public class FilmManager extends Manager<Film> {
    private static FilmManager instance = null;
    // TODO : remove mapper
    private final ObjectMapper mapper;
    private final JsonHandler<Film> jsonMapper;

    public static FilmManager getInstance() {
        if (instance == null) {
            instance = new FilmManager();
        }
        return instance;
    }
    private FilmManager() {
        jsonMapper = new FilmJsonHandler();
        mapper = new ObjectMapper();
    }

    @Override
    public String addElement(Film newObject) throws CustomException {
        if (!ActorManager.getInstance().isIdListValid(newObject.getCast())) {
            throw new ActorNotFoundException();
        }
        var objectId = newObject.getId().toString();
        if (isIdValid(objectId)) {
            throw new MovieAlreadyExistsException();
        }
        this.objectMap.put(objectId, newObject);
        return objectId;
    }

    @Override
    public String updateElement(Film newObject) throws CustomException {
        if (!ActorManager.getInstance().isIdListValid(newObject.getCast())) {
            throw new ActorNotFoundException();
        }
        var objectId = newObject.getId().toString();
        if (!isIdValid(objectId)) {
            throw new MovieNotFoundException();
        }
        objectMap.put(objectId, newObject);
        return objectId;
    }

    public String addElementJson(String jsonData) throws JsonProcessingException, CustomException {
        var deserializedObject = jsonMapper.deserialize(jsonData);
        return addElement(deserializedObject);
    }
    public ArrayList<String> addElementsJson(String jsonData) throws JsonProcessingException, CustomException {
        var objectIds = new ArrayList<String>();
        for (var deserializedObject : jsonMapper.deserializeList(jsonData)) {
            objectIds.add(addElement(deserializedObject));
        }
        return objectIds;
    }
    public String updateElementJson(String jsonData) throws JsonProcessingException, CustomException {
        var deserializedObject = jsonMapper.deserialize(jsonData);
        return updateElement(deserializedObject);
    }

    public String updateOrAddElement(String jsonData) throws JsonProcessingException, CustomException {
        try {
            return updateElementJson(jsonData);
        }
        catch (Exception e) {
            return addElementJson(jsonData);
        }
    }
    public ArrayList<String> filterElementsByGenre(String genre) {
        try {
            ArrayList<String> filteredIdList = new ArrayList<>();
            for (var pair : objectMap.entrySet()) {
                if (pair.getValue().includeGenre(genre))
                    filteredIdList.add(pair.getKey());
            }
            return filteredIdList;
        } catch (Exception e) {
            return null;
        }
    }
    public String serializeElement(String filmId, Constant.SER_MODE mode) throws CustomException {
        var film = getElementById(filmId);
        if (mode == Constant.SER_MODE.SHORT) {
            return jsonMapper.serialize(film, Constant.Movie.REMOVABLE_SHORT_SER);
        }
        else {
            return jsonMapper.serialize(film, null);
        }
    }
    // TODO : remove this issue in iemdb
    public String serializeElementList(ArrayList<String> filmIds, Constant.SER_MODE mode) throws CustomException {
        var objects = getElementsById(filmIds);
        if (mode == Constant.SER_MODE.SHORT) {
            return jsonMapper.serialize(objects, Constant.Movie.REMOVABLE_SHORT_SER);
        }
        else {
            return jsonMapper.serialize(objects, null);
        }
    }

    public void rateMovie(String filmId, String userEmail, int rate) throws CustomException {
        if (!UserManager.getInstance().isIdValid(userEmail)) {
            throw new UserNotFoundException();
        }
        getElementById(filmId).updateFilmRating(userEmail, rate);
    }

    // TODO : move these methods to another place and pass validated data to userManager. one option would be making a SchemaClass for watchList json
    public void rateMovie(String jsonData) throws JsonProcessingException, CustomException {
        JsonNode rateJsonNode = mapper.readTree(jsonData);
        ValidateRateJson(rateJsonNode);

        rateMovie(rateJsonNode.get(Constant.Rate.M_ID).asText(),
                rateJsonNode.get(Constant.Rate.U_ID).asText(),
                rateJsonNode.get(Constant.Rate.RATE).asInt());
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
//    public Film getElementById(String id) throws CustomException {
//        if (filmMap.containsKey(id)) {
//            return filmMap.get(id);
//        }
//        throw new MovieNotFoundException();
//    }

}
