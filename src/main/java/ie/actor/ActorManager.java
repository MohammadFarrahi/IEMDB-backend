package ie.actor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.Iemdb;
import ie.film.Film;
import ie.types.Constant;

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
    public String updateOrAddElement(String jsonData) throws Exception {
        String actorId = mapper.readTree(jsonData).get(Constant.Actor.ID_S).asText(); // TODO: Check method call chain arise same type of error

        if (isIdValid(actorId)) {
            updateElement(actorId, jsonData);
        }
        else {
            addElement(jsonData);
        }
        return actorId;
    }
    public String addElement(String jsonData) throws Exception {
        String actorId = mapper.readTree(jsonData).get(Constant.Actor.ID_S).asText();
        if (isIdValid(actorId)) {
            throw new Exception("actor already exists");
        }
        var newActor = mapper.readValue(jsonData, Actor.class);
        actorMap.put(actorId, newActor);
        return actorId;
    }
    public void updateElement(String id, String jsonData) throws Exception {
        if (!isIdValid(id)) {
            throw new Exception("actor not found");
        }
        mapper.readerForUpdating(actorMap.get(id)).readValue(jsonData);
        // TODO: Check if it is needed to put object to hashMap again
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

    public Actor getElement(String id) throws Exception{
        if (actorMap.containsKey(id)) {
            return actorMap.get(id);
        }
        throw new Exception("Actor not found");
    }

    public ArrayList<Actor> getElementList(ArrayList<String> idList) throws Exception{
        ArrayList<Actor> res = new ArrayList<>();

        for(var id: idList) {
            res.add(getElement(id));
        }
        return res;
    }

    public JsonNode serializeElement(String actorId, Constant.SER_MODE mode) throws Exception {
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
    public JsonNode serializeElementList(ArrayList<String> actorIds, Constant.SER_MODE mode) throws Exception {
        var actorJsonList = new ArrayList<JsonNode>();
        for (var id : actorIds) {
            actorJsonList.add(serializeElement(id, mode));
        }
        return mapper.valueToTree(actorJsonList);
    }
}
