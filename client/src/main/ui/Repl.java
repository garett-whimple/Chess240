package ui;

import ChessImpl.ChessBoardImpl;
import ChessImpl.ChessPositionImpl;
import Models.AuthToken;
import Models.Game;
import Models.User;
import Requests.JoinGameRequest;
import Responses.GameResponse;
import Responses.ListGameObject;
import Responses.ListGameResponse;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

import static ui.EscapeSequences.*;

public class Repl {
    Status status = null;
    AuthToken authToken = null;
    String username = null;
    Map<Integer, ListGameObject> currentGameList = new HashMap<>();

    private String serverUrl = null;
    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    enum Status {
        LOGGED_OUT,
        LOGGED_IN
    }

    public void run() {
        status = Status.LOGGED_OUT; //Keeps track of the status of the user to know what prompts to print out
        String response = "";
        System.out.println(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "Welcome to CHESS enter help to start");


        Scanner scanner = new Scanner(System.in);
        while (!response.equals("quit")) {

            String line = scanner.nextLine();

            response = readInput(line);
            System.out.print(response);
        }
        System.out.println();
    }

    private String printBoard(ChessBoard board, ChessGame.TeamColor color) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> stringArray = new ArrayList<>();
        sb.append(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE);
        addStringToList(stringArray, sb);
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        for (int i = 0; i < 8; i++) {
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (char)('a'+i) + " ");
            addStringToList(stringArray, sb);
        }
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        sb.append(RESET_BG_COLOR + "\n");
        addStringToList(stringArray, sb);
        for (int i = 1; i < 9; i++) {
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (9-i) + " ");
            addStringToList(stringArray, sb);
            for (int j = 1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPositionImpl(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if ((i + j)%2 == 0) {
                    sb.append(SET_BG_COLOR_WHITE);
                } else {
                    sb.append(SET_BG_COLOR_BLACK);
                }
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        sb.append(SET_TEXT_COLOR_BLUE + SET_TEXT_BOLD);
                    } else {
                        sb.append(SET_TEXT_COLOR_RED + SET_TEXT_BOLD);
                    }
                    sb.append(" " + getChessPieceLetter(currentPiece) + " ");
                    addStringToList(stringArray, sb);
                } else {
                    sb.append("   ");
                    addStringToList(stringArray, sb);
                }
            }
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (9-i) + " ");
            addStringToList(stringArray, sb);
            sb.append(RESET_BG_COLOR + "\n");
            addStringToList(stringArray, sb);
        }
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        for (int i = 0; i < 8; i++) {
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (char)('a'+i) + " ");
            addStringToList(stringArray, sb);
        }
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        sb.append(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "\n");
        addStringToList(stringArray, sb);
        if (color == ChessGame.TeamColor.BLACK) {
            Collections.reverse(stringArray);
        }
        for (String item: stringArray) {
            sb.append(item);
        }
        return sb.toString();
    }

    private void addStringToList(ArrayList<String> stringArray, StringBuilder sb) {
        stringArray.add(sb.toString());
        sb.delete(0,sb.length());
    }

    private char getChessPieceLetter(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> 'K';
            case KNIGHT -> 'N';
            case ROOK -> 'R';
            case BISHOP -> 'B';
            case QUEEN -> 'Q';
            case PAWN -> 'P';
            default -> ' ';
        };
    }

    private String help(Status status) {
        return switch (status) {
            case LOGGED_OUT -> String.format("""
                [%s] >>>
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                            """
                    , status);
            case LOGGED_IN -> String.format("""
                [%s] >>>
                create <NAME> - a game
                list - games
                join - <ID> [WHITE | BLACK | <EMPTY>] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                            """
                    , status);
            default -> "No status assigned";
        };
    }

    private String readInput(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "clear" -> clear();
                case "quit" -> "quit";
                default -> help(status);
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    public String login(String... params) throws Exception {
        if (status != Status.LOGGED_OUT) {
            throw new Exception("You must be logged out to access that function");
        }
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            User user = new User(username, password, null);
            ServerFacade sf = new ServerFacade(serverUrl);
            try {
                authToken = sf.login(user);
                this.username = username;
            } catch (Throwable e) {
                throw new Exception("Unexpected error please try again later\n");
            }
            status = Status.LOGGED_IN;
            return String.format("You signed in as %s.\n", username);
        }
        throw new Exception("Expected: <username> <password>\n");
    }

    public String logout() throws Exception {
        if (status != Status.LOGGED_IN) {
            throw new Exception("You must be logged in to access that function");
        }
        ServerFacade sf = new ServerFacade(serverUrl);
        try {
            sf.logout(authToken);
        } catch (Throwable e) {
            throw new Exception("Unexpected error please try again later\n");
        }
        status = Status.LOGGED_OUT;
        return String.format("You signed out\n");
    }

    public String register(String... params) throws Exception {
        if (status != Status.LOGGED_OUT) {
            throw new Exception("You must be logged out to access that function");
        }
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            User user = new User(username, password, email);
            ServerFacade sf = new ServerFacade(serverUrl);
            try {
                authToken = sf.registerUser(user);
                this.username = username;
            } catch (Throwable e) {
                throw new Exception("Unexpected error please try again later\n");
            }
            status = Status.LOGGED_IN;
            return String.format("You signed in as %s.\n", username);
        }
        throw new Exception("Expected: <username> <password> <email>\n");
    }

    public String create(String... params) throws Exception {
        if (status != Status.LOGGED_IN) {
            throw new Exception("You must be logged in to access that function");
        }
        GameResponse returnGame = null;
        if (params.length == 1) {
            String name = params[0];
            Game game = new Game(null, null, null, name, null);
            ServerFacade sf = new ServerFacade(serverUrl);
            try {
                returnGame = sf.createGame(game, authToken);
            } catch (Throwable e) {
                throw new Exception("Unexpected error please try again later\n");
            }
            status = Status.LOGGED_IN;
            return String.format("You created a new game with id : %s.\n", returnGame.getGameID());
        }
        throw new Exception("Expected: <NAME>\n");
    }

    //TODO FIX the Join function so that it randomly chooses a valid color

    public String join(String... params) throws Exception {
        if (status != Status.LOGGED_IN) {
            throw new Exception("You must be logged in to access that function");
        }
        GameResponse returnGame = null;
        Integer id = null;
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        if (params.length == 2 || params.length == 1) {
            id = Integer.parseInt(params[0]);
            if (params.length == 2) {
                String colorToBe = params[1];
                if (colorToBe == "WHITE") {
                    color = ChessGame.TeamColor.WHITE;
                } else {
                    color = ChessGame.TeamColor.BLACK;
                }
            } else {
                Random random = new Random();
                int index = random.nextInt(2);
                if (index == 0) {
                    color = ChessGame.TeamColor.BLACK;
                }
            }
            JoinGameRequest joinGameRequest = new JoinGameRequest(id,username, color);
            ServerFacade sf = new ServerFacade(serverUrl);
            try {
                 sf.joinGame(joinGameRequest, authToken);
            } catch (Throwable e) {
                throw new Exception("Unexpected error please try again later\n");
            }
            status = Status.LOGGED_IN;
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("You joined game %s as %s.\n", id, color));
            ChessBoard board = new ChessBoardImpl();
            board.resetBoard();
            sb.append(printBoard(board, ChessGame.TeamColor.WHITE));
            sb.append(printBoard(board, ChessGame.TeamColor.BLACK));
            return sb.toString();
        }
        throw new Exception("Expected: <ID>\n");
    }

    public String observe(String... params) throws Exception {
        if (status != Status.LOGGED_IN) {
            throw new Exception("You must be logged in to access that function");
        }
        if (params.length == 1) {
            Integer id = Integer.parseInt(params[0]);
            JoinGameRequest joinGameRequest = new JoinGameRequest(id,username, null);
            ServerFacade sf = new ServerFacade(serverUrl);
            try {
                sf.joinGame(joinGameRequest, authToken);
            } catch (Throwable e) {
                throw new Exception("Unexpected error please try again later\n");
            }
            status = Status.LOGGED_IN;
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("You are observing game %s\n", id));
            ChessBoard board = new ChessBoardImpl();
            board.resetBoard();
            sb.append(printBoard(board, ChessGame.TeamColor.WHITE));
            sb.append(printBoard(board, ChessGame.TeamColor.BLACK));
            return sb.toString();
        }
        throw new Exception("Expected: <ID>\n");
    }

    public String list() throws Exception {
        if (status != Status.LOGGED_IN) {
            throw new Exception("You must be logged in to access that function");
        }
        ListGameResponse returnList = null;
        currentGameList.clear();
        ServerFacade sf = new ServerFacade(serverUrl);
        try {
            returnList = sf.listGame(authToken);
        } catch (Throwable e) {
            throw new Exception("Unexpected error please try again later\n");
        }
        ArrayList<ListGameObject> GameList = returnList.getGames();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < GameList.size(); i++) {
            ListGameObject item = GameList.get(i);
            String newString = String.format("%d) %s: %s - BLACK: %s  WHITE: %s\n",i, item.getGameName(), item.getGameID(), item.getBlackUsername(), item.getWhiteUsername());
            stringBuilder.append(newString);
            currentGameList.put(item.getGameID(), item);
        }
        stringBuilder.append("\n");
        return stringBuilder.toString().trim();
    }

    public String clear() throws Exception {
        ServerFacade sf = new ServerFacade(serverUrl);
        try {
             sf.clear();
        } catch (Throwable e) {
            throw new Exception("Unexpected error please try again later\n");
        }
        return "Database cleared\n";
    }
}
