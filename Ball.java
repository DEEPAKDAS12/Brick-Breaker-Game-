import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ball {
    int x, y, diameter, xDir, yDir;
    Color color;

    public Ball(int x, int y, int diameter, int xDir, int yDir, Color color) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.xDir = xDir;
        this.yDir = yDir;
        this.color = color;
    }

    public void move() {
        x += xDir;
        y += yDir;
        if (x <= 0 || x >= 800 - diameter) {
            xDir = -xDir;
        }
        if (y <= 0) {
            yDir = -yDir;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, diameter, diameter);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public int getY() {
        return y;
    }
}
