package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.UserDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response loginUser(@RequestBody String loginInfoForm) throws JsonProcessingException {
        var loginJson = new ObjectMapper().readTree(loginInfoForm);
        var userEmail = loginJson.get("email").asText();
        var userPassword = loginJson.get("password").asText();
        try {
            UserDomainManager.getInstance().loginUser(userEmail, userPassword);
            return new Response(true, "okeb", userEmail);
        } catch (ObjectNotFoundException e) {
            return new Response(false, "InvalidCredential", null);
        }
    }
    @RequestMapping(value = "/auth/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response logoutUser() {
        UserDomainManager.getInstance().logoutUser();
        return new Response(true, "okeb", null);
    }
    @RequestMapping(value = "/users/{id}/watchlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getWatchlist(@PathVariable(value = "id") String userId) throws CustomException {
        return new Response(true, "okeb", UserDomainManager.getInstance().getWatchlistDTO(userId));
    }
    @RequestMapping(value = "/users/{id}/watchlist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response postNewWatchlist(@PathVariable(value = "id") String userId, @RequestBody String watchlistInfo) throws JsonProcessingException, CustomException {
        var movieId = new ObjectMapper().readTree(watchlistInfo).get("movieId").asText();
        if(!UserDomainManager.getInstance().isloggedIn(userId)) {
            return new Response(false, "Unauthorized", null);
        }
        return new Response(true, "okeb", UserDomainManager.getInstance().addToWatchlist(userId, movieId));
    }
    @RequestMapping(value = "/users/{userId}/watchlist/{movieId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteWatchlist(@PathVariable(value = "userId") String userId, @PathVariable(value = "movieId") String movieId) throws CustomException {
        if(!UserDomainManager.getInstance().isloggedIn(userId)) {
            return new Response(false, "Unauthorized", null);
        }
        UserDomainManager.getInstance().removeFromWatchList(userId, movieId);
        return new Response(true, "okeb", null);
    }
    @RequestMapping(value = "/users/{userId}/recommended", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getRecommendedMovies(@PathVariable(value = "userId") String userId) throws CustomException {
        if(!UserDomainManager.getInstance().isloggedIn(userId)) {
            return new Response(false, "Unauthorized", null);
        }
        return new Response(true, "okeb", UserDomainManager.getInstance().getRecommendedWatchlist(userId));
    }

}