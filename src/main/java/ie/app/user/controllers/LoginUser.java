package ie.app.user.controllers;

import ie.Iemdb;
import ie.app.user.UserManager;
import ie.util.types.Constant;
import ie.util.types.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Constant.URLS.ServLet.LOGIN_USER)
public class LoginUser extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var userEmail = request.getParameter(Constant.FormInputNames.USER_ID);
        if(!Email.isValid(userEmail)) {
            // TODO : handler properly. i.e. send back to "/login"
        }
        if(UserManager.getInstance().isIdValid(userEmail)) {
            Iemdb.loggedInUser = userEmail;
            response.sendRedirect(Constant.URLS.JSP.ROOT);
        }
        else {
            // TODO : handle exception
        }
    }
}
