package chess.pieces;

import chess.Piece;
import chess.Team;
import chess.Board;

/**
 * @author Ryan Gisleson
 */
public class Bishop extends Piece {

    public Bishop() {
        super();
    }

    public Bishop(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        if (!super.canMove(board, x, y))
            return false;

        int xVal = this.getX() - x;
        int yVal = this.getY() - y;
        // these are the same when point is on the diagonal of bishop
        if (Math.abs(xVal) != Math.abs(yVal))
            return false;

        int xDir = (xVal < 0) ? 1 : -1; // xVal is negative when moving right
        int yDir = (yVal < 0) ? 1 : -1; // yVal is negative when moving down
        int i = this.getX() + xDir;
        int j = this.getY() + yDir;
        while (i != x && j != y) {
            if (board.getPiece(i, j) != null)
                return false; // path is blocked
            i += xDir;
            j += yDir;
        }

        return true;
    }

}
