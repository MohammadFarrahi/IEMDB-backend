package ie.app.user.controllers;

import ie.Iemdb;
import ie.app.user.UserManager;
import ie.exception.ObjectNotFoundException;
import ie.exception.UserNotFoundException;
import ie.util.types.Constant;
import ie.util.types.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

@WebServlet(Constant.URLS.LOGIN)
public class UserLogin extends HttpServlet {
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
        // business logic
        try {
            Iemdb.loginUser(userEmail);
        } catch (ObjectNotFoundException e) {
            errorMessages.put("InvalidUser", "user does not exist!");
        }
        // results
        if(!errorMessages.isEmpty()) {
            request.setAttribute("errors", getHtmlList(errorMessages));
            response.setStatus(404);
            /* TODO : if browser reload after fist submission request, it's still post request.
                        so your error messages will not be  flash messages.
                        redirection might be a better idea to solve this issue*/
            request.getRequestDispatcher(Constant.JSP.LOGIN).forward(request, response);
        } else {
            response.sendRedirect(Constant.URLS.ROOT);
        }
    }
    // TODO : generalize this shit
    public Map<String, String> validateForm(Map<String, String> formData) {
        Map<String, String> errorMessages = new HashMap<>();
        for(var formItem : formData.entrySet()) {
            switch (formItem.getKey()) {
                case Constant.FormInputNames.USER_EMAIL :
                    if (!Email.isValid(formItem.getValue()))
                        errorMessages.put(Constant.FormInputNames.USER_EMAIL, "Input must be in format of email!");
                    break;
                default:
                    break;
            }
        }
        return errorMessages;
    }
    // TODO : generalize this shit
    public String getHtmlList(Map<String, String> dataMap) {
        var html = "";
        html += "<div><ul>";
        for(var data : dataMap.values()) {
            html += "<li>" + data + "</li";
        }
        html +="</ul><div>";
        return html;
    }
}
