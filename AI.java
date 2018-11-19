public class AI {
    int HEIGHT;
    int WIDTH;
    int ballX;
    int ballY;
    int vision;

    public AI(int height, int width) {
        this.HEIGHT = height;
        this.WIDTH = width;
        this.vision = width / 2;
    }

    public void setBallLocation(int x, int y) {
        this.ballX = x;
        this.ballY = y;
    }

    /* TODO: Fix jitter when moving the paddle.
            - Fix ai going beyond bottom border. */
    public int movePaddle(int y) {
        if (ballX >= vision) {

            if (ballY >= y + 25 + 3) {
                if (y + 50 + 3 > this.HEIGHT) {

                } else {
                    return 3;
                }
            } else if (ballY <= y + 25 - 3) {
                return -3;
            }
        }

        return 0;
    }
}