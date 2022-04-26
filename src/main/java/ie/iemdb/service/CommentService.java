package ie.iemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.domain.CommentDomainManager;
import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.ActorDTO;
import ie.iemdb.model.DTO.CommentDTO;
import ie.iemdb.model.DTO.ResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentService {
    // TODO : validation and exception handling
    @RequestMapping(value = "/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO getActorInfo(@RequestBody CommentDTO newComment) throws CustomException {
        if(newComment.getCommentMovieId() == null || newComment.getText() == null) {
            // TODO : do something with error
        }
        return CommentDomainManager.getInstance().postNewComment(newComment);
    }
//    @RequestMapping(value = "/comments/{id}/vote", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseDTO updateComment(@RequestBody CommentDTO newComment) throws CustomException {
//        if(newComment.getCommentMovieId() == null || newComment.getText() == null) {
//            // TODO : do something with error
//        }
//        return CommentDomainManager.getInstance().postNewComment(newComment);
//    }
}
