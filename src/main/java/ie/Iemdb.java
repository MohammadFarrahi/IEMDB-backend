package ie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.actor.ActorManager;
import ie.comment.CommentManager;
import ie.film.FilmManager;
import ie.types.Response;
import ie.user.UserManager;
import ie.types.Constant;

import java.util.ArrayList;

public class Iemdb {
    private Response response;
    private final UserManager userManager;
    private final FilmManager filmManager;
    private final ActorManager actorManager;
    private final CommentManager commentManager;
    public Iemdb() {
        this.userManager = new UserManager(this);
        this.filmManager = new FilmManager(this);
        this.actorManager = new ActorManager(this);
        this.commentManager = new CommentManager(this);
    }

    public String getResponse() {
        return this.response.toString();
    }

    public void runTextCommand(String command, String data) {
        String resData;
        try {
            switch (command) {
                case Constant.Command.ADD_USER -> resData = addUser(data);
                case Constant.Command.ADD_MOVIE -> resData = addMovie(data);
                case Constant.Command.ADD_ACTOR -> resData = addActor(data);
                case Constant.Command.ADD_COMMENT -> resData = addComment(data);
                case Constant.Command.RATE_MOVIE -> resData = rateMovie(data);
                default -> throw new Exception("Invalid Command");
            }
            setJsonResponse(true, resData);

        }catch (JsonProcessingException e) {
            setJsonResponse(false, e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            setJsonResponse(false, e.getMessage());
        }
    }

    private void setJsonResponse(boolean status, String message) {
        this.response = new Response(status, message);
    }

    private String addUser(String dataJson) throws Exception {
        userManager.addUser(dataJson);
        return "user added successfully";
    }

    private String addMovie(String data) throws Exception {
        if (!checkCastExistance(data)) {
            throw new Exception("Actor Not Found");
        }
        filmManager.addMovie(data);
        return "movie added successfully";
    }

    private boolean checkCastExistance(String data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList <String> castIds = mapper.convertValue(mapper.readTree(data).get("cast"), ArrayList.class);
        return actorManager.isActorPresent(castIds);
    }

    private String addActor(String data) throws Exception {
        var x = actorManager.updateOrAddActor(data);
        return "actor added successfully";
    }

    private String addComment(String data) throws Exception {
        commentManager.addComment(data, filmManager, userManager);
        return "comment added successfully";
    }

    private String rateMovie(String data) throws Exception {
        filmManager.rateMovie(data, userManager);
        return "movie rated successfully";
    }
}
