package ie.iemdb.model;


import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.InvalidVoteValueException;
import ie.iemdb.model.DTO.CommentDTO;

import java.time.LocalDate;
import java.util.HashMap;

public class Comment {
    private String id;
    private User commentOwner;
    private String text;
    private Integer commentLikes;
    private Integer commentDislikes;
    private LocalDate createdDate;
    private Movie commentMovie;

    private HashMap<String, Short> userVoteMap;


    public String getCommentOwnerNickName() {
        return commentOwner.getNickname();
    }

    public Comment ( Movie commentMovie, User commentOwner, String text) {
        this.createdDate = LocalDate.now();
        this.id = null;
        this.userVoteMap = new HashMap<>();
        this.commentLikes = 0;
        this.commentDislikes = 0;

        this.commentMovie = commentMovie;
        this.commentOwner = commentOwner;
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

    public Movie getMovie(){
        return this.commentMovie;
    }


    public boolean setId(Integer id) {
        if (this.id == null) {
            this.id = id.toString();
            return true;
        }
        return false;
    }
    public CommentDTO getDTO () {
        var DTO = new CommentDTO();
        DTO.setId(Integer.parseInt(id));
        DTO.setCommentDislikes(commentDislikes);
        DTO.setCommentLikes(commentLikes);
        DTO.setCommentMovieId(Integer.parseInt(commentMovie.getId()));
        DTO.setCommentOwnerId(commentOwner.getId());
        DTO.setCommentOwnerName(commentOwner.getName());
        DTO.setCreatedDate(createdDate);
        DTO.setText(text);
        return DTO;
    }
}