package ie.iemdb.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoviesService {
    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MovieDTO> getMoviesList() {
        // TODO : validation and exception handling
        return MovieDomainManager.getInstance().getMoviesDTOList();
    }
}
