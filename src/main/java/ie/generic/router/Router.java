package ie.generic.router;

import ie.generic.controller.Controller;
import io.javalin.Javalin;

public abstract class Router {
    public abstract void addRoutes(Javalin javalin);
}
