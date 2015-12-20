package chess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Viewer for the Game class.
 * Chess piece sprites from: https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 * @author Ryan Gisleson
 */
public class GUI implements ActionListener {

    class Square extends JButton implements ActionListener {
        private int x, y;

        Square(int x, int y) {
            super();
            this.x = x;
            this.y = y;
            this.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Piece selected = game.getSelected();

            if (selected == null) {
                tryToSelectPiece();
            } else if (selected.getX() == x && selected.getY() == y) { // unselecting piece
                game.setSelected(-1, -1);
                drawBoard();
            } else {
                int oldX = selected.getX();
                int oldY = selected.getY();
                int ret = game.attemptMove(selected, x, y);
                if (ret == 0)
                    handleSuccessfulMove();
                if (ret == 1)
                    setMessage("Illegal move, piece cannot move there.");
                if (ret == 2)
                    setMessage("Illegal move, move would put you in check.");
                squares[oldX][oldY].setBorder(BorderFactory.createEmptyBorder());
                game.setSelected(-1, -1); // sets selected to null
                drawBoard();
            }
        }

        private void handleSuccessfulMove() {
            game.nextTurn();
            String turn = (game.getTurn() == Team.WHITE) ? "White" : "Black";
            String other = (game.getTurn() == Team.WHITE) ? "Black" : "White";
            // TODO : need to check for checkmate, and end game if so
            Piece king = game.getKing(game.getTurn());
            if(game.inCheck(king, king.getX(), king.getY()))
                if (game.inCheckmate(king, king.getX(), king.getY()))
                    handleCheckmate();
                else
                    setMessage(turn + "king in check! " + turn + "'s turn.");
            else
                setMessage(turn + "'s turn.");
        }

        private void handleCheckmate() {
            drawBoard();
            Team winner = (game.getTurn() == Team.WHITE) ? Team.BLACK : Team.WHITE;
            String winnerStr = (game.getTurn() == Team.WHITE) ? "Black" : "White";
            setMessage("CHECKMATE! " + winnerStr + " wins!");
            incrementScore(winner);
            int ret = JOptionPane.showConfirmDialog(null, "Would you like " +
                    "to start another match?", "Start New Match", JOptionPane.YES_NO_OPTION);
            if (ret == 0)
                restartGame();
        }

        private void tryToSelectPiece() {
            if (game.isValidPiece(x, y)) {
                game.setSelected(x, y);
                squares[x][y].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                for (int[] move : game.getSelected().getMoves(game.getBoard())) {
                    squares[move[0]][move[1]].setBorder((BorderFactory.createLineBorder(Color.GREEN, 3)));
                }
            }
        }
    }


    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 750;

    private final Square[][] squares = new Square[8][8];
    private JLabel msgText = new JLabel("");
    private JLabel whiteText = new JLabel("Player 1");
    private JLabel blackText = new JLabel("Player 2");
    private JLabel whiteScore = new JLabel("0");
    private JLabel blackScore = new JLabel("0");
    private Game game;


    public GUI(Game game) {
        this.game = game;
        JFrame window = new JFrame("Chess");
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        JPanel chessboard = initializeBoard();
        initializeBottomBar(window);
        initializeSquares(chessboard);
        setUpMenu(window);
        showBoard(window, chessboard);
    }

    private void initializeBottomBar(JFrame window) {
        Font font = new Font("Droid Sans", 1, 20);
        JPanel bar = new JPanel();
        bar.setPreferredSize(new Dimension(WINDOW_WIDTH, 100));
        bar.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setPreferredSize(new Dimension(WINDOW_WIDTH, 50));
        top.setLayout(new BorderLayout());

        JPanel bot = new JPanel();
        bot.setPreferredSize(new Dimension(WINDOW_WIDTH, 50));
        bot.setLayout(new BorderLayout());

        JPanel whitePanel = new JPanel();
        whitePanel.setLayout(new GridBagLayout());
        whitePanel.setBackground(Color.LIGHT_GRAY);
        whitePanel.setPreferredSize(new Dimension(WINDOW_WIDTH / 2, 50));
        whitePanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 2, Color.DARK_GRAY));
        whiteText.setFont(font);
        whiteScore.setFont(font);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 7, 0, 4);
        gbc.weightx = 1;
        whitePanel.add(whiteText, gbc);
        gbc.weightx = 0;
        whitePanel.add(whiteScore, gbc);
        whiteText.setForeground(Color.WHITE);
        whiteScore.setForeground(Color.WHITE);

        JPanel blackPanel = new JPanel();
        blackPanel.setLayout(new GridBagLayout());
        blackPanel.setBackground(Color.LIGHT_GRAY);
        blackPanel.setPreferredSize(new Dimension(WINDOW_WIDTH / 2, 50));
        blackPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 0, Color.DARK_GRAY));
        blackText.setFont(font);
        blackScore.setFont(font);
        gbc.weightx = 0;
        blackPanel.add(blackScore, gbc);
        gbc.weightx = 1;
        blackPanel.add(blackText, gbc);
        blackText.setForeground(Color.BLACK);
        blackScore.setForeground(Color.BLACK);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new GridBagLayout());
        msgPanel.setBackground(Color.LIGHT_GRAY);
        msgPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 50));
        msgPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.DARK_GRAY));
        msgText.setFont(font);
        msgText.setForeground(Color.DARK_GRAY);
        msgPanel.add(msgText, new GridBagConstraints());

        top.add(whitePanel, BorderLayout.WEST);
        top.add(blackPanel, BorderLayout.EAST);
        bot.add(msgPanel, BorderLayout.CENTER);
        bar.add(top, BorderLayout.NORTH);
        bar.add(bot, BorderLayout.SOUTH);
        window.add(bar, BorderLayout.SOUTH);
    }

    private JPanel initializeBoard() {
        JPanel board = new JPanel();
        board.setPreferredSize(new Dimension(500, 500));
        board.setLayout(new GridLayout(8, 8));
        return board;
    }

    private void showBoard(JFrame window, JPanel chessboard) {
        window.add(chessboard);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
    }

    private void setUpMenu(JFrame window) {
        JMenuBar menubar = new JMenuBar();
        JMenu game = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem restartGame = new JMenuItem("Restart Game");
        JMenuItem forfeitGame = new JMenuItem("Forfeit Game");
        JMenuItem undoMove = new JMenuItem("Undo Move");
        newGame.addActionListener(this);
        restartGame.addActionListener(this);
        forfeitGame.addActionListener(this);
        undoMove.addActionListener(this);
        game.add(newGame);
        game.add(restartGame);
        game.add(forfeitGame);
        game.add(undoMove);
        menubar.add(game);

        JMenu player = new JMenu("Players");
        JMenuItem player1 = new JMenuItem("Change player one name");
        JMenuItem player2 = new JMenuItem("Change player two name");
        player1.addActionListener(this);
        player2.addActionListener(this);
        player.add(player1);
        player.add(player2);
        menubar.add(player);

        window.setJMenuBar(menubar);
    }

    private void initializeSquares(JPanel panel) {
        for (int y = 0; y < game.getBoard().getHeight(); y++) {
            for (int x = 0; x < game.getBoard().getWidth(); x++) {
                squares[x][y] = new Square(x, y);
                squares[x][y].setFocusable(false);
                setSquareColor(squares[x][y]);
                squares[x][y].setBorder(BorderFactory.createEmptyBorder()); // make squares pack tightly
                panel.add(squares[x][y]);
            }
        }
    }

    private void setSquareColor(Square square) {
        int x = square.x;
        int y = square.y;
        if (x % 2 == 0)
            if (y % 2 == 0)
                squares[x][y].setBackground(Color.white);
            else
                squares[x][y].setBackground(Color.black);
        else if (y % 2 == 0)
            squares[x][y].setBackground(Color.black);
        else
            squares[x][y].setBackground(Color.white);
    }

    private void setMessage(String message) {
        msgText.setText(message);
    }

    private ImageIcon getPieceIcon(Piece piece) {
        String imageLocation = "src/resources/"
                + piece.getTeam().toString().toLowerCase()
                + piece.getClass().getSimpleName() +".png";
        return getPieceIcon(imageLocation);
    }

    private ImageIcon getPieceIcon(String imageLocation) {
        return new ImageIcon(imageLocation);
    }

    private void drawBoard() {
        for  (Square[] col : squares) {
            for (Square square : col) {
                square.setIcon(null);
                square.setBorder(BorderFactory.createEmptyBorder());
            }
        }
        drawAllPieces();
    }

    public void drawAllPieces() {
        Piece[] blackPieces = game.getPieces(Team.BLACK);
        Piece[] whitePieces = game.getPieces(Team.WHITE);
        for (Piece piece : blackPieces) {
            if (piece == null) break;
            if (piece.isCaptured()) continue;
            drawPiece(piece, piece.getX(), piece.getY());
        }
        for (Piece piece : whitePieces) {
            if (piece == null) break;
            if (piece.isCaptured()) continue;
            drawPiece(piece, piece.getX(), piece.getY());
        }
    }

    /**
     * Draw the specified piece at given location.
     * @param piece The piece to draw.
     * @param x The x coordinate to draw the piece.
     * @param y The y coordinate to draw the piece.
     */
    public void drawPiece(Piece piece, int x, int y) {
        squares[x][y].setIcon(getPieceIcon(piece));
    }

    public void startNewGame() {
        setPlayerName(Team.WHITE);
        setPlayerName(Team.BLACK);
        setScore(Team.WHITE, 0);
        setScore(Team.BLACK, 0);
        restartGame();
    }

    private void setScore(Team team, int score) {
        JLabel label = (team == Team.WHITE) ? whiteScore : blackScore;
        label.setText(Integer.toString(score));
    }

    public void restartGame() {
        this.game = new Game(GameType.STANDARD);
        setMessage("White's turn.");
        drawBoard();
    }

    public void forfeitGame() {
        Team winner = (game.getTurn() == Team.WHITE) ? Team.BLACK : Team.WHITE;
        incrementScore(winner);
        displayWinner(winner);
        restartGame();
    }

    private void incrementScore(Team winner) {
        JLabel score = (winner == Team.WHITE) ? whiteScore : blackScore;
        setScore(winner, Integer.parseInt(score.getText() + 1));
    }

    private void displayWinner(Team team) {
        String winner = (team == Team.WHITE) ? whiteText.getText() : blackText.getText();
        msgText.setText(winner + " wins this match!");
    }

    public void setPlayerName(Team team) {
        String player = (team == Team.WHITE) ? "1" : "2";
        String name = JOptionPane.showInputDialog(null, "Input new player " + player + " name:",
                "Player " + player);
        JLabel label = (team == Team.WHITE) ? whiteText : blackText;
        label.setText(name);
    }

    public void undoMove() {
        game.undoMove(game.getLastMove());
        game.nextTurn();
        drawBoard();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String action = e.getActionCommand();
        int ret;
        String name;
        switch (action) {
            case "New Game":
                ret = JOptionPane.showConfirmDialog(null, "This will start a new game with new players, " +
                        "reseting the scores, continue?", action, JOptionPane.YES_NO_OPTION);
                if (ret == 0) // selected yes
                    startNewGame();
                break;
            case "Restart Game":
                ret = JOptionPane.showConfirmDialog(null, "This will restart the game with no change to " +
                        "scores. Do both players agree?", action, JOptionPane.YES_NO_OPTION);
                if (ret == 0)
                    restartGame();
                break;
            case "Forfeit Game":
                ret = JOptionPane.showConfirmDialog(null, "This will cause you to lose the game, " +
                        "giving your opponent 1 point, continue?", action, JOptionPane.YES_NO_OPTION);
                if (ret == 0)
                    forfeitGame();
                break;
            case "Undo Move":
                ret = JOptionPane.showConfirmDialog(null, "Would you like to undo the previous move?",
                        action, JOptionPane.YES_NO_OPTION);
                if (ret == 0)
                    undoMove();
                break;
            case "Change player one name":
                setPlayerName(Team.WHITE);
                break;
            case "Change player two name":
                setPlayerName(Team.BLACK);
                break;
            default:
                System.out.println(action);
                break;
        }
    }

    public static void main(String[] args) {
        GUI chess = new GUI(new Game());
    }
}
