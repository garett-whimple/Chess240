package ui;

import ChessImpl.ChessGameImpl;
import ChessImpl.ChessMoveImpl;
import Models.Game;
import Models.PieceAdapter;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;
import java.util.Objects;
import java.util.Scanner;

public class WSClient extends Endpoint {

    public static void main(String[] args) throws Exception {
        var ws = new WSClient("TEST");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo");
        while (true) {
            String string = scanner.nextLine();
            UserGameCommand newGameCommand = new UserGameCommand(string);
            newGameCommand.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            ws.send(newGameCommand);
        }
    }
    static String username;
    public Session session;

    public WSClient(String username) throws Exception {
        this.username = username;
        URI uri = new URI("ws://localhost:4567/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                runMessage(message);
            }
        });
    }
    public void runMessage(String message) {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());
        var command = builder.create().fromJson(message, ServerMessage.class);
        switch (command.getServerMessageType()) {
            case ERROR -> error(command);
            case NOTIFICATION -> notification(command);
            case LOAD_GAME -> loadGame(command);
        }
    }

    public void error(ServerMessage message) {
        System.out.print(message.getErrorMessage());
    }

    public void notification(ServerMessage message) {
        System.out.print(message.getMessage());
    }

    public void loadGame(ServerMessage message) {
        Game game = message.getGame();
        String blackUsername = game.getBlackUsername();
        String whiteUsername = game.getWhiteUsername();
        ChessGame.TeamColor color = game.getGame().getTeamTurn();
        if (Objects.equals(username, blackUsername)) {
            color = ChessGame.TeamColor.BLACK;
        } else if (Objects.equals(username, whiteUsername)) {
            color = ChessGame.TeamColor.WHITE;
        }
        UI ui = new UI();
        System.out.print("\n");
        System.out.print(ui.printBoard(game.getGame().getBoard(), color, message.getHighlightPosition()));
    }

    public void send(UserGameCommand msg) throws Exception {
        Gson gson = new Gson();
        this.session.getBasicRemote().sendText(gson.toJson(msg));
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
