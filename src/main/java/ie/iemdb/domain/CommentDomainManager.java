package ie.iemdb.domain;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.repository.UserRepo;

public class CommentDomainManager {
  public void voteComment(String commentId, String userId, int vote) throws CustomException {
    if (! UserRepo.getInstance().isIdValid(userId)) {
        throw new UserNotFoundException();
    }
    // getElementById(commentId).updateCommentVotes(userId, vote);
    //TODO: connect it to the repo the method in the repo is: updateCommentVotes
}
}
