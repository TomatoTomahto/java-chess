package chess;

import chess.pieces.*;

/**
 * @author Ryan Gisleson
 */
public class GuiTest {
    /**
     * This class is not for junit testing, it is for creating
     * tests to run manually and observe the gui is working correctly.
     */

    // standard chess game
    public static void testGuiForStandardGame() {
        new GUI(new Game(GameType.STANDARD));
    }

    // standard chess board setup with custom pieces swapped in
    public static void testGuiForCustomGame() {
        new GUI(new Game(GameType.CUSTOM));
    }

    public static void testGuiWithCustomSetup() {
        Game game = new Game();
        game.addPiece(new Pawn(Team.BLACK), 3, 6);
        game.addPiece(new Knight(Team.BLACK), 7, 2);
        game.addPiece(new Squirrel(Team.WHITE), 2, 4);
        game.addPiece(new King(Team.WHITE), 0, 3);
        new GUI(game);
    }

    public static void main(String[] args) {
        testGuiForCustomGame();
    }
}
