package ChessImpl;

import chess.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class ChessGameImpl implements ChessGame {
    ChessBoard board = new ChessBoardImpl();
    TeamColor teamTurn = TeamColor.WHITE;
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    public Collection<ChessMove> allValidCheckedMoves(TeamColor currentTeamColor) {
        ChessBoard copyBoard = new ChessBoardImpl();
        board.setEqual(copyBoard);


        Collection<ChessMove> allCurrentMoves;
        Collection<ChessMove> validMoves = new ArrayList<>();
        allCurrentMoves = board.allValidMoves(currentTeamColor);

        for (ChessMove currentMove: allCurrentMoves) {
            board.setEqual(copyBoard);
            ChessPiece currentPiece = copyBoard.getPiece(currentMove.getStartPosition());
            copyBoard.addPiece(currentMove.getEndPosition(),currentPiece);
            copyBoard.removePiece(currentMove.getStartPosition());
            if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && (currentMove.getStartPosition().getColumn() - currentMove.getEndPosition().getColumn() > 1)) {
                ChessPosition kingCopy1 = new ChessPositionImpl(currentMove.getStartPosition().getRow(),currentMove.getStartPosition().getColumn()-1);
                copyBoard.addPiece(kingCopy1,currentPiece);
                copyBoard.addPiece(currentMove.getStartPosition(),currentPiece);
            } else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && (currentMove.getStartPosition().getColumn() - currentMove.getEndPosition().getColumn() < -1)) {
                ChessPosition kingCopy1 = new ChessPositionImpl(currentMove.getStartPosition().getRow(),currentMove.getStartPosition().getColumn()+1);
                copyBoard.addPiece(kingCopy1,currentPiece);
                copyBoard.addPiece(currentMove.getStartPosition(),currentPiece);
            }
            if (!copyBoard.isInCheck(currentTeamColor)) {
                validMoves.add(currentMove);
            }
        }
        return validMoves;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        Collection<ChessMove> allValidMoveArray = allValidCheckedMoves(board.getPiece(startPosition).getTeamColor());
        for (ChessMove currentMove : allValidMoveArray) {
            if (currentMove.getStartPosition().getRow() == startPosition.getRow() && currentMove.getStartPosition().getColumn() == startPosition.getColumn()) {
                returnList.add(currentMove);
            }
        }
        return returnList;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (allValidCheckedMoves(getTeamTurn()).contains(move)) {
            ChessPiece currentPiece =  board.getPiece(move.getStartPosition());
            checkCastle(move, currentPiece);
            if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && move.getStartPosition().getColumn() != move.getEndPosition().getColumn() && board.getPiece(move.getEndPosition()) == null) {
                if (board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK) {
                    board.removePiece(new ChessPositionImpl(move.getEndPosition().getRow()+1,move.getEndPosition().getColumn()));
                } else {
                    board.removePiece(new ChessPositionImpl(move.getEndPosition().getRow()-1,move.getEndPosition().getColumn()));
                }
            }
            clearEnPassant();
            if (move.getPromotionPiece() != null) {
                currentPiece = new ChessPieceImpl(board.getPiece(move.getStartPosition()).getTeamColor(),move.getPromotionPiece());
            } else {
                currentPiece = board.getPiece(move.getStartPosition());
            }
            if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                checkEnPassant(currentPiece, move);
            }
            board.addPiece(move.getEndPosition(),currentPiece);
            board.removePiece(move.getStartPosition());
            currentPiece.setMoved(true);
            if (teamTurn == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            } else {
                setTeamTurn(TeamColor.BLACK);
            }
        } else {
            throw new InvalidMoveException();
        }
    }

    private void checkCastle(ChessMove currentMove, ChessPiece currentPiece) {
        ChessPiece rook = new ChessPieceImpl(currentPiece.getTeamColor(), ChessPiece.PieceType.ROOK);
        if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && (currentMove.getStartPosition().getColumn() - currentMove.getEndPosition().getColumn() > 1)) {
            ChessPosition oldRookPostion = new ChessPositionImpl(currentMove.getStartPosition().getRow(),1);
            ChessPosition newRookPostiion = new ChessPositionImpl(currentMove.getStartPosition().getRow(),currentMove.getStartPosition().getColumn()-1);
            board.addPiece(newRookPostiion,board.getPiece(oldRookPostion));
            board.removePiece(oldRookPostion);
        } else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && (currentMove.getStartPosition().getColumn() - currentMove.getEndPosition().getColumn() < -1)) {
            ChessPosition oldRookPostion = new ChessPositionImpl(currentMove.getStartPosition().getRow(),8);
            ChessPosition newRookPostiion = new ChessPositionImpl(currentMove.getStartPosition().getRow(),currentMove.getStartPosition().getColumn()+1);
            board.addPiece(newRookPostiion,board.getPiece(oldRookPostion));
            board.removePiece(oldRookPostion);
        }
    }

    private void clearEnPassant() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece currentPiece = board.getPiece(new ChessPositionImpl(i,j));
                if (currentPiece != null) {
                    currentPiece.setValidNextMove(null);
                }
            }
        }
    }

    private void checkEnPassant(ChessPiece currentPiece, ChessMove move) {
        ChessPosition positionToCheck = null;
        ChessPiece pieceToCheck = null;
        if (currentPiece.getTeamColor() == TeamColor.WHITE && move.getStartPosition().getRow() == 2 && move.getEndPosition().getRow() == 4){
            positionToCheck = new ChessPositionImpl(4, move.getEndPosition().getColumn()-1);
            if (move.getEndPosition().getColumn() > 1 && board.getPiece(positionToCheck) != null) {
                pieceToCheck = board.getPiece(positionToCheck);
                if (pieceToCheck.getTeamColor() == TeamColor.BLACK && pieceToCheck.getPieceType() == ChessPiece.PieceType.PAWN) {
                    pieceToCheck.setValidNextMove(new ChessMoveImpl(positionToCheck, new ChessPositionImpl(move.getEndPosition().getRow()-1,move.getEndPosition().getColumn())));
                }
            }
            positionToCheck = new ChessPositionImpl(4, move.getEndPosition().getColumn()+1);
            if (move.getEndPosition().getColumn() < 8 && board.getPiece(positionToCheck) != null) {
                pieceToCheck = board.getPiece(positionToCheck);
                if (pieceToCheck.getTeamColor() == TeamColor.BLACK && pieceToCheck.getPieceType() == ChessPiece.PieceType.PAWN) {
                    pieceToCheck.setValidNextMove(new ChessMoveImpl(positionToCheck, new ChessPositionImpl(move.getEndPosition().getRow()-1,move.getEndPosition().getColumn())));
                }
            }
        }
        if (currentPiece.getTeamColor() == TeamColor.BLACK && move.getStartPosition().getRow() == 7 && move.getEndPosition().getRow() == 5){
            positionToCheck = new ChessPositionImpl(5, move.getEndPosition().getColumn()-1);
            if (move.getEndPosition().getColumn() > 1 && board.getPiece(positionToCheck) != null) {
                pieceToCheck = board.getPiece(positionToCheck);
                if (pieceToCheck.getTeamColor() == TeamColor.WHITE && pieceToCheck.getPieceType() == ChessPiece.PieceType.PAWN) {
                    pieceToCheck.setValidNextMove(new ChessMoveImpl(positionToCheck, new ChessPositionImpl(move.getEndPosition().getRow()+1,move.getEndPosition().getColumn())));
                }
            }
            positionToCheck = new ChessPositionImpl(5, move.getEndPosition().getColumn()+1);
            if (move.getEndPosition().getColumn() < 8 && board.getPiece(positionToCheck) != null) {
                pieceToCheck = board.getPiece(positionToCheck);
                if (pieceToCheck.getTeamColor() == TeamColor.WHITE && pieceToCheck.getPieceType() == ChessPiece.PieceType.PAWN) {
                    pieceToCheck.setValidNextMove(new ChessMoveImpl(positionToCheck, new ChessPositionImpl(move.getEndPosition().getRow()+1,move.getEndPosition().getColumn())));
                }
            }
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return board.isInCheck(teamColor);
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            if(allValidCheckedMoves(teamColor).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> currentMoves = new ArrayList<>();
        currentMoves.addAll(allValidCheckedMoves(teamColor));
        return currentMoves.isEmpty();
    }

    @Override
    public void setBoard(ChessBoard board) {
        board.setEqual(this.board);
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
