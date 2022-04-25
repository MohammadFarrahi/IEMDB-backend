package ie.iemdb.domain;

import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.repository.MovieRepo;

import java.util.ArrayList;
import java.util.List;

public class MovieDomainManager {
    private static MovieDomainManager instance;

    public static MovieDomainManager getInstance() {
        if (instance == null) {
            instance = new MovieDomainManager();
        }
        return instance;
    }
    public List<MovieBriefDTO> getMoviesDTOList() throws ObjectNotFoundException {
        var movies = MovieRepo.getInstance().getElementsById(null);
        List<MovieBriefDTO> moviesDTO = new ArrayList<>();
        movies.forEach(movie -> moviesDTO.add(movie.getShortDTO()));
        return moviesDTO;
    }
}
