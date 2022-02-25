package ie.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Map;

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

    public String addElement(String data) throws Exception {
        var comment = mapper.readValue(data, Comment.class);

        JsonNode jsonNode = mapper.readTree(data);
        var userId = jsonNode.get(Constant.Comment.U_ID).asText();
        var movieId = jsonNode.get(Constant.Comment.M_ID).asText();
        if (!database.modelExists(userId, Constant.Model.USER)) {
            throw new Exception("User not found");
        }
        if (!database.modelExists(movieId, Constant.Model.FILM)) {
            throw new Exception("Movie not found");
        }
        commentMap.put(Comment.lastId.toString(), comment);
        return Comment.lastId.toString();
    }

    public boolean isIdValid(String id) {
        return commentMap.containsKey(id);
    }

    public boolean isIdListValid(ArrayList<String> ids) {
        for (var id : ids){
            if(!commentMap.containsKey(id))
                return false;
        }
        return true;
    }

    public void voteComment(String jsonData) throws Exception {
        JsonNode voteJsonNode = mapper.readTree(jsonData);
        var validatedVoteJson = ValidateVoteJson(voteJsonNode);
        ValidateVoteData(validatedVoteJson);

        commentMap.get(validatedVoteJson.get(Constant.Vote.C_ID).asText())
                .updateCommentVotes(validatedVoteJson.get(Constant.Vote.U_ID).asText(), validatedVoteJson.get(Constant.Vote.VOTE).asInt());
    }

    private void ValidateVoteData(Map<String, JsonNode> validatedJson) throws Exception {
        var userEmail = validatedJson.get(Constant.Vote.U_ID).asText();
        var commentId = validatedJson.get(Constant.Vote.C_ID).asText();
        if (!database.modelExists(userEmail, Constant.Model.USER)) {
            throw new Exception("ne user with this id");
        }
        if (!isIdValid(commentId)) {
            throw new Exception("no comment with this id");
        }
    }

    private Map<String, JsonNode> ValidateVoteJson(JsonNode voteJsonNode) throws Exception {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        voteJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);
        var voteJsonFieldNames = Constant.Vote.getSet();
        boolean exceptionFlag = (jsonFiledNames.size() != voteJsonFieldNames.size());
        exceptionFlag |= !(voteJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)));

        Map<String, JsonNode> validatedJson = new HashMap<>();
        validatedJson.put(Constant.Vote.U_ID, voteJsonNode.get(Constant.Vote.U_ID));
        validatedJson.put(Constant.Vote.C_ID, voteJsonNode.get(Constant.Vote.C_ID));
        validatedJson.put(Constant.Vote.VOTE, voteJsonNode.get(Constant.Vote.VOTE));
        exceptionFlag |= !(validatedJson.get(Constant.Vote.C_ID).isInt() &&
                validatedJson.get(Constant.Vote.U_ID).isTextual() &&
                validatedJson.get(Constant.Vote.VOTE).isInt());
        if (exceptionFlag) {
            throw new Exception("invalid input vote");
        }
        return validatedJson;
    }
}

