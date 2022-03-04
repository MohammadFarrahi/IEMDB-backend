package ie.app.user;

import ie.generic.controller.Controller;
import io.javalin.http.Context;

public class UserController extends Controller {
    private UserView viewHandler;
    public UserController() { viewHandler = new UserView(); }
    public void getWatchListHandler(Context context) {
    }
}
