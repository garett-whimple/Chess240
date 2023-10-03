import chess.*;

import java.util.Collection;

public class ChessPieceImpl implements ChessPiece {
    private ChessGame.TeamColor color;
    private PieceType type;

    public ChessPieceImpl(ChessGame.TeamColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
