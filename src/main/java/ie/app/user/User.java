package ie.app.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.exception.CustomException;
import ie.exception.MovieAlreadyExistsException;
import ie.exception.MovieNotFoundException;
import ie.util.types.Constant;
import ie.util.types.Email;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class User {
    private Email email;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birthDate;

    private ArrayList<String> watchList;

    // for jackson serialization
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
    public String getNickname() {
        return this.nickname;
    }
    @JsonGetter(Constant.User.B_DATE)
    private String getBirthDate() {
        return this.birthDate.toString();
    }

    @JsonCreator
    private User (
            @JsonProperty(value = Constant.User.E_ID, required = true) String email,
            @JsonProperty(value = Constant.User.PASS, required = true) String password,
            @JsonProperty(value = Constant.User.NICKNAME, required = true) String nickname,
            @JsonProperty(value = Constant.User.NAME, required = true) String name,
            @JsonProperty(value = Constant.User.B_DATE, required = true) String birthDate) throws CustomException {
        this.watchList = new ArrayList<>();

        this.email = new Email(email);
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = LocalDate.parse(birthDate);
    }

    public String getId() { return getEmail(); }

    @JsonIgnore()
    public ArrayList<String> getWatchList() {
        return this.watchList;
    }

    public void addToWatchList(String id) throws CustomException {
        if(watchList.contains(id))
            throw new MovieAlreadyExistsException();
        watchList.add(id);
    }

    public void removeFromWatchList(String id) throws CustomException {
        if(!watchList.contains(id))
            throw new CustomException("MovieNotFoundInWatchList");
        watchList.remove(id);
    }

    public boolean isOlderThan(Integer age) {
        return age <= Period.between(birthDate, LocalDate.now()).getYears();
    }
}
