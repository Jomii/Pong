import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/* TODO:
    - Add sound. 
*/

/**
 * A classic Pong game with an option
 * to play a 2-player game or VS. AI.
 * 
 * Controls:
 * P-1: w and s keys.
 * P-2: up and down -arrowkeys.
 * P to pause.
 * Esc to go back to main menu.
 */
@SuppressWarnings("serial")
public class Pong extends JFrame {
    final static int HEIGHT = 500;
    final static int WIDTH = 700;
    final int fps = 60;
    public static boolean isRunning = false;
    boolean paused = false;
    public static boolean menu = true;
    static Paddle p1;
    static Paddle p2;
    public static boolean isAI = true;
    static int score1 = 0;
    static int score2 = 0;
    static int ballX = WIDTH / 2;
    static int ballY = HEIGHT / 2;
    static int speed = 0;
    boolean toLeft = true;

    /* Image drawn from this buffer to prevent flickering. */
    BufferedImage backBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    /*
     * Insets so that the canvas is being sized correctly and doesnt include e.g.
     * titlebar.
     */
    Insets insets;
    InputHandler input;
    AI ai;

    public static void main(String[] args) {
        Pong game = new Pong();
        game.run();
        System.exit(0);
    }

    /* Starts the game and runs it in a loop. */
    public void run() {
        initialize();

        while (isRunning) {
            if (menu) {
                Graphics g = getGraphics();

                Graphics bbg = this.backBuffer.getGraphics();

                /* Clear the board. */
                bbg.setColor(Color.BLACK);
                bbg.fillRect(0, 0, WIDTH, HEIGHT);
                /* Draw buttons */
                bbg.setColor(Color.WHITE);
                bbg.drawString("PONG", WIDTH / 2 - 15, 50);
                bbg.translate(WIDTH / 2, HEIGHT / 3);
                bbg.drawRect(-20, -20, 40, 30);
                bbg.drawString("VS. AI", -15, 0);

                bbg.drawRect(-20, 60, 40, 30);
                bbg.drawString("2-P", -10, 80);

                bbg.drawRect(-20, 140, 40, 30);
                bbg.drawString("QUIT", -15, 160);
                g.drawImage(backBuffer, this.insets.left, this.insets.top, this);
            } else {

                long time = System.currentTimeMillis();

                update();
                draw();

                time = (1000 / this.fps) - (System.currentTimeMillis() - time);

                if (time > 0) {
                    try {
                        Thread.sleep(time);
                    } catch (Exception e) {
                    }
                }

            }

        }

        setVisible(false);
    }

    static void reset() {
        p1.reset();
        p2.reset();
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        speed = 0;
        score1 = 0;
        score2 = 0;
    }

    /* Will set up everything the game needs to run. */
    void initialize() {
        setTitle("Pong");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true); // Determines what the insets are.
        isRunning = true;

        this.insets = getInsets();
        setSize(this.insets.left + WIDTH + this.insets.right, this.insets.top + HEIGHT + this.insets.bottom);
        this.input = new InputHandler(this);
        this.p1 = new Paddle(10, HEIGHT / 2 - 25, HEIGHT);
        this.p2 = new Paddle(WIDTH - 20, HEIGHT / 2 - 25, HEIGHT);
        this.ai = new AI(WIDTH, HEIGHT);
    }

    /* Check for input, move things and check for win conditions. */
    void update() {
        if (this.input.isKeyDown(KeyEvent.VK_P)) {
            paused = !paused;
            /* Set a delay so the game is not instantly unpaused. */
            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
        /* Only update gamestate if game is not paused. */
        if (!paused) {
            if (this.input.isKeyDown(KeyEvent.VK_ESCAPE)) {
                menu = true;
            }

            moveBall();
            if (isAI) {
                ai.setBallLocation(ballX, ballY);
                p2.move(ai.movePaddle(p2.getY()));
            }

            if (this.input.isKeyDown(KeyEvent.VK_W)) {
                p1.move(-5);
            }

            if (this.input.isKeyDown(KeyEvent.VK_S)) {
                /* Take into account the size of the paddle (50) */
                if (p1.getY() + 50 < HEIGHT) {
                    p1.move(5);
                }
            }

            if (this.input.isKeyDown(KeyEvent.VK_UP)) {
                p2.move(-5);
            }

            if (this.input.isKeyDown(KeyEvent.VK_DOWN)) {
                if (p2.getY() + 50 < HEIGHT) {
                    p2.move(5);
                }
            }

            if (ballX < 0 || ballX > WIDTH) {
                if (ballX < 0) {
                    score2++;
                } else {
                    score1++;
                }

                /* Reset the location of the ball. */
                ballX = WIDTH / 2;
                ballY = HEIGHT / 2;
                speed = 0;

            }

        }

    }

    /* Draws everything. */
    void draw() {
        Graphics g = getGraphics();

        Graphics bbg = this.backBuffer.getGraphics();

        /* Clear the board. */
        bbg.setColor(Color.BLACK);
        bbg.fillRect(0, 0, WIDTH, HEIGHT);

        bbg.setColor(Color.WHITE);
        /* Draw score. */
        if (paused) {
            bbg.drawString("PAUSED", WIDTH / 2 - 10, 15);
        } else {
            bbg.drawString("SCORE", WIDTH / 2 - 10, 15);
            bbg.drawString(Integer.toString(score1), WIDTH / 2 - 20, 30);
            bbg.drawString(Integer.toString(score2), WIDTH / 2 + 40, 30);
        }

        /* Draw paddles and ball. */
        p1.draw(bbg);
        p2.draw(bbg);
        bbg.fillOval(ballX, ballY, 10, 10);

        g.drawImage(backBuffer, this.insets.left, this.insets.top, this);
    }

    void moveBall() {
        /* Change direction of ball if hitting the paddle. */
        if (ballX == p1.getX() + 5 && Math.abs((ballY + 5) - (p1.getY() + 25)) <= 25) {
            this.toLeft = false;
            /* Adjust the vertical speed of the ball. The further the ball is
                from the center of the paddle the more speed it gets. */
            speed = ((ballY + 5) - (p1.getY() + 25)) / 5;
        } else if (ballX == p2.getX() - 5 && (Math.abs((ballY + 5) - (p2.getY() + 25)) <= 25)) {
            this.toLeft = true;
            speed = ((ballY + 5) - (p2.getY() + 25)) / 5;
        }

        /* Move the ball to the left or to the right depending on the direction. */
        if (this.toLeft) {
            ballX -= 5;
        } else {
            ballX += 5;
        }

        /* Change direction of ball if hitting the top or bottom */
        if (ballY <= 0 || ballY >= HEIGHT - 10) {
            speed = -speed;
        }

        /* Add vertical movement */
        ballY += speed;

    }

}