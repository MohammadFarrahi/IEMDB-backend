package ie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Actor {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String nationality;
//    private static final String[] jsonFieldNames;

    // For jackson usage
    @JsonProperty(value= "id", required = true)
    private void setId(String id) {
        if(Integer.parseInt(id) < 1) {
            // TODO: throw exception
            System.out.println("id invalid");
        }
        this.id = id;
    }
    @JsonProperty(value= "name", required = true)
    private void setName(String name) {
        this.name = name;
    }
    @JsonProperty(value= "birthDate", required = true)
    private void setBirthDate(String birthDate) {
        try {
            this.birthDate = LocalDate.parse(birthDate);
        }
        catch(DateTimeException e) {
            // TODO: throw exception
            System.out.println("date invalid");
        }
    }
    @JsonProperty(value= "nationality", required = true)
    private void setNationality(String nationality) {
        this.nationality = nationality;
    }
    @JsonCreator
    private Actor(){}

    public Actor (
             String id,
             String name,
             String birthDate,
             String nationality
    ) {
        // TODO: handle validation on name and nationality
        setId(id);
        setName(name);
        setBirthDate(birthDate);
        setNationality(nationality);
    }

    public String getId(){
        return id;
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
}
