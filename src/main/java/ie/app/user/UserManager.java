package ie.app.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.app.film.Film;
import ie.exception.*;
import ie.generic.model.JsonHandler;
import ie.generic.model.Manager;
import ie.app.film.FilmManager;
import ie.util.types.Constant;
import kotlin.Pair;

import java.util.*;

public class UserManager extends Manager<User> {
    private static UserManager instance = null;
    // TODO : remove mapper
    private final ObjectMapper mapper;
    private final JsonHandler<User> jsonMapper;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    private UserManager () {
        jsonMapper = new UserJsonHandler();
        mapper = new ObjectMapper();
        this.notFoundException = new UserNotFoundException();
    }

    @Override
    public String addElement(User newObject) throws CustomException {
        var objectId = newObject.getId();
        if (isIdValid(objectId)) {
            throw new CustomException("ObjectAlreadyExists"); // TODO : customize message
        }
        this.objectMap.put(objectId, newObject);
        return objectId;
    }
    @Override
    public String updateElement(User newObject) throws UserNotFoundException {
        var objectId = newObject.getId();
        if (!isIdValid(objectId)) {
            throw new UserNotFoundException();
        }
        objectMap.put(objectId, newObject);
        return objectId;
    }
    public String updateOrAddElementJson(String jsonData) throws JsonProcessingException, CustomException {
        try {
            return updateElementJson(jsonData);
        }
        catch (Exception e) {
            return addElementJson(jsonData);
        }
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

    public ArrayList<String> getWatchList(String userId) throws CustomException {
        return getElementById(userId).getWatchList();
    }

    public void addToWatchList(String userId, String movieId) throws CustomException {
        var user = getElementById(userId);
        if (!user.isOlderThan(FilmManager.getInstance().getElementById(movieId).getAgeLimit()))
            throw new AgeLimitException();

        user.addToWatchList(movieId);
    }
    public void removeFromWatchList(String userId, String movieId) throws CustomException {
        var user = getElementById(userId);
        user.removeFromWatchList(movieId);
    }

    public ArrayList<String> getRecommendedWatchlist(User user){
        ArrayList <Pair <String, Double>> scoreFilmList = new ArrayList<>();

        try {
            var films = FilmManager.getInstance().getElementsById(null);
            var watchListFilms = FilmManager.getInstance().getElementsById(user.getWatchList());
            for(var film : films) {
                scoreFilmList.add(new Pair<>(film.getId().toString(), calFilmScore(film, watchListFilms)));
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }

        // sorting by score and returning best 3
        Collections.sort(scoreFilmList, new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(final Pair<String, Double> o1, final Pair<String, Double> o2) {
                return o2.getSecond().compareTo(o1.getSecond());
            }
        });
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < scoreFilmList.size(); i++){
            if(i > 2)
                break;
            result.add(scoreFilmList.get(i).getFirst());
        }

        return result;
    }

    public Double calFilmScore(Film film, List<Film> films){
        double score = 0;
        score += film.getBaseScoreForWatchList();
        double similarity = 0;
        for(var wFilm : films){
            score += film.getSameGenre(wFilm);
        }
        score += 3 * similarity;
        return score;
    }


    // TODO : move these methods to another place and pass validated data to userManager. one option would be making a SchemaClass for watchList json
    public void addToWatchList(String JsonData) throws JsonProcessingException, CustomException {
        var jsonNode = mapper.readTree(JsonData);
        validatedWListJson(jsonNode);
        addToWatchList(jsonNode.get(Constant.WatchList.U_ID).asText(), jsonNode.get(Constant.WatchList.M_ID).asText());
    }
    public void removeFromWatchList(String JsonData) throws JsonProcessingException, CustomException {
        var jsonNode = mapper.readTree(JsonData);
        validatedWListJson(jsonNode);
        removeFromWatchList(jsonNode.get(Constant.WatchList.U_ID).asText(), jsonNode.get(Constant.WatchList.M_ID).asText());
    }
    private void validatedWListJson(JsonNode WListJsonNode) throws CustomException {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        WListJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);
        var voteJsonFieldNames = Constant.WatchList.getSet();

        boolean exceptionFlag = (jsonFiledNames.size() != voteJsonFieldNames.size());
        exceptionFlag |= !(voteJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)));
        exceptionFlag |= !(WListJsonNode.get(Constant.WatchList.M_ID).isInt() && WListJsonNode.get(Constant.Vote.U_ID).isTextual());
        if (exceptionFlag) {
            throw new InvalidCommandException();
        }
    }
    public String getWatchListJson (String data) throws JsonProcessingException, CustomException  {
        var jsonNode = mapper.readTree(data);
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);
        if(jsonFiledNames.size() != 1 || !jsonFiledNames.get(0).equals(Constant.WatchList.U_ID))
            throw new InvalidCommandException();

        var watchListJson = FilmManager.getInstance().serializeElementList(getWatchList(jsonNode.get(Constant.WatchList.U_ID).asText()), Constant.SER_MODE.SHORT);
        var node = mapper.createObjectNode();
        node.set("WatchList", mapper.readTree(watchListJson));
        return mapper.writeValueAsString(node);
    }
//    public User getElementById(String id) throws CustomException {
//        if(userMap.containsKey(id)){
//            return userMap.get(id);
//        }
//        throw new UserNotFoundException();
//    }
}
