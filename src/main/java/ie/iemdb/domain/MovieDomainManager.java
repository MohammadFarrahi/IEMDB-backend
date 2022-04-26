package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.MovieDTO;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;

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

    public MovieDTO getMovieDTO(String movieId) throws ObjectNotFoundException {
        var movie = MovieRepo.getInstance().getElementById(movieId);
        var DTO = movie.getDTO();
        DTO.setUserRate(movie.getUserRate(UserRepo.loggedInUser.getId()));
        return DTO;
    }

    public void rateMovie(String movieId, int rate) throws CustomException {
        var ratingUser = UserRepo.loggedInUser;
        MovieRepo.getInstance().getElementById(movieId).updateMovieRating(ratingUser.getId(), rate);
    }
}
