package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class Server {
    private ArrayList<String> names = new ArrayList<>();

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/name/{username}", this::createUser);
        javalin.get("/name", this::getUsers);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void createUser(Context ctx) {
        try {
            names.add(ctx.pathParam("username"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getUsers(Context ctx) {
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(Map.of("name", names)));
    }
}
