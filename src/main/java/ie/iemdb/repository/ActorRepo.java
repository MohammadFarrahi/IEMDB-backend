package ie.iemdb.repository;


import ie.iemdb.exception.ActorAlreadyExistsException;
import ie.iemdb.exception.ActorNotFoundException;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Actor;

import java.util.ArrayList;
import java.util.List;


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
    public void addElement(Actor newObject) throws CustomException {
        var objectId = newObject.getId().toString();
        if (isIdValid(objectId)) {
            throw new ActorAlreadyExistsException();
        }
        this.objectMap.put(objectId, newObject);
    }

    @Override
    public void updateElement(Actor newObject) throws ActorNotFoundException {
        var objectId = newObject.getId().toString();
        if (!isIdValid(objectId)) {
            throw new ActorNotFoundException();
        }
        objectMap.put(objectId, newObject);
    }

    public ArrayList<Actor> getCastForMovie(int movieId) {
        return new ArrayList<>();
    }
}
