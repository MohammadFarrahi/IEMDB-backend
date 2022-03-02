package ie.model.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.Iemdb;
import ie.exception.*;
import ie.generic.model.JsonHandler;
import ie.generic.model.Manager;
import ie.util.types.Constant;
import java.util.ArrayList;
import java.util.HashSet;

public class UserManager extends Manager<User> {
    private static UserManager instance = null;
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
    public String updateElementJson(String jsonData) throws JsonProcessingException, CustomException {
        var deserializedObject = jsonMapper.deserialize(jsonData);
        return updateElement(deserializedObject);
    }

    public JsonNode getWatchList (String data) throws JsonProcessingException, CustomException  {
        var jsonNode = mapper.readTree(data);

        ArrayList<String> jsonFiledNames = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        if(jsonFiledNames.size() != 1 || !jsonFiledNames.get(0).equals(Constant.WatchList.U_ID))
            throw new InvalidCommandException();

        var user = getElementById(jsonNode.get(Constant.WatchList.U_ID).asText());
        var watchList = database.serializeElementList(user.getWatchList(), Constant.Model.FILM , Constant.SER_MODE.SHORT);

        var node = mapper.createObjectNode();
        node.set("WatchList", watchList);
        return (JsonNode) node;
    }

    public void addToWatchList(String userId, String movieId) throws CustomException {
        var user = getElementById(userId);
        if (!user.isOlderThan(database.getFilmById(movieId).getAgeLimit()))
            throw new AgeLimitException();

        user.addToWatchList(movieId);
    }
    public void removeFromWatchList(String userId, String movieId) throws CustomException {
        var user = getElementById(userId);
        user.removeFromWatchList(movieId);
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
//    public User getElementById(String id) throws CustomException {
//        if(userMap.containsKey(id)){
//            return userMap.get(id);
//        }
//        throw new UserNotFoundException();
//    }
}
