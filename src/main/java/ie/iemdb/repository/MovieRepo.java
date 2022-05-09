package ie.iemdb.repository;

import ie.iemdb.exception.*;
import ie.iemdb.model.Movie;
import ie.iemdb.util.types.Constant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.util.stream.Collectors;

public class MovieRepo extends Repo<Movie, Integer> {
    private static MovieRepo instance = null;
    public static final String CAST_TABLE = "Cast";

    public static final String MOVIE_TABLE = "Movie";


    private String nameFilter;
    private String sortType;
    private boolean filterFlag;
    private boolean sortFlag;

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
        filterFlag = true;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
        sortFlag = true;
    }

    public List<Movie> fetchMoviesForUser() throws SQLException {
            var movies = getAllElements();
            if (filterFlag) {
                movies = filterElementsByName(movies, nameFilter);
                filterFlag = false;
            }
            if (sortFlag) {
                movies = sortElements(movies, sortType);
                sortFlag = false;
            }
            return movies;
    }

    public List<Movie> filterElementsByName(List<Movie> movies, String name) {
        if (name == null)
            return movies;
        return movies.stream().filter(movie -> movie.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> sortElements(List<Movie> movies, String type) {
        if (type == null)
            return movies;
        switch (type) {
            case Constant.ActionType.SORT_IMDB ->
                Collections.sort(movies, (f1, f2) -> f2.getImdbRate().compareTo(f1.getImdbRate()));
            case Constant.ActionType.SORT_DATE ->
                Collections.sort(movies, (f1, f2) -> f2.getReleaseDate().compareTo(f1.getReleaseDate()));
            default -> {
            }
        }
        return movies;
    }

    public static MovieRepo getInstance() {
        if (instance == null) {
            instance = new MovieRepo();
        }
        return instance;
    }

    private MovieRepo() {
        nameFilter = null;
        sortType = null;
        filterFlag = false;
        sortFlag = false;
        this.notFoundException = new MovieNotFoundException();
    }

    @Override
    protected String getGetElementByIdStatement() {
        return null;
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, Integer id) {

    }

    @Override
    protected String getGetAllElementsStatement() {
        return null;
    }

    @Override
    protected Movie convertResultSetToDomainModel(ResultSet rs) {
        return null;
    }

    @Override
    protected ArrayList<Movie> convertResultSetToDomainModelList(ResultSet rs) {
        return null;
    }

    @Override
    protected String getAddElementStatement(){
        return null;
    }

    @Override
    public void addElement(Movie newObject) throws SQLException {
    }


    public ArrayList<Movie> filterElementsByGenre(String genre, List<Movie> elements) {
        try {
            ArrayList<Movie> filteredList = new ArrayList<>();
            for (var element : elements) {
                if (element.includeGenre(genre))
                filteredList.add(element);
            }
            return filteredList;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Movie> filterElementsByYear(int from, int to, List<Movie> elements) throws CustomException {
        var filteredList = new ArrayList<Movie>();
        for (var element : elements) {
            if (!element.isCreatedBefore(from) && !element.isCreatedAfter(to)) {
                filteredList.add(element);
            }
        }
        return filteredList;
    }

    public ArrayList<Movie> getMoviesForActor(int actorId){
        return new ArrayList<>();
    }

    public ArrayList<Movie> getWatchlistForUser(String username) {
        return new ArrayList<>();
    }

    public Movie getMovieForComment(int commentId) {
        return null;
    }

        // public void rateMovie(String movieId, String userEmail, int rate) throws CustomException {
    //     if (!UserRepo.getInstance().isIdValid(userEmail)) {
    //         throw new UserNotFoundException();
    //     }
    //     getElementById(movieId).updateMovieRating(userEmail, rate);
    // }
}
