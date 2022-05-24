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

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "false")
public class CommentService {
    @RequestMapping(value = "/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response postNewComment(@RequestBody CommentDTO newComment, HttpServletRequest request) throws SQLException {
        if(newComment.getCommentMovieId() == null || newComment.getText() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            newComment.setCommentOwnerId((String)request.getAttribute("userEmail"));
            return new Response(true, "okeb", CommentDomainManager.getInstance().postNewComment(newComment));
        } catch (CustomException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
        }
    }
    @RequestMapping(value = "/comments/{id}/vote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response voteComment(@PathVariable(value = "id") Integer commentId, @RequestBody String voteObj, HttpServletRequest request) throws SQLException {
        try {
            var voteValue = new ObjectMapper().readTree(voteObj).get("vote").asInt();
            return new Response(true, "okeb", CommentDomainManager.getInstance().voteComment(commentId, voteValue, (String)request.getAttribute("userEmail")));
        }
        catch (Exception e) {
            if(e instanceof SQLException){
                throw (SQLException) e;
            }
            if(e instanceof ObjectNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Related Source Not Found", e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
