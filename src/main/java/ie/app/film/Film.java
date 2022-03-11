package ie.app.film;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.exception.CustomException;
import ie.exception.InvalidRateScoreException;
import ie.util.types.Constant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Film {
    private String id;
    private String name;
    private String summary;
    private LocalDate releaseDate;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<String> cast;
    private Double imdbRate;
    private Double averageRating;
    private Integer duration;
    private Integer ageLimit;

    private ArrayList<String> comments;
    private HashMap<String, Integer> userRateMap;

    // for jackson serialization
    @JsonGetter(Constant.Movie.NAME)
    public String getName() {
        return this.name;
    }
    @JsonGetter(Constant.Movie.SUMM)
    public String getSummary() {
        return this.summary;
    }
    @JsonGetter(Constant.Movie.R_DATE)
    public String getReleaseDate() {
        return this.releaseDate.toString();
    }
    @JsonGetter(Constant.Movie.DIRECTOR)
    public String getDirector() {
        return this.director;
    }
    @JsonGetter(Constant.Movie.WRITERS)
    public ArrayList<String> getWriters() {
        return this.writers;
    }
    @JsonGetter(Constant.Movie.GENRE)
    public ArrayList<String> getGenres() {
        return this.genres;
    }
    @JsonGetter(Constant.Movie.RATING)
    public Double getAverageRating() {
        return this.averageRating == null ? 0.0 : this.averageRating;
    }
    @JsonGetter(Constant.Movie.DURATION)
    public Integer getDuration() {
        return this.duration;
    }
    @JsonGetter(Constant.Movie.IMDB)
    public Double getImdbRate() { return imdbRate; }

    @JsonGetter(Constant.Movie.ID_G)
    public Integer getId() {
        return Integer.parseInt(this.id);
    }
    @JsonGetter(Constant.Movie.AGE_L)
    public Integer getAgeLimit() {
        return this.ageLimit;
    }
    @JsonGetter(Constant.Movie.CAST)
    public ArrayList<String> getCast() {
        return this.cast;
    }
    @JsonGetter(Constant.Movie.COMMENTS)
    public ArrayList<String> getComments() {
        return this.comments;
    }


    @JsonCreator
    private Film (
            @JsonProperty(value=Constant.Movie.ID_S, required = true) String id,
            @JsonProperty(value=Constant.Movie.NAME, required = true) String name,
            @JsonProperty(value=Constant.Movie.SUMM, required = true) String summary,
            @JsonProperty(value=Constant.Movie.DIRECTOR, required = true) String director,
            @JsonProperty(value=Constant.Movie.CAST, required = true) ArrayList<String> cast,
            @JsonProperty(value =Constant.Movie.R_DATE, required = true) String releaseDate,
            @JsonProperty(value=Constant.Movie.WRITERS, required = true) ArrayList<String> writers,
            @JsonProperty(value=Constant.Movie.GENRE, required = true) ArrayList<String> genres,
            @JsonProperty(value=Constant.Movie.AGE_L, required = true) Integer ageLimit,
            @JsonProperty(value=Constant.Movie.DURATION, required = true) Integer duration,
            @JsonProperty(value=Constant.Movie.IMDB, required = true) Double imdbRate) {
        userRateMap = new HashMap<>();
        comments = new ArrayList<>();
        averageRating = null;

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
    }

    public void addCommentId(String commentId) {
        this.comments.add(commentId);
    }

    public boolean includeGenre(String genre) {
        return genres.contains(genre);
    }

    public void updateFilmRating(String userEmail, int rate) throws CustomException {
        if (!(1 <= rate && rate <= 10)) { throw new InvalidRateScoreException(); }
        averageRating = averageRating == null ? Double.valueOf(0) : averageRating;
        double sumOfRates = averageRating * userRateMap.size();
        sumOfRates = sumOfRates - userRateMap.getOrDefault(userEmail, 0);
        userRateMap.put(userEmail, rate);
        averageRating = (sumOfRates + rate) / userRateMap.size();
        averageRating = Math.floor(averageRating * 10) / 10;
    }

    public boolean isCreatedBefore(int year) throws CustomException {
        if(!(0 <= year && year <= 9999)) {
            throw new CustomException("InvalidYear");
        }
        return releaseDate.getYear() < year;
    }
    public boolean isCreatedAfter(int year) throws CustomException {
        if(!(0 <= year && year <= 9999)) {
            throw new CustomException("InvalidYear");
        }
        return releaseDate.getYear() > year;
    }

    public Double getBaseScoreForWatchList(){
        return getImdbRate() + getAverageRating();
    }

    public Integer getSameGenre(Film other){
        int count = 0;
        for(var genre : genres){
            if(other.getGenres().contains(genre))
                count += 1;
        }
        return count;
    }
}
