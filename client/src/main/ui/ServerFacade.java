package ui;

import Models.AuthToken;
import Models.Game;
import Models.GsonSerializer;
import Models.User;
import Responses.GameResponse;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthToken registerUser(User user) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthToken.class, null);
    }

    public void clear(int id) throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public AuthToken login(User user) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthToken.class, null);
    }

    public void logout(AuthToken authToken) throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    //TODO FIX LISTGAME

    public AuthToken listGame(AuthToken authToken) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null, AuthToken.class, authToken);
    }

    //TODO FIX LISTGAME

    public Game createGame(Game game, AuthToken authToken) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, game, Game.class, null);
    }

    public void joinGame(Game game, AuthToken authToken) throws Exception {
        var path = "/game";
        this.makeRequest("PUT", path, game, null, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthToken authToken) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken !=null) {
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestProperty("Authorization", "Bearer YOUR_TOKEN_HERE");
            }

            insertBody(request, http);

            http.connect();
            //throwIfNotSuccessful(http);
            if (http.getResponseCode() != 200) {
                throw new Exception("Failure: " + http.getResponseCode());
            }
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    private static void insertBody(Object bodyObject, HttpURLConnection http) throws IOException {
        if (bodyObject != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String bodyString = new Gson().toJson(bodyObject);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(bodyString.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = GsonSerializer.deserialize(reader, responseClass);
                }
            }
        }
        return response;
    }
}
