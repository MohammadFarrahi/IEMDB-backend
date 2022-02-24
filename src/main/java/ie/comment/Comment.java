package ie.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.film.Film;
import ie.types.Constant;
import ie.types.Email;
import ie.user.User;

import java.time.LocalDate;
import java.util.Date;

public class Comment {
    private String id;
    private LocalDate createdDate;
    private String commentFilm;
    private String commentOwner;
    private String text;

    @JsonCreator
    private Comment() {}

    @JsonProperty(value = Constant.Comment.ID, required = true)
    private void setId(String id) {
        this.id = id;
    }

    @JsonProperty(value = Constant.Comment.M_ID, required = true)
    private void setCommentFilm(String id) {
        this.commentFilm = id;
    }

    @JsonProperty(value = Constant.Comment.U_ID, required = true)
    private void setCommentOwner(String id) {
        this.commentOwner = id;
    }

    @JsonProperty(value = Constant.Comment.CONTENT, required = true)
    private void setText(String text) {
        this.text = text;
    }

    @JsonProperty(value = Constant.Comment.C_DATE, required = true)
    private void setCreatedDate(String createdDate){
        this.createdDate = LocalDate.parse(createdDate);
    }

}