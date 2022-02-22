package ie;

import com.fasterxml.jackson.core.JsonProcessingException;
import ie.types.Command;
import ie.types.Response;
import ie.user.UserManager;

import javax.management.InvalidAttributeValueException;

public class Iemdb {
    private Response response;
    private final UserManager userManager;
    public Iemdb() {
        this.userManager = new UserManager();
    }

    public String getResponse() {
        return this.response.toString();
    }

    public void runCommand(Command command, String data) {
        String resData = "";
        try {
            switch (command) {
                case ADD_USER -> addUser(data);
                default -> throw new Exception("Invalid Command");
            }
            setJsonResponse(true, resData);

        }catch (JsonProcessingException e) {
            setJsonResponse(false, "Invalid Command");
        }
        catch (Exception e){
            setJsonResponse(false, e.getMessage());
        }
    }

    private void setJsonResponse(boolean status, String message) {
        this.response = new Response(status, message);
    }

    private void addUser(String dataJson) throws Exception {
        userManager.addUser(dataJson);

    }


}
