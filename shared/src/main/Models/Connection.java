package Models;

import java.util.Objects;
import org.eclipse.jetty.websocket.api.Session;

public class Connection {
    Session session;
    ConnectionType type;

    public enum ConnectionType {
        WHITE,
        BLACK,
        OBSERVER
    }

    public Connection(Session session, ConnectionType type) {
        this.session = session;
        this.type = type;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection that)) return false;
        return Objects.equals(getSession(), that.getSession()) && Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSession(), getType());
    }

}
