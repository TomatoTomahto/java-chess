package chess;

import chess.pieces.King;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan Gisleson
 */
public class BoardTest {

    @Test
    public void testDefaultBoardCreation() {
        Board board = new Board();
        assertTrue(board.getWidth() == 8);
        assertTrue(board.getHeight() == 8);
    }

    @Test
    public void testBoardConstructor() {
        Random rand = new Random();
        int width = rand.nextInt(50) + 1;
        rand = new Random();
        int height = rand.nextInt(50) + 1;
        Board board = new Board(width, height);
        assertTrue(board.getWidth() == width);
        assertTrue(board.getHeight() == height);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBoardNegArg() {
        Board board = new Board(-1, -1);
    }

    @Test
    public void testSetPiece() {
        Board board = new Board();
        assertTrue(board.getPiece(0, 0) == null);
        Piece piece = new King(Team.WHITE);
        board.setPiece(piece, 0, 0);
        assertEquals(King.class, board.getPiece(0, 0).getClass());
    }

    @Test
    public void testSetPieceOffBoard() {
        Board board = new Board();
        assertTrue(board.getPiece(0, 0) == null);
        Piece piece = new King(Team.WHITE);
        board.setPiece(piece, 0, 0);
        assertTrue(board.getPiece(0, 0) instanceof King);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPieceOffBoard() {
        Board board = new Board();
        board.getPiece(10, 10);
    }
}