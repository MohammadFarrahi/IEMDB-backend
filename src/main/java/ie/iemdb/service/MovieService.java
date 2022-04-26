package ie.iemdb.service;

import ie.iemdb.domain.MovieDomainManager;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.MovieDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieService {
    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MovieBriefDTO> getMoviesList() throws ObjectNotFoundException {
        // TODO : validation and exception handling
        return MovieDomainManager.getInstance().getMoviesDTOList();
    }
    @RequestMapping(value = "/movies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MovieDTO getMovieInfo(@PathVariable(value = "id") String movieId) throws ObjectNotFoundException {
        return MovieDomainManager.getInstance().getMovieDTO(movieId);
    }
}
