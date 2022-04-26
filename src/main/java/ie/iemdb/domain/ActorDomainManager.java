package ie.iemdb.domain;

import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.Actor;
import ie.iemdb.repository.ActorRepo;

public class ActorDomainManager {
    // TODO : what if we had a instance in ActorService ??
    private static ActorDomainManager instance;

    public static ActorDomainManager getInstance() {
        if (instance == null) {
            instance = new ActorDomainManager();
        }
        return instance;
    }
    public Actor getActor(String actorId) throws ObjectNotFoundException {
        return ActorRepo.getInstance().getElementById(actorId);
    }
}
