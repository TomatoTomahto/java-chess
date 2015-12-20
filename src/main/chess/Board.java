package chess;

/**
 * @author Ryan Gisleson
 */
public class Board {
    /**
     * A board is aligned with [0][0] being the top-left corner.
     * Black gets the top side and white gets the bottom.
     * Empty squares are represented by null.
     */
    private Piece[][] board;
    private int width, height;

    /**
     * Default constructor builds an 8x8 board, the standard chess board size.
     */
    public Board() {
        width = 8;
        height = 8;
        board = new Piece[width][height];
    }

    /**
     * Constructor that builds a board based on specified dimensions.
     * @param width Width for board
     * @param height Height for board
     */
    public Board(int width, int height) throws IllegalArgumentException {
        if (width <= 0 || height <=0)
            throw new IllegalArgumentException("Board must have positive dimensions.");
        this.width = width;
        this.height = height;
        board = new Piece[width][height];
    }

    /**
     * Check if space exists on board.
     * @param x The x coordinate
     * @param y The y coordinate
     * @return True if on board, False if not on board
     */
    public boolean isValidSpace(int x, int y) {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height);
    }

    /**
     * Sets the piece to the board position specified.
     * @param piece Piece to be placed
     * @param x The x coordinate to place piece on
     * @param y The y coordinate to place piece on
     */
    public void setPiece(Piece piece, int x, int y) {
        if (isValidSpace(x, y))
            board[x][y] = piece;
        piece.setX(x);
        piece.setY(y);
    }

    public void removePiece(Piece piece) {
        removePiece(piece.getX(), piece.getY());
    }

    public void removePiece(int x, int y) {
        board[x][y] = null;
    }

    public Piece getPiece(int x, int y) throws IllegalArgumentException {
        if (!isValidSpace(x, y)) {
            System.err.println("x = " + x + ", y = " + y);
            throw new IllegalArgumentException("Not a valid space on board.");
        }
        return board[x][y];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
