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

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "false")
public class MovieService {
    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMoviesList(@RequestParam(required = false) String filterBy, @RequestParam(required = false) String filterValue) throws SQLException {
        List<MovieBriefDTO> result = new ArrayList<>();
        try {
            if(filterBy != null && filterValue != null) {
                result = getFilteredMoviesList(filterBy, filterValue);
            } else {
                result = MovieDomainManager.getInstance().getMoviesDTOList();
            }
            return new Response(true, "okeb", result);
        } catch (CustomException e) {
            if (e instanceof ObjectNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    private List<MovieBriefDTO> getFilteredMoviesList(String filterBy, String filterValue) throws CustomException, SQLException {
        switch (filterBy) {
            case "name":
                return MovieDomainManager.getInstance().getFilteredMoviesByName(filterValue);
            case "genre":
                return MovieDomainManager.getInstance().getFilteredMoviesByGenre(filterValue);
            case "date":
                var datePoints = filterValue.split(",");
                if(datePoints.length != 2) throw new CustomException("Invalid date Points");
                return MovieDomainManager.getInstance().getFilteredMoviesByYear(Integer.valueOf(datePoints[0]), Integer.valueOf(datePoints[1]));
            default:
                throw new CustomException("Invalid filter type");
        }
    }

    @RequestMapping(value = "/movies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMovieInfo(@PathVariable(value = "id") Integer movieId, HttpServletRequest request) throws SQLException {
        try {
            return new Response(true, "okeb", MovieDomainManager.getInstance().getMovieDTO(movieId, (String)request.getAttribute("userEmail")));
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @RequestMapping(value = "/movies/{id}/rate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response postMovieRate(@PathVariable(value = "id") Integer movieId, @RequestBody String rateObj, HttpServletRequest request) throws SQLException {
        try {
            var rate = new ObjectMapper().readTree(rateObj).get("rate").asInt();
            return new Response(true, "okeb", MovieDomainManager.getInstance().rateMovie(movieId, rate, (String)request.getAttribute("userEmail")));
        } catch (Exception e) {
            if(e instanceof SQLException){
                throw (SQLException) e;
            }
            if (e instanceof ObjectNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
