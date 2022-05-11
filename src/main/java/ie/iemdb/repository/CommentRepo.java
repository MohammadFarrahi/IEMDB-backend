package ie.iemdb.repository;

import ie.iemdb.exception.CommentNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Comment;
import ie.iemdb.model.User;

import javax.print.DocFlavor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentRepo extends Repo<Comment, Integer> {
    private static CommentRepo instance = null;
    public static Integer lastCommentId = 0;
    private static final String COMMENT_TABLE = "Comment";
    private static final String VOTE_MAP_TABLE = "VoteMap";


    public static CommentRepo getInstance() {
        if (instance == null) {
            instance = new CommentRepo();
        }
        return instance;
    }

    private CommentRepo() {
        initCommentTable();
        initVoteMapTable();
        this.notFoundException = new CommentNotFoundException();
    }

    private void initCommentTable() {
        initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "id INT NOT NULL AUTO_INCREMENT,\n" +
                                "userId VARCHAR(255),\n" +
                                "movieId INT,\n" +
                                "text VARCHAR(255),\n" +
                                "createdDate VARCHAR(255)\n" +
                                "PRIMARY KEY(id),\n" +
                                "FOREIGN KEY (userId) REFERENCES " + UserRepo.USER_TABLE + "(email),\n" +
                                "FOREIGN KEY (movieId) REFERENCES " + MovieRepo.MOVIE_TABLE + "(id),\n" +
                                ");", COMMENT_TABLE
                )
        );
    }

    private void initVoteMapTable() {
        initTable(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s( \n" +
                                "userId VARCHAR(255),\n" +
                                "commentId INT), \n" +
                                "vote INT,\n" +
                                "PRIMARY KEY(userId, commentId), \n" +
                                "FOREIGN KEY (userId) REFERENCES " + UserRepo.USER_TABLE + "(email),\n" +
                                "FOREIGN KEY (commentId) REFERENCES " + COMMENT_TABLE + "(id),\n" +
                                ");", VOTE_MAP_TABLE
                )
        );
    }


    @Override
    protected String getGetElementByIdStatement() {
        return String.format("SELECT* FROM %s a WHERE a.id = ?;", COMMENT_TABLE);
    }

    @Override
    protected void fillGetElementByIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setString(1, id.toString());
    }

    @Override
    protected String getGetAllElementsStatement() {
        return String.format("SELECT * FROM %s;", COMMENT_TABLE);
    }

    @Override
    protected Comment convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        var newComment = new Comment(
                rs.getInt("id"),
                rs.getString("text"),
                rs.getString("createdDate"),
                getUserVoteMap(rs.getInt("id"))
        );
        newComment.setRetriever(new Retriever());
        return newComment;
    }

    private HashMap<String, Short> getUserVoteMap(Integer commentId) throws SQLException {
        var userVoteMap = new HashMap<String, Short>();
        String sql = String.format("SELECT userId, vote FROM %s WHERE commentId=?;", VOTE_MAP_TABLE);
        var res = executeQuery(sql, List.of(commentId.toString()));
        while (res.next()) {
            userVoteMap.put(res.getString("userId"), res.getShort("vote"));
        }
        return userVoteMap;
    }

    @Override
    protected ArrayList<Comment> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        while (rs.next()) {
            comments.add(this.convertResultSetToDomainModel(rs));
        }
        return comments;
    }

    @Override
    protected String getAddElementStatement() {
        return String.format("INSERT INTO %s (text, userId, movieId, createdDate,)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);", COMMENT_TABLE);
    }

    @Override
    public void addElement(Comment newObject) throws SQLException {
        var tupleMap = newObject.getDBTuple();
        executeUpdate(getAddElementStatement(), List.of(
                tupleMap.get("text"),
                tupleMap.get("userId"),
                tupleMap.get("movieId"),
                tupleMap.get("createdDate")
        ));
    }


    public void updateCommentVotes(Integer commentId, String userId, int vote) throws CustomException, SQLException {
        String sql = String.format(
                "INSERT INTO %s (userId, commentId, vote)\n" +
                        "VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE\n" +
                        "vote=?;", VOTE_MAP_TABLE);
        executeUpdate(sql, List.of(userId, commentId.toString(), String.valueOf(vote), String.valueOf(vote)));
    }

    public Integer getMovieIdForComment(Integer commentId) throws SQLException {
        String sql = String.format(
                "SELECT commentMovie\n" +
                        "FROM %s\n" +
                        "WHERE id=?;", COMMENT_TABLE);
        var res = executeQuery(sql, List.of(commentId.toString()));
        return res.getInt("commentMovie");
    }

    public String getUserIdForComment(Integer commentId) throws SQLException {
        String sql = String.format(
                "SELECT commentOwner\n" +
                        "FROM %s\n" +
                        "WHERE id=?;", COMMENT_TABLE);
        var res = executeQuery(sql, List.of(commentId.toString()));
        return res.getString("commentOwner");
    }

    public ArrayList<Comment> getCommentsForMovie(Integer movieId) throws SQLException {
        var comments = new ArrayList<Comment>();
        String sql = String.format(
                "SELECT c.id, c.text, c.createdDate\n" +
                        "FROM %s c\n" +
                        "WHERE c.movieId=?;", COMMENT_TABLE);
        var res = executeQuery(sql, List.of(movieId.toString()));
        return convertResultSetToDomainModelList(res);
    }
}
