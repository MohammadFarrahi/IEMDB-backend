package ie.iemdb.repository;

import ie.iemdb.exception.CommentNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentRepo extends Repo<Comment, Integer> {
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
    protected String getGetElementByIdStatement() {
        return null;
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, Integer id) {

    }

    @Override
    protected String getGetAllElementsStatement() {
        return null;
    }

    @Override
    protected Comment convertResultSetToDomainModel(ResultSet rs) {
        return null;
    }

    @Override
    protected ArrayList<Comment> convertResultSetToDomainModelList(ResultSet rs) {
        return null;
    }

    @Override
    public void addElement(Comment newObject) throws SQLException {
    }


    public void updateCommentVotes(Integer commentId, String userId, int vote) throws CustomException {
        getElementById(commentId).updateCommentVotes(userId, vote);
    }

    public Integer getMovieIdForComment(Integer commentId) throws SQLException {
        String sql = String.format(
                "SELECT commentMovie\n" +
                        "FROM %s\n" +
                        "WHERE id=?;", COMMENT_TABLE);
        var res = executeQuery(sql, List.of(commentId.toString()));
        return res.getInt("commentMovie");
    }

    public Integer getUserIdForComment(Integer commentId) throws SQLException {
        String sql = String.format(
                "SELECT commentOwner\n" +
                        "FROM %s\n" +
                        "WHERE id=?;", COMMENT_TABLE);
        var res = executeQuery(sql, List.of(commentId.toString()));
        return res.getInt("commentOwner");
    }
}
