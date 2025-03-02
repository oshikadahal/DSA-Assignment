import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Block {
    int[][] shape;
    Color color;
    int x, y;

    // Constructor that accepts shape and color
    public Block(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 4;  // Start at the middle of the board
        this.y = 0;  // Start at the top of the board
    }

    // Rotate the block (90 degrees clockwise)
    public void rotate() {
        int n = shape.length;
        int[][] newShape = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newShape[i][j] = shape[n - j - 1][i];
            }
        }
        shape = newShape;
    }

    // Static method to randomly select one of the colors
    public static Color getRandomColor() {
        // Define possible colors: Lavender, Yellow, Orange
        Color[] colors = {new Color(230, 230, 250), Color.YELLOW, Color.ORANGE};
        Random rand = new Random();
        return colors[rand.nextInt(colors.length)];
    }
}

class GameBoard {
    private final int rows = 20;
    private final int cols = 10;
    private final int[][] board = new int[rows][cols];

    public boolean canMove(Block block) {
        for (int i = 0; i < block.shape.length; i++) {
            for (int j = 0; j < block.shape[i].length; j++) {
                if (block.shape[i][j] != 0) {
                    int newX = block.x + j;
                    int newY = block.y + i;
                    if (newX < 0 || newX >= cols || newY >= rows || (newY >= 0 && board[newY][newX] != 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void placeBlock(Block block) {
        for (int i = 0; i < block.shape.length; i++) {
            for (int j = 0; j < block.shape[i].length; j++) {
                if (block.shape[i][j] != 0) {
                    int newX = block.x + j;
                    int newY = block.y + i;
                    if (newY >= 0 && newY < rows && newX >= 0 && newX < cols) {
                        board[newY][newX] = 1;
                    }
                }
            }
        }
    }

    public int removeFullLines() {
        int linesRemoved = 0;
        for (int i = rows - 1; i >= 0; i--) {
            boolean full = true;
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesRemoved++;
                for (int k = i; k > 0; k--) {
                    System.arraycopy(board[k - 1], 0, board[k], 0, cols);
                }
                Arrays.fill(board[0], 0);
                i++; // Check the same row again after shifting
            }
        }
        return linesRemoved;
    }

    public void clear() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], 0);
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(j * 30, i * 30, 30, 30);
                }
            }
        }
    }
}

public class TetrisGame extends JPanel implements ActionListener, KeyListener {
    private final GameBoard gameBoard;
    private final Queue<Block> blockQueue;
    private Block currentBlock;
    private int score;
    private boolean gameOver;
    private javax.swing.Timer timer;

    public TetrisGame() {
        gameBoard = new GameBoard();
        blockQueue = new LinkedList<>();
        score = 0;
        gameOver = false;

        // Set up the initial queue with random blocks
        enqueueNewBlock();
        setPreferredSize(new Dimension(300, 600));
        setFocusable(true);
        addKeyListener(this);
        this.timer = new javax.swing.Timer(500, this);
        this.timer.start();
    }

    public void enqueueNewBlock() {
        int[][] shape = new int[][]{
            {1, 1, 1, 1}  // Example: A straight line block (O shape)
        };
        // Randomly choose a color (Lavender, Yellow, Orange)
        Color color = Block.getRandomColor();
        currentBlock = new Block(shape, color);
        blockQueue.offer(currentBlock);
    }

    public void moveDown() {
        currentBlock.y++;
        if (!gameBoard.canMove(currentBlock)) {
            currentBlock.y--;
            gameBoard.placeBlock(currentBlock);
            int linesRemoved = gameBoard.removeFullLines();
            score += linesRemoved * 100; // Update score based on lines removed
            enqueueNewBlock();
            if (!gameBoard.canMove(currentBlock)) {
                gameOver = true;
                timer.stop();
            }
        }
        repaint();
    }

    public void rotateBlock() {
        currentBlock.rotate();
        if (!gameBoard.canMove(currentBlock)) {
            currentBlock.rotate();
            currentBlock.rotate();
            currentBlock.rotate();
        }
        repaint();
    }

    public void moveLeft() {
        currentBlock.x--;
        if (!gameBoard.canMove(currentBlock)) {
            currentBlock.x++;
        }
        repaint();
    }

    public void moveRight() {
        currentBlock.x++;
        if (!gameBoard.canMove(currentBlock)) {
            currentBlock.x--;
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveDown();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            rotateBlock();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moveDown();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameBoard.draw(g);
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over! Score: " + score, 100, 300);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        TetrisGame game = new TetrisGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}