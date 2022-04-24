package ie.iemdb.repository;


import ie.iemdb.exception.AgeLimitException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;

import kotlin.Pair;

import java.util.*;

public class UserRepo extends Repo<User> {
    private static UserRepo instance = null;

    public static UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }
    private UserRepo() {
        this.notFoundException = new UserNotFoundException();
    }

    @Override
    public String addElement(User newObject) throws CustomException {
        var objectId = newObject.getId();
        if (isIdValid(objectId)) {
            throw new CustomException("ObjectAlreadyExists");
        }
        this.objectMap.put(objectId, newObject);
        return objectId;
    }
    @Override
    public String updateElement(User newObject) throws UserNotFoundException {
        var objectId = newObject.getId();
        if (!isIdValid(objectId)) {
            throw new UserNotFoundException();
        }
        objectMap.put(objectId, newObject);
        return objectId;
    }


    public ArrayList<String> getWatchList(String userId) throws CustomException {
        return getElementById(userId).getWatchList();
    }

    public void addToWatchList(String userId, String movieId) throws CustomException {
        var user = getElementById(userId);
        if (!user.isOlderThan(MovieRepo.getInstance().getElementById(movieId).getAgeLimit()))
            throw new AgeLimitException();

        user.addToWatchList(movieId);
    }
    public void removeFromWatchList(String userId, String movieId) throws CustomException {
        var user = getElementById(userId);
        user.removeFromWatchList(movieId);
    }

    public ArrayList<String> getRecommendedWatchlist(User user){
        ArrayList <Pair <String, Double>> scoreMovieList = new ArrayList<>();

        try {
            var movies = MovieRepo.getInstance().getElementsById(null);
            var watchListIds = user.getWatchList();
            var watchListMovies = MovieRepo.getInstance().getElementsById(watchListIds);
            for(var movie : movies) {
                if (watchListIds.contains(movie.getId().toString())) { continue; }
                scoreMovieList.add(new Pair<>(movie.getId().toString(), calMovieScore(movie, watchListMovies)));
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }

        // sorting by score and returning best 3
        Collections.sort(scoreMovieList, new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(final Pair<String, Double> o1, final Pair<String, Double> o2) {
                return o2.getSecond().compareTo(o1.getSecond());
            }
        });
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < scoreMovieList.size(); i++){
            if(i > 2)
                break;
            result.add(scoreMovieList.get(i).getFirst());
        }

        return result;
    }

    public Double calMovieScore(Movie movie, List<Movie> movies){
        double score = 0;
        score += movie.getBaseScoreForWatchList();
        double similarity = 0;
        for(var wMovie : movies){
            similarity += movie.getSameGenre(wMovie);
        }
        score += 3 * similarity;
        return score;
    }

}
