package ie.actor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.types.Constant;

import java.util.HashMap;
import java.util.List;

public class ActorManager {
    private final HashMap<String, Actor> actorMap;

    public ActorManager() {
        actorMap = new HashMap<String, Actor>();
    }
    public Actor updateOrAddActor(String jsonData) throws JsonProcessingException {
        var deserializer = new ObjectMapper();
        String objectId = deserializer.readTree(jsonData).get(Constant.Actor.ID).toString();

        if (actorMap.containsKey(objectId)) {
            var existingActor = actorMap.get(objectId);
            deserializer.readerForUpdating(existingActor).readValue(jsonData);
            return existingActor;
        }
        var newActor = deserializer.readValue(jsonData, Actor.class);
        actorMap.put(objectId, newActor);
        return newActor;
    }
//    public boolean isActorPresent(String id) {
//        return actorMap.containsKey(id);
//    }
    public boolean isActorPresent(List<String> ids) {
        return actorMap.keySet().containsAll(ids);
    }
}
