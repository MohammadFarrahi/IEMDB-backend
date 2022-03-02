package ie.app.actor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.util.types.Constant;

import java.time.LocalDate;

public class Actor {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String nationality;

    // for jackson serialization
    @JsonGetter(Constant.Actor.ID_G)
    public Integer getId() {
        return Integer.parseInt(id);
    }
    @JsonGetter(Constant.Actor.NAME)
    private String getName() {
        return name;
    }
    @JsonGetter(Constant.Actor.B_DATE)
    private String getBirthDate() {
        return birthDate.toString();
    }
    @JsonGetter(Constant.Actor.NATION)
    private String getNationality() {
        return nationality;
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
    }

}
