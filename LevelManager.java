import java.awt.Color;

public class LevelManager {
    private int currentLevel;
    public final int totalLevels;

    public LevelManager(int totalLevels) {
        this.totalLevels = totalLevels;
        this.currentLevel = 1;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void nextLevel() {
        if (currentLevel < totalLevels) {
            currentLevel++;
        }
    }

    public Brick[][] getBricks() {
        int rows = 3 + currentLevel;
        int cols = 7;
        Brick[][] bricks = new Brick[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                bricks[i][j] = new Brick(j * 70 + 30, i * 30 + 50, 60, 20, Color.GREEN);
            }
        }
        return bricks;
    }

    public int getBallSpeed() {
        return 2 + currentLevel;
    }
}
