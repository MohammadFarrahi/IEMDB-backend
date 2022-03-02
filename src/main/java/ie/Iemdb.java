package ie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.model.actor.ActorManager;
import ie.model.comment.CommentManager;
import ie.exception.CustomException;
import ie.exception.InvalidCommandException;
import ie.model.film.Film;
import ie.model.film.FilmManager;
import ie.util.types.Response;
import ie.model.user.UserManager;
import ie.util.types.Constant;
import java.util.ArrayList;

public class Iemdb {
    private Response response;
    private final UserManager userManager;
    private final FilmManager filmManager;
    private final ActorManager actorManager;
    private final CommentManager commentManager;
    private final ObjectMapper mapper;

    public Iemdb() {
        this.userManager = UserManager.getInstance();
        this.filmManager = FilmManager.getInstance();
        this.actorManager = ActorManager.getInstance();
        this.commentManager = CommentManager.getInstance();
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
                case Constant.Command.GET_MOVIE_BY_ID -> resData = getMovieByIdJson(data);
                case Constant.Command.GET_MOVIE_LIST -> resData = getMoviesListJson();
                case Constant.Command.GET_MOVIES_BY_GENRE -> resData = getMoviesByGenreJson(data);
                case Constant.Command.GET_WATCH_LIST -> resData = getWatchList(data);
                default -> throw new InvalidCommandException();
            }
            setJsonResponse(true, resData);

        } catch (CustomException e) {
            setJsonResponse(false, e.getMessage());
        } catch (Exception e) {
            setJsonResponse(false, InvalidCommandException.message);
        }
    }

    private String voteComment(String data) throws CustomException, JsonProcessingException {
        commentManager.voteComment(data);
        return Constant.SuccessMessage.VOTE_COMMENT;
    }

    private void setJsonResponse(boolean status, String message) {
        this.response = new Response(status, message);
    }

    private String addUser(String dataJson) throws CustomException, JsonProcessingException {
        userManager.updateOrAddElementJson(dataJson);
        return Constant.SuccessMessage.ADD_USER;
    }

    private String addMovie(String data) throws CustomException, JsonProcessingException {
        filmManager.updateOrAddElement(data);
        return Constant.SuccessMessage.ADD_MOVIE;
    }

    private String addToWatchList(String data) throws CustomException, JsonProcessingException {
        userManager.addToWatchList(data);
        return Constant.SuccessMessage.ADD_TO_WATCH_LIST;
    }

    private String removeFromWatchList(String data) throws CustomException, JsonProcessingException {
        userManager.removeFromWatchList(data);
        return Constant.SuccessMessage.REMOVE_FROM_WATCH_LIST;
    }

    private String getMovieByIdJson(String data) throws CustomException, JsonProcessingException {
        var jsonNode = filmManager.getMovieByIdJson(data);
        return mapper.writeValueAsString(jsonNode);
    }

    private String getMoviesListJson() throws CustomException, JsonProcessingException {
        return mapper.writeValueAsString(serializeElementList(null, Constant.Model.FILM, Constant.SER_MODE.SHORT));
    }

    private String getMoviesByGenreJson(String data) throws CustomException, JsonProcessingException {
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

    private String addActor(String data)throws CustomException, JsonProcessingException {
        var x = actorManager.updateOrAddElementJson(data);
        return Constant.SuccessMessage.ADD_ACTOR;
    }

    private String addComment(String data) throws CustomException, JsonProcessingException {
        var commentId = commentManager.addElement(data);
        return "comment with id " + commentId + " added successfully";
    }

    private String rateMovie(String data) throws CustomException, JsonProcessingException {
        filmManager.rateMovie(data);
        return Constant.SuccessMessage.RATE_MOVIE;
    }

    private String getWatchList(String data) throws CustomException, JsonProcessingException {
        var jsonNode = userManager.getWatchList(data);
        return mapper.writeValueAsString(jsonNode);
    }

    public JsonNode serializeElementList(ArrayList<String> idList, Constant.Model modelType, Constant.SER_MODE mode) throws CustomException {
        switch (modelType) {
            case ACTOR:
                return actorManager.serializeElementList(idList, mode);
            case FILM:
                return filmManager.serializeElementList(idList, mode);
            case COMMENT:
                return commentManager.serializeElementList(idList, mode);
            default:
                return null;
        }
    }
     public Film getFilmById(String id) throws CustomException {
        return filmManager.getElement(id);
     }
}
