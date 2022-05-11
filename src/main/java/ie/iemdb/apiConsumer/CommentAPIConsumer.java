package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.MovieNotFoundException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.exception.UserNotFoundException;
import ie.iemdb.model.Comment;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;
import ie.iemdb.repository.CommentRepo;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;

import java.sql.SQLException;


public class CommentAPIConsumer extends APIConsumer {
    public CommentAPIConsumer(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected void loadRepo(JsonNode arrayNode) {
        try {
            var repo = CommentRepo.getInstance();
            for (var node : arrayNode) {
                try {

                    var newComment = makeNewComment(node);
                    repo.addElement(newComment);
                } catch (UserNotFoundException | MovieNotFoundException | SQLException e) {
                    //ignore
                }
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private Comment makeNewComment(JsonNode node) throws ObjectNotFoundException, SQLException {
        String userEmail = node.get("userEmail").asText();
        var movieId = node.get("movieId").asInt();
        String text = node.get("text").asText();
        Movie commentMovie = getCommentMovie(movieId);
        User commentOwner = getCommentOwner(userEmail);
        return new Comment(commentMovie, commentOwner, text);
    }

    private Movie getCommentMovie(Integer movieId) throws ObjectNotFoundException, SQLException {
        return MovieRepo.getInstance().getElementById(movieId);
    }

    private User getCommentOwner(String userEmail) throws ObjectNotFoundException, SQLException {
        return UserRepo.getInstance().getElementById(userEmail);
    }

    private void addCommentToMovie(JsonNode node, Comment newComment) throws ObjectNotFoundException, SQLException {
        var movieId = node.get("movieId").asInt();
        Movie commentMovie = getCommentMovie(movieId);
        commentMovie.addComment(newComment);
    }

}
