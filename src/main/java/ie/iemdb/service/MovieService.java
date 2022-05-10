package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.MovieDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieService {
    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMoviesList() throws SQLException {
        try {
            return new Response(true, "okeb", MovieDomainManager.getInstance().getMoviesDTOList());
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @RequestMapping(value = "/movies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMovieInfo(@PathVariable(value = "id") Integer movieId) throws SQLException {
        try {
            return new Response(true, "okeb", MovieDomainManager.getInstance().getMovieDTO(movieId));
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @RequestMapping(value = "/movies/{id}/rate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response postMovieRate(@PathVariable(value = "id") Integer movieId, @RequestBody String rateObj) {
        try {
            var rate = new ObjectMapper().readTree(rateObj).get("rate").asInt();
            return new Response(true, "okeb", MovieDomainManager.getInstance().rateMovie(movieId, rate));
        } catch (Exception e) {
            if (e instanceof ObjectNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
