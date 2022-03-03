package ie.app.actor;

import ie.app.film.Film;
import ie.generic.model.JsonHandler;
import ie.generic.view.View;
import ie.util.types.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ActorView extends View {
    public String getActorHtmlResponse(Actor actor, List<Film> performedMovies) throws IOException {
        var actorJson = JsonHandler.getNodeOfObject(actor);
        var template = Jsoup.parse(new File(Constant.Template.ACTOR), "UTF-8");
        var listItems = template.select("li");
        var table = template.select("table");
        listItems.get(0).append(actorJson.get(Constant.Actor.NAME).asText());
        listItems.get(1).append(actorJson.get(Constant.Actor.B_DATE).asText());
        listItems.get(2).append(actorJson.get(Constant.Actor.NATION).asText());
        listItems.get(3).append(String.valueOf(performedMovies.size()));
        for(var movie : performedMovies) {
            var filmJson = JsonHandler.getNodeOfObject(movie);
            var filmHtml = new Element("tr");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.NAME).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.IMDB).asDouble() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.RATING).asDouble() + "</td>");
            // TODO : get "/movies" route from FilmRouter
            filmHtml.append("<td><a href=\"" + "/movies" + "/" + filmJson.get(Constant.Movie.ID_G).asInt() + "\">Link</a></td>");
            table.append(filmHtml.html());
        }
        return template.html();
    }
}
