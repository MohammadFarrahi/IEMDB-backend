package ie.iemdb.repository;

import ie.iemdb.exception.CommentNotFoundException;
import ie.iemdb.exception.CustomException;
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
    public void addElement(Comment newObject) throws CustomException {
        if (newObject.setId(lastCommentId + 1)) {
            objectMap.put((++lastCommentId).toString(), newObject);
        } else {
            throw new CustomException("InvalidComment");
        }
    }

    @Override
    public void updateElement(Comment newObject) throws CustomException {
    }

    public void updateCommentVotes(Comment comment, String userId, int vote) throws CustomException {
        comment.updateCommentVotes(userId, vote);
    }
}
