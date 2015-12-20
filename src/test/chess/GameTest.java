package chess;

import chess.pieces.*;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Ryan Gisleson
 */
public class GameTest {

    private Game game;

    public void initEmptyGame() {
        game = new Game();
    }

    public void initStandardGame() {
        game = new Game(GameType.STANDARD);
    }

    public void initCustomGame() {
        game = new Game(GameType.CUSTOM);
    }

    @After
    public void afterTest() {
        game = null;
    }

    @Test
    public void testPieces() {
        initStandardGame();
        Piece[] pieces = game.getPieces(Team.BLACK);
        for (int i = 0; i < 16; i++) {
            assertTrue(pieces[i].getTeam() == Team.BLACK);
        }
        pieces = game.getPieces(Team.WHITE);
        for (int i = 0; i < 16; i++) {
            assertTrue(pieces[i].getTeam() == Team.WHITE);
        }
    }

    @Test
    public void testStartTurn() {
        initEmptyGame();
        assertTrue(game.getTurn() == Team.WHITE);
    }

    @Test
    public void testGetBlackPieces() {
        initStandardGame();
        for (Piece piece : game.getPieces(Team.BLACK))
            assertEquals(Team.BLACK, piece.getTeam());
    }

    @Test
    public void testGetWhitePieces() {
        initStandardGame();
        for (Piece piece : game.getPieces(Team.WHITE))
            assertEquals(Team.WHITE, piece.getTeam());
    }

    @Test
    public void testGetWhiteKing() {
        initStandardGame();
        Piece piece = game.getKing(Team.WHITE);
        assertEquals(Team.WHITE, piece.getTeam());
        assertEquals(King.class, piece.getClass());
    }

    @Test
    public void testGetBlackKing() {
        initStandardGame();
        Piece piece = game.getKing(Team.BLACK);
        assertEquals(Team.BLACK, piece.getTeam());
        assertEquals(King.class, piece.getClass());
    }

    @Test
    public void testInCheckRook() {
        initEmptyGame();
        Piece king = new King(Team.WHITE);
        Piece attacker = new Rook(Team.BLACK);
        game.addPiece(king, 4, 4);
        game.addPiece(attacker, 4, 5);
        assertTrue(game.inCheck(king, 4, 4));
    }

    @Test
    public void testInCheckBishop() {
        initEmptyGame();
        Piece king = new King(Team.WHITE);
        Piece attacker = new Bishop(Team.BLACK);
        game.addPiece(king, 4, 4);
        game.addPiece(attacker, 5, 5);
        assertTrue(game.inCheck(king, 4, 4));
    }

    @Test
    public void testInCheckKnight() {
        initEmptyGame();
        Piece king = new King(Team.WHITE);
        Piece attacker = new Knight(Team.BLACK);
        game.addPiece(king, 4, 4);
        game.addPiece(attacker, 5, 6);
        assertTrue(game.inCheck(king, 4, 4));
    }

    @Test
    public void testInCheckPawn() {
        initEmptyGame();
        Piece king = new King(Team.WHITE);
        Piece attacker = new Pawn(Team.BLACK);
        game.addPiece(king, 4, 4);
        game.addPiece(attacker, 3, 3);
        assertTrue(game.inCheck(king, 4, 4));
    }

    @Test
    public void testInCheckQueen() {
        initEmptyGame();
        Piece king = new King(Team.WHITE);
        Piece attacker = new Queen(Team.BLACK);
        game.addPiece(king, 4, 4);
        game.addPiece(attacker, 5, 5);
        assertTrue(game.inCheck(king, 4, 4));
    }

    @Test
    public void testKingMoveOutOfCheck() {
        initEmptyGame();
        Piece king = new King(Team.WHITE);
        Piece attacker = new Bishop(Team.BLACK);
        game.addPiece(king, 4, 4);
        game.addPiece(attacker, 5, 5);
        assertTrue(game.inCheck(king, king.getX(), king.getY()));
        assertFalse(game.inCheckmate(king, king.getX(), king.getY()));
        king.movePiece(game.getBoard(), 4, 5);
        assertFalse(game.inCheck(king, king.getX(), king.getY()));
        assertFalse(game.inCheckmate(king, king.getX(), king.getY()));
    }
}