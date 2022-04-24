package ie.iemdb.repository;

import ie.iemdb.exception.CommentNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.model.Comment;

public class CommentRepo extends Repo<Comment> {
    private static CommentRepo instance = null;
    public static Integer lastCommentId = 0;

    public static CommentRepo getInstance() {
        if (instance == null) {
            instance = new CommentRepo();
        }
        return instance;
    }
    private CommentRepo() {
        this.notFoundException = new CommentNotFoundException();
    }
    @Override
    public String addElement(Comment newObject) throws CustomException {
        if (! UserRepo.getInstance().isIdValid(newObject.getCommentOwner())) {
            throw new UserNotFoundException();
        }
        var movie = MovieRepo.getInstance().getElementById(newObject.getCommentMovie());

        if(newObject.setId(lastCommentId+1)) {
            objectMap.put((++lastCommentId).toString(), newObject);
            movie.addCommentId(lastCommentId.toString());
            return lastCommentId.toString();
        } else {
            throw new CustomException("InvalidComment");
        }
    }
    @Override
    public String updateElement(Comment newObject) throws CustomException {
        return null;
    }

    public void voteComment(String commentId, String userId, int vote) throws CustomException {
        if (! UserRepo.getInstance().isIdValid(userId)) {
            throw new UserNotFoundException();
        }
        getElementById(commentId).updateCommentVotes(userId, vote);
    }
}

