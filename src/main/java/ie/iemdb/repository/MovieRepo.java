package ie.iemdb.repository;

import ie.iemdb.exception.*;
import ie.iemdb.model.Movie;
import ie.iemdb.util.types.Constant;

import java.util.*;

import java.util.stream.Collectors;

public class MovieRepo extends Repo<Movie> {
    private static MovieRepo instance = null;

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

    public List<Movie> fetchMoviesForUser() {
        try {
            var movies = getElementsById(null);
            if (filterFlag) {
                movies = filterElementsByName(movies, nameFilter);
                filterFlag = false;
            }
            if (sortFlag) {
                movies = sortElements(movies, sortType);
                sortFlag = false;
            }
            return movies;
        } catch (ObjectNotFoundException e) {
        }
        return null;

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
    public void addElement(Movie newObject) throws CustomException {
        // if (!ActorRepo.getInstance().isIdListValid(newObject.getCast())) {
        //     throw new ActorNotFoundException();
        // }
        var objectId = newObject.getId().toString();
        if (isIdValid(objectId)) {
            throw new MovieAlreadyExistsException();
        }
        this.objectMap.put(objectId, newObject);
        newObject.getCast().forEach(actor -> actor.addToPerformedMovies(newObject));
    }

    @Override
    public void updateElement(Movie newObject) throws CustomException {
        var objectId = newObject.getId();
        if (!isIdValid(objectId)) {
            throw new MovieNotFoundException();
        }
        objectMap.put(objectId, newObject);
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
