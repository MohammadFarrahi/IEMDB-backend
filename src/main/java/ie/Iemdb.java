package ie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.exception.CustomException;
import ie.exception.InvalidCommandException;
import ie.app.actor.ActorManager;
import ie.app.comment.CommentManager;
import ie.app.film.FilmManager;
import ie.app.user.UserManager;
import ie.util.types.Constant;
import ie.util.types.Response;
import org.jsoup.Jsoup;

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

    public void fetchData() throws CustomException {
        String json = null;
        try {
            var s1 = actorManager.addElementsJson(Jsoup.connect(Constant.FetchApiUrl.BASE + Constant.FetchApiUrl.ACTOR).ignoreContentType(true).execute().body());
            var s2 = userManager.addElementsJson(Jsoup.connect(Constant.FetchApiUrl.BASE + Constant.FetchApiUrl.USER).ignoreContentType(true).execute().body());
            var s3 = filmManager.addElementsJson(Jsoup.connect(Constant.FetchApiUrl.BASE + Constant.FetchApiUrl.MOVIE).ignoreContentType(true).execute().body());
            var s4 = commentManager.addElementsJson(Jsoup.connect(Constant.FetchApiUrl.BASE + Constant.FetchApiUrl.COMMENT).ignoreContentType(true).execute().body());
        } catch (Exception e) {
            throw new CustomException("DataFetchingFailed");
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
                case Constant.Command.GET_WATCH_LIST -> resData = getWatchListJson(data);
                default -> throw new InvalidCommandException();
            }
            setJsonResponse(true, resData);

        } catch (CustomException e) {
            setJsonResponse(false, e.getMessage());
        } catch (Exception e) {
            setJsonResponse(false, InvalidCommandException.message);
        }
    }
    public String getResponse() {
        try {
            return this.response.stringify();
        }catch (Exception e) {
            return "error";
        }
    }
    private void setJsonResponse(boolean status, String message) {
        this.response = new Response(status, message);
    }

    private String voteComment(String data) throws CustomException, JsonProcessingException {
        commentManager.voteComment(data);
        return Constant.SuccessMessage.VOTE_COMMENT;
    }

    private String addUser(String dataJson) throws CustomException, JsonProcessingException {
        userManager.updateOrAddElementJson(dataJson);
        return Constant.SuccessMessage.ADD_USER;
    }

    private String addMovie(String data) throws CustomException, JsonProcessingException {
        filmManager.updateOrAddElement(data);
        return Constant.SuccessMessage.ADD_MOVIE;
    }

    private String addActor(String data)throws CustomException, JsonProcessingException {
        var x = actorManager.updateOrAddElementJson(data);
        return Constant.SuccessMessage.ADD_ACTOR;
    }

    private String addComment(String data) throws CustomException, JsonProcessingException {
        var commentId = commentManager.addElementJson(data);
        return "comment with id " + commentId + " added successfully";
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
        // TODO : move validation part to another place (not in filmManager)
        var jsonNode = mapper.readTree(data);
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);
        if(jsonFiledNames.size() != 1 || !jsonFiledNames.get(0).equals(Constant.WatchList.M_ID)) {
            throw new InvalidCommandException();
        }

        return filmManager.serializeElement(jsonNode.get(Constant.WatchList.M_ID).asText(), Constant.SER_MODE.LONG);
    }

    private String getMoviesListJson() throws CustomException, JsonProcessingException {
        return filmManager.serializeElementList(null, Constant.SER_MODE.SHORT);
    }

    private String getMoviesByGenreJson(String data) throws CustomException, JsonProcessingException {
        // TODO : move validation part to another place (not in filmManager)
        var jsonNode = mapper.readTree(data);
        ArrayList<String> jsonFiledNames = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(jsonFiledNames::add);
        if(jsonFiledNames.size() != 1 || !jsonFiledNames.get(0).equals("genre"))
            throw new InvalidCommandException();

        return filmManager.serializeElementList(filmManager.filterElementsByGenre(jsonNode.get("genre").asText()), Constant.SER_MODE.SHORT);
    }

    private String rateMovie(String data) throws CustomException, JsonProcessingException {
        filmManager.rateMovie(data);
        return Constant.SuccessMessage.RATE_MOVIE;
    }

    private String getWatchListJson(String data) throws CustomException, JsonProcessingException {
        return userManager.getWatchListJson(data);
    }
//    public static ArrayList<String> convertListToString(ArrayList<Integer> intList) {
//        ArrayList<String> stringList = new ArrayList<>();
//        intList.forEach((n) -> {
//            stringList.add(String.valueOf(n));
//        });
//        return stringList;
//    }
}
