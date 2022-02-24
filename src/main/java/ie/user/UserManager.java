package ie.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.Iemdb;
import ie.types.Constant;

import java.util.HashMap;

public class UserManager {
    private final HashMap<String, User> userMap;
    private final Iemdb database;

    public UserManager (Iemdb database) {
        this.database = database;
        userMap = new HashMap<>();
    }

    public void addUser(String jsonData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String email = objectMapper.readTree(jsonData).get(Constant.User.E_ID).asText();
        if (userMap.containsKey(email))
            throw new Exception("Duplicate");

        User user = objectMapper.readValue(jsonData, User.class);
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
    public boolean isUserPresent(String email) {
        return userMap.containsKey(email);
    }

}
