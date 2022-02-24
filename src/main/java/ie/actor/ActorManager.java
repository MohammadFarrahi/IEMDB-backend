package ie.actor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import ie.Iemdb;
import ie.film.Film;
import ie.types.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActorManager {
    private final HashMap<String, Actor> actorMap;
    private final Iemdb database;
    private final ObjectMapper mapper;

    public ActorManager(Iemdb database) {
        mapper = new ObjectMapper();

        this.database = database;
        actorMap = new HashMap<String, Actor>();
    }
    public Actor updateOrAddActor(String jsonData) throws JsonProcessingException {
        String objectId = mapper.readTree(jsonData).get(Constant.Actor.ID).toString();

        if (actorMap.containsKey(objectId)) {
            var existingActor = actorMap.get(objectId);
            mapper.readerForUpdating(existingActor).readValue(jsonData);
            return existingActor;
        }
        var newActor = mapper.readValue(jsonData, Actor.class);
        actorMap.put(objectId, newActor);
        return newActor;
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

    public JsonNode serializeElement(String id, Constant.SER_MODE mode){
        try {
            var actor = getElement(id);
            var jsonNode = mapper.valueToTree(actor);
            return jsonNode;

        }catch (Exception e){
            return null;
        }
    }

    public JsonNode serializeElementList(ArrayList<String> idList, Constant.SER_MODE mode){
        try {
            var actorList = getElementList(idList);
            var jsonNode = mapper.valueToTree(actorList);
            return jsonNode;
        }catch (Exception e){
            return null;
        }
    }


    }
