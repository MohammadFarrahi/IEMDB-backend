package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.Comment;
import ie.iemdb.model.Movie;
import ie.iemdb.model.User;
import ie.iemdb.repository.CommentRepo;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.repository.UserRepo;


public class CommentAPIConsumer extends APIConsumer{
    public CommentAPIConsumer(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected void loadRepo(JsonNode arrayNode) {
        try {
            var repo = CommentRepo.getInstance();
            for (var node : arrayNode) {
                var newComment = makeNewComment(node);
                repo.addElement(newComment);
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private Comment makeNewComment(JsonNode node) throws ObjectNotFoundException {
        String userEmail = node.get("userEmail").asText();
        String movieId = node.get("movieId").asText();
        String text = node.get("text").asText();
        Movie commentMovie = getCommentMovie(movieId);
        User commentOwner = getCommentOwner(userEmail);
        return new Comment(commentMovie, commentOwner, text);
    }

    private Movie getCommentMovie(String movieId) throws ObjectNotFoundException {
        return MovieRepo.getInstance().getElementById(movieId);
    }

    private User getCommentOwner(String userEmail) throws ObjectNotFoundException {
        return UserRepo.getInstance().getElementById(userEmail);
    }

}
