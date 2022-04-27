package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.UserDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.DTO.ResponseDTO;
import ie.iemdb.util.types.Email;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO loginUser(@RequestBody String loginInfoForm) throws CustomException, JsonProcessingException {
        var loginJson = new ObjectMapper().readTree(loginInfoForm);
        var userEmail = new Email(loginJson.get("email").asText()).toString();
        var userPassword = loginJson.get("password").asText();
        return UserDomainManager.getInstance().loginUser(userEmail, userPassword);
    }
    @RequestMapping(value = "/auth/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO logoutUser() throws CustomException, JsonProcessingException {
        return UserDomainManager.getInstance().logoutUser();
    }
}