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
    private int gameId;
    /**
     * Username of the user playing white
     */
    private String whiteUserName;
    /**
     * Username of the user playing black
     */
    private String blackUserName;
    /**
     * Name that is given to the game
     */
    private String gameName;
    /**
     * Variable that holds the game object (has information like board and the current turn)
     */
    private ChessGame game;

    /**
     * Constructor that creates a Game Object
     * @param gameId Identification number of the Game
     * @param whiteUserName Username of the user playing white
     * @param blackUserName Name that is given to the game
     * @param gameName Name that is given to the game
     * @param game Variable that holds the game object (has information like board and the current turn)
     */
    public Game(int gameId, String whiteUserName, String blackUserName, String gameName, ChessGame game) {
        this.gameId = gameId;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
        this.gameName = gameName;
        this.game = game;
    }

    /**
     * Constructor that creates a Game Object
     * @param gameId Identification number of the Game
     * @param whiteUserName Username of the user playing white
     * @param blackUserName Name that is given to the game
     * @param gameName Name that is given to the game
     */
    public Game(int gameId, String whiteUserName, String blackUserName, String gameName) {
        this.gameId = gameId;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
        this.gameName = gameName;
        this.game = new ChessGameImpl();
    }

    /**
     * Returns gameId
     * @return gameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Returns whiteUserName
     * @return whiteUserName
     */
    public String getWhiteUserName() {
        return whiteUserName;
    }

    /**
     * Returns blackUserName
     * @return blackUserName
     */
    public String getBlackUserName() {
        return blackUserName;
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
    public ChessGame getGame() {
        return game;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setWhiteUserName(String whiteUserName) {
        this.whiteUserName = whiteUserName;
    }

    public void setBlackUserName(String blackUserName) {
        this.blackUserName = blackUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game game1)) return false;
        return getGameId() == game1.getGameId() && Objects.equals(getWhiteUserName(), game1.getWhiteUserName()) && Objects.equals(getBlackUserName(), game1.getBlackUserName()) && Objects.equals(getGameName(), game1.getGameName()) && Objects.equals(getGame(), game1.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getWhiteUserName(), getBlackUserName(), getGameName(), getGame());
    }
}
