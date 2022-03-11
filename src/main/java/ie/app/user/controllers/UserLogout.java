package ie.app.user.controllers;

import ie.Iemdb;
import ie.generic.controller.Controller;
import ie.util.types.Constant;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Constant.URLS.LOGOUT)
public class UserLogout extends Controller {
    // TODO : how to make it a post request ?
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Iemdb.logoutUser(null);
        response.sendRedirect(Constant.URLS.LOGIN);
    }
    // TODO : how to handle different(useless) http methods properly with proper code-page-message
}
