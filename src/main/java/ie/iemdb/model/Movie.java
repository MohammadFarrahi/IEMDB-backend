package ie.iemdb.model;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.InvalidRateScoreException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Movie {
    private String id;
    private String name;
    private String summary;
    private LocalDate releaseDate;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<Actor> cast;
    private Double imdbRate;
    private Double averageRating;
    private Integer duration;
    private Integer ageLimit;

    private String coverImgUrl;
    private String imgUrl;

    private ArrayList<Comment> comments;
    private HashMap<String, Integer> userRateMap;

    private Movie(
            String id,
            String name,
            String summary,
            String director,
            ArrayList<Actor> cast,
            String releaseDate,
            ArrayList<String> writers,
            ArrayList<String> genres,
            Integer ageLimit,
            Integer duration,
            Double imdbRate, 
            String coverImgUrl,
            String imgUrl) {

        this.userRateMap = new HashMap<>();
        this.comments = new ArrayList<>();
        this.averageRating = null;
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.director = director;
        this.cast = cast;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.writers = writers;
        this.genres = genres;
        this.ageLimit = ageLimit;
        this.duration = duration;
        this.imdbRate = imdbRate;
        this.imgUrl = imgUrl;
        this.coverImgUrl = coverImgUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getReleaseDate() {
        return this.releaseDate.toString();
    }

    public ArrayList<String> getGenres() {
        return this.genres;
    }

    public Double getAverageRating() {
        return this.averageRating == null ? 0.0 : this.averageRating;
    }

    public Double getImdbRate() {
        return imdbRate;
    }

    public String getId() {
        return this.id;
    }

    public Integer getAgeLimit() {
        return this.ageLimit;
    }

    public ArrayList<Actor> getCast() {
        return this.cast;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public boolean includeGenre(String genre) {
        return genres.contains(genre);
    }

    public void updateMovieRating(String userEmail, int rate) throws CustomException {
        if (!(1 <= rate && rate <= 10)) {
            throw new InvalidRateScoreException();
        }
        averageRating = averageRating == null ? Double.valueOf(0) : averageRating;
        double sumOfRates = averageRating * userRateMap.size();
        sumOfRates = sumOfRates - userRateMap.getOrDefault(userEmail, 0);
        userRateMap.put(userEmail, rate);
        averageRating = (sumOfRates + rate) / userRateMap.size();
        averageRating = Math.floor(averageRating * 10) / 10;
    }

    public boolean isCreatedBefore(int year) throws CustomException {
        if (!(0 <= year && year <= 9999)) {
            throw new CustomException("InvalidYear");
        }
        return releaseDate.getYear() < year;
    }

    public boolean isCreatedAfter(int year) throws CustomException {
        if (!(0 <= year && year <= 9999)) {
            throw new CustomException("InvalidYear");
        }
        return releaseDate.getYear() > year;
    }

    public Double getBaseScoreForWatchList() {
        return getImdbRate() + getAverageRating();
    }

    public Integer getSimilarGenreCount(Movie other) {
        int count = 0;
        for (var genre : genres) {
            if (other.getGenres().contains(genre))
                count += 1;
        }
        return count;
    }
}
