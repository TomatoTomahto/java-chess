package chess;

/**
 * @author Ryan Gisleson
 */
class Move {
    /**
     * Store data for a given move
     */
    private Piece attacker;
    private Piece defender; // will be null if attacker moved to a square with no opposing piece
    private int x; // x coordinate attacker moved to
    private int y; // y coordinate attacker moved to
    private int oldX; // previous x coordinate
    private int oldY; // previous y coordinate

    public Move(Piece attacker, Piece defender, int x, int y) {
        this.attacker = attacker;
        this.defender = defender;
        this.oldX = attacker.getX();
        this.oldY = attacker.getY();
        this.x = x;
        this.y = y;
    }

    public Piece getAttacker() {
        return attacker;
    }

    public Piece getDefender() {
        return defender;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }
}
