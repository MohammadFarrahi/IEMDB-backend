package ie.model.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.Iemdb;
import ie.exception.*;
import ie.generic.model.JsonHandler;
import ie.generic.model.Manager;
import ie.model.film.FilmManager;
import ie.model.user.UserManager;
import ie.util.types.Constant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CommentManager extends Manager<Comment> {
    private static CommentManager instance = null;
    private final JsonHandler<Comment> jsonMapper;
    private final ObjectMapper mapper;

    public static CommentManager getInstance() {
        if (instance == null) {
            instance = new CommentManager();
        }
        return instance;
    }
    private CommentManager() {
        jsonMapper = new CommentJsonHandler();
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    }
    @Override
    public String addElement(Comment newObject) throws CustomException {
        if (!UserManager.getInstance().isIdValid(newObject.getCommentOwner())) {
            throw new UserNotFoundException();
        }
        var film = FilmManager.getInstance().getElementById(newObject.getCommentFilm());

        objectMap.put(Comment.lastId.toString(), newObject);
        film.addCommentId(Comment.lastId.toString());
        return Comment.lastId.toString();
    }
    @Override
    public String updateElement(Comment newObject) throws CustomException {
        return null;
    }

    public String addElementJson(String jsonData) throws JsonProcessingException, CustomException {
        var comment = jsonMapper.deserialize(jsonData);
        return addElement(comment);
    }

    public void voteComment(String jsonData) throws JsonProcessingException, CustomException {
        JsonNode voteJsonNode = mapper.readTree(jsonData);
        var validatedVoteJson = ValidateVoteJson(voteJsonNode);
        ValidateVoteData(validatedVoteJson);

        objectMap.get(validatedVoteJson.get(Constant.Vote.C_ID).asText())
                .updateCommentVotes(validatedVoteJson.get(Constant.Vote.U_ID).asText(), validatedVoteJson.get(Constant.Vote.VOTE).asInt());
    }

    private void ValidateVoteData(Map<String, JsonNode> validatedJson)  throws CustomException  {
        var userEmail = validatedJson.get(Constant.Vote.U_ID).asText();
        var commentId = validatedJson.get(Constant.Vote.C_ID).asText();
        if (!UserManager.getInstance().isIdValid(userEmail)) {
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

    public JsonNode serializeElement(String commentId, Constant.SER_MODE mode) throws CustomException {
        var comment = getElementById(commentId);
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

