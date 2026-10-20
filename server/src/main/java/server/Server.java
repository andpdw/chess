package server;

import com.google.gson.Gson;
import dataaccess.LocalDataStorage;
import io.javalin.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    private LocalDataStorage dataStorage = new LocalDataStorage();

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        //javalin.post("/name", this::createUser);
        //javalin.get("/name", this::getAllUsers);
        javalin.delete("/db", this::clearDatabase);
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this::logoutUser);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void clearDatabase(Context ctx) {
        return;
    }

    private void registerUser(Context ctx) {
        return;
    }

    private void loginUser(Context ctx) {
        return;
    }

    private void logoutUser(Context ctx) {
        return;
    }

    private void listGames(Context ctx) {
        return;
    }

    private void createGame(Context ctx) {
        return;
    }

    private void joinGame(Context ctx) {
        return;
    }

    /*private void createUser(Context ctx) {
        try {
            UserData data = getBodyObject(ctx, UserData.class);
            if (dataStorage.GetUserData(data.getUsername()) == null) {
                dataStorage.SetUserData(data);
                ctx.status(200);
            } else {
                throw new RuntimeException("Username already in use");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getAllUsers(Context ctx) {
        String json = new Gson().toJson(dataStorage.GetAllUsers());
        ctx.json(json);
    }*/

    private static <T> T getBodyObject(Context ctx, Class<T> returnClass) {
        var bodyObject = new Gson().fromJson(ctx.body(), returnClass);

        if (bodyObject == null) {
            throw new RuntimeException("Missing Body Information");
        }
        return bodyObject;
    }
}
