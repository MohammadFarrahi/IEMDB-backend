package ie.iemdb.service;

import ie.iemdb.domain.MovieDomainManager;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieService {
    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MovieBriefDTO> getMoviesList() throws ObjectNotFoundException {
        // TODO : validation and exception handling
        return MovieDomainManager.getInstance().getMoviesDTOList();
    }
}
