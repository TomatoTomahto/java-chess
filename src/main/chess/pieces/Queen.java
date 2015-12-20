package chess.pieces;

import chess.Piece;
import chess.Team;
import chess.Board;

/**
 * @author Ryan Gisleson
 */
public class Queen extends Piece {

    public Queen() {
        super();
    }

    public Queen(Team team) {
        super(team);
    }

    public boolean canMove(Board board, int x, int y) {
        if (!super.canMove(board, x, y))
            return false;
        // test if a rook could make move
        Piece dummyRook = new Rook(this.getTeam());
        dummyRook.setX(this.getX());
        dummyRook.setY(this.getY());
        // test if a bishop could make move
        Piece dummyBishop = new Bishop(this.getTeam());
        dummyBishop.setX(this.getX());
        dummyBishop.setY(this.getY());

        return dummyRook.canMove(board, x, y) || dummyBishop.canMove(board, x, y);
    }
}
