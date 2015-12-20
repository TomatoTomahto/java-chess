package chess;

import chess.pieces.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan Gisleson
 */
public class PieceTest {

    private Board board;

    @Before
    public void beforeTest() {
        board = new Board();
    }

    @After
    public void afterTest() {
        board = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultConstructor() {
        Piece piece = new King();
    }

    @Test
    public void testConstructor() {
        Piece piece = new King(Team.WHITE);
        assertEquals(Team.WHITE, piece.getTeam());
        piece = new King(Team.BLACK);
        assertEquals(Team.BLACK, piece.getTeam());
        assertFalse(piece.isCaptured());
    }

    @Test
    public void testValidMove() {
        Piece rook = new Rook(Team.BLACK);
        board.setPiece(rook, 0, 0);
        assertTrue(rook.canMove(board, 6, 0));
        assertFalse(rook.canMove(board, 6, 6));
    }

    @Test
    public void testGetX() {
        Piece piece = new Queen(Team.BLACK);
        board.setPiece(piece, 5, 6);
        assertEquals(5, piece.getX());
    }

    @Test
    public void testGetY() {
        Piece piece = new Queen(Team.BLACK);
        board.setPiece(piece, 5, 6);
        assertEquals(6, piece.getY());
    }

    @Test
    public void testSetX() {
        Piece piece = new Queen(Team.BLACK);
        piece.setX(5);
        assertEquals(5, piece.getX());
    }

    @Test
    public void testSetY() {
        Piece piece = new Queen(Team.BLACK);
        piece.setY(6);
        assertEquals(6, piece.getY());
    }
}