package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.CommentDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.model.DTO.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response postNewComment(@RequestBody CommentDTO newComment) throws CustomException {
        if(newComment.getCommentMovieId() == null || newComment.getText() == null) {
            // TODO : do something with error
        }
        return new Response(true, "okeb", CommentDomainManager.getInstance().postNewComment(newComment));
    }
    @RequestMapping(value = "/comments/{id}/vote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response voteComment(@PathVariable(value = "id") Integer commentId, @RequestBody String voteObj) throws CustomException, JsonProcessingException {
        var voteValue = new ObjectMapper().readTree(voteObj).get("vote").asInt();
        return new Response(true, "okeb", CommentDomainManager.getInstance().voteComment(commentId.toString(), voteValue));
    }
}
