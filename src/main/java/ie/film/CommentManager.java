package ie.film;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.types.Constant;
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
        var commentFilm = filmManager.getFilm(commentJsonNode.get(Constant.Comment.M_ID).asText());
        var commentOwner = userManager.getUser(commentJsonNode.get(Constant.Comment.U_ID).asText());
        var commentText = commentJsonNode.get(Constant.Comment.CONTENT).asText();
        var newComment = new Comment((++lastCommentId).toString(), commentFilm, commentOwner, commentText);

        commentFilm.addFilmComment(newComment);
        commentOwner.addUserComment(newComment);

        return newComment;
    }
    private boolean isValidJsonData(JsonNode commentJsonNode) {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        commentJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        var commentJsonFieldNames = Constant.Comment.getSet();
        if(jsonFiledNames.size() != commentJsonFieldNames.size())
            return false;
        if(!commentJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)))
            return false;
        if(!(commentJsonNode.get(Constant.Comment.M_ID).isInt() && commentJsonNode.get(Constant.Comment.U_ID).isTextual() &&
                commentJsonNode.get(Constant.Comment.CONTENT).isTextual()))
            return false;

        return true;
    }
}

