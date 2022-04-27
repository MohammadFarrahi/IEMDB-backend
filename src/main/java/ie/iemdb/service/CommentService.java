package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.CommentDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.InvalidVoteValueException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.model.DTO.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response postNewComment(@RequestBody CommentDTO newComment) {
        if(newComment.getCommentMovieId() == null || newComment.getText() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            return new Response(true, "okeb", CommentDomainManager.getInstance().postNewComment(newComment));
        } catch (CustomException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
        }
    }
    @RequestMapping(value = "/comments/{id}/vote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response voteComment(@PathVariable(value = "id") Integer commentId, @RequestBody String voteObj) {
        try {
            var voteValue = new ObjectMapper().readTree(voteObj).get("vote").asInt();
            return new Response(true, "okeb", CommentDomainManager.getInstance().voteComment(commentId.toString(), voteValue));
        }
        catch (Exception e) {
            if(e instanceof ObjectNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
