package chess.pieces;

import chess.Board;
import chess.Team;
import chess.Piece;

/**
 * @author Ryan Gisleson
 */
public class Archbishop extends Piece {
    /**
     * Special piece that gets movement options of both the bishop and the knight.
     */

    public Archbishop() {
        super();
    }

    public Archbishop(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        if (!super.canMove(board, x, y))
            return false;
        // test if a knight could make move
        Piece dummyKnight = new Knight(this.getTeam());
        dummyKnight.setX(this.getX());
        dummyKnight.setY(this.getY());
        // test if a bishop could make move
        Piece dummyBishop = new Bishop(this.getTeam());
        dummyBishop.setX(this.getX());
        dummyBishop.setY(this.getY());

        return dummyKnight.canMove(board, x, y) || dummyBishop.canMove(board, x, y);
    }

}
