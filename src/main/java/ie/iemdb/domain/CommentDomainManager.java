package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.model.Comment;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.model.DTO.ResponseDTO;
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
  public void voteComment(String commentId, String userId, int vote) throws CustomException {
    if (! UserRepo.getInstance().isIdValid(userId)) {
        throw new UserNotFoundException();
    }
    // getElementById(commentId).updateCommentVotes(userId, vote);
    //TODO: connect it to the repo the method in the repo is: updateCommentVotes
}

    public ResponseDTO postNewComment(CommentDTO commentDTO) throws CustomException {
        // TODO : handle if new comment is actually repeatitve comment
        var commentMovie = MovieRepo.getInstance().getElementById(commentDTO.getCommentMovieId().toString());
        var newComment = new Comment(commentMovie, UserRepo.loggedInUser, commentDTO.getText());
        CommentRepo.getInstance().addElement(newComment);
        commentMovie.addComment(newComment);
        return new ResponseDTO(true, "Okeb");
    }
}
