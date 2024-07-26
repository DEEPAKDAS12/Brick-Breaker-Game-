import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener, MouseListener {
    private boolean play = false;
    private boolean gameStarted = false; // Flag for game start
    private boolean showNextLevelMessage = false;
    private int score = 0;

    private int totalBricks;
    private LevelManager levelManager;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir;
    private int ballYdir;

    private MapGenerator map;

    private SoundManager soundManager;

    public Gameplay() {
        // Initialize LevelManager with total number of levels
        levelManager = new LevelManager(3); // Example with 3 levels

        // Initialize the map with the current level's bricks
        map = new MapGenerator(levelManager.getBricks().length, levelManager.getBricks()[0].length);
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Initialize the game timer
        timer = new Timer(delay, this);
        timer.start();

        soundManager = new SoundManager();

        // Initialize ball speed based on the current level
        int ballSpeed = levelManager.getBallSpeed();
        ballXdir = -ballSpeed;
        ballYdir = -ballSpeed;

        // Calculate total bricks for the current level
        totalBricks = levelManager.getBricks().length * levelManager.getBricks()[0].length;

        // Adding a shutdown hook to print goodbye message
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Thank you for playing! Come Back Again Bye!");
        }));
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Drawing map
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // The paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // The ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        // Introduction screen
        if (!gameStarted) {
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Brick Breaker", 250, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Arrow Key to Start", 230, 350);
            g.drawString("Use Left/Right Arrow keys to move the paddle", 150, 400);
            g.drawString("Hit the bricks with the ball to break them", 180, 450);
            return; // Do not continue painting the rest
        }

        // Game Over
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: " + score, 190, 300);

            soundManager.playSound("C:\\Users\\macha\\OneDrive\\Desktop\\bricks_smasher\\game_over.wav");

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // Victory
        if (totalBricks <= 0) {
            if (levelManager.getCurrentLevel() == levelManager.totalLevels) {
                play = false;
                ballXdir = 0;
                ballYdir = 0;
                g.setColor(Color.green);
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("You Won, Scores: " + score, 190, 300);

                soundManager.playSound("C:\\Users\\macha\\OneDrive\\Desktop\\bricks_smasher\\victory.wav");

                g.setFont(new Font("serif", Font.BOLD, 20));
                g.drawString("Press Enter to Restart", 230, 350);
            } else {
                play = false;
                showNextLevelMessage = true;
                g.setColor(Color.green);
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("Level " + levelManager.getCurrentLevel() + " Complete!", 190, 300);

                g.setFont(new Font("serif", Font.BOLD, 20));
                g.drawString("Press Enter for Next Level", 230, 350);
            }
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            // Ball and paddle collision detection
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
                soundManager.playSound("C:\\Users\\macha\\OneDrive\\Desktop\\bricks_smasher\\hit.wav");
            }

            // Ball and brick collision detection
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            soundManager.playSound("C:\\Users\\macha\\OneDrive\\Desktop\\bricks_smasher\\break.wav"); // Play break sound

                            // Ball direction change on collision
                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }

            // Ball movement
            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Right key pressed
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }

            // Start the game if it hasn't started yet
            if (!gameStarted) {
                gameStarted = true;
                play = true;
            }
        }

        // Left key pressed
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }

            // Start the game if it hasn't started yet
            if (!gameStarted) {
                gameStarted = true;
                play = true;
            }
        }

        // Enter key pressed
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                if (showNextLevelMessage) {
                    // Move to the next level
                    levelManager.nextLevel();
                    map = new MapGenerator(levelManager.getBricks().length, levelManager.getBricks()[0].length);
                    ballposX = 120;
                    ballposY = 350;
                    int ballSpeed = levelManager.getBallSpeed();
                    ballXdir = -ballSpeed;
                    ballYdir = -ballSpeed;
                    playerX = 310;
                    totalBricks = levelManager.getBricks().length * levelManager.getBricks()[0].length;
                    showNextLevelMessage = false;
                } else {
                    // Restart the game
                    ballposX = 120;
                    ballposY = 350;
                    ballXdir = -1;
                    ballYdir = -2;
                    playerX = 310;
                    score = 0;
                    totalBricks = levelManager.getBricks().length * levelManager.getBricks()[0].length;
                    map = new MapGenerator(levelManager.getBricks().length, levelManager.getBricks()[0].length);

                }
                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
