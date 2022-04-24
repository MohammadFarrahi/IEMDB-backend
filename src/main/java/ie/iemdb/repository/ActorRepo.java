package ie.iemdb.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import ie.iemdb.exception.ActorNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Actor;
import ie.iemdb.util.types.Constant;


import java.util.ArrayList;

public class ActorRepo extends Repo<Actor> {
    private static ActorRepo instance = null;

    public static ActorRepo getInstance() {
        if (instance == null) {
            instance = new ActorRepo();
        }
        return instance;
    }

    private ActorRepo() {
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
}
