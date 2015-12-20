package chess.pieces;

import chess.Team;
import chess.Board;
import chess.Piece;

/**
 * @author Ryan Gisleson
 */
public class Knight extends Piece {

    public Knight() {
        super();
    }

    public Knight(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        if (!super.canMove(board, x, y))
            return false;
        int xVal = Math.abs(this.getX() - x);
        int yVal = Math.abs(this.getY() - y);
        // matches knight movement rules
        return (xVal == 2 && yVal == 1) || (xVal == 1 && yVal == 2);
    }
}
