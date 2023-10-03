import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class ChessBoardImpl implements ChessBoard {
    public ChessBoardImpl() {}
    ChessPiece[][] board = new ChessPiece[8][8];
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow()][position.getColumn()] = null;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
       return board[position.getRow()][position.getColumn()];
    }

    @Override
    public void resetBoard() {
        ChessGame.TeamColor currentColor = null;
        ChessPiece.PieceType currentType = null;
        ChessPiece currentPiece = null;
        board = new ChessPiece[8][8];
        for (int i = 0; i < board.length; i++) {
            if (i < 2) {
                //black pieces
                currentColor= ChessGame.TeamColor.BLACK;
            }
            if (i > 5) {
                //white pieces
                currentColor= ChessGame.TeamColor.WHITE;
            }
            if (i == 1 || i == 6) {
                for (int j = 0; j < board[i].length; j++) {
                    currentType = ChessPiece.PieceType.PAWN;
                    currentPiece = new ChessPieceImpl(currentColor, currentType, false);
                    board[i][j] = currentPiece;
                }
            }
            if (i == 0 || i == 7) {
                for (int j = 0; j < board[i].length; j++) {
                    switch (j) {
                        case 0, 7:
                            currentType = ChessPiece.PieceType.ROOK;
                            break;
                        case 1, 6:
                            currentType = ChessPiece.PieceType.KNIGHT;
                            break;
                        case 2, 5:
                            currentType = ChessPiece.PieceType.BISHOP;
                            break;
                        case 3:
                            currentType = ChessPiece.PieceType.QUEEN;
                            break;
                        case 4:
                            currentType = ChessPiece.PieceType.KING;
                            break;
                    }
                    currentPiece = new ChessPieceImpl(currentColor, currentType, false);
                    board[i][j] = currentPiece;
                }
            }
        }
    }
}
