import chess.*;

import java.util.Collection;

public class ChessPieceImpl implements ChessPiece {
    private ChessGame.TeamColor color;
    private PieceType type;
    private boolean hasMoved;

    public ChessPieceImpl(ChessGame.TeamColor color, PieceType type, boolean hasMoved) {
        this.color = color;
        this.type = type;
        this.hasMoved = hasMoved;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
