package ie.iemdb.service;

import ie.iemdb.domain.ActorDomainManager;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.ActorDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActorService {
    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ActorDTO getActorInfo(@PathVariable(value = "id") String actorId) throws ObjectNotFoundException {
        // TODO : validation and exception handling
        return ActorDomainManager.getInstance().getActor(actorId).getDTO();
    }
}

