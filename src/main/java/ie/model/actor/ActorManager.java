package ie.model.actor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.Iemdb;
import ie.exception.ActorNotFoundException;
import ie.exception.CustomException;
import ie.util.types.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class ActorManager {
    private final HashMap<String, Actor> actorMap;
    private final Iemdb database;
    private final ObjectMapper mapper;

    public ActorManager(Iemdb database) {
        mapper = new ObjectMapper();

        this.database = database;
        actorMap = new HashMap<String, Actor>();
    }
    public String updateOrAddElement(String jsonData) throws JsonProcessingException, CustomException {
        String actorId = mapper.readTree(jsonData).get(Constant.Actor.ID_S).asText();

        if (isIdValid(actorId)) {
            updateElement(actorId, jsonData);
        }
        else {
            addElement(jsonData);
        }
        return actorId;
    }
    public String addElement(String jsonData) throws JsonProcessingException, CustomException {
        String actorId = mapper.readTree(jsonData).get(Constant.Actor.ID_S).asText();
        if (isIdValid(actorId)) {
            throw new CustomException("ActorAlreadyExists");
        }
        var newActor = mapper.readValue(jsonData, Actor.class);
        actorMap.put(actorId, newActor);
        return actorId;
    }
    public void updateElement(String id, String jsonData) throws JsonProcessingException, CustomException {
        if (!isIdValid(id)) {
            throw new ActorNotFoundException();
        }
        mapper.readerForUpdating(actorMap.get(id)).readValue(jsonData);
    }
    public boolean isIdListValid(ArrayList<String> ids) {
        for (var id : ids){
            if(!actorMap.containsKey(id))
                return false;
        }
        return true;
    }
    public boolean isIdValid(String id){
        return actorMap.containsKey(id);
    }

    public Actor getElement(String id) throws CustomException {
        if (actorMap.containsKey(id)) {
            return actorMap.get(id);
        }
        throw new ActorNotFoundException();
    }

    public ArrayList<Actor> getElementList(ArrayList<String> idList) throws CustomException {
        ArrayList<Actor> res = new ArrayList<>();

        for(var id: idList) {
            res.add(getElement(id));
        }
        return res;
    }

    public JsonNode serializeElement(String actorId, Constant.SER_MODE mode) throws CustomException {
        var actor = getElement(actorId);
        try {
            var actorJsonNode = (ObjectNode) mapper.valueToTree(actor);
            if (mode == Constant.SER_MODE.SHORT) {
                actorJsonNode.remove(Constant.Actor.REMOVABLE_SHORT_SER);
            }
            return actorJsonNode;
        } catch (Exception e) {
            return null;
        }
    }
    public JsonNode serializeElementList(ArrayList<String> actorIds, Constant.SER_MODE mode) throws CustomException {
        var actorJsonList = new ArrayList<JsonNode>();
        for (var id : actorIds) {
            actorJsonList.add(serializeElement(id, mode));
        }
        return mapper.valueToTree(actorJsonList);
    }
}
