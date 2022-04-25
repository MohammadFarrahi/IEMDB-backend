package ie.iemdb.model.DTO;

import ie.iemdb.model.Movie;
import ie.iemdb.model.User;

import java.time.LocalDate;
import java.util.HashMap;

public class CommentDTO {
    private Integer id;
    private String commentOwnerId;
    private String commentOwnerName;
    private Integer commentMovieId;
    private String text;
    private Integer commentLikes;
    private Integer commentDislikes;
    private LocalDate createdDate;

    public Integer getId() {
        return id;
    }

    public String getCommentOwnerId() {
        return commentOwnerId;
    }

    public String getCommentOwnerName() {
        return commentOwnerName;
    }

    public String getText() {
        return text;
    }

    public Integer getCommentLikes() {
        return commentLikes;
    }

    public Integer getCommentDislikes() {
        return commentDislikes;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentMovieId() {
        return commentMovieId;
    }

    public void setCommentOwnerId(String commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }

    public void setCommentOwnerName(String commentOwnerName) {
        this.commentOwnerName = commentOwnerName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCommentLikes(Integer commentLikes) {
        this.commentLikes = commentLikes;
    }

    public void setCommentDislikes(Integer commentDislikes) {
        this.commentDislikes = commentDislikes;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public void setCommentMovieId(Integer commentMovieId) {
        this.commentMovieId = commentMovieId;
    }
}
