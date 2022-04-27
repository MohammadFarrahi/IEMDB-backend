package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Comment;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.repository.CommentRepo;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;

public class CommentDomainManager {

    private static CommentDomainManager instance;
    public static CommentDomainManager getInstance() {
        if (instance == null) {
            instance = new CommentDomainManager();
        }
        return instance;
    }
    public CommentDTO voteComment(String commentId, int vote) throws CustomException {
        CommentRepo.getInstance().updateCommentVotes(commentId, UserRepo.loggedInUser.getId(), vote);
        return CommentRepo.getInstance().getElementById(commentId).getDTO();
    }

    public CommentDTO postNewComment(CommentDTO commentDTO) throws CustomException {
        // TODO : handle if new comment is actually repeatitve comment
        var commentMovie = MovieRepo.getInstance().getElementById(commentDTO.getCommentMovieId().toString());
        var newComment = new Comment(commentMovie, UserRepo.loggedInUser, commentDTO.getText());
        CommentRepo.getInstance().addElement(newComment);
        commentMovie.addComment(newComment);
        return newComment.getDTO();
    }
}
