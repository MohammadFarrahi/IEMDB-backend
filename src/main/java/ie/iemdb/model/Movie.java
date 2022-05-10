package ie.iemdb.model;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.InvalidRateScoreException;
import ie.iemdb.model.DTO.ActorBriefDTO;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.model.DTO.MovieDTO;
import ie.iemdb.repository.Retriever;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Movie {
    private Integer id;
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
    private Retriever retriever;

    public Movie(
            Integer id,
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
        this.releaseDate = LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.writers = writers;
        this.genres = genres;
        this.ageLimit = ageLimit;
        this.duration = duration;
        this.imdbRate = imdbRate;
        this.imgUrl = imgUrl;
        this.coverImgUrl = coverImgUrl;
    }

    // Movie without cast for lazy loading
    public Movie(
            Integer id,
            String name,
            String summary,
            String director,
            String releaseDate,
            ArrayList<String> writers,
            ArrayList<String> genres,
            Integer ageLimit,
            Integer duration,
            Double imdbRate,
            String coverImgUrl,
            String imgUrl,
            HashMap<String, Integer> userRateMap) {

        this.userRateMap = userRateMap;
        this.averageRating = calculateAvgRate();
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.director = director;
        this.releaseDate = LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.writers = writers;
        this.genres = genres;
        this.ageLimit = ageLimit;
        this.duration = duration;
        this.imdbRate = imdbRate;
        this.imgUrl = imgUrl;
        this.coverImgUrl = coverImgUrl;

        this.cast = null;
        this.comments = null;
    }

    private Double calculateAvgRate() {
        int sum = 0;
        for (var value : this.userRateMap.values()) {
            sum += value;
        }
        return (sum / (double) this.userRateMap.values().size());
    }

    public void setRetriever(Retriever retriever) {
        this.retriever = retriever;
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

    public Integer getId() {
        return this.id;
    }

    public Integer getAgeLimit() {
        return this.ageLimit;
    }

    public ArrayList<Actor> getCast() throws SQLException {
        if (this.cast == null)
            this.cast = retriever.getCastForMovie(this.id);
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

    public MovieBriefDTO getShortDTO() {
        var movieBriefDTO = new MovieBriefDTO();
        movieBriefDTO.setId(id);
        movieBriefDTO.setName(name);
        movieBriefDTO.setSummary(summary);
        movieBriefDTO.setImdbRate(imdbRate);
        movieBriefDTO.setCoverImgUrl(coverImgUrl);
        movieBriefDTO.setReleaseDate(releaseDate.toString());
        movieBriefDTO.setDuration(duration);
        movieBriefDTO.setGenres(genres);
        movieBriefDTO.setAverageRating(averageRating);
        movieBriefDTO.setDirector(director);
        return movieBriefDTO;
    }

    public MovieDTO getDTO() throws SQLException {
        var DTO = new MovieDTO();
        DTO.setId(id);
        DTO.setAgeLimit(ageLimit);
        DTO.setAverageRating(averageRating);
        DTO.setCoverImgUrl(coverImgUrl);
        DTO.setImdbRate(imdbRate);
        DTO.setDirector(director);
        DTO.setName(name);
        DTO.setDuration(duration);
        DTO.setGenres(genres);
        DTO.setWriters(writers);
        DTO.setImgUrl(imgUrl);
        DTO.setReleaseDate(releaseDate);
        DTO.setSummary(summary);
        DTO.setRateCount(userRateMap.size());

        var castDTO = new ArrayList<ActorBriefDTO>();
        cast.forEach(actor -> castDTO.add(actor.getBriefDTO()));
        DTO.setCast(castDTO);

        var commentsDTO = new ArrayList<CommentDTO>();
        for (Comment comment : comments) {
            commentsDTO.add(comment.getDTO());
        }
        DTO.setComments(commentsDTO);
        return DTO;
    }

    public Map<String, String> getDBTuple() {
        Map<String, String> tuple = new HashMap<>();
        tuple.put("id", this.id.toString());;
        tuple.put("name", this.name);
        tuple.put("summary", this.summary);
        tuple.put("director", this.director);
        tuple.put("releaseDate", this.releaseDate.toString());
        tuple.put("writers", String.join(",", this.writers));
        tuple.put("ageLimit", this.ageLimit.toString());
        tuple.put("duration", this.duration.toString());
        tuple.put("imdbRate", this.imdbRate.toString());
        tuple.put("coverImgUrl", this.coverImgUrl);
        tuple.put("imgUrl", this.toString());
        return tuple;
    }

    public Integer getUserRate(String userId) {
        if (!userRateMap.containsKey(userId)) {
            return null;
        }
        return userRateMap.get(userId);
    }
}
