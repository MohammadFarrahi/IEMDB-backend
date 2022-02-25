package ie.film;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ie.comment.Comment;
import ie.types.Constant;

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
    private Float imdbRate;
    private Double averageRating;
    private Integer duration;
    private Integer ageLimit;

    private ArrayList<String> comments;
    private HashMap<String, Integer> userRateMap;

    @JsonCreator
    private Film(){
        userRateMap = new HashMap<>();
        comments = new ArrayList<>();
        averageRating = null;
    }

    @JsonProperty(value=Constant.Movie.ID_S, required = true)
    private void setId (String id){
        this.id = id;
    }

    @JsonProperty(value=Constant.Movie.NAME, required = true)
    private void setName (String name){
        this.name = name;
    }

    @JsonProperty(value=Constant.Movie.SUMM, required = true)
    private void setSummary (String summary){
        this.summary = summary;
    }

    @JsonProperty(value=Constant.Movie.DIRECTOR, required = true)
    private void setDirector (String director){
        this.director = director;
    }

    @JsonProperty(value=Constant.Movie.CAST, required = true)
    private void setCast (ArrayList<String> cast){
        this.cast = cast;
    }

    @JsonProperty(value =Constant.Movie.R_DATE, required = true)
    private void setReleaseDate(String releaseDate){
        this.releaseDate = LocalDate.parse(releaseDate);
    }

    @JsonProperty(value=Constant.Movie.WRITERS, required = true)
    private void setWriters (ArrayList<String> writers){
        this.writers = writers;
    }

    @JsonProperty(value=Constant.Movie.GENRE, required = true)
    private void setGenres (ArrayList<String> genres){
        this.genres = genres;
    }

    @JsonProperty(value=Constant.Movie.AGE_L, required = true)
    private void setAgeLimit (Integer ageLimit){
        this.ageLimit = ageLimit;
    }

    @JsonProperty(value=Constant.Movie.DURATION, required = true)
    private void setDuration (Integer duration){
        this.duration = duration;
    }

    @JsonProperty(value=Constant.Movie.IMDB, required = true)
    private void setImdbRate (Float imdbRate){
        this.imdbRate = imdbRate;
    }

    @JsonGetter(Constant.Movie.ID_G)
    private Integer getId() {
        return Integer.parseInt(this.id);
    }

    @JsonGetter(Constant.Movie.NAME)
    private String getName() {
        return this.name;
    }

    @JsonGetter(Constant.Movie.SUMM)
    private String getSummary() {
        return this.summary;
    }

    @JsonGetter(Constant.Movie.R_DATE)
    private String getReleaseDate() {
        return this.releaseDate.toString();
    }

    @JsonGetter(Constant.Movie.DIRECTOR)
    private String getDirector() {
        return this.director;
    }

    @JsonGetter(Constant.Movie.WRITERS)
    private ArrayList<String> getWriters() {
        return this.writers;
    }

    @JsonGetter(Constant.Movie.GENRE)
    private ArrayList<String> getGenres() {
        return this.genres;
    }

    @JsonGetter(Constant.Movie.RATING)
    private Double getAverageRating() {
        return this.averageRating;
    }

    @JsonGetter(Constant.Movie.DURATION)
    private Integer getDuration() {
        return this.duration;
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

    public void addCommentId(String commentId) {
        this.comments.add(commentId);
    }

    public boolean includeGenre(String genre) {
        return genres.contains(genre);
    }


    public void updateFilmRating(String userEmail, int rate) throws Exception {
        if (!(1 <= rate && rate <= 10)) { throw new Exception("invalid rate number"); }
        averageRating = averageRating == null ? Double.valueOf(0) : averageRating;
        double sumOfRates = averageRating * userRateMap.size();
        sumOfRates = sumOfRates - userRateMap.getOrDefault(userEmail, 0);
        userRateMap.put(userEmail, rate);
        averageRating = (sumOfRates + rate) / userRateMap.size();
        averageRating = Math.floor(averageRating * 10) / 10;
    }
}
