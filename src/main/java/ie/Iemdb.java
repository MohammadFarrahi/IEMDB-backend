package ie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    private final ObjectMapper mapper;

    public Iemdb() {
        this.userManager = new UserManager(this);
        this.filmManager = new FilmManager(this);
        this.actorManager = new ActorManager(this);
        this.commentManager = new CommentManager(this);
        this.mapper = new ObjectMapper();
    }

    public String getResponse() {
        try {
            return this.response.stringify();
        }catch (Exception e) {
            return "error";
        }
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
                case Constant.Command.VOTE_COMMENT -> resData = voteComment(data);
                case Constant.Command.ADD_TO_WATCH_LIST -> resData = addToWatchList(data);
                case Constant.Command.REMOVE_FROM_WATCH_LIST -> resData = removeFromWatchList(data);
                case Constant.Command.GET_MOVIE_BY_ID -> resData = getMovie(data);
                case Constant.Command.GET_MOVIE_LIST -> resData = getMoviesList();
                case Constant.Command.GET_MOVIES_BY_GENRE -> resData = getMoviesByGenre(data);
                case Constant.Command.GET_WATCH_LIST -> resData = getWatchList(data);
                default -> throw new Exception("Invalid Command");
            }
            setJsonResponse(true, resData);

        } catch (JsonProcessingException e) {
            setJsonResponse(false, e.getMessage());
        } catch (Exception e) {
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

    private String getMovie(String data) throws Exception {
        var jsonNode = filmManager.getMovie(data);
//        System.out.println(jsonNode.toPrettyString());
        return mapper.writeValueAsString(jsonNode);
    }

    private String getMoviesList() throws Exception {
        var jsonNode = filmManager.getMoviesList();
        return mapper.writeValueAsString(jsonNode);


    }

    private String getMoviesByGenre(String data) throws Exception {
        var jsonNode = filmManager.getMoviesByGenre(data);
        return mapper.writeValueAsString(jsonNode);
    }

    public static ArrayList<String> convertListToString(ArrayList<Integer> intList) {
        ArrayList<String> stringList = new ArrayList<>();
        intList.forEach((n) -> {
            stringList.add(String.valueOf(n));
        });
        return stringList;
    }

    private String addActor(String data) throws Exception {
        var x = actorManager.updateOrAddElement(data);
        return "actor " + x + " added successfully";
    }

    private String addComment(String data) throws Exception {
        commentManager.addElement(data);
        return "comment added successfully";
    }

    private String rateMovie(String data) throws Exception {
        filmManager.rateMovie(data);
        return "movie rated successfully";
    }

    private String getWatchList(String data) throws Exception {
        var jsonNode = userManager.getWatchList(data);
        return mapper.writeValueAsString(jsonNode);
    }

    public Boolean modelListExists(ArrayList<String> idList, Constant.Model modelType) {
        boolean res;
        switch (modelType) {
            case FILM -> res = filmManager.isIdListValid(idList);
            case USER -> res = userManager.isIdListValid(idList);
            case ACTOR -> res = actorManager.isIdListValid(idList);
            case COMMENT -> res = commentManager.isIdListValid(idList);
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

    public JsonNode serializeElementList(ArrayList<String> idList, Constant.Model modelType, Constant.SER_MODE mode) {
        try {
            switch (modelType) {
                case ACTOR:
                    return actorManager.serializeElementList(idList, mode);

                case FILM:
                    var filmList = filmManager.getElementList(idList);
                    return filmManager.serializeElementList(filmList, mode);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public JsonNode serializeElement(String id, Constant.Model modelType, Constant.SER_MODE mode) {
        switch (modelType) {
            case ACTOR:
                return actorManager.serializeElement(id, mode);
            default:
                return null;
        }
    }
}
