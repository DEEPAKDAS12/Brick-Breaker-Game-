import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Paddle {
    int x, y, width, height, xDir;
    Color color;

    public Paddle(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.xDir = 0;
    }

    public void move() {
        x += xDir;
        if (x <= 0) {
            x = 0;
        }
        if (x >= 800 - width) {
            x = 800 - width;
        }
    }

    public void setXDir(int xDir) {
        this.xDir = xDir;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
