package ie.actor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.types.Constant;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Actor {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String nationality;

    // For jackson usage
    @JsonProperty(value= Constant.Actor.ID, required = true)
    private void setId(String id) throws Exception {
        if(Integer.parseInt(id) < 1) {
            throw new Exception("invalid actor id");
        }
        this.id = id;
    }
    @JsonProperty(value= Constant.Actor.NAME, required = true)
    private void setName(String name) {
        this.name = name;
    }
    @JsonProperty(value= Constant.Actor.B_DATE, required = true)
    private void setBirthDate(String birthDate) {
        this.birthDate = LocalDate.parse(birthDate);
    }
    @JsonProperty(value= Constant.Actor.NATION, required = true)
    private void setNationality(String nationality) {
        this.nationality = nationality;
    }
    @JsonGetter(Constant.Actor.ID)
    private String getId() {
        return id;
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
    private Actor(){}

    public Actor (
             String id,
             String name,
             String birthDate,
             String nationality
    ) throws Exception {
        // TODO: handle validation on name and nationality
        setId(id);
        setName(name);
        setBirthDate(birthDate);
        setNationality(nationality);
    }

    @Override
    public String toString() {
        ObjectMapper serializer = new ObjectMapper();
        try {
            return serializer.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
