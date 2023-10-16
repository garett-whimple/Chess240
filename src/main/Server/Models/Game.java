package Server.Models;

import ChessImpl.ChessGameImpl;
import chess.ChessGame;

/**
 * Object that represents a Game
 */
public class Game {
    private int gameId;
    private String whiteUserName;
    private String blackUserName;
    private String gameName;
    private ChessGame game;

    /**
     * Constructor that creates a Game Object
     * @param gameId
     * @param whiteUserName
     * @param blackUserName
     * @param gameName
     * @param game
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
     * @param gameId
     * @param whiteUserName
     * @param blackUserName
     * @param gameName
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
}
