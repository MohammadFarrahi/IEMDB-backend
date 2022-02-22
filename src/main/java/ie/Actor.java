package ie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Actor {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String nationality;

    @JsonCreator
    public Actor (
            @JsonProperty(value= "id", required = true) String id,
            @JsonProperty(value= "name", required = true) String name,
            @JsonProperty(value= "birthDate", required = true) String birthDate,
            @JsonProperty(value= "nationality", required = true) String nationality
    ) {
        // TODO: logical validation of fileds must be handled
        this.id = id;
        this.name = name;
        this.birthDate = LocalDate.parse(birthDate);
        this.nationality = nationality;
    }
    @Override
    public String toString() {
        return "Actor{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", nationality='" + nationality + '\'' +
                '}';
    }
    // TODO: serialization methods must be added.
    // TODO: update methodes must be added.
}
