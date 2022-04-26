package ie.iemdb.service;

import ie.iemdb.domain.ActorDomainManager;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.ActorDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActorService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ActorDTO getActorInfo(@PathVariable(value = "id") String actorId) throws ObjectNotFoundException {
        return ActorDomainManager.getInstance().getActorDTO(actorId);
    }
}

