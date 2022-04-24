package ie.iemdb.model;


import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.InvalidVoteValueException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.repository.UserRepo;
import java.time.LocalDate;
import java.util.HashMap;

public class Comment {
    private String id;
    private String commentOwner;
    private String text;
    private Integer commentLikes;
    private Integer commentDislikes;
    private LocalDate createdDate;
    private String commentMovie;
    private HashMap<String, Short> userVoteMap;


    public String getCommentOwnerNickName() {
        try {
            return UserRepo.getInstance().getElementById(commentOwner).getNickname();
        } catch (ObjectNotFoundException e) {
            return "SomeCoolGuy";
        }
    }

    public Comment ( String commentMovieId, String commentOwnerId, String text) {
        this.createdDate = LocalDate.now();
        this.id = null;
        this.userVoteMap = new HashMap<>();
        this.commentLikes = 0;
        this.commentDislikes = 0;

        this.commentMovie = commentMovieId;
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

    public String getCommentOwner() {
      return null;
    }

    public String getCommentMovie() {
        return this.commentMovie;
    }
}