package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.LocalDataStorage;
import dataaccess.UnauthorizedException;
import io.javalin.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AuthData;
import model.LoginData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Server {
    private LocalDataStorage dataStorage = new LocalDataStorage();

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
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
        try {
            dataStorage.ClearAuthData();
            dataStorage.ClearGameData();
            dataStorage.ClearUserData();
            ctx.status(200);
        } catch (Exception e) {
            ctx.json(exceptionToJson(e));
            ctx.status(500);
        }
    }

    private void registerUser(Context ctx) {
        try {
            UserData userData = getBodyObject(ctx, UserData.class);
            if (!userData.validData()) {
                throw new BadRequestException("bad request");
            }
            if (dataStorage.GetUserData(userData.getUsername()) == null) {
                dataStorage.SetUserData(userData);

                AuthData authData = new AuthData(userData.getUsername());
                dataStorage.SetAuthData(authData);
                String json = new Gson().toJson(authData);
                ctx.json(json);
                ctx.status(200);
            } else {
                throw new AlreadyTakenException("already taken");
            }
        } catch (AlreadyTakenException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(403);
        } catch (BadRequestException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(400);
        } catch (Exception e) {
            ctx.json(exceptionToJson(e));
            ctx.status(500);
        }
    }

    private void loginUser(Context ctx) {
        try {
            LoginData loginData = getBodyObject(ctx, LoginData.class);
            if (!loginData.validData()) {
                throw new BadRequestException("bad request");
            }

            UserData userData = dataStorage.GetUserData(loginData.getUsername());
            if (userData == null || (!Objects.equals(userData.getPassword(), loginData.getPassword()))) {
                throw new UnauthorizedException("unauthorized");
            } else {
                AuthData authData = new AuthData(loginData.getUsername());
                dataStorage.SetAuthData(authData);
                String json = new Gson().toJson(authData);
                ctx.json(json);
                ctx.status(200);
            }

        } catch (BadRequestException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(400);
        } catch (UnauthorizedException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(401);
        } catch (Exception e) {
            ctx.json(exceptionToJson(e));
            ctx.status(500);
        }

    }

    private void logoutUser(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            AuthData authData = dataStorage.GetAuthData(authToken);
            if (authData == null) {
                throw new UnauthorizedException("unauthorized");
            }
            dataStorage.RemoveAuthData(authToken);
            ctx.status(200);
        }catch (UnauthorizedException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(401);
        } catch (Exception e) {
            ctx.json(exceptionToJson(e));
            ctx.status(500);
        }
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

    private String exceptionToJson(Exception e) {
        return "{\"message\":\"Error: " + e.getMessage() + "\"}";
    }
}
