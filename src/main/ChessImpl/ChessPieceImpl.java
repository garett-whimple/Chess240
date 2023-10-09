package ChessImpl;

import chess.*;

import java.util.Collection;

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

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> returnArray = board.validMoves(myPosition);
        if (validNextMove != null) {
            returnArray.add(validNextMove);
        }
        return returnArray;
    }
    public void setValidNextMove(ChessMove move) {
        validNextMove = move;
    }
}
