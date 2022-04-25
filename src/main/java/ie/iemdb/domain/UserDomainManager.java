package ie.iemdb.domain;

import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;
import ie.iemdb.repository.MovieRepo;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserDomainManager {
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

}
