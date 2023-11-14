package ChessImpl;

import chess.*;

import java.util.Collection;
import java.util.Objects;

public class ChessPieceImpl implements ChessPiece {
    private ChessGame.TeamColor color;
    private PieceType type;
    private boolean hasMoved;
    private ChessMove validNextMove = null;

    public ChessPieceImpl(ChessGame.TeamColor color, PieceType type, boolean hasMoved) {
        this.color = color;
        this.type = type;
        this.hasMoved = hasMoved;
    }

    public ChessPieceImpl(ChessGame.TeamColor color, PieceType type) {
        this.color = color;
        this.type = type;
        this.hasMoved = false;
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
    public boolean hasMoved() {
        return !hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessBoardImpl board1 = (ChessBoardImpl) board;
        Collection<ChessMove> returnArray = board1.validMoves(myPosition);
        if (validNextMove != null) {
            returnArray.add(validNextMove);
        }
        return returnArray;
    }
    public void setValidNextMove(ChessMove move) {
        validNextMove = move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPieceImpl that)) return false;
        return hasMoved == that.hasMoved && color == that.color && type == that.type && Objects.equals(validNextMove, that.validNextMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type, hasMoved, validNextMove);
    }
}
