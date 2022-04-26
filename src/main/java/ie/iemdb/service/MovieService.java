package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.MovieDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.MovieDTO;
import ie.iemdb.model.DTO.ResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MovieBriefDTO> getMoviesList() throws ObjectNotFoundException {
        return MovieDomainManager.getInstance().getMoviesDTOList();
    }
    @RequestMapping(value = "/movies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MovieDTO getMovieInfo(@PathVariable(value = "id") String movieId) throws ObjectNotFoundException {
        return MovieDomainManager.getInstance().getMovieDTO(movieId);
    }
    @RequestMapping(value = "/movies/{id}/rate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO postMovieRate(@PathVariable(value = "id") String movieId, @RequestBody String rateObj) throws CustomException, JsonProcessingException {
        var rate = new ObjectMapper().readTree(rateObj).get("rate").asInt();
        return MovieDomainManager.getInstance().rateMovie(movieId, rate);
    }
}
