import javax.swing.JFrame;

public class GamePanel extends JFrame {
    public GamePanel() {
        Gameplay gamePlay = new Gameplay();
        add(gamePlay);
        setTitle("Brick Breaker Game");
        setBounds(10, 10, 700, 600);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
