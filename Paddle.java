import java.awt.*;
import java.awt.image.BufferedImage;

public class Paddle {
    private int x;
    private int y;
    private int height;

    public Paddle(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    public void move(int value) {
        /* Only move the paddle when it would still remain inside the games borders. */
        if (y + 50 + value < height && y + value > 0 && y - value < height) {
            y += value;
        }
    }

    /* Return paddle to the center. */
    public void reset() {
        this.y = height / 2 - 25;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    public void draw(Graphics bbg) {
        bbg.setColor(Color.WHITE);
        bbg.fillRect(x, y, 10, 50);
    }
}