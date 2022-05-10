package ie.iemdb.model;


import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.InvalidVoteValueException;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.repository.Retriever;

import javax.print.DocFlavor;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Comment {
    private Integer id;
    private User commentOwner = null;
    private String text;
    private Integer commentLikes;
    private Integer commentDislikes;
    private LocalDate createdDate;
    private Movie commentMovie = null;

    private HashMap<String, Short> userVoteMap;
    private Retriever retriever;


    public String getCommentOwnerNickName() throws SQLException {
        return getCommentOwner().getNickname();
    }

    private User getCommentOwner() throws SQLException {
        if(this.commentOwner == null)
            this.commentOwner = this.retriever.getUserForComment(this.id);
        return this.commentOwner;
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

    public Comment (Integer id, String text, String createdDate, HashMap<String, Short> userVoteMap) {
        this.createdDate = LocalDate.parse(createdDate);
        this.id = id;
        this.userVoteMap = userVoteMap;
        this.text = text;
        setLikesAndDislikes();
    }

    private void setLikesAndDislikes(){
        int likes = 0;
        int dislikes = 0;
        for(var value : userVoteMap.values()){
            if(value == 1)
                likes++;
            else if(value == -1)
                dislikes++;
        }
        this.commentDislikes = dislikes;
        this.commentLikes = likes;
    }

    public void setRetriever(Retriever retriever){
        this.retriever = retriever;
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

    public Movie getMovie() throws SQLException {
        if(this.commentMovie == null)
            this.commentMovie = this.retriever.getMovieForComment(this.id);
        return this.commentMovie;
    }


    public boolean setId(Integer id) {
        if (this.id == null) {
            this.id = id;
            return true;
        }
        return false;
    }
    public CommentDTO getDTO () throws SQLException {
        var DTO = new CommentDTO();
        DTO.setId(id);
        DTO.setCommentDislikes(commentDislikes);
        DTO.setCommentLikes(commentLikes);
        DTO.setCommentMovieId(getMovie().getId());
        DTO.setCommentOwnerId(getCommentOwner().getId());
        DTO.setCommentOwnerName(getCommentOwner().getName());
        DTO.setCreatedDate(createdDate);
        DTO.setText(text);
        return DTO;
    }

    public Map<String, String> getDBTuple(){
        Map<String, String> tuple = new HashMap<>();
        tuple.put("text", this.text);
        tuple.put("userId", this.commentOwner.getId());
        tuple.put("movieId", this.commentMovie.getId().toString());
        tuple.put("createdDate", this.createdDate.toString());
        tuple.put("commentLikes", this.commentLikes.toString());
        tuple.put("commentDislikes", this.commentDislikes.toString());
        return tuple;
    }
}