package ie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.film.ActorManager;
import ie.film.FilmManager;
import ie.types.Command;
import ie.types.Response;
import ie.user.UserManager;

import java.util.ArrayList;

public class Iemdb {
    private Response response;
    private final UserManager userManager;
    private final FilmManager filmManager;
    private final ActorManager actorManager;
    public Iemdb() {
        this.userManager = new UserManager();
        this.filmManager = new FilmManager();
        this.actorManager = new ActorManager();
    }

    public String getResponse() {
        return this.response.toString();
    }

    public void runCommand(Command command, String data) {
        String resData;
        try {
            switch (command) {
                case ADD_USER -> resData = addUser(data);
                case ADD_MOVIE -> resData = addMovie(data);
                case ADD_ACTOR -> resData = addActor(data);
                default -> throw new Exception("Invalid Command");
            }
            setJsonResponse(true, resData);

        }catch (JsonProcessingException e) {
            setJsonResponse(false, e.getMessage());
        }
        catch (Exception e){
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
        ObjectMapper mapper = new ObjectMapper();
        ArrayList <String> cast = mapper.convertValue(mapper.readTree(data).get("cast"), ArrayList.class);
        if (!isCastValid(cast))
            throw new Exception("Actor Not Found");
        filmManager.addMovie(data);
        return "movie added successfully";
    }

    private Boolean isCastValid(ArrayList<String> cast) {
        //TODO: implement this part after making actor manager
        return true;
    }

    private String addActor(String data) throws Exception {
        var x = actorManager.updateOrAddActor(data);
        return "actor added successfully";
    }

}
