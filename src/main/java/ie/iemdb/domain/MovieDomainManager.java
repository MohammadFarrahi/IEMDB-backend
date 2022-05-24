package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.MovieDTO;
import ie.iemdb.model.Movie;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;

import java.sql.SQLException;
import java.time.LocalDate;
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
        return getMoviesDTO(movies);
    }
//    public List<MovieBriefDTO> get
    public MovieDTO getMovieDTO(Integer movieId, String userId) throws ObjectNotFoundException, SQLException {
        var movie = MovieRepo.getInstance().getElementById(movieId);
        return getMovieDTO(movie, userId);
    }

    public MovieDTO rateMovie(Integer movieId, int rate, String userId) throws CustomException, SQLException {
        var movie = MovieRepo.getInstance().getElementById(movieId);
        movie.updateMovieRating(userId, rate);
        MovieRepo.getInstance().rateMovie(movieId, userId, rate);
        return getMovieDTO(movie, userId);
    }
    private MovieDTO getMovieDTO(Movie movie, String userId) throws SQLException {
        var DTO = movie.getDTO();
        if(userId != null){
            DTO.setUserRate(movie.getUserRate(userId));
        }
        return DTO;
    }
    private List<MovieBriefDTO> getMoviesDTO(List<Movie> movies) throws SQLException {
        List<MovieBriefDTO> moviesDTO = new ArrayList<>();
        movies.forEach(movie -> moviesDTO.add(movie.getShortDTO()));
        return moviesDTO;
    }

    public List<MovieBriefDTO> getFilteredMoviesByName(String filterValue) throws SQLException {
        var movies = MovieRepo.getInstance().getFilteredElementsByName(filterValue);
        return getMoviesDTO(movies);
    }

    public List<MovieBriefDTO> getFilteredMoviesByGenre(String filterValue) throws SQLException {
        var movies = MovieRepo.getInstance().getFilteredElementsByGenre(filterValue);
        return getMoviesDTO(movies);
    }

    public List<MovieBriefDTO> getFilteredMoviesByYear(Integer from, Integer to) throws CustomException, SQLException {
        if(to < from) throw new CustomException("Invalid date points");
        var movies = MovieRepo.getInstance().getFilteredElementsByYear(from, to);
        return getMoviesDTO(movies);
    }
}
