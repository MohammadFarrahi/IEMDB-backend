package ie.film;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.types.Email;
import ie.user.User;

import java.util.Date;
import java.util.HashSet;

public class Comment {
    // TODO: Handle this properly (attribute below)
    public static final HashSet<String> commentJsonFieldNames = new HashSet<String>(){{
        add("userEmail");
        add("movieId");
        add("text");
    }};
    private String id;
    private Date createdDate;
    private Film commentFilm;
    private User commentOwner;
    private String text;

    @JsonCreator
    public Comment(String id, Film commentFilm, User commentOwner, String text) {
        this.id = id;
        this.createdDate = new Date();
        this.commentOwner = commentOwner;
        this.commentFilm = commentFilm;
        this.text = text;
    }
}