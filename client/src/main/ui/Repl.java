package ui;

import ChessImpl.ChessBoardImpl;
import ChessImpl.ChessMoveImpl;
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
import webSocketMessages.userCommands.UserGameCommand;

import java.util.*;

import static ui.EscapeSequences.*;

public class Repl {
    Status status = null;
    AuthToken authToken = null;
    String username = null;

    Integer gameId = null;

    WSClient wsClient = null;
    Map<Integer, ListGameObject> currentGameList = new HashMap<>();

    private String serverUrl = null;
    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    enum Status {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME,
        OBSERVING
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

    public String printBoard(ChessBoard board, ChessGame.TeamColor color) {
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
            case IN_GAME -> String.format("""
                [%s] >>>
                redraw - board
                highlight <Position> - valid moves a your piece
                move <Start Position> <End Position> [PIECE TYPE] - a piece
                leave - game
                resign - game
                help - with possible commands
                            """
                    , status);
            case OBSERVING -> String.format("""
                [%s] >>>
                redraw - board
                leave - game
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
                case "redraw" -> redraw();
                case "highlight" -> highlight(params);
                case "move" -> move(params);
                case "leave" -> leave();
                case "resign" -> resign();
                case "quit" -> "quit";
                default -> help(status);
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    public String redraw() throws Exception {
        if (status != Status.IN_GAME && status != Status.OBSERVING) {
            throw new Exception("You must be playing a game to access that function");
        }
        UserGameCommand newCommand = new UserGameCommand(authToken.getAuthToken());
        newCommand.setGameID(gameId);
        newCommand.setCommandType(UserGameCommand.CommandType.REDRAW);
        wsClient.send(newCommand);
        return "Redrawing: ";
    }

    public String highlight(String... params) throws Exception {
        if (status != Status.IN_GAME) {
            throw new Exception("You must be playing a game to access that function");
        }
        if (params.length == 1) {
            String positionString = params[0];
            ChessPositionImpl position = parseChessMove(positionString);
            UserGameCommand newCommand = new UserGameCommand(authToken.getAuthToken());
            newCommand.setGameID(gameId);
            newCommand.setHighlightPosition(position);
            newCommand.setCommandType(UserGameCommand.CommandType.REDRAW);
            wsClient.send(newCommand);
            return "IN HIGHLIGHT";
        }
        throw new Exception("Expected: <POSITION>\n");
    }

    public String move(String... params) throws Exception {
        if (status != Status.IN_GAME) {
            throw new Exception("You must be playing a game to access that function");
        }
        ChessPiece.PieceType promotionType = null;
        if (params.length == 2 || params.length == 3) {
            if (params.length == 3) {
                String promotionPiece = params[2];
                promotionType = switch (promotionPiece) {
                    case "Q" -> ChessPiece.PieceType.QUEEN;
                    case "B" -> ChessPiece.PieceType.BISHOP;
                    case "K" -> ChessPiece.PieceType.KNIGHT;
                    case "R" -> ChessPiece.PieceType.ROOK;
                    default -> throw new Exception("INVALID PROMOTION PIECE");
                };
            }
            String startingPositionString = params[0];
            String endingPositionString = params[1];
            ChessPositionImpl startingPosition = parseChessMove(startingPositionString);
            ChessPositionImpl endingPosition = parseChessMove(endingPositionString);
            UserGameCommand newCommand = new UserGameCommand(authToken.getAuthToken());
            newCommand.setGameID(gameId);
            newCommand.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            ChessMoveImpl move = new ChessMoveImpl(startingPosition, endingPosition, promotionType);
            newCommand.setMove(move);
            wsClient.send(newCommand);
            return "IN MOVE: ";
        }
        throw new Exception("Expected: <Starting Position> <Ending Position>\n");
    }

    public ChessPositionImpl parseChessMove(String string) throws Exception {
        if (string.length() != 2) {
            throw new Exception("Expected: <Starting Position> <Ending Position>");
        }
        char colChar = string.charAt(0);
        char rowChar = string.charAt(1);

        int col = colChar - 'a' + 1;
        int row = Character.getNumericValue(rowChar);

        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new Exception("Starting or Ending row is not in the valid range");
        }
        ChessPositionImpl position = new ChessPositionImpl(row, col);
        return position;
    }

    public String leave() throws Exception {
        if (status != Status.IN_GAME && status != Status.OBSERVING) {
            throw new Exception("You must be playing a game to access that function");
        }
        UserGameCommand newCommand = new UserGameCommand(authToken.getAuthToken());
        newCommand.setGameID(gameId);
        newCommand.setCommandType(UserGameCommand.CommandType.LEAVE);
        wsClient.send(newCommand);
        status = Status.LOGGED_IN;
        return "IN LEAVE\n";
    }

    public String resign() throws Exception {
        if (status != Status.IN_GAME) {
            throw new Exception("You must be playing a game to access that function");
        }
        UserGameCommand newCommand = new UserGameCommand(authToken.getAuthToken());
        newCommand.setGameID(gameId);
        newCommand.setCommandType(UserGameCommand.CommandType.RESIGN);
        wsClient.send(newCommand);
        status = Status.LOGGED_IN;
        return "IN RESIGN";
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
                if (Objects.equals(colorToBe, "white")) {
                    color = ChessGame.TeamColor.WHITE;
                } else {
                    color = ChessGame.TeamColor.BLACK;
                }
            } else {
                ArrayList<ChessGame.TeamColor> validColorArray = new ArrayList<>();
                Random random = new Random();
                if (currentGameList.get(id).getBlackUsername() == null) {
                    validColorArray.add(ChessGame.TeamColor.BLACK);
                }
                if (currentGameList.get(id).getWhiteUsername() == null) {
                    validColorArray.add(ChessGame.TeamColor.WHITE);
                }
                if (validColorArray.isEmpty()) {
                    throw new Exception("No available spots open on this game");
                }
                int index = random.nextInt(validColorArray.size());
                color = validColorArray.get(index);
            }
            JoinGameRequest joinGameRequest = new JoinGameRequest(id,username, color);
            ServerFacade sf = new ServerFacade(serverUrl);
            try {
                sf.joinGame(joinGameRequest, authToken);
                wsClient = new WSClient(username);
                UserGameCommand command = new UserGameCommand(authToken.getAuthToken());
                command.setCommandType(UserGameCommand.CommandType.JOIN_PLAYER);
                command.setGameID(id);
                command.setColor(color);
                wsClient.send(command);
            } catch (Throwable e) {
                throw new Exception("Unexpected error please try again later\n");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("You joined game %s as %s.\n", id, color));
            status = Status.IN_GAME;
            gameId = id;
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
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("You are observing game %s\n", id));
            wsClient = new WSClient(username);
            UserGameCommand command = new UserGameCommand(authToken.getAuthToken());
            command.setCommandType(UserGameCommand.CommandType.JOIN_OBSERVER);
            command.setGameID(id);
            wsClient.send(command);
            status = Status.OBSERVING;
            gameId = id;
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
