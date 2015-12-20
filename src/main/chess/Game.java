package chess;

import chess.pieces.*;

/**
 * @author Ryan Gisleson
 */
public class Game {
    /**
     * This class handles the game logic for a standard game of chess.
     */
    private Team turn = Team.WHITE; // white goes first
    private final Board board = new Board(); // game board for chess
    private Piece whiteKing; // pointers to both kings to quickly check for win conditions
    private Piece blackKing;
    private Piece selected = null; // what piece has been selected
    private Piece[] blackPieces = new Piece[16];
    private Piece[] whitePieces = new Piece[16];

    private Move lastMove = null;

    public Game() {}

    public Game(GameType type) {
        if (type != null)
            setupStandardGame();
    if (type == GameType.CUSTOM)
        setupCustomPieces();
    }

    /**
     * Setup an 8x8 board with chess pieces and also store king pointers
     */
    private void setupStandardGame() {
        // place pawns
        for (int i = 0; i < 8; i++) {
            blackPieces[i] = new Pawn(Team.BLACK);
            whitePieces[i] = new Pawn(Team.WHITE);
            board.setPiece(blackPieces[i], i, 1);
            board.setPiece(whitePieces[i], i, 6);
        }
        // black pieces
        blackPieces[8] = new Rook(Team.BLACK);
        board.setPiece(blackPieces[8], 0, 0);
        blackPieces[9] = new Rook(Team.BLACK);
        board.setPiece(blackPieces[9], 7, 0);
        blackPieces[10] = new Knight(Team.BLACK);
        board.setPiece(blackPieces[10], 1, 0);
        blackPieces[11] = new Knight(Team.BLACK);
        board.setPiece(blackPieces[11], 6, 0);
        blackPieces[12] = new Bishop(Team.BLACK);
        board.setPiece(blackPieces[12], 2, 0);
        blackPieces[13] = new Bishop(Team.BLACK);
        board.setPiece(blackPieces[13], 5, 0);
        blackPieces[14] = new Queen(Team.BLACK);
        board.setPiece(blackPieces[14], 3, 0);
        blackPieces[15] = new King(Team.BLACK);
        board.setPiece(blackPieces[15], 4, 0);
        // white pieces
        whitePieces[8] = new Rook(Team.WHITE);
        board.setPiece(whitePieces[8], 0, 7);
        whitePieces[9] = new Rook(Team.WHITE);
        board.setPiece(whitePieces[9], 7, 7);
        whitePieces[10] = new Knight(Team.WHITE);
        board.setPiece(whitePieces[10], 1, 7);
        whitePieces[11] = new Knight(Team.WHITE);
        board.setPiece(whitePieces[11], 6, 7);
        whitePieces[12] = new Bishop(Team.WHITE);
        board.setPiece(whitePieces[12], 2, 7);
        whitePieces[13] = new Bishop(Team.WHITE);
        board.setPiece(whitePieces[13], 5, 7);
        whitePieces[14] = new Queen(Team.WHITE);
        board.setPiece(whitePieces[14], 3, 7);
        whitePieces[15] = new King(Team.WHITE);
        board.setPiece(whitePieces[15], 4, 7);

        blackKing = blackPieces[15];
        whiteKing = whitePieces[15];
    }

    /**
     * Replace one bishop with an archbishop and one knight with a squirrel.
     */
    private void setupCustomPieces() {
        // black pieces
        blackPieces[10] = new Squirrel(Team.BLACK);
        board.setPiece(blackPieces[10], 1, 0);
        blackPieces[13] = new Archbishop(Team.BLACK);
        board.setPiece(blackPieces[13], 5, 0);
        // white pieces
        whitePieces[11] = new Squirrel(Team.WHITE);
        board.setPiece(whitePieces[11], 6, 7);
        whitePieces[12] = new Archbishop(Team.WHITE);
        board.setPiece(whitePieces[12], 2, 7);
    }

    public void nextTurn() {
        turn = (turn == Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    /**
     * Checks whether or not check can be prevented.
     * @param king The king in check
     * @param x The x coordinate of the king
     * @param y The y coordinate of the king
     * @return True if king will be in checkmate, False otherwise
     */
    public boolean inCheckmate(Piece king, int x, int y) {
        return !kingCanEscapeCheck(king, x, y) && !canDefendCheck(king);
    }

    private boolean kingCanEscapeCheck(Piece king, int x, int y) {
        boolean ret = true;
        if (king.canMove(getBoard(), x + 1, y - 1)) ret = ret && !inCheck(king, x + 1, y - 1);
        if (king.canMove(getBoard(), x + 1, y)) ret = ret && !inCheck(king, x + 1, y);
        if (king.canMove(getBoard(), x + 1, y + 1)) ret = ret && !inCheck(king, x + 1, y + 1);
        if (king.canMove(getBoard(), x, y + 1)) ret = ret && !inCheck(king, x, y + 1);
        if (king.canMove(getBoard(), x - 1, y + 1)) ret = ret && !inCheck(king, x - 1, y + 1);
        if (king.canMove(getBoard(), x - 1, y)) ret = ret && !inCheck(king, x - 1, y);
        if (king.canMove(getBoard(), x - 1, y - 1)) ret = ret && !inCheck(king, x - 1, y - 1);
        if (king.canMove(getBoard(), x, y - 1)) ret = ret && !inCheck(king, x, y - 1);
        return ret;
    }

    private boolean canDefendCheck(Piece king) {
        Piece[] pieces = getAlliedPieces(king);
        Move actual = getLastMove(); // store so we can test moves
        boolean ret = false;
        for (Piece piece : pieces) {
            if (piece == null) break; // end of pieces
            if (piece.isCaptured()) continue;
            for (int y = 0; y < getBoard().getHeight(); y++) {
                for (int x = 0; x < getBoard().getWidth(); x++) {
                    if (attemptMove(piece, x, y) == 0) {
                        undoMove(getLastMove());
                        ret = true;
                    }
                }
            }
        }
        setLastMove(actual);
        return ret;
    }

    /**
     * Checks whether a king at specified position would be in check
     * @param king The king to consider
     * @param x The x coordinate to check on board
     * @param y The y coordinate to check on board
     * @return If a king at specified point would be in check
     */
    public boolean inCheck(Piece king, int x, int y) {
        Piece[] pieces = getOpposingPieces(king);
        return board.isValidSpace(x, y) && checkPiecesCanAttack(pieces, x, y);
    }

    /**
     * Goes through piece list to see if any could attack specified space.
     * @param pieces The list of pieces of one color
     * @param x The x coordinate to move to
     * @param y The y coordinate to move to
     * @return True if one piece can move there, False otherwise
     */
    private boolean checkPiecesCanAttack(Piece[] pieces, int x, int y) {
        for (Piece piece : pieces) {
            if (piece == null)
                continue;
            if (!piece.isCaptured() && piece.canMove(board, x, y))
                return true;
        }
        return false;
    }

    /**
     * Adds piece to gameboard at given coordinates. Also adds to corresponding piece list.
     * @param piece The piece to add.
     * @param x The x coordinate to add piece at.
     * @param y The y coordinate to add piece at.
     */
    public void addPiece(Piece piece, int x, int y) {
        Piece[] pieces = (piece.getTeam() == Team.WHITE) ? getPieces(Team.WHITE) : getPieces(Team.BLACK);
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == null) {
                pieces[i] = piece;
                break;
            } else if (i == pieces.length - 1) return; // shouldnt add piece if piece list is full
        }
        board.setPiece(piece, x, y);
    }

    /**
     * Tries to move piece, checking to make sure it is a legal move
     * @return 0 if piece was moved
     *         1 if not a valid space
     *         2 if move would put you in check
     */
    public int attemptMove(Piece piece, int x, int y) {
        if (!piece.canMove(getBoard(), x, y))
            return 1;
        Move testMove = piece.movePiece(getBoard(), x, y);
        Piece king = getKing(piece.getTeam());
        if (inCheck(king, king.getX(), king.getY())) {
            undoMove(testMove);
            return 2;
        }
        setLastMove(testMove);
        return 0;
    }

    /**
     * Undo the given move.
     * @return False if move could not be undone, True otherwise
     */
    public boolean undoMove(Move move) {
        if (move == null) return false;
        Piece attacker = move.getAttacker();
        Piece defender = move.getDefender();
        int x = move.getX();
        int y = move.getY();
        int oldX = move.getOldX();
        int oldY = move.getOldY();

        board.removePiece(x, y);
        board.setPiece(attacker, oldX, oldY);

        if (defender != null) {
            board.setPiece(defender, x, y);
            defender.setCaptured(false);
        }

        move = null;
        return true;
    }

    public boolean isValidPiece(int x, int y) {
        Team turn = getTurn();
        Piece piece = getBoard().getPiece(x, y);
        return (piece != null) && piece.getTeam() == turn;
    }

    private Piece[] getAlliedPieces(Piece piece) {
        return getPieces(piece.getTeam());
    }

    private Piece[] getOpposingPieces(Piece piece) {
        return (piece.getTeam() == Team.WHITE) ? getPieces(Team.BLACK) : getPieces(Team.WHITE);
    }

    public Piece getKing(Team team) {
        return (team == Team.WHITE) ? whiteKing : blackKing;
    }

    public Team getTurn() {
        return turn;
    }

    public Board getBoard() {
        return board;
    }

    public Piece[] getPieces(Team team) {
        return (team == Team.WHITE) ? whitePieces : blackPieces;
    }

    public Piece getSelected() {
        return selected;
    }

    public void setSelected(int x, int y) {
        if (x < 0 && y < 0)
            selected = null;
        else
            selected = board.getPiece(x, y);
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }
}
