package Server.DataAccessObjects;

import ChessImpl.ChessPieceImpl;
import chess.ChessPiece;
import com.google.gson.*;

public class PieceAdapter implements JsonDeserializer<ChessPiece> {
    @Override
    public ChessPiece deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Gson().fromJson(jsonElement, ChessPieceImpl.class);
    }
}
