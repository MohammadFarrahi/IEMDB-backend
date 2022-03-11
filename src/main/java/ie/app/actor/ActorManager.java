package ie.app.actor;

import com.fasterxml.jackson.core.JsonProcessingException;
import ie.exception.ActorNotFoundException;
import ie.exception.CustomException;
import ie.generic.model.JsonHandler;
import ie.generic.model.Manager;
import ie.util.types.Constant;

import java.util.ArrayList;

public class ActorManager extends Manager<Actor> {
    private static ActorManager instance = null;
    private final JsonHandler<Actor> jsonMapper;

    public static ActorManager getInstance() {
        if (instance == null) {
            instance = new ActorManager();
        }
        return instance;
    }

    private ActorManager() {
        jsonMapper = new ActorJsonHandler();
        this.notFoundException = new ActorNotFoundException();
    }

    @Override
    public String addElement(Actor newObject) throws CustomException {
        var objectId = newObject.getId().toString();
        if (isIdValid(objectId)) {
            throw new CustomException("ObjectAlreadyExists"); // TODO : customize message
        }
        this.objectMap.put(objectId, newObject);
        return objectId;
    }
    @Override
    public String updateElement(Actor newObject) throws ActorNotFoundException {
        var objectId = newObject.getId().toString();
        if (!isIdValid(objectId)) {
            throw new ActorNotFoundException();
        }
        objectMap.put(objectId, newObject);
        return objectId;
    }
    public String updateOrAddElementJson(String jsonData) throws JsonProcessingException, CustomException {
        try {
            return updateElementJson(jsonData);
        }
        catch (Exception e) {
            return addElementJson(jsonData);
        }
    }
    public String addElementJson(String jsonData) throws JsonProcessingException, CustomException {
        var deserializedObject = jsonMapper.deserialize(jsonData);
        return addElement(deserializedObject);
    }
    public ArrayList<String> addElementsJson(String jsonData) throws JsonProcessingException, CustomException {
        var objectIds = new ArrayList<String>();
        for (var deserializedObject : jsonMapper.deserializeList(jsonData)) {
            objectIds.add(addElement(deserializedObject));
        }
        return objectIds;
    }
    public String updateElementJson(String jsonData) throws JsonProcessingException, CustomException {
        var deserializedObject = jsonMapper.deserialize(jsonData);
        return updateElement(deserializedObject);
    }
    public String serializeElement(String actorId, Constant.SER_MODE mode) throws CustomException {
        var actor = getElementById(actorId);
        if (mode == Constant.SER_MODE.SHORT) {
            return jsonMapper.serialize(actor, Constant.Actor.REMOVABLE_SHORT_SER);
        }
        else {
            return jsonMapper.serialize(actor, null);
        }
    }
    // TODO : remove this issue in iemdb
    public String serializeElementList(ArrayList<String> actorIds, Constant.SER_MODE mode) throws CustomException {
        var objects = getElementsById(actorIds);
        if (mode == Constant.SER_MODE.SHORT) {
            return jsonMapper.serialize(objects, Constant.Actor.REMOVABLE_SHORT_SER);
        }
        else {
            return jsonMapper.serialize(objects, null);
        }
    }
//    public Actor getElement(String id) throws CustomException {
//        if (actorMap.containsKey(id)) {
//            return actorMap.get(id);
//        }
//        throw new ActorNotFoundException();
//    }
//    public ArrayList<Actor> getElementList(ArrayList<String> idList) throws CustomException {
//        ArrayList<Actor> res = new ArrayList<>();
//
//        for(var id: idList) {
//            res.add(getElement(id));
//        }
//        return res;
//    }
}
