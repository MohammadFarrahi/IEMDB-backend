package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.ResponseDTO;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserDomainManager {

  private static UserDomainManager instance;
  public static UserDomainManager getInstance() {
    if (instance == null) {
      instance = new UserDomainManager();
    }
    return instance;
  }

  public ArrayList<Movie> getRecommendedWatchlist(User user) {
    var scoreMovieList = makeMovieScorePairs(user);
    scoreMovieList = getSortedMovieScorePairs(scoreMovieList);
    return getTopThreeMovies(scoreMovieList);
  }

  private ArrayList<Pair<Movie, Double>> makeMovieScorePairs(User user) {
    ArrayList<Pair<Movie, Double>> scoreMovieList = new ArrayList<>();
    try {
      var movies = MovieRepo.getInstance().getElementsById(null);
      var watchList = user.getWatchList();
      for (var movie : movies) {
        if (user.hasMovieInWatchList(movie))
          continue;

        scoreMovieList.add(new Pair<>(movie, calMovieScore(movie, watchList)));
      }
    } catch (ObjectNotFoundException e) {
      e.printStackTrace();
    }
    return scoreMovieList;
  }

  private ArrayList<Pair<Movie, Double>> getSortedMovieScorePairs(ArrayList<Pair<Movie, Double>> scoreMovieList){
    Collections.sort(scoreMovieList, new Comparator<Pair<Movie, Double>>() {
      @Override
      public int compare(final Pair<Movie, Double> o1, final Pair<Movie, Double> o2) {
        return o2.getSecond().compareTo(o1.getSecond());
      }
    });
    return scoreMovieList;
  }

  private ArrayList<Movie> getTopThreeMovies(ArrayList<Pair<Movie, Double>> scoreMovieList){
    ArrayList<Movie> result = new ArrayList<>();
    for (int i = 0; i < scoreMovieList.size(); i++) {
      if (i > 2)
        break;
      result.add(scoreMovieList.get(i).getFirst());
    }

    return result;
  }

  private Double calMovieScore(Movie movie, List<Movie> movies) {
    double score = 0;
    score += movie.getBaseScoreForWatchList();
    double similarity = 0;
    for (var wMovie : movies) {
      similarity += movie.getSimilarGenreCount(wMovie);
    }
    score += 3 * similarity;
    return score;
  }

  public ResponseDTO loginUser(String userEmail, String userPassword) {
    try {
      var user = UserRepo.getInstance().getElementById(userEmail);
      if(!user.checkPassword(userPassword)) {
        throw new ObjectNotFoundException();
      }
      UserRepo.getInstance().loginUser(user);
      return new ResponseDTO(true, "Okeb");
    } catch (ObjectNotFoundException e) {
      return new ResponseDTO(false, "InvalidCredential");
    }
  }

  public ResponseDTO logoutUser() {
    UserRepo.getInstance().logoutUser();
    return new ResponseDTO(true, "Okeb");
  }

  public List<MovieBriefDTO> getWatchlistDTO(String userId) throws CustomException {
    List<MovieBriefDTO> DTOList = new ArrayList<>();
      var watchlist = UserRepo.getInstance().getWatchList(userId);
      watchlist.forEach(watchlistItem -> DTOList.add(watchlistItem.getShortDTO()));
      return DTOList;
  }

  public boolean isloggedIn(String userId) {
    return UserRepo.loggedInUser.getId().equals(userId);
  }

  public ResponseDTO addToWatchlist(String userId, String movieId) throws CustomException {
    var user = UserRepo.getInstance().getElementById(userId);
    var movie = MovieRepo.getInstance().getElementById(movieId);
    UserRepo.getInstance().addToWatchList(user, movie);

    return new ResponseDTO(true, "Okeb");
  }

  public ResponseDTO removeFromWatchList(String userId, String movieId) throws CustomException {
    var user = UserRepo.getInstance().getElementById(userId);
    UserRepo.getInstance().removeFromWatchList(user, movieId);
    return new ResponseDTO(true, "Okeb");
  }
}
