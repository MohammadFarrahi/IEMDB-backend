package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.exception.UserAlreadyExistsException;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.UserDTO;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ie.iemdb.security.PasswordEncoder;
import kotlin.Pair;

public class UserDomainManager {

  private static UserDomainManager instance;
  public static UserDomainManager getInstance() {
    if (instance == null) {
      instance = new UserDomainManager();
    }
    return instance;
  }

  public List<MovieBriefDTO> getRecommendedWatchlist(String userId) throws ObjectNotFoundException, SQLException {
    var scoreMovieList = makeMovieScorePairs(UserRepo.getInstance().getElementById(userId));
    scoreMovieList = getSortedMovieScorePairs(scoreMovieList);
    var recommendedMovies = getTopThreeMovies(scoreMovieList);

    List<MovieBriefDTO> DTOList = new ArrayList<>();
    recommendedMovies.forEach(movie -> DTOList.add(movie.getShortDTO()));
    return DTOList;
  }

  private ArrayList<Pair<Movie, Double>> makeMovieScorePairs(User user) throws SQLException {
    ArrayList<Pair<Movie, Double>> scoreMovieList = new ArrayList<>();
    var movies = MovieRepo.getInstance().getAllElements();
    var watchList = user.getWatchList();
    for (var movie : movies) {
      if (user.hasMovieInWatchList(movie))
        continue;

      scoreMovieList.add(new Pair<>(movie, calMovieScore(movie, watchList)));
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

  public void loginUser(String userEmail, String userPassword) throws ObjectNotFoundException, SQLException {
      var user = UserRepo.getInstance().getElementById(userEmail);
      if(!user.checkPassword(userPassword)) {
        throw new ObjectNotFoundException();
      }
  }

//  public void logoutUser() {
//    UserRepo.getInstance().logoutUser();
//  }

  public List<MovieBriefDTO> getWatchlistDTO(String userId) throws CustomException, SQLException {
    List<MovieBriefDTO> DTOList = new ArrayList<>();
      var watchlist = UserRepo.getInstance().getWatchList(userId);
      watchlist.forEach(watchlistItem -> DTOList.add(watchlistItem.getShortDTO()));
      return DTOList;
  }

  public MovieBriefDTO addToWatchlist(String userId, Integer movieId) throws CustomException, SQLException {
    var user = UserRepo.getInstance().getElementById(userId);
    var movie = MovieRepo.getInstance().getElementById(movieId);
    UserRepo.getInstance().addToWatchList(user, movie);

    return movie.getShortDTO();
  }

  public void removeFromWatchList(String userId, Integer movieId) throws ObjectNotFoundException, SQLException {
    var user = UserRepo.getInstance().getElementById(userId);
    UserRepo.getInstance().removeFromWatchList(user, movieId);
  }

  public void registerNewUser(UserDTO userInfo) throws CustomException, SQLException {
    var newUser = new User(userInfo.getEmail(), PasswordEncoder.encode(userInfo.getPassword()), userInfo.getNickname(), userInfo.getName(), userInfo.getBirthDate());
    try {
      if(UserRepo.getInstance().getElementById(newUser.getId()) != null) {
        throw new UserAlreadyExistsException();
      }
    } catch (ObjectNotFoundException e) {
      UserRepo.getInstance().addElement(newUser);
    }
  }

    public void registerOrLoinUser(UserDTO userInfo) throws CustomException, SQLException {
      var newUser = new User(userInfo.getEmail(), userInfo.getPassword(), userInfo.getNickname(), userInfo.getName(), userInfo.getBirthDate());
      try {
        if(UserRepo.getInstance().getElementById(newUser.getId()) != null) {
          UserRepo.getInstance().updateElement(newUser);
        }
      } catch (ObjectNotFoundException e) {
        UserRepo.getInstance().addElement(newUser);
      }
  }
}
