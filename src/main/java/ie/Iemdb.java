package ie;

import com.fasterxml.jackson.core.JsonProcessingException;
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
                case Constant.Command.ADD_TO_WATCH_LIST -> resData = addToWatchList(data);
                case Constant.Command.REMOVE_FROM_WATCH_LIST -> resData = removeFromWatchList(data);
                case Constant.Command.VOTE_COMMENT -> resData = voteComment(data);
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

    private String voteComment(String data) throws Exception {
        commentManager.voteComment(data);
        return "comment voted successfully";
    }

    private void setJsonResponse(boolean status, String message) {
        this.response = new Response(status, message);
    }

    private String addUser(String dataJson) throws Exception {
        userManager.updateOrAddElement(dataJson);
        return "user added successfully";
    }

    private String addMovie(String data) throws Exception {
        filmManager.updateOrAddElement(data);
        return "movie added successfully";
    }

    private String addToWatchList(String data) throws Exception {
        userManager.addToWatchList(data);
        return "Movie added to watchlist successfully";
    }

    private String removeFromWatchList(String data) throws Exception {
        userManager.removeFromWatchList(data);
        return "Movie removed from watch list";
    }

    public static ArrayList<String> convertListToString(ArrayList<Integer> intList){
        ArrayList <String> stringList = new ArrayList<>();
        intList.forEach((n) -> {stringList.add(String.valueOf(n));});
        return stringList;
    }

    private String addActor(String data) throws Exception {
        var x = actorManager.updateOrAddElement(data);
        return "actor " + x  + " added successfully";
    }

    private String addComment(String data) throws Exception {
        commentManager.addElement(data);
        return "comment added successfully";
    }

    private String rateMovie(String data) throws Exception {
        filmManager.rateMovie(data);
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
