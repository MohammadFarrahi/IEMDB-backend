package ie.iemdb.model.DTO;

import ie.iemdb.model.Actor;
import ie.iemdb.model.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieDTO extends ResponseDTO{
    public MovieDTO() {
        super(true, "OK");
    }
    private Integer id;
    private String name;
    private String summary;
    private LocalDate releaseDate;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<ActorBriefDTO> cast;
    private Double imdbRate;
    private Double averageRating;
    private Integer duration;
    private Integer ageLimit;
    private String coverImgUrl;
    private String imgUrl;
    private ArrayList<CommentDTO> comments;
    private Integer rateCount;
    private Integer UserRate;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public ArrayList<String> getWriters() {
        return writers;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<ActorBriefDTO> getCast() {
        return cast;
    }

    public Double getImdbRate() {
        return imdbRate;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getAgeLimit() {
        return ageLimit;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ArrayList<CommentDTO> getComments() {
        return comments;
    }

    public Integer getRateCount() {
        return rateCount;
    }

    public Integer getUserRate() {
        return UserRate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setWriters(ArrayList<String> writers) {
        this.writers = writers;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void setCast(ArrayList<ActorBriefDTO> cast) {
        this.cast = cast;
    }

    public void setImdbRate(Double imdbRate) {
        this.imdbRate = imdbRate;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setAgeLimit(Integer ageLimit) {
        this.ageLimit = ageLimit;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setComments(ArrayList<CommentDTO> comments) {
        this.comments = comments;
    }

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
    }

    public void setUserRate(Integer userRate) {
        UserRate = userRate;
    }
}
