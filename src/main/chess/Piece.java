package chess;

import java.util.ArrayList;

/**
 * @author Ryan Gisleson
 */
public abstract class Piece {
    private final Team team; // team of piece from list of colors
    private int x, y; // coordinates on board
    private boolean captured = false; // whether or not piece has been removed from play

    // Pieces must have colors
    public Piece() {
        throw new IllegalArgumentException("Must specify a team");
    }


    public Piece(Team team) {
        this.team = team;
    }

    /**
     * Check whether two pieces are the same team.
     * @param otherPiece Piece to compare colors with
     * @return Returns true if the colors are the same, false otherwise
     */
    public boolean isSameColor(Piece otherPiece) {
        return otherPiece != null && this.getTeam() == otherPiece.getTeam();
    }

    /**
     * If there is a piece at target location, check whether it is opposing color.
     * Subclasses will use this along with their specific move logic.
     * @param board Game board pieces are on
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return True if piece is allowed to be moved to location, false otherwise
     */
    public boolean canMove(Board board, int x, int y) {
        if (!board.isValidSpace(x, y)) return false;
        if (this.getX() == x && this.getY() == y) return false;
        boolean valid = board.isValidSpace(x, y);
        Piece otherPiece = board.getPiece(x, y);
        if (otherPiece != null) // there is already a piece at that position
            // other piece must be opposite team or we cannot take space
            valid = valid && !this.isSameColor(otherPiece);
        return valid;
    }

    /**
     * Tells whether a piece can capture another by checking if it can move to the other
     * pieces position
     * @param board The board the pieces are on
     * @param otherPiece The piece to be captured
     * @return True if otherPiece can be captured, False otherwise
     */
    public boolean canCapture(Board board, Piece otherPiece) {
        return canMove(board, otherPiece.getX(), otherPiece.getY());
    }

    /**
     * Checks if given piece can be moved to space, performing move if it can
     * @param x The x coordinate to move piece to
     * @param y The y coordinate to move piece to
     * @param board The board to move piece on.
     * @return Move information if move is performed, move is null if not performed
     */
    public Move movePiece(Board board, int x, int y) {
        if (this.canMove(board, x, y)) {
            Move move = new Move(this, board.getPiece(x, y), x, y);
            board.removePiece(this);
            if (board.getPiece(x, y) != null) {
                board.getPiece(x, y).setCaptured(true); // capture piece if one exists
            }
            board.setPiece(this, x, y);
            return move;
        }
        else
            return null;

    }

    /**
     * Gets a list of all positions piece could move to, without checking legality of moves
     * @param board The board the pieces are on
     * @return Array of moves where move[i][0] and move[i][1] correspond to the the x and y endpoints of the ith move
     */
    public int[][] getMoves(Board board) {
        ArrayList<int[]> moves = new ArrayList<>();
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (this.canMove(board, x, y)) {
                    int[] move = new int[2];
                    move[0] = x;
                    move[1] = y;
                    moves.add(move);
                }
            }
        }
        return moves.toArray(new int[0][]);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }
}
