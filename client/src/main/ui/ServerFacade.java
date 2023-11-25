package ui;

import Models.AuthToken;
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
        var path = "/pet";
        return this.makeRequest("POST", path, user, AuthToken.class);
    }

    public void deletePet(int id) throws Exception {
        var path = String.format("/pet/%s", id);
        this.makeRequest("DELETE", path, null, null);
    }

    public void deleteAllPets() throws Exception {
        var path = "/pet";
        this.makeRequest("DELETE", path, null, null);
    }

    public void listGames() throws Exception {
        var path = "/pet";
        var response = this.makeRequest("GET", path, null, null);
        //return response.pet();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
