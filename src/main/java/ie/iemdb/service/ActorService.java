package ie.iemdb.service;

import ie.iemdb.domain.ActorDomainManager;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "false")
public class ActorService {
    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getActorInfo(@PathVariable(value = "id") Integer actorId) throws SQLException {
        try {
            return new Response(true, "okeb", ActorDomainManager.getInstance().getActorDTO(actorId));
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Not Found", e);
        }
    }
}

