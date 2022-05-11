package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.MovieDTO;
import ie.iemdb.model.Movie;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;

import java.sql.SQLException;
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
    public List<MovieBriefDTO> getMoviesDTOList() throws ObjectNotFoundException, SQLException {
        var movies = MovieRepo.getInstance().getAllElements();
        List<MovieBriefDTO> moviesDTO = new ArrayList<>();
        movies.forEach(movie -> moviesDTO.add(movie.getShortDTO()));
        return moviesDTO;
    }

    public MovieDTO getMovieDTO(Integer movieId) throws ObjectNotFoundException, SQLException {
        var movie = MovieRepo.getInstance().getElementById(movieId);
        return getMovieDTO(movie);
    }

    public MovieDTO rateMovie(Integer movieId, int rate) throws CustomException, SQLException {
        var ratingUser = UserRepo.loggedInUser;
        var movie = MovieRepo.getInstance().getElementById(movieId);
        movie.updateMovieRating(ratingUser.getId(), rate);
        MovieRepo.getInstance().rateMovie(movieId, ratingUser.getId(), rate);
        return getMovieDTO(movie);
    }
    private MovieDTO getMovieDTO(Movie movie) throws SQLException {
        var DTO = movie.getDTO();
        if(UserRepo.loggedInUser != null){
            DTO.setUserRate(movie.getUserRate(UserRepo.loggedInUser.getId()));
        }
        return DTO;
    }
}
