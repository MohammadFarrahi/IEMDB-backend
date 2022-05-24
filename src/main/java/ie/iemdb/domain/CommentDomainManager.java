package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Comment;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.repository.CommentRepo;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;

import java.sql.SQLException;

public class CommentDomainManager {

    private static CommentDomainManager instance;
    public static CommentDomainManager getInstance() {
        if (instance == null) {
            instance = new CommentDomainManager();
        }
        return instance;
    }
    public CommentDTO voteComment(Integer commentId, int vote, String userId) throws CustomException, SQLException {
        var comment = CommentRepo.getInstance().getElementById(commentId);
        comment.updateCommentVotes(userId, vote);
        CommentRepo.getInstance().updateCommentVotes(commentId, userId, vote);
        return comment.getDTO();
    }

    public CommentDTO postNewComment(CommentDTO commentDTO) throws CustomException, SQLException {
        // TODO : handle if new comment is actually repeatitve comment
        var commentMovie = MovieRepo.getInstance().getElementById(commentDTO.getCommentMovieId());
        var user = UserRepo.getInstance().getElementById(commentDTO.getCommentOwnerId());
        var newComment = new Comment(commentMovie, user, commentDTO.getText());
        CommentRepo.getInstance().addElement(newComment);
        return newComment.getDTO();
    }
}
