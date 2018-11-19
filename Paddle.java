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
        if (y + value > 0 && y + value < height && y - value < height) {
            y += value;
        }
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