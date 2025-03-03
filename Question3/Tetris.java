import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Tetromino {
    int[][] shape;

    public Tetromino(int[][] shape) {
        this.shape = shape;
    }

    public int[][] rotate() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = shape[i][j];
            }
        }
        return rotated;
    }
}

public class Tetris extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int TILE_SIZE = 30;
    private javax.swing.Timer timer;
    private int currentX, currentY;
    private int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];
    private Tetromino currentTetromino;
    private Queue<Tetromino> nextBlocks = new LinkedList<>();
    private Random random = new Random();
    private int score = 0;
    private int level = 1;
    
    private final int[][][] TETROMINOES = {
            {{1, 1, 1, 1}},
            {{1, 1}, {1, 1}},
            {{0, 1, 0}, {1, 1, 1}},
            {{1, 1, 0}, {0, 1, 1}},
            {{0, 1, 1}, {1, 1, 0}},
            {{1, 0, 0}, {1, 1, 1}},
            {{0, 0, 1}, {1, 1, 1}}
    };

    public Tetris() {
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE + 150, BOARD_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        timer = new javax.swing.Timer(500, this);
        initGame();
    }

    private void initGame() {
        for (int i = 0; i < 3; i++) nextBlocks.add(new Tetromino(TETROMINOES[random.nextInt(TETROMINOES.length)]));
        newPiece();
        timer.start();
    }

    private void newPiece() {
        currentTetromino = nextBlocks.poll();
        nextBlocks.add(new Tetromino(TETROMINOES[random.nextInt(TETROMINOES.length)]));
        currentX = BOARD_WIDTH / 2 - currentTetromino.shape[0].length / 2;
        currentY = 0;
        if (!isValidMove(currentTetromino.shape, currentX, currentY)) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        }
    }

    private boolean isValidMove(int[][] shape, int x, int y) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= BOARD_WIDTH || newY < 0 || newY >= BOARD_HEIGHT || (newY >= 0 && board[newY][newX] == 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placePiece() {
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                if (currentTetromino.shape[i][j] == 1) {
                    board[currentY + i][currentX + j] = 1;
                }
            }
        }
        clearLines();
        newPiece();
    }

    private void clearLines() {
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean fullLine = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 0) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                for (int k = i; k > 0; k--) {
                    board[k] = board[k - 1].clone();
                }
                board[0] = new int[BOARD_WIDTH];
                score += 100;
                if (score % 500 == 0) {
                    level++;
                    timer.setDelay(Math.max(100, 500 - level * 50));
                }
                i++;
            }
        }
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (isValidMove(currentTetromino.shape, currentX - 1, currentY)) {
                    currentX--;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (isValidMove(currentTetromino.shape, currentX + 1, currentY)) {
                    currentX++;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (isValidMove(currentTetromino.shape, currentX, currentY + 1)) currentY++;
                break;
            case KeyEvent.VK_UP:
                int[][] rotated = currentTetromino.rotate();
                if (isValidMove(rotated, currentX, currentY)) currentTetromino.shape = rotated;
                break;
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isValidMove(currentTetromino.shape, currentX, currentY + 1)) {
            currentY++;
        } else {
            placePiece();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, BOARD_WIDTH * TILE_SIZE + 10, 20);
        g.drawString("Level: " + level, BOARD_WIDTH * TILE_SIZE + 10, 40);
        
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 1) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                if (currentTetromino.shape[i][j] == 1) {
                    g.setColor(new Color(230, 230, 250));
                    g.fillRect((currentX + j) * TILE_SIZE, (currentY + i) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        Tetris game = new Tetris();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}