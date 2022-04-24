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
        // TODO : validation of sortType is missing
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
    public String addElement(Movie newObject) throws CustomException {
        if (!ActorRepo.getInstance().isIdListValid(newObject.getCast())) {
            throw new ActorNotFoundException();
        }
        var objectId = newObject.getId().toString();
        if (isIdValid(objectId)) {
            throw new MovieAlreadyExistsException();
        }
        this.objectMap.put(objectId, newObject);
        ActorRepo.getInstance().getElementsById(newObject.getCast())
                .forEach(actor -> actor.addToPerformedMovies(newObject.getId().toString()));
        return objectId;
    }

    @Override
    public String updateElement(Movie newObject) throws CustomException {
        if (!ActorRepo.getInstance().isIdListValid(newObject.getCast())) {
            throw new ActorNotFoundException();
        }
        var objectId = newObject.getId().toString();
        if (!isIdValid(objectId)) {
            throw new MovieNotFoundException();
        }
        objectMap.put(objectId, newObject);
        return objectId;
    }

    public ArrayList<String> filterElementsByGenre(String genre) {
        try {
            ArrayList<String> filteredIdList = new ArrayList<>();
            for (var pair : objectMap.entrySet()) {
                if (pair.getValue().includeGenre(genre))
                    filteredIdList.add(pair.getKey());
            }
            return filteredIdList;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<String> filterElementsByYear(int from, int to) throws CustomException {
        var ids = new ArrayList<String>();
        for (var movie : objectMap.entrySet()) {
            if (!movie.getValue().isCreatedBefore(from) && !movie.getValue().isCreatedAfter(to)) {
                ids.add(movie.getKey());
            }
        }
        return ids;
    }

    public void rateMovie(String movieId, String userEmail, int rate) throws CustomException {
        if (!UserRepo.getInstance().isIdValid(userEmail)) {
            throw new UserNotFoundException();
        }
        getElementById(movieId).updateMovieRating(userEmail, rate);
    }

}
