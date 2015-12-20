package chess.pieces;

import chess.Board;
import chess.Piece;
import chess.Team;
import chess.pieces.Rook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan Gisleson
 */
public class RookTest {

    private Board board;
    private Piece piece;

    @Before
    public void beforeTest() {
        board = new Board();
        piece = new Rook(Team.WHITE);
    }

    @After
    public void afterTest() {
        board = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructorFails() {
        Piece piece = new Rook();
    }

    @Test
    public void testConstructor() {
        Piece piece = new Rook(Team.WHITE);
        assertEquals(Rook.class, piece.getClass());
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
                else if (piece.getX() - x == 0 || piece.getY() - y == 0)
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
        board.setPiece(piece, 5, 7);
        Piece otherPiece = new Rook(Team.BLACK);
        board.setPiece(otherPiece, 5, 5);
        assertTrue(piece.canCapture(board, otherPiece));
    }
}
