package ui;

import ChessImpl.*;
import chess.*;

import java.util.ArrayList;
import java.util.Collections;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class UI {

    public String printBoard(ChessBoard board, ChessGame.TeamColor color, ChessPositionImpl highlightPosition) {
        ArrayList<ChessMove> highlightMoves = null;
        if (highlightPosition != null) {
            ChessPieceImpl highlightPiece = (ChessPieceImpl) board.getPiece(highlightPosition);
            highlightMoves = (ArrayList<ChessMove>) highlightPiece.pieceMoves(board, highlightPosition);
        }
        StringBuilder sb = new StringBuilder();
        ArrayList<String> stringArray = new ArrayList<>();
        sb.append(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE);
        addStringToList(stringArray, sb);
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        for (int i = 7; i > -1; i--) {
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (char)('a'+i) + " ");
            addStringToList(stringArray, sb);
        }
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        sb.append(RESET_BG_COLOR + "\n");
        addStringToList(stringArray, sb);
        for (int i = 1; i < 9; i++) {
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (i) + " ");
            addStringToList(stringArray, sb);
            for (int j = 8; j > 0; j--) {
                ChessPosition currentPosition = new ChessPositionImpl(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                boolean highlight = false;
                boolean yellow = false;
                if (highlightMoves != null) {
                    for (ChessMove move: highlightMoves) {
                        if (move.getEndPosition().getColumn() == currentPosition.getColumn() && move.getEndPosition().getRow() == currentPosition.getRow()) {
                            highlight = true;
                        }
                    }
                }
                if (highlightPosition != null) {
                    if (highlightPosition.getColumn() == currentPosition.getColumn() && highlightPosition.getRow() == currentPosition.getRow()) {
                        yellow = true;
                    }
                }
                if (yellow) {
                    sb.append(SET_BG_COLOR_YELLOW);
                } else if ((i + j)%2 == 0) {
                    if (highlight) {
                        sb.append(SET_BG_COLOR_GREEN);
                    } else {
                        sb.append(SET_BG_COLOR_WHITE);
                    }
                } else {
                    if (highlight) {
                        sb.append(SET_BG_COLOR_DARK_GREEN);
                    } else {
                        sb.append(SET_BG_COLOR_BLACK);
                    }
                }
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        sb.append(SET_TEXT_COLOR_BLUE + SET_TEXT_BOLD);
                    } else {
                        sb.append(SET_TEXT_COLOR_RED + SET_TEXT_BOLD);
                    }
                    sb.append(" " + getChessPieceLetter(currentPiece) + " ");
                    addStringToList(stringArray, sb);
                } else {
                    sb.append("   ");
                    addStringToList(stringArray, sb);
                }
            }
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (i) + " ");
            addStringToList(stringArray, sb);
            sb.append(RESET_BG_COLOR + "\n");
            addStringToList(stringArray, sb);
        }
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        for (int i = 7; i > -1; i--) {
            sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (char)('a'+i) + " ");
            addStringToList(stringArray, sb);
        }
        sb.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "   ");
        addStringToList(stringArray, sb);
        sb.append(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "\n");
        addStringToList(stringArray, sb);
        if (color == ChessGame.TeamColor.WHITE) {
            Collections.reverse(stringArray);
        }
        for (String item: stringArray) {
            sb.append(item);
        }
        return sb.toString();
    }

    private void addStringToList(ArrayList<String> stringArray, StringBuilder sb) {
        stringArray.add(sb.toString());
        sb.delete(0,sb.length());
    }

    private char getChessPieceLetter(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> 'K';
            case KNIGHT -> 'N';
            case ROOK -> 'R';
            case BISHOP -> 'B';
            case QUEEN -> 'Q';
            case PAWN -> 'P';
            default -> ' ';
        };
    }
}
