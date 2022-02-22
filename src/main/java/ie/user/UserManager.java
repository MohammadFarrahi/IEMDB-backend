package ie.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class UserManager {
    private final HashMap<String, User> userMap;

    public UserManager () {
        userMap = new HashMap<>();
    }

    public void addUser(String jsonData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String email = objectMapper.readTree(jsonData).get("email").asText();
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
            throw new Exception("Not found");
    }

}
