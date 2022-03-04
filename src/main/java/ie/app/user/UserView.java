package ie.app.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import ie.app.film.Film;
import ie.generic.model.JsonHandler;
import ie.generic.view.View;
import ie.util.types.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserView  extends View {
    public String getWatchListHtmlResponse(User user, List<Film> userWatchList) throws IOException {
        var template = Jsoup.parse(new File(Constant.Template.W_LIST), "UTF-8");
        var userJson = JsonHandler.getNodeOfObject(user);

        var listItems = template.select("li");
        listItems.get(0).append(userJson.get(Constant.User.NAME).asText());
        listItems.get(1).append(userJson.get(Constant.User.NICKNAME).asText());

        var table = template.select("table").first();
        var removeForm = Jsoup.parse(new File(Constant.Template.W_LIST_R_F), "UTF-8");
        for(var film : userWatchList) {
            var filmJson = JsonHandler.getNodeOfObject(film);
            var filmHtml = new Element("tr");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.NAME).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.R_DATE).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.DIRECTOR).asText() + "</td>");
            filmHtml.append("<td>" + View.getCSVFromList(filmJson.get(Constant.Movie.GENRE).toPrettyString()) + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.IMDB).asDouble() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.RATING).asDouble() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.DURATION).asInt() + "</td>");
            // TODO : get "/movies" route from FilmRouter
            filmHtml.append("<td><a href=\"" + "/movies" + "/" + filmJson.get(Constant.Movie.ID_G).asInt() + "\">Link</a></td>");
            removeForm.getElementById("form_movie_id").attr("value", film.getId().toString());
            filmHtml.append("<td>" + removeForm.html() + "</td>");
            table.append(filmHtml.html());
        }
        return template.html();
    }
}
