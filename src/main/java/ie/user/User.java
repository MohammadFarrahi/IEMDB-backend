package ie.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.comment.Comment;
import ie.types.Constant;
import ie.types.Email;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    private Email email;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birthDate;

    private ArrayList<Comment> userComments;

    @JsonCreator
    private User(){}

    @JsonProperty(value = Constant.User.E_ID, required = true)
    private void setEmail(String email) throws Exception {
        this.email = new Email(email);
    }

    @JsonProperty(value = Constant.User.PASS, required = true)
    private void setPassword(String password){
        this.password = password;
    }

    @JsonProperty(value = Constant.User.NICKNAME, required = true)
    private void setNickname(String nickname){
        this.nickname = nickname;
    }

    @JsonProperty(value = Constant.User.NAME, required = true)
    private void setName(String name){
        this.name = name;
    }

    @JsonProperty(value = Constant.User.B_DATE, required = true)
    private void setBirthDate(String birthDate){
        this.birthDate = LocalDate.parse(birthDate);
    }

    @JsonGetter(Constant.User.NAME)
    private String getName() {
        return this.name;
    }

    @JsonGetter(Constant.User.E_ID)
    private String getEmail() {
        return this.email.toString();
    }

    @JsonGetter(Constant.User.PASS)
    private String getPassword() {
        return this.password;
    }

    @JsonGetter(Constant.User.NICKNAME)
    private String getNickname() {
        return this.nickname;
    }

    @JsonGetter(Constant.User.B_DATE)
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

    public void addUserComment(Comment newComment) {
        userComments.add(newComment);
    }
}
