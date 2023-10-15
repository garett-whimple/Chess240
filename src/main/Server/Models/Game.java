package Server.Models;

import ChessImpl.ChessGameImpl;
import chess.ChessGame;

public class Game {
    private int gameId;
    private String whiteUserName;
    private String blackUserName;
    private String gameName;
    private ChessGame game;
    public Game(int gameId, String whiteUserName, String blackUserName, String gameName, ChessGame game) {
        this.gameId = gameId;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
        this.gameName = gameName;
        this.game = game;
    }

    public Game(int gameId, String whiteUserName, String blackUserName, String gameName) {
        this.gameId = gameId;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
        this.gameName = gameName;
        this.game = new ChessGameImpl();
    }

    public int getGameId() {
        return gameId;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }
}
