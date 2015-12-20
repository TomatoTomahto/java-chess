package chess.pieces;

import chess.Board;
import chess.Piece;
import chess.Team;
import chess.pieces.Rook;
import chess.pieces.Squirrel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Ryan Gisleson
 */
public class SquirrelTest {

    private Board board;
    private Piece piece;

    @Before
    public void beforeTest() {
        board = new Board();
        piece = new Squirrel(Team.WHITE);
    }

    @After
    public void afterTest() {
        board = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructorFails() {
        Piece piece = new Squirrel();
    }

    @Test
    public void testConstructor() {
        Piece piece = new Squirrel(Team.WHITE);
        assertEquals(Squirrel.class, piece.getClass());
        assertEquals(Team.WHITE, piece.getTeam());
    }

    @Test
    public void testCanMoveFailsOnCurrentPos() {
        board.setPiece(piece, 3, 3);
        assertFalse(piece.canMove(board, 3, 3));
    }

    @Test
    public void testCanMove() {
        board.setPiece(piece, 3, 6);
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (x == piece.getX() && y == piece.getY())
                    assertFalse(piece.canMove(board, x, y));
                else if ((Math.abs(piece.getX() - x) == 2 && Math.abs(piece.getY() - y) <= 2)
                         || (Math.abs(piece.getX() - x) <= 2 && Math.abs(piece.getY() - y) == 2))
                    assertTrue(piece.canMove(board, x, y));
                else
                    assertFalse(piece.canMove(board, x, y));
            }
        }
    }

    @Test
    public void testGetMoves() {
        board.setPiece(piece, 6, 2);
        int[][] moves = piece.getMoves(board);
        for (int[] move : moves)
            assertTrue(piece.canMove(board, move[0], move[1]));
    }

    @Test
    public void testCanCapture() {
        board.setPiece(piece, 7, 7);
        Piece otherPiece = new Rook(Team.BLACK);
        board.setPiece(otherPiece, 5, 5);
        assertTrue(piece.canCapture(board, otherPiece));
    }
}