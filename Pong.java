import InputHandler;
import Paddle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/* TODO:
    - Fix AI going out of bounds.
    - Fix AI stuttering movement at slow speeds.
    - Fix pausing and unpausing.
    - Extract the ball to its own class.
    - Add a main menu.
    - Add sound. 
*/

@SuppressWarnings("serial")
public class Pong extends JFrame {
    final int HEIGHT = 500;
    final int WIDTH = 700;
    final int fps = 60;
    boolean isRunning = false;
    boolean paused = false;
    Paddle p1;
    Paddle p2;
    int score1 = 0;
    int score2 = 0;

    int ballX = WIDTH / 2;
    int ballY = HEIGHT / 2;
    int speed = 0;
    boolean toLeft = true;

    /* Image drawn from this buffer to prevent flickering. */
    BufferedImage backBuffer = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_RGB);
    /* Insets so that the canvas is being sized correctly and doesnt include e.g. titlebar. */
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

        while(isRunning) {
            long time = System.currentTimeMillis();

            update();            
            draw();

            time = (1000 / this.fps) - (System.currentTimeMillis() - time);

            if (time > 0) {
                try {
                    Thread.sleep(time);
                } catch (Exception e) {}

            }

        }

        setVisible(false);
    }

    /* Will set up everything the game needs to run. */
    void initialize() {
        setTitle("Pong");
        setSize(this.WIDTH, this.HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true); // Determines what the insets are.
        this.isRunning = true;

        this.insets = getInsets();
        setSize(this.insets.left + this.WIDTH + this.insets.right, this.insets.top + this.HEIGHT + this.insets.bottom);
        this.input = new InputHandler(this);
        this.p1 = new Paddle(10, HEIGHT / 2 - 25, HEIGHT);
        this.p2 = new Paddle(WIDTH - 20, HEIGHT / 2 - 25, HEIGHT);
        this.ai = new AI(this.WIDTH, this.HEIGHT);
    }

    /* Check for input, move things and check for win conditions. */
    void update() {
        /* Keep listening to p key to unpause. */
        if (this.input.isKeyDown(KeyEvent.VK_P)) {
            if (paused) {
                paused = false;
            } else {
                paused = true;
            }
        }
        /* Only update gamestate if game not paused. */
        if (!paused) {  
                moveBall();
                ai.setBallLocation(this.ballX, this.ballY);
                p2.move(ai.movePaddle(p2.getY()));
                
            if (this.input.isKeyDown(KeyEvent.VK_W)) {
                p1.move(-5);
            }
            
            if (this.input.isKeyDown(KeyEvent.VK_S)) {
                // *Refactor this
                if (p1.getY() + 50 >= this.HEIGHT) {
                } else {
                    p1.move(5);
                }
            }
            
            if (this.input.isKeyDown(KeyEvent.VK_UP)) {
                p2.move(-5);
            }
            
            if (this.input.isKeyDown(KeyEvent.VK_DOWN)) {
                // *Refactor this
                if(p2.getY() + 50 >= this.HEIGHT) {

                } else {
                    p2.move(5);
                }
            }
            
            if (ballX < 0 || ballX > this.WIDTH) {
                if (ballX < 0) {
                    score2++;
                } else {
                    score1++;
                }
                
                /* Reset the location of ball. */
                ballX = this.WIDTH / 2;
                ballY = this.HEIGHT / 2;
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
        bbg.fillRect(0, 0, this.WIDTH, this.HEIGHT);

        bbg.setColor(Color.WHITE);
        /* Draw score. */
        bbg.drawString("SCORE", this.WIDTH / 2 - 10, 15);
        bbg.drawString(Integer.toString(score1), this.WIDTH / 2 - 20, 30);
        bbg.drawString(Integer.toString(score2), this.WIDTH / 2 + 40, 30);

        /* Draw paddles and ball. */
        p1.draw(bbg);
        p2.draw(bbg);
        bbg.fillOval(ballX, ballY, 10, 10);

        g.drawImage(backBuffer, this.insets.left, this.insets.top, this);
    }

    void moveBall() {
        /* Calculate the y axis difference of paddle 1 (y) and ball. */
        int difference = Math.abs((ballY + 5) - (p1.getY() + 25));
        
        /* Change direction of ball if hitting the paddle. */
        if (ballX == p1.getX() + 5 && difference <= 25) {
            this.toLeft = false;
            /* Adjust the vertical speed of the ball. Formula is difference / 5
                So when hitting the top half of paddle, the ball starts moving up. */
            speed = ((ballY + 5) - (p1.getY() + 25)) / 5;
        } else if (ballX == p2.getX() - 5 && (Math.abs((ballY + 5) - (p2.getY() + 25)) <= 25)){
            this.toLeft = true;
            speed = ((ballY + 5) - (p2.getY() + 25)) / 5;
        }

        if (this.toLeft) {
            ballX -= 5;
        } else {
            ballX += 5;
        }
        
        /* Change direction of ball if hitting the top or bottom */
        if (ballY <= 0 || ballY >= this.HEIGHT - 10) {
            speed = -speed;
        }

        /* Vertical movement */
        ballY += speed;

    }

}