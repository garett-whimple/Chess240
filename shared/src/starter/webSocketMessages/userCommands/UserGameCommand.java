package webSocketMessages.userCommands;

import ChessImpl.ChessMoveImpl;
import ChessImpl.ChessPositionImpl;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }


    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        REDRAW
    }

    protected CommandType commandType;

    private final String authToken;

    private ChessMoveImpl move;

    private Integer gameID;
    private ChessGame.TeamColor playerColor;

    private ChessPositionImpl highlightPosition;

    public ChessPositionImpl getHighlightPosition() {
        return highlightPosition;
    }

    public void setHighlightPosition(ChessPositionImpl highlightPosition) {
        this.highlightPosition = highlightPosition;
    }

    public ChessGame.TeamColor getColor() {
        return playerColor;
    }

    public void setColor(ChessGame.TeamColor color) {
        this.playerColor = color;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public void setMove(ChessMoveImpl move) {
        this.move = move;
    }

    public ChessMoveImpl getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
