package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.User;
import ie.iemdb.repository.UserRepo;

public class UserAPIConsumer extends APIConsumer{
    UserAPIConsumer(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected void loadRepo(JsonNode arrayNode) {
        try {
            var repo = UserRepo.getInstance();
            for (var node : arrayNode) {
                var newUser = makeNewUser(node);
                repo.addElement(newUser);
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private User makeNewUser(JsonNode node) throws CustomException {
        String email = node.get("email").asText();
        String name = node.get("name").asText();
        String birthDate = node.get("birthDate").asText();
        String password = node.get("password").asText();
        String nickname = node.get("nickname").asText();

        return new User (email, password, nickname, name, birthDate);
    }
}
