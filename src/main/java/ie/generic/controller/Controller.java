package ie.generic.controller;

import ie.generic.view.HtmlUtility;
import ie.util.types.Constant;
import ie.util.types.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller extends HttpServlet {
    public Map<String, String> validateForm(Map<String, String> formData) {
        Map<String, String> errorMessages = new HashMap<>();
        for(var formItem : formData.entrySet()) {
            switch (formItem.getKey()) {
                case Constant.FormInputNames.USER_EMAIL :
                    if (!Email.isValid(formItem.getValue()))
                        errorMessages.put(Constant.FormInputNames.USER_EMAIL, "Input must be in format of email!");
                    break;
                case Constant.FormInputNames.MOVIE_ID :
                    var movieId = formItem.getValue();
                    if(movieId == null || !movieId.matches("-?\\d+(\\.\\d+)?"))
                        errorMessages.put(Constant.FormInputNames.MOVIE_ID, "Input must be in format of numeric id!");
                default:
                    break;
            }
        }
        return errorMessages;
    }
    public String[] splitPathParams(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/"))
            return null;
        return pathInfo.replace("/", " ").trim().split(" ");
    }
    // if pathParts is not in form of pathType, it will return null
    public Map<String, String> validatePath(String[] pathParts, Constant.PathType pathType) {
        Map<String, String> errorMessages = new HashMap<>();

        if (pathParts == null)
            return null;
        switch (pathType) {
            case ID:
                if (pathParts.length != 1 ||  !pathParts[0].matches("-?\\d+(\\.\\d+)?"))
                    errorMessages.put("BadIdFormat", "Id format is not proper");
                break;
            default:
                break;
        }
        return errorMessages;
    }

    public void send404Response(HttpServletRequest request, HttpServletResponse response, Map<String, String> errorMessages)
            throws ServletException, IOException {
        request.setAttribute("errors", HtmlUtility.getHtmlList(errorMessages));
        response.setStatus(404);
        request.getRequestDispatcher(Constant.JSP._404_).forward(request, response);
    }
    public void sendBadRequestResponse(HttpServletRequest request, HttpServletResponse response, Map<String, String> errorMessages)
            throws ServletException, IOException {
        request.setAttribute("errors", HtmlUtility.getHtmlList(errorMessages));
        response.setStatus(400);
        request.getRequestDispatcher(Constant.JSP.ERROR).forward(request, response);
    }
}
