package Server;

import ChessImpl.ChessBoardImpl;
import ChessImpl.ChessGameImpl;
import ChessImpl.ChessMoveImpl;
import ChessImpl.ChessPositionImpl;
import Models.*;
import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.GameDAO;
import Server.DataAccessObjects.UserDAO;
import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WSHandler {
    static Database db = new Database();
    private static AuthDAO authDAO = new AuthDAO(db);
    private static UserDAO userDAO = new UserDAO(db);
    private static GameDAO gameDAO = new GameDAO(db);
    private static Map<Integer, GameConnection> connectionMap = new HashMap<>();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPosition.class, new PositionAdapter());
        var command = builder.create().fromJson(message, UserGameCommand.class);

        AuthToken authToken = checkAuth(command.getAuthString());
        if ( authToken == null) {
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            serverMessage.setErrorMessage("You are not authorized to do this action");
            sendMessage(session,serverMessage);
            return;
        }
        runCommand(authToken, command, session);
        System.out.printf("Received: %s", message);
    }
    @OnWebSocketClose
    public void onClose(Session session,int integer, String message) {
        System.out.println("IN CLOSE FUNCTION");
        connectionMap.forEach((key, value) -> value.removeSession(session));
    }

    private static void runCommand(AuthToken authToken, UserGameCommand command, Session session){
        try {
            switch (command.getCommandType()) {
                case LEAVE -> leave(authToken, command, session);
                case MAKE_MOVE -> makeMove(authToken, command, session);
                case RESIGN -> resign(authToken, command, session);
                case JOIN_PLAYER -> joinPlayer(authToken, command, session);
                case JOIN_OBSERVER -> joinObserver(authToken, command, session);
                case REDRAW -> redraw(authToken, command, session);
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    private static void leave(AuthToken authToken, UserGameCommand command, Session session){
        try {
            Game game = gameDAO.find(command.getGameID());
            GameConnection gameConnection = connectionMap.get(command.getGameID());

            Connection.ConnectionType connectionType = gameConnection.findConnectionType(session);
            ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            if (connectionType == Connection.ConnectionType.WHITE) {
                game.setWhiteUserName(null);
                gameDAO.update(game);
                newMessage.setMessage("White Player has left the game");
                gameConnection.setPlayerSession(null, ChessGame.TeamColor.WHITE);
                sendGroupMessage(gameConnection,session,newMessage);
            } else if (connectionType == Connection.ConnectionType.BLACK) {
                game.setBlackUserName(null);
                gameDAO.update(game);
                newMessage.setMessage("Black Player has left the game");
                gameConnection.setPlayerSession(null, ChessGame.TeamColor.BLACK);
                sendGroupMessage(gameConnection,session, newMessage);
            } else {
                gameConnection.removeSession(session);
                newMessage.setMessage("Observer has left the game");
                sendGroupMessage(gameConnection,session, newMessage);
            }
        } catch (Throwable e) {
            return;
        }
    }

    private static void makeMove(AuthToken authToken, UserGameCommand command, Session session) throws Exception{
            Game game = gameDAO.find(command.getGameID());
            GameConnection gameConnection = connectionMap.get(command.getGameID());
            if(Objects.equals(game.getBlackUsername(), "RESIGNED") || Objects.equals(game.getWhiteUsername(), "RESIGNED")){
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errorMessage.setErrorMessage("The game is over no moves can be made");
                sendMessage(session, errorMessage);
                return;
            }

            Connection.ConnectionType connectionType = gameConnection.findConnectionType(session);
            ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            ChessGame.TeamColor color = null;
            String colorString = null;

            if (game.getGame().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                colorString = "White";
                if (!Objects.equals(game.getWhiteUsername(), authToken.getUsername())) {
                    ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errorMessage.setErrorMessage("You do not have the right authorization to make a move");
                    sendMessage(session, errorMessage);
                    return;
                }
            } else {
                colorString = "Black";
                if (!Objects.equals(game.getBlackUsername(), authToken.getUsername())) {
                    ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errorMessage.setErrorMessage("You do not have the right authorization to make a move");
                    sendMessage(session, errorMessage);
                }
            }
            ChessMove move = command.getMove();
            try {
                ChessGameImpl chessGame = game.getGame();
                chessGame.makeMove(move);
                if (chessGame.isInCheckmate(chessGame.getTeamTurn())) {
                    ServerMessage checkMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    checkMessage.setMessage(String.format("%s is in Checkmate", chessGame.getTeamTurn()));
                    sendMessage(session, checkMessage);
                } else if (chessGame.isInCheck(chessGame.getTeamTurn())) {
                    ServerMessage checkMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    checkMessage.setMessage(String.format("%s is in Check", chessGame.getTeamTurn()));
                    sendMessage(session, checkMessage);
                }

            } catch (InvalidMoveException e) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errorMessage.setErrorMessage("Invalid Move");
                sendMessage(session,errorMessage);
                return;
            }

            gameDAO.update(game);
            String message = String.format("%s: %s Player has made a move",authToken.getUsername(), colorString);
            newMessage.setMessage(message);
            sendGroupMessage(gameConnection,session,newMessage);

            ServerMessage reloadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            reloadMessage.setGame(game);
            sendGroupMessage(gameConnection, session, reloadMessage);
            sendMessage(session,reloadMessage);
    }

    private static void resign(AuthToken authToken, UserGameCommand command, Session session) throws Exception{
        Game game = gameDAO.find(command.getGameID());
        GameConnection gameConnection = connectionMap.get(command.getGameID());
        if(Objects.equals(game.getBlackUsername(), "RESIGNED") || Objects.equals(game.getWhiteUsername(), "RESIGNED")){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("The game is over you can not resign");
            sendMessage(session, errorMessage);
            return;
        }

        Connection.ConnectionType connectionType = gameConnection.findConnectionType(session);
        ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        if (connectionType == Connection.ConnectionType.WHITE) {
            game.setGameBoard(null);
            game.setWhiteUserName("RESIGNED");
            gameDAO.update(game);
            newMessage.setMessage(String.format("%s: White Player has resigned and left the game",authToken.getUsername()));
            gameConnection.setPlayerSession(null, ChessGame.TeamColor.WHITE);
            sendMessage(session, newMessage);
            sendGroupMessage(gameConnection,session,newMessage);
        } else if (connectionType == Connection.ConnectionType.BLACK) {
            game.setGameBoard(null);
            game.setBlackUserName("RESIGNED");
            gameDAO.update(game);
            newMessage.setMessage(String.format("%s: Black Player has resigned and left the game",authToken.getUsername()));
            gameConnection.setPlayerSession(null, ChessGame.TeamColor.BLACK);
            sendMessage(session, newMessage);
            sendGroupMessage(gameConnection,session, newMessage);
        } else {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Invalid Resign");
            sendMessage(session,errorMessage);
            return;
        }
    }

    private static void joinPlayer(AuthToken authToken, UserGameCommand command, Session session) throws Exception{
        System.out.println("IN JOIN PLAYER\n");

        GameConnection gameConnection = connectionMap.get(command.getGameID());
        if (gameConnection == null) {
            gameConnection = new GameConnection();
        }
        Game game = gameDAO.find(command.getGameID());
        if (game == null) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Cannot join that game because it does not exist");
            sendMessage(session, errorMessage);
            return;
        }
        String username = authToken.getUsername();
        String whiteUsername = game.getWhiteUsername();
        String blackUsername = game.getBlackUsername();
        if ((command.getColor() == ChessGame.TeamColor.WHITE && !Objects.equals(whiteUsername, username))||(command.getColor() == ChessGame.TeamColor.BLACK && !Objects.equals(blackUsername, username))) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Cannot join as that color because it is already taken");
            sendMessage(session, errorMessage);
            return;
        }
        gameConnection.setPlayerSession(session, command.getColor());
        connectionMap.put(command.getGameID(), gameConnection);
        ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        ChessGame.TeamColor color = command.getColor();
        newMessage.setMessage(String.format("%s joined as %s", username, color));

        ServerMessage loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGame.setGame(game);
        sendGroupMessage(gameConnection, session, newMessage);
        sendMessage(session,loadGame);
    }

    private static void joinObserver(AuthToken authToken, UserGameCommand command, Session session) throws Exception{
        System.out.println("IN JOIN OBSERVER\n");
        GameConnection gameConnection = connectionMap.get(command.getGameID());
        if (gameConnection == null) {
            gameConnection = new GameConnection();
        }
        Game game = gameDAO.find(command.getGameID());
        if (game == null) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Cannot join that game because it does not exist");
            sendMessage(session, errorMessage);
            return;
        }
        gameConnection.addObserverSession(session);
        connectionMap.put(command.getGameID(), gameConnection);

        ServerMessage loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGame.setGame(game);
        sendMessage(session,loadGame);

        ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        ChessGame.TeamColor color = command.getColor();
        newMessage.setMessage(String.format("%s joined as Observer", authToken.getUsername()));
        sendGroupMessage(gameConnection,session, newMessage);
    }


    private static void redraw(AuthToken authToken, UserGameCommand command, Session session) {
        System.out.println("IN REDRAW\n");
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        try {
            Game game = gameDAO.find(command.getGameID());
            serverMessage.setGame(game);
            serverMessage.setHighlightPosition(command.getHighlightPosition());
            sendMessage(session, serverMessage);
        } catch (Throwable e) {
            return;
        }
    }

    private static void sendGroupMessage(GameConnection gameConnection, Session exceptSession, ServerMessage message) throws Exception {
        ArrayList<Session> sessionArrayList = new ArrayList<>();
        sessionArrayList.addAll(gameConnection.getObserverSessions());
        sessionArrayList.add(gameConnection.getBlackSession());
        sessionArrayList.add(gameConnection.getWhiteSession());
        for (Session session: sessionArrayList ) {
            if (session != null && session != exceptSession) {
                Gson gson = new Gson();
                System.out.println(String.format("Printing %s type Message to session : %s\n", message.getServerMessageType(), session));
                session.getRemote().sendString(gson.toJson(message));
            }
        }
    }

    private static void sendMessage(Session session, ServerMessage message) throws Exception {
        Gson gson = new Gson();
        String returnString = gson.toJson(message);
        session.getRemote().sendString(returnString);
    }



    private static AuthToken checkAuth(String auth) throws DataAccessException {
        AuthToken authToken = authDAO.find(auth);
        if (authToken == null) {
            return null;
        } else {
            return authToken;
        }
    }
}
