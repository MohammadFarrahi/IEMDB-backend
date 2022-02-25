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
        ValidateVoteJson(voteJsonNode);
        var validatedData = ValidateVoteData(voteJsonNode);

        commentMap.get(validatedData.get(Constant.Vote.C_ID).asText())
                .updateCommentVotes(validatedData.get(Constant.Vote.U_ID).asText(), validatedData.get(Constant.Vote.VOTE).asInt());
    }

    private Map<String, JsonNode> ValidateVoteData(JsonNode rateJsonNode) throws Exception {
        Map<String, JsonNode> validatedData = new HashMap<>();
        validatedData.put(Constant.Vote.U_ID, rateJsonNode.get(Constant.Vote.U_ID));
        validatedData.put(Constant.Vote.C_ID, rateJsonNode.get(Constant.Vote.C_ID));
        validatedData.put(Constant.Vote.VOTE, rateJsonNode.get(Constant.Vote.VOTE));

        var userEmail = validatedData.get(Constant.Vote.U_ID).asText();
        var commentId = validatedData.get(Constant.Vote.C_ID).asText();
        if (!database.modelExists(userEmail, Constant.Model.USER)) {
            throw new Exception("ne user with this id");
        }
        if (!isIdValid(commentId)) {
            throw new Exception("no comment with this id");
        }
        return validatedData;
    }

    private void ValidateVoteJson(JsonNode rateJsonNode) throws Exception {
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        rateJsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);

        var voteJsonFieldNames = Constant.Vote.getSet();
        boolean exceptionFlag = (jsonFiledNames.size() != voteJsonFieldNames.size());
        exceptionFlag |= !(voteJsonFieldNames.equals(new HashSet<String>(jsonFiledNames)));
        exceptionFlag |= !(rateJsonNode.get(Constant.Vote.C_ID).isInt() &&
                rateJsonNode.get(Constant.Vote.U_ID).isTextual() &&
                rateJsonNode.get(Constant.Vote.VOTE).isInt());
        if (exceptionFlag) {
            throw new Exception("invalid input vote");
        }
    }
}

