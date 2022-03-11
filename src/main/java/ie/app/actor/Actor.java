package ie.app.actor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.util.types.Constant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Actor {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String nationality;
    private ArrayList<String> performedMovies;

    // for jackson serialization
    @JsonGetter(Constant.Actor.ID_G)
    public Integer getId() {
        return Integer.parseInt(id);
    }
    @JsonGetter(Constant.Actor.NAME)
    public String getName() {
        return name;
    }
    @JsonGetter(Constant.Actor.B_DATE)
    public String getBirthDate() {
        return birthDate.toString();
    }
    @JsonGetter(Constant.Actor.NATION)
    public String getNationality() {
        return nationality;
    }
    @JsonGetter(Constant.Actor.MOVIES)
    public List<String> getPerformedMovies() {
        return performedMovies;
    }

    @JsonCreator
    private Actor (
            @JsonProperty(value= Constant.Actor.ID_S, required = true) String id,
            @JsonProperty(value= Constant.Actor.NAME, required = true) String name,
            @JsonProperty(value= Constant.Actor.B_DATE, required = true) String birthDate,
            @JsonProperty(value= Constant.Actor.NATION, required = true) String nationality) {
        this.id = id;
        this.name = name;
        this.birthDate = LocalDate.parse(birthDate);
        this.nationality = nationality;
        this.performedMovies = new ArrayList<>();
    }

    public void addToPerformedMovies(String id) {
        performedMovies.add(id);
    }
}
