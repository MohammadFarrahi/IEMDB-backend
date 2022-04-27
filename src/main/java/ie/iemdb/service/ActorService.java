package ie.iemdb.service;

import ie.iemdb.domain.ActorDomainManager;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActorService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getActorInfo(@PathVariable(value = "id") String actorId) {
        try {
            return new Response(true, "okeb", ActorDomainManager.getInstance().getActorDTO(actorId));
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Not Found", e);
        }
    }
}

