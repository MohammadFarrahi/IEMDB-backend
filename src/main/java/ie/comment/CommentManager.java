package ie.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.Iemdb;
import ie.exception.*;
import ie.types.Constant;
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

    public String addElement(String data) throws JsonProcessingException, CustomException {
        var comment = mapper.readValue(data, Comment.class);
        JsonNode jsonNode = mapper.readTree(data);

        var userId = jsonNode.get(Constant.Comment.U_ID).asText();
        var movieId = jsonNode.get(Constant.Comment.M_ID).asText();
        if (!database.modelExists(userId, Constant.Model.USER)) {
            throw new MovieNotFoundException();
        }
        var film = database.getFilmById(movieId);

        commentMap.put(Comment.lastId.toString(), comment);
        film.addCommentId(Comment.lastId.toString());
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

    public void voteComment(String jsonData) throws JsonProcessingException, CustomException {
        JsonNode voteJsonNode = mapper.readTree(jsonData);
        var validatedVoteJson = ValidateVoteJson(voteJsonNode);
        ValidateVoteData(validatedVoteJson);

        commentMap.get(validatedVoteJson.get(Constant.Vote.C_ID).asText())
                .updateCommentVotes(validatedVoteJson.get(Constant.Vote.U_ID).asText(), validatedVoteJson.get(Constant.Vote.VOTE).asInt());
    }

    private void ValidateVoteData(Map<String, JsonNode> validatedJson)  throws CustomException  {
        var userEmail = validatedJson.get(Constant.Vote.U_ID).asText();
        var commentId = validatedJson.get(Constant.Vote.C_ID).asText();
        if (!database.modelExists(userEmail, Constant.Model.USER)) {
            throw new UserNotFoundException();
        }
        if (!isIdValid(commentId)) {
            throw new CommentNotFoundException();
        }
    }

    private Map<String, JsonNode> ValidateVoteJson(JsonNode voteJsonNode) throws CustomException {
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
            throw new InvalidCommandException();
        }
        return validatedJson;
    }

    public Comment getElement(String id) throws CustomException {
        if (commentMap.containsKey(id)) {
            return commentMap.get(id);
        }
        throw new CommentNotFoundException();
    }

    public JsonNode serializeElement(String commentId, Constant.SER_MODE mode) throws CustomException {
        var comment = getElement(commentId);
        try {
            var commentJsonNode = (ObjectNode) mapper.valueToTree(comment);
            if (mode == Constant.SER_MODE.SHORT) {
                commentJsonNode.remove(Constant.Comment.REMOVABLE_SHORT_SER);
            }
            return commentJsonNode;
        } catch (Exception e) {
            return null;
        }
    }
    public JsonNode serializeElementList(ArrayList<String> commentIds, Constant.SER_MODE mode) throws CustomException {
        var commentJsonList = new ArrayList<JsonNode>();
        for (var id : commentIds) {
            commentJsonList.add(serializeElement(id, mode));
        }
        return mapper.valueToTree(commentJsonList);
    }

}

