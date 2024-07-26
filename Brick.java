import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Brick {
    int x, y, width, height;
    Color color;
    boolean isBroken;

    public Brick(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.isBroken = false;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
