import java.awt.Component;
import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener {
    boolean[] keys;

    public InputHandler(Component c) {
        this.keys = new boolean[256];
        c.addKeyListener(this);
        c.addMouseListener(this);
    }

    public boolean isKeyDown(int keyCode) {
        if (keyCode > 0 && keyCode < 256) {
            return keys[keyCode];
        }

        return false;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() > 0 && e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() > 0 && e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = false;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        if (e.getX() > Pong.WIDTH / 2 - 30 && e.getX() < Pong.WIDTH / 2 + 30) {

            if (Pong.menu) {
                /* Turn AI on and close menu. */
                if (e.getY() > Pong.HEIGHT / 3 + 15 && e.getY() < Pong.HEIGHT / 3 + 65) {
                    Pong.isAI = true;
                    Pong.reset();
                    Pong.menu = false;
                }
                /* Turn AI off and close menu. */
                if (e.getY() > Pong.HEIGHT / 3 + 95 && e.getY() < Pong.HEIGHT / 3 + 145) {
                    Pong.isAI = false;
                    Pong.reset();
                    Pong.menu = false;
                }
                /* Exits the game. */
                if (e.getY() > Pong.HEIGHT / 3 + 175 && e.getY() < Pong.HEIGHT / 3 + 225) {
                    System.out.println("exit pressed");
                    Pong.isRunning = false;
                }
            }

        }

    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}