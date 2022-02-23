package ie.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.types.Email;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDate;

public class User {
    private Email email;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birthDate;

    @JsonCreator
    private User(){}

    @JsonProperty(value = "email", required = true)
    private void setEmail(String email) throws Exception {
        this.email = new Email(email);
    }

    @JsonProperty(value = "password", required = true)
    private void setPassword(String password){
        this.password = password;
    }

    @JsonProperty(value = "nickname", required = true)
    private void setNickname(String nickname){
        this.nickname = nickname;
    }

    @JsonProperty(value = "name", required = true)
    private void setName(String name){
        this.name = name;
    }

    @JsonProperty(value = "birthDate", required = true)
    private void setBirthDate(String birthDate){
        this.birthDate = LocalDate.parse(birthDate);
    }

    @JsonGetter("name")
    private String getName() {
        return this.name;
    }

    @JsonGetter("email")
    private String getEmail() {
        return this.email.toString();
    }

    @JsonGetter("password")
    private String getPassword() {
        return this.password;
    }

    @JsonGetter("nickname")
    private String getNickname() {
        return this.nickname;
    }

    @JsonGetter("birthDate")
    private String getBirthDate() {
        return this.birthDate.toString();
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this) ;
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
