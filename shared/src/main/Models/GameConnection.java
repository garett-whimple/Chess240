package Models;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.Objects;

public class GameConnection {
    Session whiteSession = null;
    Session blackSession = null;
    ArrayList<Session> observerSessions = new ArrayList<>();

    public enum ConnectionType {
        WHITE,
        BLACK,
        OBSERVER
    }
    public Connection.ConnectionType findConnectionType(Session session) {
        Connection.ConnectionType returnValue = null;
        if (whiteSession == session) {
            returnValue = Connection.ConnectionType.WHITE;
        } else if (blackSession == session) {
            returnValue = Connection.ConnectionType.BLACK;
        } else if (observerSessions.contains(session)) {
            returnValue = Connection.ConnectionType.OBSERVER;
        }
        return returnValue;
    }

    public Session getWhiteSession() {
        return whiteSession;
    }

    public void setWhiteSession(Session whiteSession) {
        this.whiteSession = whiteSession;
    }

    public Session getBlackSession() {
        return blackSession;
    }

    public void setBlackSession(Session blackSession) {
        this.blackSession = blackSession;
    }

    public ArrayList<Session> getObserverSessions() {
        return observerSessions;
    }

    public void addObserverSession(Session observerSession) {
        this.observerSessions.add(observerSession);
    }

    public void removeSession(Session session) {
        if (whiteSession == session) {
            whiteSession = null;
        }
        if (blackSession == session) {
            blackSession = null;
        }
        this.observerSessions.remove(session);
    }

    public void setPlayerSession(Session session, ChessGame.TeamColor color) {
        if(color == ChessGame.TeamColor.BLACK) {
            blackSession = session;
        } else if (color == ChessGame.TeamColor.WHITE) {
            whiteSession = session;
        }
    }

    public Session getPlayerSession(ChessGame.TeamColor color) {
        Session returnSession = null;
        if(color == ChessGame.TeamColor.BLACK) {
            returnSession = blackSession;
        } else if (color == ChessGame.TeamColor.WHITE) {
            returnSession = whiteSession;
        }
        return returnSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameConnection that)) return false;
        return Objects.equals(getWhiteSession(), that.getWhiteSession()) && Objects.equals(getBlackSession(), that.getBlackSession()) && Objects.equals(getObserverSessions(), that.getObserverSessions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWhiteSession(), getBlackSession(), getObserverSessions());
    }
}
