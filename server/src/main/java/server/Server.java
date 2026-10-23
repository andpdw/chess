package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AlreadyTakenException;
import handler.HandlerClass;
import io.javalin.http.Handler;
import service.BadRequestException;
import dataaccess.LocalDataStorage;
import dataaccess.UnauthorizedException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import service.JoinGameData;
import service.LoginData;
import model.UserData;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Server {
    private HandlerClass handler;
    private LocalDataStorage dataStorage = new LocalDataStorage();

    private final Javalin javalin;

    public Server() {
        handler = new HandlerClass();
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
            handler.clear();
            ctx.status(200);
        } catch (Exception e) {
            ctx.json(exceptionToJson(e));
            ctx.status(500);
        }
    }

    private void registerUser(Context ctx) {
        try {
            UserData userData = getBodyObject(ctx, UserData.class);
            AuthData authData = handler.registerUser(userData);

            String json = new Gson().toJson(authData);
            ctx.json(json);
            ctx.status(200);
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
            AuthData authData = handler.loginUser(loginData);

            String json = new Gson().toJson(authData);
            ctx.json(json);
            ctx.status(200);
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
            handler.logoutUser(authToken);

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
        try {
            String authToken = ctx.header("authorization");
            List<GameData> gameDataList = handler.listGames(authToken);

            String json = new Gson().toJson(Map.of("games", gameDataList));
            ctx.json(json);
            ctx.status(200);
        } catch (UnauthorizedException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(401);
        } catch (Exception e) {
            ctx.json(exceptionToJson(e));
            ctx.status(500);
        }
    }

    private void createGame(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            String gameName;

            Map<String, String> bodyObject = getBodyObject(ctx, Map.class);
            if (bodyObject.get("gameName") == null || bodyObject.get("gameName").isEmpty()) {
                throw new BadRequestException("bad request");
            } else {
                gameName = bodyObject.get("gameName");
            }

            int gameID = handler.createGame(authToken, gameName);
            String json = new Gson().toJson(Map.of("gameID", gameID));
            ctx.json(json);
            ctx.status(200);
        }catch (BadRequestException e) {
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

    private void joinGame(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            JoinGameData joinGameData = getBodyObject(ctx, JoinGameData.class);
            handler.joinGame(authToken, joinGameData);

            ctx.status(200);
        }catch (BadRequestException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(400);
        } catch (UnauthorizedException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(401);
        } catch (AlreadyTakenException e) {
            ctx.json(exceptionToJson(e));
            ctx.status(403);
        } catch (Exception e) {
            ctx.json(exceptionToJson(e));
            ctx.status(500);
        }
    }

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
