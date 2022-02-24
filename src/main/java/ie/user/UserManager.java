package ie.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.Iemdb;
import ie.types.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {
    private final HashMap<String, User> userMap;
    private final Iemdb database;
    private final ObjectMapper mapper;

    public UserManager (Iemdb database) {
        mapper = new ObjectMapper();

        this.database = database;
        userMap = new HashMap<>();
    }

    public void addUser(String jsonData) throws Exception {
        String email = mapper.readTree(jsonData).get(Constant.User.E_ID).asText();
        if (userMap.containsKey(email))
            throw new Exception("Duplicate");

        User user = mapper.readValue(jsonData, User.class);
        this.userMap.put(email, user);
    }

    public User getUser(String email) throws Exception {
        var user = userMap.get(email);
        if (user != null) {
            return user;
        }
        else
            throw new Exception("User Not found");
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

        var userId = jsonNode.get(Constant.WatchList.U_ID).asText();
        var movieId = jsonNode.get(Constant.WatchList.M_ID).asText();

        if(!database.modelExists(movieId, Constant.Model.FILM))
            throw new Exception("Movie not found");
        var user = getElement(userId);
        user.addToWatchList(movieId);
    }

    public User getElement(String id) throws Exception {
        if(userMap.containsKey(id)){
            return userMap.get(id);
        }
        throw new Exception("User not found");
    }
}
