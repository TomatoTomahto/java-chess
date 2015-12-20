package chess.pieces;

import chess.Piece;
import chess.Team;
import chess.Board;

/**
 * @author Ryan Gisleson
 */
public class Squirrel extends Piece {
    /**
     * Can only move to squares two away, jumping over any adjacent squares in the process.
     */

    public Squirrel() {
        super();
    }

    public Squirrel(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        if (!super.canMove(board, x, y))
            return false;
        int xVal = Math.abs(this.getX() - x);
        int yVal = Math.abs(this.getY() - y);
        // matches cannot move further than 2 squares in any direction
        return (xVal == 2 && yVal <= 2) || (xVal <= 2 && yVal == 2);
    }
}
