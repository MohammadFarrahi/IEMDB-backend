package ie.iemdb.domain;

import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.Actor;
import ie.iemdb.model.DTO.ActorDTO;
import ie.iemdb.repository.ActorRepo;

import java.sql.SQLException;

public class ActorDomainManager {
    // TODO : what if we had a instance in ActorService ??
    private static ActorDomainManager instance;

    public static ActorDomainManager getInstance() {
        if (instance == null) {
            instance = new ActorDomainManager();
        }
        return instance;
    }
    public ActorDTO getActorDTO(Integer actorId) throws ObjectNotFoundException, SQLException {
        return ActorRepo.getInstance().getElementById(actorId).getDTO();
    }
}
