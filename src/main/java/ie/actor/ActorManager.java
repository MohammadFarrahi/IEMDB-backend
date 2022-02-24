package ie.actor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.Iemdb;
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
}
