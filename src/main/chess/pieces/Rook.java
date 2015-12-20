package chess.pieces;

import chess.Board;
import chess.Piece;
import chess.Team;

/**
 * @author Ryan Gisleson
 */
public class Rook extends Piece {

    public Rook() {
        super();
    }

    public Rook(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        if (!super.canMove(board, x, y))
            return false;
        int xVal = this.getX() - x;
        int yVal = this.getY() - y;
        // if either [x/y]Val is 0 then rook is same column/row as specified point
        if (xVal != 0 && yVal != 0)
            return false;

        // check all spaces between target point and current
        if (xVal == 0) {
            int dir = (yVal < 0) ? 1 : -1; // whether rook is moving up or down
            for (int i = this.getY() + dir; i != y; i += dir) {
                if (board.getPiece(x, i) != null)
                    return false; // there is piece between the rook and target square
            }
        }
        else { // yVal == 0
            int dir = (xVal < 0) ? 1 : -1; // whether rook is moving up or down
            for (int i = this.getX() + dir; i != x; i += dir) {
                if (board.getPiece(i, y) != null)
                    return false; // there is piece between the rook and target square
            }
        }

        return true;
    }
}
