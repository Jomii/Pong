public class AI {
    int HEIGHT;
    int WIDTH;
    int y;
    int ballX;
    int ballY;
    int vision;

    public AI(int height, int width, int y) {
        this.HEIGHT = height;
        this.WIDTH = width;
        this.y = y;
        this.vision = width / 2;
    }

    public void setBallLocation(int x, int y) {
        this.ballX = x;
        this.ballY = y;
    }

    public void move() {
        if (ballX >= vision) {

            if (ballY >= y + 5) {
                if(y + 25 + 3 >= this.HEIGHT) {
                    y = this.HEIGHT - 25;
                } else {
                y += 3;
            }
            } else if (ballY <= y - 5) {
                if (y - 3 <= 25) {
                    y = 25;
                } else {
                    y -= 3;
                }
            }
        }
    }
}