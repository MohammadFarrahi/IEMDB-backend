package ie.film;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import ie.Actor;

import java.time.LocalDate;
import java.util.ArrayList;

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
    private Integer duration;
    private Integer ageLimit;

    @JsonCreator
    private Film(){}

    @JsonProperty(value="id", required = true)
    private void setId (String id){
        this.id = id;
    }

    @JsonProperty(value="name", required = true)
    private void setName (String name){
        this.name = name;
    }

    @JsonProperty(value="summary", required = true)
    private void setSummary (String summary){
        this.summary = summary;
    }

    @JsonProperty(value="director", required = true)
    private void setDirector (String director){
        this.director = director;
    }

    @JsonProperty(value="cast", required = true)
    private void setCast (ArrayList<String> cast){
        this.cast = cast;
    }

    @JsonProperty(value = "releaseDate", required = true)
    private void setReleaseDate(String releaseDate){
        this.releaseDate = LocalDate.parse(releaseDate);
    }

    @JsonProperty(value="writers", required = true)
    private void setWriters (ArrayList<String> writers){
        this.writers = writers;
    }

    @JsonProperty(value="genres", required = true)
    private void setGenres (ArrayList<String> genres){
        this.genres = genres;
    }

    @JsonProperty(value="ageLimit", required = true)
    private void setAgeLimit (Integer ageLimit){
        this.ageLimit = ageLimit;
    }

    @JsonProperty(value="duration", required = true)
    private void setDuration (Integer duration){
        this.duration = duration;
    }

    @JsonProperty(value="imdbRate", required = true)
    private void setImdbRate (Float imdbRate){
        this.imdbRate = imdbRate;
    }




}
