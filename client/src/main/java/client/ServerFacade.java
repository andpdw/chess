package client;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private String serverUrl;
    private HttpClient client = HttpClient.newHttpClient();

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clearDatabase() {
        var request = buildRequest("DELETE", "/db", null, null);
        sendRequest(request);
    }

    public AuthData registerUser(UserData userData) {
        var request = buildRequest("POST", "/user", userData, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logoutUser(String authToken) {
        var request = buildRequest("DELETE", "/session", null, authToken);
        sendRequest(request);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (authToken != null) {
            request.header("authorization", authToken);
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private <T> T handleResponse(HttpResponse<String> reponse, Class<T> responseClass) {
        if (reponse == null) {
            System.out.println("Did not get a response");
            return null;
        }

        var status = reponse.statusCode();
        if (status != 200) {
            var body = reponse.body();
            if (body != null) {
                System.out.println("Error in response: " + body);
            }

            System.out.println("Error in response, no body");
        }

        if (responseClass != null) {
            return new Gson().fromJson(reponse.body(), responseClass);
        }

        return null;
    }
}
