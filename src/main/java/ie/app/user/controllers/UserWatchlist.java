package ie.app.user.controllers;

import ie.Iemdb;
import ie.app.user.UserManager;
import ie.generic.controller.Controller;
import ie.util.types.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Constant.URLS.WATCH_LIST)
public class UserWatchlist extends Controller {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
//        var user = UserManager.getInstance().getElementById(Iemdb.loggedInUser);
    }
}
