package Models;

import ChessImpl.ChessPieceImpl;
import ChessImpl.ChessPositionImpl;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;

public class PositionAdapter implements JsonDeserializer<ChessPosition> {
    @Override
    public ChessPosition deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Gson().fromJson(jsonElement, ChessPositionImpl.class);
    }
}
