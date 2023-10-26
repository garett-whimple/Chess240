package Server.Services.Responses;

import java.util.Objects;

public class ListGameObject {
    Integer gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;

    public ListGameObject(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListGameObject that)) return false;
        return Objects.equals(getGameID(), that.getGameID()) && Objects.equals(getWhiteUsername(), that.getWhiteUsername()) && Objects.equals(getBlackUsername(), that.getBlackUsername()) && Objects.equals(getGameName(), that.getGameName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameID(), getWhiteUsername(), getBlackUsername(), getGameName());
    }
}
