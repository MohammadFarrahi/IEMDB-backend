package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.UserDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.Response;
import ie.iemdb.model.DTO.UserDTO;
import ie.iemdb.security.DTO.JwtRequestDTO;
import ie.iemdb.security.DTO.JwtResponseDTO;
import ie.iemdb.security.JwtTokenUtil;
import ie.iemdb.util.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "false")
@ComponentScan(basePackages ={"ie.iemdb.security", "ie.iemdb.util"})
public class UserService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ApiClient apiClient;
    @Value("${Oauth.client-secret}")
    private String oauthClientSecret;
    @Value("${Oauth.client-id}")
    private String oauthClientId;
    private final ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/auth/oauth-login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response loginWithOauth(@RequestParam(value="code", required = true) String code) throws SQLException {
        try {
            var accessTokenUrl = String.format("https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s",
                    oauthClientId, oauthClientSecret, code);
            var accessTokenCallResult = mapper.readTree(apiClient.post(accessTokenUrl, "Accept", "application/json"));
            var userInfoCallResult = apiClient.get("https://api.github.com/user",
                            "Authorization",String.format("token %s", accessTokenCallResult.get("access_token").asText()));
            return authenticateUser(userInfoCallResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SomethingWentWrong", e);
        } catch (CustomException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    private Response authenticateUser(String userInfoCallResult) throws JsonProcessingException, SQLException, CustomException {
        var jsonResult = mapper.readTree(userInfoCallResult);
        var userDTO = new UserDTO();
        userDTO.setNickname(jsonResult.get("login").asText());
        userDTO.setEmail(jsonResult.get("email").asText());
        userDTO.setName(jsonResult.get("name").asText());
        userDTO.setPassword(null);
        userDTO.setBirthDate(LocalDate.parse(jsonResult.get("created_at").asText().split("T")[0]).minusYears(18).toString());

        var userEmail = userDTO.getEmail();
        UserDomainManager.getInstance().registerOrLoinUser(userDTO);
        return new Response(true, "okeb", new JwtResponseDTO(userEmail, jwtTokenUtil.generateToken(userEmail)));
    }

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response registerUser(@RequestBody UserDTO newUserInfo) throws SQLException {
        if(newUserInfo.checkNullability()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            UserDomainManager.getInstance().registerNewUser(newUserInfo);
            return new Response(true, "okeb", null);
        } catch (CustomException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response loginUser(@RequestBody JwtRequestDTO loginInfo) throws SQLException {
        try {
            if(!loginInfo.checkNullability()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            var userEmail = loginInfo.getEmail();
            var userPassword = loginInfo.getPassword();
            UserDomainManager.getInstance().loginUser(userEmail, userPassword);
            return new Response(true, "okeb", new JwtResponseDTO(userEmail, jwtTokenUtil.generateToken(userEmail)));
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "InvalidCredential", e);
        }
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
    public Response postNewWatchlist(@PathVariable(value = "id") String userId, @RequestBody String watchlistInfo) throws SQLException {
        try {
            var movieId = mapper.readTree(watchlistInfo).get("movieId").asInt();
            return new Response(true, "okeb", UserDomainManager.getInstance().addToWatchlist(userId, movieId));
        } catch (Exception e) {
            if(e instanceof SQLException){
                throw (SQLException) e;
            }
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