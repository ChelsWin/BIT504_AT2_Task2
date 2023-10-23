import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class GameMain extends JPanel implements MouseListener {
    // Constants for game
    // Number of ROWS by COLS cell constants
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final String TITLE = "Tic Tac Toe";

    // Constants for dimensions used for drawing
    // Cell width and height
    public static final int CELL_SIZE = 100;
    // Drawing canvas
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    // Noughts and Crosses are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    /* Declare game object variables */
    // The game board
    private final Board BOARD;
    // For displaying game status message
    private final JLabel STATUS_BAR;
    private GameState currentState;
    // The current player
    private Player currentPlayer;

    /**
     * Constructor to set up the UI and game components on the panel
     */
    public GameMain() {
        // Add a mouse listener for user interaction
        addMouseListener(this);

        // Set up the status bar (JLabel) to display status message
        STATUS_BAR = new JLabel("         ");
        STATUS_BAR.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        STATUS_BAR.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        STATUS_BAR.setOpaque(true);
        STATUS_BAR.setBackground(Color.LIGHT_GRAY);

        // Layout of the panel is in border layout
        setLayout(new BorderLayout());
        add(STATUS_BAR, BorderLayout.SOUTH);
        // Account for statusBar height in overall height
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));

        // Create a new instance of the Board class
        BOARD = new Board();

        // Initialise the game board
        initGame();
    }

    public static void main(String[] args) {
        // Run GUI code in Event Dispatch thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create a main window to contain the panel
                JFrame frame = new JFrame(TITLE);

                // Create the new GameMain panel and add it to the frame
                GameMain gameMain = new GameMain();
                frame.add(gameMain);

                // Set the default close operation of the frame to exit_on_close
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    /**
     * Custom painting codes on this JPanel
     */
    public void paintComponent(Graphics g) {
        // Fill background and set colour to white
        super.paintComponent(g);
        setBackground(Color.WHITE);
        // Ask the game board to paint itself
        BOARD.paint(g);

        // Set status bar message
        if (currentState == GameState.Playing) {
            STATUS_BAR.setForeground(Color.BLACK);
            if (currentPlayer == Player.Cross) {
                // Display 'X's Turn' on the status bar
                STATUS_BAR.setText("X's Turn");
            } else {
                // Display 'O's Turn' on the status bar
                STATUS_BAR.setText("O's Turn");
            }
        } else if (currentState == GameState.Draw) {
            STATUS_BAR.setForeground(Color.RED);
            STATUS_BAR.setText("It's a Draw! Click to play again.");
        } else if (currentState == GameState.Cross_won) {
            STATUS_BAR.setForeground(Color.RED);
            STATUS_BAR.setText("'X' Won! Click to play again.");
        } else if (currentState == GameState.Nought_won) {
            STATUS_BAR.setForeground(Color.RED);
            STATUS_BAR.setText("'O' Won! Click to play again.");
        }
    }

    /**
     * Initialise the game-board contents and the current status of GameState and Player
     */
    public void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // All cells empty
                BOARD.cells[row][col].content = Player.Empty;
            }
        }
        currentState = GameState.Playing;
        currentPlayer = Player.Cross;
    }

    /**
     * After each turn check to see if the current player hasWon by putting their symbol in that position,
     * If they have the GameState is set to won for that player
     * If no winner then isDraw is called to see if deadlocked, if not GameState stays as PLAYING
     */
    public void updateGame(Player thePlayer, int row, int col) {
        // Check for win after play
        if (BOARD.hasWon(thePlayer, row, col)) {
            // Check which player has won and update the currentState to the appropriate gameState for the winner
            if (thePlayer == Player.Cross) {
                currentState = GameState.Cross_won;
            } else {
                currentState = GameState.Nought_won;
            }
        } else if (BOARD.isDraw()) {
            // Set the currentState to the draw gameState
            currentState = GameState.Draw;
        }
        // Otherwise no change to current state of playing
    }

    /**
     * Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
     * UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
     * If win or Draw then call is made to method that resets the game board. Finally, a call is made to refresh the canvas so that new symbol appears
     */

    public void mouseClicked(MouseEvent e) {
        // Get the coordinates of where the click event happened
        int mouseX = e.getX();
        int mouseY = e.getY();
        // Get the row and column clicked
        int rowSelected = mouseY / CELL_SIZE;
        int colSelected = mouseX / CELL_SIZE;
        if (currentState == GameState.Playing) {
            if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && BOARD.cells[rowSelected][colSelected].content == Player.Empty) {
                // Move
                BOARD.cells[rowSelected][colSelected].content = currentPlayer;
                // Update currentState
                updateGame(currentPlayer, rowSelected, colSelected);
                // Switch player
                if (currentPlayer == Player.Cross) {
                    currentPlayer = Player.Nought;
                } else {
                    currentPlayer = Player.Cross;
                }
            }
        } else {
            // Game over and restart
            initGame();
        }

        // Redraw the graphics on the UI
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Auto-generated, event not used

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Auto-generated, event not used

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Auto-generated,event not used

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Auto-generated, event not used

    }

    // The different game states
    private enum GameState {
        Playing, Draw, Cross_won, Nought_won
    }
}
