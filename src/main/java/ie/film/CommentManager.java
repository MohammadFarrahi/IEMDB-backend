package ie.film;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.user.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CommentManager {
    ObjectMapper deserializer;
    private Integer lastCommentId;
    private HashMap<String, Comment> commentMap;

    public CommentManager() {
        deserializer = new ObjectMapper();
        deserializer.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        lastCommentId = 0;
    }

    public Comment addComment(String jsonData, FilmManager filmManager, UserManager userManager) throws Exception{
        JsonNode commentJsonNode = deserializer.readTree(jsonData);
        if(!isValidJsonData(commentJsonNode)) {
            throw new Exception("Invalid addComment");
        }
        var newComment = createNewComment(commentJsonNode, filmManager, userManager);
        commentMap.put(lastCommentId.toString(), newComment);
        return newComment;
    }

    private Comment createNewComment(JsonNode commentJsonNode, FilmManager filmManager, UserManager userManager) throws Exception {
        var commentFilm = filmManager.getFilm(commentJsonNode.get("movieId").asText());
        var commentOwner = userManager.getUser(commentJsonNode.get("userEmail").asText());
        var commentText = commentJsonNode.get("text").asText();
        var newComment = new Comment((++lastCommentId).toString(), commentFilm, commentOwner, commentText);

        commentFilm.addFilmComment(newComment);
        commentOwner.addUserComment(newComment);

        return newComment;
    }
    private boolean isValidJsonData(JsonNode commentJsonNode) {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        commentJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        if(jsonFiledNames.size() != Comment.commentJsonFieldNames.size())
            return false;
        if(!Comment.commentJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)))
            return false;
        if(!(commentJsonNode.get("movieId").isInt() && commentJsonNode.get("userEmail").isTextual() &&
                commentJsonNode.get("test").isTextual()))
            return false;

        return true;
    }
}

