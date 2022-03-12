package ie.app.film;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

import ie.app.actor.ActorManager;
import ie.app.user.UserManager;
import ie.exception.*;
import ie.generic.model.JsonHandler;
import ie.generic.model.Manager;
import ie.util.types.Constant;

import java.util.stream.Collectors;

public class FilmManager extends Manager<Film> {
    private static FilmManager instance = null;
    // TODO : remove mapper
    private final ObjectMapper mapper;
    private final JsonHandler<Film> jsonMapper;

    private String nameFilter;
    private String sortType;
    private boolean filterFlag;
    private boolean sortFlag;
    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
        filterFlag = true;
    }
    public void setSortType(String sortType) {
        // TODO : validation of sortType is missing
        this.sortType = sortType;
        sortFlag = true;
    }
    public List<Film> fetchFilmsForUser() {
        try {
            var films = getElementsById(null);
            if(filterFlag) {
                films = filterElementsByName(films,  nameFilter);
                filterFlag = false;
            }
            if(sortFlag) {
                films = sortElements(films, sortType);
                sortFlag = false;
            }
            return films;
        } catch (ObjectNotFoundException e) {}
        return null;

    }
    public List<Film> filterElementsByName(List<Film> films, String name) {
        if(name==null)
            return films;
        return films.stream().filter(film -> film.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }
    public List<Film> sortElements(List<Film> films, String type){
        if(type == null)
            return films;
        switch (type) {
            case Constant.ActionType.SORT_IMDB -> Collections.sort(films, (f1, f2) -> f2.getImdbRate().compareTo(f1.getImdbRate()));
            case Constant.ActionType.SORT_DATE -> Collections.sort(films, (f1, f2) -> f2.getReleaseDate().compareTo(f1.getReleaseDate()));
            default -> {}
        }
        return films;
    }

    public static FilmManager getInstance() {
        if (instance == null) {
            instance = new FilmManager();
        }
        return instance;
    }
    private FilmManager() {
        jsonMapper = new FilmJsonHandler();
        mapper = new ObjectMapper();
        nameFilter = null;
        sortType = null;
        filterFlag = false;
        sortFlag = false;
        this.notFoundException = new MovieNotFoundException();
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
        ActorManager.getInstance().getElementsById(newObject.getCast()).forEach(actor -> actor.addToPerformedMovies(newObject.getId().toString()));
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
    public ArrayList<String> filterElementsByYear(int from, int to) throws CustomException {
        var ids = new ArrayList<String>();
        for(var film : objectMap.entrySet()) {
            if(!film.getValue().isCreatedBefore(from) && !film.getValue().isCreatedAfter(to)) {
                ids.add(film.getKey());
            }
        }
        return ids;
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
