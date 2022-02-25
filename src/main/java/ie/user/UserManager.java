package ie.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.Iemdb;
import ie.actor.Actor;
import ie.types.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserManager {
    private final HashMap<String, User> userMap;
    private final Iemdb database;
    private final ObjectMapper mapper;

    public UserManager (Iemdb database) {
        mapper = new ObjectMapper();

        this.database = database;
        userMap = new HashMap<>();
    }

    public String updateOrAddElement(String jsonData) throws Exception {
        String email = mapper.readTree(jsonData).get(Constant.User.E_ID).asText();

        if (isIdValid(email)) {
            updateElement(email, jsonData);
        }
        else {
            addElement(jsonData);
        }
        return email;
    }

    public String addElement(String jsonData) throws Exception {
        String email = mapper.readTree(jsonData).get(Constant.User.E_ID).asText();
        if (isIdValid(email)) {
            throw new Exception("user already exists");
        }
        var newUser = mapper.readValue(jsonData, User.class);
        userMap.put(email, newUser);
        return email;
    }
    public void updateElement(String id, String jsonData) throws Exception {
        if (!isIdValid(id)) {
            throw new Exception("user not found");
        }
        mapper.readerForUpdating(userMap.get(id)).readValue(jsonData);
        // TODO: Check if it is needed to put object to hashMap again
    }

    public JsonNode getWatchList (String data) throws Exception {
        var jsonNode = mapper.readTree(data);
        var id = jsonNode.get("userEmail").asText();
        var user = getElement(id);
        return database.serializeElementList(user.getWatchList(), Constant.Model.FILM , Constant.SER_MODE.SHORT);
    }

    public boolean isIdValid(String email) {
        return userMap.containsKey(email);
    }

    public boolean isIdListValid(ArrayList<String> ids) {
        for (var id : ids){
            if(!userMap.containsKey(id))
                return false;
        }
        return true;
    }

    public void addToWatchList(String data) throws Exception {
        var jsonNode = mapper.readTree(data);
        validatedWListJson(jsonNode);
        var userId = jsonNode.get(Constant.WatchList.U_ID).asText();
        var movieId = jsonNode.get(Constant.WatchList.M_ID).asText();


        var user = getElement(userId);
        if (!user.isOlderThan(database.getFilmById(movieId).getAgeLimit()))
            throw new Exception("you age is not good");

        user.addToWatchList(movieId);
    }

    public void removeFromWatchList(String data) throws Exception {
        var jsonNode = mapper.readTree(data);
        validatedWListJson(jsonNode);
        var userId = jsonNode.get(Constant.WatchList.U_ID).asText();
        var movieId = jsonNode.get(Constant.WatchList.M_ID).asText();

        var user = getElement(userId);
        user.removeFromWatchList(movieId);
    }

    private void validatedWListJson(JsonNode WListJsonNode) throws Exception {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        WListJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);
        var voteJsonFieldNames = Constant.WatchList.getSet();

        boolean exceptionFlag = (jsonFiledNames.size() != voteJsonFieldNames.size());
        exceptionFlag |= !(voteJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)));
        exceptionFlag |= !(WListJsonNode.get(Constant.WatchList.M_ID).isInt() && WListJsonNode.get(Constant.Vote.U_ID).isTextual());
        if (exceptionFlag) {
            throw new Exception("invalid input vote");
        }
    }

    public User getElement(String id) throws Exception {
        if(userMap.containsKey(id)){
            return userMap.get(id);
        }
        throw new Exception("User not found");
    }
}
