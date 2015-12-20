package chess.pieces;

import chess.Piece;
import chess.Team;
import chess.Board;

/**
 * @author Ryan Gisleson
 */
public class Pawn extends Piece {

    public Pawn() {
        super();
    }

    public Pawn(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        // doesn't use super.canMove since pawn capturing rules are different
        Team team = this.getTeam();
        int xVal = this.getX() - x;
        int yVal = this.getY() - y;

        // special checks to allow two square moves on first move
        if (team == Team.WHITE && yVal == 2 && xVal == 0)
            return getY() == 6 && board.getPiece(x, y) == null && board.getPiece(x, y+1) == null;
        if (team == Team.BLACK && yVal == -2 && xVal == 0)
            return getY() == 1 && board.getPiece(x, y) == null && board.getPiece(x, y-1) == null;

        if (Math.abs(yVal) > 1 || Math.abs(xVal) > 1)
            return false; // pawns cannot ever move more than 1 square sideways or 2 squares forward

        // need to check first if pawn is attacking an opposing piece
        if (board.getPiece(x, y) != null && (xVal == 1 || xVal == -1)) { // single diagonal moves
            if (team == Team.WHITE && yVal == 1) { // forward diagonal move for white
                return !this.isSameColor(board.getPiece(x, y));
            } else if (team == Team.BLACK && yVal == -1) { // forward diagonal move for black
                return !this.isSameColor(board.getPiece(x, y));
            } else return false; // pawns cannot move sideways otherwise
        } else if (xVal == 0) { // moving straight forward
            if (team == Team.WHITE && yVal == 1) {
                return board.getPiece(x, y) == null;
            } else if (team == Team.BLACK && yVal == -1) {
                return board.getPiece(x, y) == null;
            }
        }
        return false; // shouldn't get here
    }
}
