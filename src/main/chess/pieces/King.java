package chess.pieces;

import chess.Board;
import chess.Piece;
import chess.Team;

/**
 * @author Ryan Gisleson
 */
public class King extends Piece {

    public King() {
        super();
    }

    public King(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        int xVal = Math.abs(this.getX() - x);
        int yVal = Math.abs(this.getY() - y);
        return xVal <= 1 && yVal <= 1 && super.canMove(board, x, y);
    }
}
