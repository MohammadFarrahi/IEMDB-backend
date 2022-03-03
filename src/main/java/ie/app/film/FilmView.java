package ie.app.film;

import ie.app.actor.Actor;
import ie.generic.model.JsonHandler;
import ie.generic.view.View;
import ie.util.types.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilmView extends View {
    public String getMoviesHtmlResponse(List<Film> films, List<List<Actor>> filmsCast) throws IOException {
        var template = Jsoup.parse(new File(Constant.Template.MOVIES), "UTF-8");
        var table = template.select("table").first();
        var index = 0;
        for(var film : films) {
            var filmJson = JsonHandler.getNodeOfObject(film);
            var filmHtml = new Element("tr");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.NAME).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.SUMM).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.R_DATE).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.DIRECTOR).asText() + "</td>");
            filmHtml.append("<td>" + View.getCSVFromList(filmJson.get(Constant.Movie.WRITERS).toPrettyString()) + "</td>");
            filmHtml.append("<td>" + View.getCSVFromList(filmJson.get(Constant.Movie.GENRE).toPrettyString()) + "</td>");
            filmHtml.append("<td>" + getActorNames(filmsCast.get(index++)) + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.IMDB).asDouble() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.RATING).asDouble() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.DURATION).asInt() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.AGE_L).asInt() + "</td>");
            filmHtml.append("<td><a href=\"" + Constant.Server.MOVIES + "/" + filmJson.get(Constant.Movie.ID_G).asInt() + "\">Link</a></td>");
            table.append(filmHtml.html());
        }
        return template.html();
    }
    private String getActorNames(List<Actor> actors) {
        var actorsNames = new ArrayList<String>();
        actors.forEach(actor -> actorsNames.add(" " + actor.getName()));
        return View.getCSVFromList(actorsNames.toString());
    }
}
