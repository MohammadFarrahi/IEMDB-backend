package ie.app.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.exception.CustomException;
import ie.exception.InvalidVoteValueException;
import ie.util.types.Constant;
import java.time.LocalDate;
import java.util.HashMap;

public class Comment {
    private String id;
    private String commentOwner;
    private String text;
    private Integer commentLikes;
    private Integer commentDislikes;
    private LocalDate createdDate;
    private String commentFilm;
    private HashMap<String, Short> userVoteMap;

    // for jackson serialization
    @JsonGetter(Constant.Comment.ID)
    public Integer getId() {
        return Integer.parseInt(id);
    }
    @JsonGetter(Constant.Comment.CONTENT)
    public String getText() {
        return text;
    }
    @JsonGetter(Constant.Comment.LIKES)
    public Integer getCommentLikes() {
        return commentLikes;
    }
    @JsonGetter(Constant.Comment.DISLIKES)
    public Integer getCommentDislikes() {
        return commentDislikes;
    }
    @JsonGetter(Constant.Comment.C_DATE)
    private String getCreatedDate() {
        return createdDate.toString();
    }


    @JsonGetter(Constant.Comment.U_ID)
    public String getCommentOwner() { return commentOwner; }
    @JsonGetter(Constant.Comment.M_ID)
    public String getCommentFilm() {
        return commentFilm;
    }

    @JsonCreator
    private Comment (
            @JsonProperty(value = Constant.Comment.M_ID, required = true) String commentFilmId,
            @JsonProperty(value = Constant.Comment.U_ID, required = true) String commentOwnerId,
            @JsonProperty(value = Constant.Comment.CONTENT, required = true) String text) {
        this.createdDate = LocalDate.now();
        this.id = null;
        this.userVoteMap = new HashMap<>();
        this.commentLikes = 0;
        this.commentDislikes = 0;

        this.commentFilm = commentFilmId;
        this.commentOwner = commentOwnerId;
        this.text = text;
    }

    public void updateCommentVotes(String userId, Integer vote) throws CustomException {
        if (!(-1 <= vote && vote <= 1))
            throw new InvalidVoteValueException();
        if(userVoteMap.containsKey(userId)) {
            var prevVote = userVoteMap.get(userId);
            if (prevVote > 0)
                this.commentLikes -= prevVote;
            else
                this.commentDislikes += prevVote;
        }
        if (vote > 0)
            this.commentLikes += vote;
        else
            this.commentDislikes -= vote;
        userVoteMap.put(userId, vote.shortValue());
    }


    public boolean setId(Integer id) {
        if (this.id == null) {
            this.id = id.toString();
            return true;
        }
        return false;
    }
}