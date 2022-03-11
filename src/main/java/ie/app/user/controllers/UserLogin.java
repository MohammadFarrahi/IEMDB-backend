package ie.app.user.controllers;

import ie.Iemdb;
import ie.exception.ObjectNotFoundException;
import ie.generic.controller.Controller;
import ie.util.types.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

@WebServlet(Constant.URLS.LOGIN)
public class UserLogin extends Controller {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (Iemdb.isLoggedIn(null))
            response.sendRedirect(Constant.URLS.ROOT);
        else
            request.getRequestDispatcher(Constant.JSP.LOGIN).forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var userEmail = request.getParameter(Constant.FormInputNames.USER_EMAIL);

        // validation logic
        var errorMessages = validateForm(Map.ofEntries(entry(Constant.FormInputNames.USER_EMAIL, userEmail)));
        if(!errorMessages.isEmpty()) {
            sendBadRequestResponse(request, response, errorMessages);
        }
        // business logic
        try {
            Iemdb.loginUser(userEmail);
        } catch (ObjectNotFoundException e) {
            errorMessages.put("InvalidUser", "User Not Found");
        }
        // results
        if(!errorMessages.isEmpty()) {
            send404Response(request, response, errorMessages);
        } else {
            var query = request.getQueryString();
            if(query != null)
                response.sendRedirect(query.split("=")[1]);
            else
                response.sendRedirect(Constant.URLS.ROOT);
        }
    }
}
