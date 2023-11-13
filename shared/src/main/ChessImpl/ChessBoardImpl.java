package ChessImpl;

import chess.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ChessBoardImpl implements ChessBoard {
    public ChessBoardImpl() {}
    public ChessPiece[][] board = new ChessPiece[8][8];
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow()-1][position.getColumn()-1] = null;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
       return board[position.getRow()-1][position.getColumn()-1];
    }

    public boolean isInCheck(ChessGame.TeamColor teamColor) {
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        if (teamColor == ChessGame.TeamColor.BLACK) {
            enemyMoves.addAll(allValidMoves(ChessGame.TeamColor.WHITE));
        } else {
            enemyMoves.addAll(allValidMoves(ChessGame.TeamColor.BLACK));
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPositionImpl(i,j);
                ChessPiece currentChessPiece = getPiece(currentPosition);
                if (currentChessPiece == null) {
                    continue;
                }
                if (currentChessPiece.getTeamColor() == teamColor && currentChessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    for (ChessMove currentMove : enemyMoves) {
                        if(currentMove.getEndPosition().getColumn() == currentPosition.getColumn() && currentMove.getEndPosition().getRow() == currentPosition.getRow()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
                currentColor= ChessGame.TeamColor.WHITE;
            }
            if (i > 5) {
                //white pieces
                currentColor= ChessGame.TeamColor.BLACK;
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

    public Collection<ChessMove> allValidMoves(ChessGame.TeamColor currentTeamColor) {
        Collection<ChessMove> returnList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition currentPosition = new ChessPositionImpl(i,j, true);
                ChessPiece currentPiece = getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == currentTeamColor) {
                    returnList.addAll(currentPiece.pieceMoves(this,currentPosition));
                }
            }
        }
        return returnList;
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        ChessPiece currentPiece =  getPiece(startPosition);
        if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            returnList = validPawnMoves(startPosition);
        } else if (currentPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            returnList = validRookMoves(startPosition);
        } else if (currentPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            returnList = validKnightMoves(startPosition);
        } else if (currentPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            returnList = validBishopMoves(startPosition);
        } else if (currentPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            returnList = validQueenMoves(startPosition);
        } else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
            returnList = validKingMoves(startPosition);
        }
        return returnList;
    }

    private Collection<ChessMove> validPawnMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int currentRow = startPosition.getRow();
        int currentColumn = startPosition.getColumn();
        ChessPosition currentEndPosition = null;
        ChessPiece currentPiece =  getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int multiplyValue = 1;
        if (currentColor == ChessGame.TeamColor.BLACK) {
            multiplyValue = -1;
        }
        currentEndPosition = new ChessPositionImpl(currentRow+(multiplyValue), currentColumn);
        if (getPiece(currentEndPosition) == null) {
            if ((currentColor == ChessGame.TeamColor.BLACK && currentRow == 2)||(currentColor == ChessGame.TeamColor.WHITE && currentRow == 7)) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.ROOK));
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.QUEEN));
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.BISHOP));
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.KNIGHT));
            } else {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            }
            if((currentColor == ChessGame.TeamColor.BLACK && currentRow == 7)||(currentColor == ChessGame.TeamColor.WHITE && currentRow == 2)) {
                currentEndPosition = new ChessPositionImpl(currentRow+(2*multiplyValue), currentColumn);
                if (getPiece(currentEndPosition) == null) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
            }
        }
        if (currentColumn > 1) {
            currentEndPosition = new ChessPositionImpl(currentRow+multiplyValue, currentColumn-1);
            if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    if ((currentColor == ChessGame.TeamColor.BLACK && currentRow == 2)||(currentColor == ChessGame.TeamColor.WHITE && currentRow == 7)) {
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.ROOK));
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.QUEEN));
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.BISHOP));
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                    }
                }
            }
        }
        if (currentColumn < 8) {
            currentEndPosition = new ChessPositionImpl(currentRow+multiplyValue, currentColumn+1);
            if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    if ((currentColor == ChessGame.TeamColor.BLACK && currentRow == 2)||(currentColor == ChessGame.TeamColor.WHITE && currentRow == 7)) {
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.ROOK));
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.QUEEN));
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.BISHOP));
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                    }
                }
            }
        }
        return returnList;
    }

    private Collection<ChessMove> validRookMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int currentRow = startPosition.getRow();
        int currentColumn = startPosition.getColumn();
        ChessPosition currentEndPosition = null;
        ChessPiece currentPiece =  getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        //UP
        for (int i = 1; i < 9 - currentRow; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow+i, currentColumn);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
        }
        //DOWN
        for (int i = 1; i < currentRow; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow-i, currentColumn);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
        }
        //RIGHT
        for (int i = 1; i < 9 - currentColumn; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow, currentColumn+i);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
        }
        //LEFT
        for (int i = 1; i < currentColumn; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow, currentColumn-i);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
        }
        return returnList;
    }

    private Collection<ChessMove> validBishopMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int currentRow = startPosition.getRow();
        int currentColumn = startPosition.getColumn();
        ChessPosition currentEndPosition = null;
        ChessPiece currentPiece =  getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int nextRow = currentRow + 1;
        int nextColumn = currentColumn + 1;
        while (nextRow < 9 && nextColumn < 9) {
            currentEndPosition = new ChessPositionImpl(nextRow, nextColumn);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
            nextColumn ++;
            nextRow ++;
        }
        nextColumn = currentColumn -1;
        nextRow = currentRow -1;
        while (nextRow > 0 && nextColumn > 0) {
            currentEndPosition = new ChessPositionImpl(nextRow, nextColumn);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
            nextColumn --;
            nextRow --;
        }
        nextColumn = currentColumn + 1;
        nextRow = currentRow - 1;
        while (nextRow > 0 && nextColumn < 9) {
            currentEndPosition = new ChessPositionImpl(nextRow, nextColumn);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
            nextColumn ++;
            nextRow --;
        }
        nextColumn = currentColumn - 1;
        nextRow = currentRow + 1;
        while (nextRow < 9 && nextColumn > 0) {
            currentEndPosition = new ChessPositionImpl(nextRow, nextColumn);
            if (getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (getPiece(currentEndPosition) != null) {
                if (getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
            nextColumn --;
            nextRow ++;
        }
        return returnList;
    }

    private Collection<ChessMove> validQueenMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        Collection<ChessMove> currentList = new ArrayList<>();
        currentList = validBishopMoves(startPosition);
        returnList.addAll(currentList);
        currentList = validRookMoves(startPosition);
        returnList.addAll(currentList);
        return returnList;
    }

    private Collection<ChessMove> validKnightMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int currentRow = startPosition.getRow();
        int currentColumn = startPosition.getColumn();
        ChessPosition currentEndPosition = null;
        ChessPiece currentPiece = getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();

        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                if ((i == j) || ((i*-1) == j) || (i == 0) || (j == 0)) {
                    continue;
                }
                currentEndPosition = new ChessPositionImpl(currentRow + i, currentColumn + j);
                if (checkOpenMove(currentEndPosition, currentColor)) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
            }
        }
        return returnList;
    }

    private Collection<ChessMove> validKingMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int currentRow = startPosition.getRow();
        int currentColumn = startPosition.getColumn();
        ChessPosition currentEndPosition = null;
        ChessPiece currentPiece =  getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i ==0) && (j==0)) {
                    continue;
                }
                currentEndPosition = new ChessPositionImpl(currentRow + i, currentColumn + j);
                if (checkOpenMove(currentEndPosition, currentColor)) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
            }
        }

        if (((currentPiece.hasMoved()) && startPosition.getColumn() == 5 && startPosition.getRow() == 8 && currentColor == ChessGame.TeamColor.BLACK) ||((currentPiece.hasMoved()) && startPosition.getColumn() == 5 && startPosition.getRow() == 1 && currentColor == ChessGame.TeamColor.WHITE)) {
            if (getPiece(new ChessPositionImpl(startPosition.getRow(), 8))!= null && getPiece(new ChessPositionImpl(startPosition.getRow(), 8)).getPieceType() == ChessPiece.PieceType.ROOK && getPiece(new ChessPositionImpl(startPosition.getRow(), 8)).getTeamColor() == currentColor && (getPiece(new ChessPositionImpl(startPosition.getRow(), 8)).hasMoved())) {
                if (getPiece(new ChessPositionImpl(startPosition.getRow(), 7)) == null && getPiece(new ChessPositionImpl(startPosition.getRow(), 6)) == null) {
                    returnList.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(startPosition.getRow(),7)));
                }
            }
            if (getPiece(new ChessPositionImpl(startPosition.getRow(), 1)) != null && getPiece(new ChessPositionImpl(startPosition.getRow(), 1)).getPieceType() == ChessPiece.PieceType.ROOK && getPiece(new ChessPositionImpl(startPosition.getRow(), 1)).getTeamColor() == currentColor && (getPiece(new ChessPositionImpl(startPosition.getRow(), 1)).hasMoved())){
                if (getPiece(new ChessPositionImpl(startPosition.getRow(), 2)) == null && getPiece(new ChessPositionImpl(startPosition.getRow(), 3)) == null && getPiece(new ChessPositionImpl(startPosition.getRow(), 4)) == null) {
                    returnList.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(startPosition.getRow(),3)));
                }
            }
        }
        return returnList;
    }

    private boolean checkOpenMove(ChessPosition endingPosition, ChessGame.TeamColor color) {
        if (endingPosition.getColumn() > 8 || endingPosition.getColumn() < 1 || endingPosition.getRow() > 8 || endingPosition.getRow() < 1) {
            return false;
        }
        if (getPiece(endingPosition) == null) {
            return true;
        } else if (getPiece(endingPosition) != null) {
            if (getPiece(endingPosition).getTeamColor() != color) {
                return true;
            }
        }
        return false;
    }

    public void setEqual(ChessBoard copyBoard) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition currentPosition = new ChessPositionImpl(i, j, true);
                ChessPiece currentPiece = getPiece(currentPosition);
                copyBoard.addPiece(currentPosition, currentPiece);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoardImpl that)) return false;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}
