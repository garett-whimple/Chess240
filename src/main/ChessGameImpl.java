import chess.*;

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
        allCurrentMoves = allValidMoves(currentTeamColor);

        for (ChessMove currentMove: allCurrentMoves) {
            copyBoard.setEqual(board);
            ChessPiece currentPiece = board.getPiece(currentMove.getStartPosition());
            board.addPiece(currentMove.getEndPosition(),currentPiece);
            board.removePiece(currentMove.getStartPosition());
            if (!isInCheck(currentTeamColor)) {
                validMoves.add(currentMove);
            }
        }
        copyBoard.setEqual(board);
        return validMoves;
    }

    public Collection<ChessMove> allValidMoves(TeamColor currentTeamColor) {
        Collection<ChessMove> returnList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition currentPosition = new ChessPositionImpl(i,j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == currentTeamColor) {
                    returnList.addAll(validMoves(currentPosition));
                }
            }
        }
        return returnList;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        ChessPiece currentPiece =  board.getPiece(startPosition);
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
        ChessPiece currentPiece =  board.getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        currentEndPosition = new ChessPositionImpl(currentRow+1, currentColumn);
        if (board.getPiece(currentEndPosition) == null) {
            returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            if(!currentPiece.hasMoved()) {
                currentEndPosition = new ChessPositionImpl(currentRow+2, currentColumn);
                if (board.getPiece(currentEndPosition) == null) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
            }
        }
        if (currentColumn > 0) {
            currentEndPosition = new ChessPositionImpl(currentRow+1, currentColumn-1);
            if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
            }
        }
        if (currentColumn < 7) {
            currentEndPosition = new ChessPositionImpl(currentRow+1, currentColumn+1);
            if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
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
        ChessPiece currentPiece =  board.getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        for (int i = 1; i < 8 - currentRow; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow+i, currentColumn);
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
        }
        for (int i = 1; i < currentRow + 1; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow-i, currentColumn);
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
        }
        for (int i = 1; i < 8 - currentColumn; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow, currentColumn+i);
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
        }
        for (int i = 1; i < currentRow + 1; i++) {
            currentEndPosition = new ChessPositionImpl(currentRow, currentColumn-i);
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
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
        ChessPiece currentPiece =  board.getPiece(startPosition);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int nextRow = currentRow + 1;
        int nextColumn = currentColumn + 1;
        while (nextRow < 8 && nextColumn < 8) {
            currentEndPosition = new ChessPositionImpl(nextRow, nextColumn);
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
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
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
            nextColumn --;
            nextRow --;
        }
        nextColumn = currentColumn + 1;
        nextRow = currentRow - 1;
        while (nextRow > 0 && nextColumn < 8) {
            currentEndPosition = new ChessPositionImpl(nextRow, nextColumn);
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
                    returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
                }
                break;
            }
            nextColumn ++;
            nextRow --;
        }
        nextColumn = currentColumn - 1;
        nextRow = currentRow + 1;
        while (nextRow < 8 && nextColumn > 0) {
            currentEndPosition = new ChessPositionImpl(nextRow, nextColumn);
            if (board.getPiece(currentEndPosition) == null) {
                returnList.add(new ChessMoveImpl(startPosition, currentEndPosition));
            } else if (board.getPiece(currentEndPosition) != null) {
                if (board.getPiece(currentEndPosition).getTeamColor() != currentColor) {
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
        ChessPiece currentPiece =  board.getPiece(startPosition);
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
        ChessPiece currentPiece =  board.getPiece(startPosition);
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
        return returnList;
    }

    private boolean checkOpenMove(ChessPosition endingPosition, TeamColor color) {
        if (endingPosition.getColumn() > 7 || endingPosition.getColumn() < 0 || endingPosition.getRow() > 7 || endingPosition.getRow() < 0) {
            return false;
        }
        if (board.getPiece(endingPosition) == null) {
            return true;
        } else if (board.getPiece(endingPosition) != null) {
            if (board.getPiece(endingPosition).getTeamColor() != color) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves;
        if (isInCheck(getTeamTurn())) {
            validMoves = allValidCheckedMoves(getTeamTurn());
        } else {
            validMoves = allValidMoves(getTeamTurn());

        }
        if(validMoves.contains(move)) {
            ChessPiece currentPiece = board.getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(),currentPiece);
            board.removePiece(move.getStartPosition());
        } else {
            //Throw Exception
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        if (teamColor == TeamColor.BLACK) {
            enemyMoves.addAll(allValidMoves(TeamColor.WHITE));
        } else {
            enemyMoves.addAll(allValidMoves(TeamColor.BLACK));
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition currentPosition = new ChessPositionImpl(i,j);
                ChessPiece currentChessPiece = board.getPiece(currentPosition);
                if (currentChessPiece.getTeamColor() == teamColor && currentChessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    for (ChessMove currentMove : enemyMoves) {
                        if(currentMove.getEndPosition() == currentPosition) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
        currentMoves.addAll(allValidMoves(teamColor));
        return currentMoves.isEmpty();
    }

    @Override
    public void setBoard(ChessBoard board) {
        board.resetBoard();
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
