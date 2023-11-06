package Server.Models;

import ChessImpl.ChessGameImpl;
import chess.ChessGame;

import java.util.Objects;

/**
 * Object that represents a Game
 */
public class Game {
    /**
     * Identification number of the Game
     */
    private Integer gameId;
    /**
     * Username of the user playing white
     */
    private String whiteUsername;
    /**
     * Username of the user playing black
     */
    private String blackUsername;
    /**
     * Name that is given to the game
     */
    private String gameName;
    /**
     * Variable that holds the game object (has information like board and the current turn)
     */
    private ChessGame gameBoard;

    /**
     * Constructor that creates a Game Object
     * @param gameId Identification number of the Game
     * @param whiteUsername Username of the user playing white
     * @param blackUsername Name that is given to the game
     * @param gameName Name that is given to the game
     * @param gameBoard Variable that holds the game object (has information like board and the current turn)
     */
    public Game(Integer gameId, String whiteUsername, String blackUsername, String gameName, ChessGame gameBoard) {
        this.gameId = gameId;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.gameBoard = gameBoard;
    }

    /**
     * Constructor that creates a Game Object
     * @param gameId Identification number of the Game
     * @param whiteUsername Username of the user playing white
     * @param blackUsername Name that is given to the game
     * @param gameName Name that is given to the game
     */
    public Game(int gameId, String whiteUsername, String blackUsername, String gameName) {
        this.gameId = gameId;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.gameBoard = new ChessGameImpl();
    }

    /**
     * Returns gameId
     * @return gameId
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * Returns whiteUsername
     * @return whiteUsername
     */
    public String getWhiteUsername() {
        return whiteUsername;
    }

    /**
     * Returns blackUsername
     * @return blackUsername
     */
    public String getBlackUsername() {
        return blackUsername;
    }

    /**
     * Returns gameName
     * @return gameName
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns game
     * @return game
     */
    public ChessGameImpl getGame() {
        return (ChessGameImpl) gameBoard;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setWhiteUserName(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUserName(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setGameBoard(ChessGame gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game game1)) return false;
        return Objects.equals(getGameId(), game1.getGameId()) && Objects.equals(getWhiteUsername(), game1.getWhiteUsername()) && Objects.equals(getBlackUsername(), game1.getBlackUsername()) && Objects.equals(getGameName(), game1.getGameName()) && Objects.equals(getGame(), game1.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getWhiteUsername(), getBlackUsername(), getGameName(), getGame());
    }
}
