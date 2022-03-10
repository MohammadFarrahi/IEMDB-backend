package ie.app.user.controllers;

import ie.Iemdb;
import ie.util.types.Constant;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Constant.URLS.LOGOUT)
public class UserLogout extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Iemdb.loggedInUser = null;
        response.sendRedirect(Constant.URLS.LOGIN);
    }
}
