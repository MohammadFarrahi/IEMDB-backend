package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.UserDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.ResponseDTO;
import ie.iemdb.util.types.Email;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseDTO logoutUser() {
        return UserDomainManager.getInstance().logoutUser();
    }
    @RequestMapping(value = "/users/{id}/watchlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MovieBriefDTO> getWatchlist(@PathVariable(value = "id") String userId) throws CustomException {
        return UserDomainManager.getInstance().getWatchlistDTO(userId);
    }
    @RequestMapping(value = "/users/{id}/watchlist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO postNewWatchlist(@PathVariable(value = "id") String userId, @RequestBody String watchlistInfo) throws JsonProcessingException, CustomException {
        var movieId = new ObjectMapper().readTree(watchlistInfo).get("movieId").asText();
        if(!UserDomainManager.getInstance().isloggedIn(userId)) {
            return new ResponseDTO(false, "Unauthorized");
        }
        return UserDomainManager.getInstance().addToWatchlist(userId, movieId);
    }
    @RequestMapping(value = "/users/{userId}/watchlist/{movieId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO deleteWatchlist(@PathVariable(value = "userId") String userId, @PathVariable(value = "movieId") String movieId) throws CustomException {
        if(!UserDomainManager.getInstance().isloggedIn(userId)) {
            return new ResponseDTO(false, "Unauthorized");
        }
        return UserDomainManager.getInstance().removeFromWatchList(userId, movieId);
    }

}