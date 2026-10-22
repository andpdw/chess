package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.LocalDataStorage;
import dataaccess.UnauthorizedException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.LoginData;
import model.UserData;

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
        try {
            String authToken = ctx.header("authorization");
            AuthData authData = dataStorage.GetAuthData(authToken);
            if (authData == null) {
                throw new UnauthorizedException("unauthorized");
            }

            String json = new Gson().toJson(Map.of("games", dataStorage.GetAllGames()));
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
            AuthData authData = dataStorage.GetAuthData(authToken);
            if (authData == null) {
                throw new UnauthorizedException("unauthorized");
            }

            Map<String, String> bodyObject = getBodyObject(ctx, Map.class);

            if (bodyObject.get("gameName") == null || bodyObject.get("gameName").isEmpty()) {
                throw new BadRequestException("bad request");
            }

            GameData gameData = new GameData(dataStorage.GetNextGameID(), bodyObject.get("gameName"));
            dataStorage.SetGameData(gameData);

            String json = new Gson().toJson(Map.of("gameID", gameData.getID()));
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
            AuthData authData = dataStorage.GetAuthData(authToken);
            if (authData == null) {
                throw new UnauthorizedException("unauthorized");
            }

            JsonObject bodyObject = getBodyObject(ctx, JsonObject.class);
            if (bodyObject.get("playerColor") == null || bodyObject.get("gameID") == null) {
                throw new BadRequestException("bad request");
            }
            String playerColor = bodyObject.get("playerColor").getAsString();
            int gameID = bodyObject.get("gameID").getAsInt();

            if (dataStorage.GetGameData(gameID) != null) {
                GameData gameData = dataStorage.GetGameData(gameID);

                if (Objects.equals(playerColor, "WHITE")) {
                    if (gameData.getWhiteUsername() == null || Objects.equals(gameData.getWhiteUsername(), "")) {
                        gameData.setWhiteUserName(authData.getUsername());
                    } else {
                        throw new AlreadyTakenException("already taken");
                    }
                } else if (Objects.equals(playerColor, "BLACK")) {
                    if (gameData.getBlackUsername() == null || Objects.equals(gameData.getBlackUsername(), "")) {
                        gameData.setBlackUserName(authData.getUsername());
                    } else {
                        throw new AlreadyTakenException("already taken");
                    }
                } else {
                    throw new BadRequestException("bad request");
                }

                dataStorage.SetGameData(gameData);
            } else {
                throw new BadRequestException("bad request");
            }

            String json = new Gson().toJson(Map.of("games", dataStorage.GetAllGames()));
            ctx.json(json);
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
