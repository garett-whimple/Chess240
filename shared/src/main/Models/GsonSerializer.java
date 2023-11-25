package Models;

import ChessImpl.ChessPieceImpl;
import com.google.gson.*;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

public class GsonSerializer {
    public static <T> T deserialize(String json, Class<T> responseClass) {
        return deserialize(new StringReader(json), responseClass);
    }

    public static <T> T deserialize(Reader reader, Class<T> responseClass) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Deserialize FriendList to ArrayFriendList if used.
        gsonBuilder.registerTypeAdapter(ChessPieceImpl.class, new PieceAdapter());
        return gsonBuilder.create().fromJson(reader, responseClass);
    }
}


