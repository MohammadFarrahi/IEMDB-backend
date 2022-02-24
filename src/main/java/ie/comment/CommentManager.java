package ie.comment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.Iemdb;
import ie.film.FilmManager;
import ie.types.Constant;
import ie.user.UserManager;

import javax.crypto.spec.PSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CommentManager {
    ObjectMapper mapper;
    private Integer lastCommentId;
    private HashMap<String, Comment> commentMap;
    private final Iemdb database;

    public CommentManager(Iemdb database) {
        this.database = database;

        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        this.commentMap = new HashMap<>();
        lastCommentId = 0;
    }

    public Comment addComment(String data) throws Exception{
        var comment = mapper.readValue(data, Comment.class);

        JsonNode jsonNode = mapper.readTree(data);
        var userId = jsonNode.get(Constant.Comment.U_ID).asText();
        var movieId = jsonNode.get(Constant.Comment.M_ID).asText();
        if (!database.modelExists(userId, Constant.Model.USER)) {
            throw new Exception("User not found");
        }

        if (!database.modelExists(movieId, Constant.Model.FILM))
            throw new Exception("Movie not found");
        commentMap.put(Comment.lastId.toString(), comment);
        return comment;
    }

    public boolean isIdValid(String id) {
        return commentMap.containsKey(id);
    }

}

