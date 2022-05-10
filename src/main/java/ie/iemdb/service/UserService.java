package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.UserDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserService {
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response loginUser(@RequestBody String loginInfoForm) throws SQLException {
        try {
            var loginJson = new ObjectMapper().readTree(loginInfoForm);
            var userEmail = loginJson.get("email").asText();
            var userPassword = loginJson.get("password").asText();
            UserDomainManager.getInstance().loginUser(userEmail, userPassword);
            return new Response(true, "okeb", userEmail);
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "InvalidCredential", e);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/auth/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response logoutUser() {
        UserDomainManager.getInstance().logoutUser();
        return new Response(true, "okeb", null);
    }
    @RequestMapping(value = "/users/{id}/watchlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getWatchlist(@PathVariable(value = "id") String userId) throws SQLException {
        try {
            return new Response(true, "okeb", UserDomainManager.getInstance().getWatchlistDTO(userId));
        } catch (CustomException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
        }
    }
    @RequestMapping(value = "/users/{id}/watchlist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response postNewWatchlist(@PathVariable(value = "id") String userId, @RequestBody String watchlistInfo) {
        try {
            var movieId = new ObjectMapper().readTree(watchlistInfo).get("movieId").asInt();
            return new Response(true, "okeb", UserDomainManager.getInstance().addToWatchlist(userId, movieId));
        } catch (Exception e) {
            if(e instanceof ObjectNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }
    @RequestMapping(value = "/users/{userId}/watchlist/{movieId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteWatchlist(@PathVariable(value = "userId") String userId, @PathVariable(value = "movieId") Integer movieId) {
        try {
            UserDomainManager.getInstance().removeFromWatchList(userId, movieId);
            return new Response(true, "okeb", null);
        } catch (ObjectNotFoundException | SQLException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
        }
    }
    @RequestMapping(value = "/users/{userId}/recommended", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getRecommendedMovies(@PathVariable(value = "userId") String userId) throws SQLException {
        try {
            return new Response(true, "okeb", UserDomainManager.getInstance().getRecommendedWatchlist(userId));
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
        }
    }

}