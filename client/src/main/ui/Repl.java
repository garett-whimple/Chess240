package ui;

import Models.AuthToken;
import Models.User;

import java.util.Scanner;
import java.util.Arrays;

public class Repl {
    Status status = null;
    AuthToken authToken = null;

    private String serverUrl = null;
    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    enum Status {
        BASIC,
        LOGGED_OUT,
        LOGGED_IN
    }

    public void run() {
        status = Status.LOGGED_OUT; //Keeps track of the status of the user to know what prompts to print out
        String response = "";
        System.out.println("\uD83D\uDC36 CHESS");

        Scanner scanner = new Scanner(System.in);
        while (!response.equals("quit")) {

            String line = scanner.nextLine();

            response = readInput(line);
            System.out.print(response);
        }
        System.out.println();
    }

    private String help(Status status) {
        return switch (status) {
            case LOGGED_OUT -> """
                    Return This String.
                    """;
            default -> """
                    Default Print String
                    """;
        };
    }

    private String readInput(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "quit" -> "quit";
                default -> help(status);
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    public String signIn(String... params) throws Exception {
        if (params.length == 2) {
            status = Status.LOGGED_IN;
            String username = params[1];
            String password = params[0];
            User user = new User(username, password, null);
            ServerFacade sf = new ServerFacade(serverUrl);
            try {
                authToken = sf.login(user);
            } catch (Throwable e) {
                throw new Exception("Unexpected error please try again later");
            }
            return String.format("You signed in as %s.", username);
        }
        throw new Exception("Expected: <username> <password>");
    }
}
