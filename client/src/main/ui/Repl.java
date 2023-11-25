package ui;

import java.util.Scanner;
import java.util.Arrays;

public class Repl {
    Status status = null;

    private String serverUrl = null;
    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    enum Status {
        BASIC,
        SIGNED_OUT,
        SIGNED_IN
    }

    public void run() {
        status = Status.SIGNED_OUT; //Keeps track of the status of the user to know what prompts to print out
        String response = "";
        System.out.println("\uD83D\uDC36 CHESS");

        Scanner scanner = new Scanner(System.in);
        while (!response.equals("quit")) {
            System.out.println(printMenu(status));

            String line = scanner.nextLine();

            response = readInput(line);
            System.out.print(response);
        }
        System.out.println();
    }

    private String printMenu(Status status) {
        return switch (status) {
            case SIGNED_OUT -> """
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
                case "rescue" -> rescuePet(params);
                case "list" -> listPets();
                case "signout" -> signOut();
                case "adopt" -> adoptPet(params);
                case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    public String signIn(String... params) throws Exception {
        if (params.length == 2) {
            status = Status.SIGNED_IN;
            String username = params[1];
            String password = params[0];
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception("Expected: <username> <password>");
    }
}
