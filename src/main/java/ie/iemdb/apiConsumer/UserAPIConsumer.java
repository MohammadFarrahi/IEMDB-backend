package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.UserAlreadyExistsException;
import ie.iemdb.model.User;
import ie.iemdb.repository.UserRepo;
import ie.iemdb.security.PasswordEncoder;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;

public class UserAPIConsumer extends APIConsumer{
    public UserAPIConsumer(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected void loadRepo(JsonNode arrayNode) {
        try {
            var repo = UserRepo.getInstance();
            for (var node : arrayNode) {
                try {
                    var newUser = makeNewUser(node);
                    repo.addElement(newUser);
                }catch (UserAlreadyExistsException | DateTimeParseException | SQLException e){
                    e.printStackTrace();
                };

            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private User makeNewUser(JsonNode node) throws CustomException {
        String email = node.get("email").asText();
        String name = node.get("name").asText();
        String birthDate = node.get("birthDate").asText();
        String password = PasswordEncoder.encode(node.get("password").asText());
        String nickname = node.get("nickname").asText();

        return new User (email, password, nickname, name, birthDate);
    }
}
