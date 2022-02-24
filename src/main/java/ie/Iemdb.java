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
        filmManager.addMovie(data);
        return "movie added successfully";
    }


    public static ArrayList<String> convertListToString(ArrayList<Integer> intList){
        ArrayList <String> stringList = new ArrayList<>();
        intList.forEach((n) -> {stringList.add(String.valueOf(n));});
        return stringList;
    }

    private String addActor(String data) throws Exception {
        var x = actorManager.updateOrAddActor(data);
        return "actor added successfully";
    }

    private String addComment(String data) throws Exception {
        commentManager.addComment(data);
        return "comment added successfully";
    }

    private String rateMovie(String data) throws Exception {
        filmManager.rateMovie(data, userManager);
        return "movie rated successfully";
    }

    public Boolean modelListExists(ArrayList<String> id, Constant.Model modelType) {
        boolean res;
        switch (modelType) {
            case FILM -> res = filmManager.isIdListValid(id);
            case USER -> res = userManager.isIdListValid(id);
            case ACTOR -> res = actorManager.isIdListValid(id);
            case COMMENT -> res = commentManager.isIdListValid(id);
            default -> res = false;
        }
        return res;
    }

    public Boolean modelExists(String id, Constant.Model modelType) {
        boolean res;
        switch (modelType) {
            case FILM -> res = filmManager.isIdValid(id);
            case USER -> res = userManager.isIdValid(id);
            case ACTOR -> res = actorManager.isIdValid(id);
            case COMMENT -> res = commentManager.isIdValid(id);
            default -> res = false;
        }
        return res;
    }
}
