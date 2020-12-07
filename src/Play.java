import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Page that contains the game itself. The game is played on a Court, but this Page also contains
 * a time label (showing how many seconds are left before the next enemy wave starts), an options 
 * pane (showing the score, current level, and pause button), and an HP bar.
 */
@SuppressWarnings("serial")
public class Play extends Page {    
    
    private Court court;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel hpLabel;
    private JLabel timeLabel;
    
    /*
     * Sets up the Court, options pane, HP bar, and time left pane.
     */
    Play() {
        setLayout(new BorderLayout());
        
        createCourt();
        createOptions();
        createBottomBar();
        createTimeLeft();  
        
        court.toggle();
    }
    
    /*
     * Creates the Court, on which the game is played.
     */
    private void createCourt() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(
                BORDER_SPACE, 2 * BORDER_SPACE, BORDER_SPACE, BORDER_SPACE));
        court = new Court(this);
        panel.add(court);
        
        add(panel, BorderLayout.CENTER);
    }
    
    /*
     * Creates the options pane, which consists of the score, the current level, and a pause button.
     */
    private void createOptions() {
        Game.addCard(new Pause(this), "Pause");
        Page options = new Page();
        options.setPreferredSize(new Dimension(200, 400));
        options.setLayout(new GridLayout(3, 1));
        
        scoreLabel = options.makeLabel("Score: " + court.getScore(), FONT_SIZE_SMALL, 
                null, SwingConstants.CENTER);
        levelLabel = options.makeLabel("Level: " + court.getLevel(), FONT_SIZE_SMALL, 
                null, SwingConstants.CENTER);
        
        JButton pause = options.makeButton("Pause", FONT_SIZE_SMALL, null, BUTTON_SMALL);
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.toggle();
                Game.showCard("Pause");
            }
        });
        
        add(options, BorderLayout.EAST);
    }
    
    /*
     * Creates the HP bar, accompanied to the left by a label of the HP.
     */
    private void createBottomBar() {
        Page bottomBar = new Page();
        bottomBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        hpLabel = bottomBar.makeLabel("HP: " + court.getPlayerCopy().getHP() + " / " + Player.HP, 
                FONT_SIZE_SMALL, c, SwingConstants.CENTER);
        c.weightx = 1;
        
        JPanel hpPanel = new JPanel();
        hpPanel.add(new HPBar(court));
        hpPanel.setBorder(BorderFactory.createEmptyBorder(
                20, 20, 20, 20));
        bottomBar.add(hpPanel, c);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(
                BORDER_SPACE, BORDER_SPACE, 2 * BORDER_SPACE, BORDER_SPACE));
        
        add(bottomBar, BorderLayout.SOUTH);
    }
    
    /*
     * Creates the time left pane, displaying how many seconds are left before the next enemy wave
     * starts.
     */
    private void createTimeLeft() {
        Page timeLeft = new Page();
        timeLabel = timeLeft.makeLabel("Time until next wave: " + court.getTimeLeft(), 
                FONT_SIZE_SMALL, null, SwingConstants.CENTER);
        add(timeLeft, BorderLayout.NORTH);
    }
    
    /*
     * Getter method for the score.
     */
    public int getScore() {
        if (court != null) {
            return court.getScore();
        } else {
            return -1;
        }
    }
    
    /*
     * Getter method for the level.
     */
    public int getLevel() {
        if (court != null) {
            return court.getLevel();
        } else {
            return -1;
        }
    }
    
    /*
     * Switches the game state between active and inactive
     */
    public void toggleCourt() {
        court.toggle();
    }
    
    /*
     * Repaints the Court and labels.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        scoreLabel.setText("Score: " + court.getScore());
        levelLabel.setText("Level: " + court.getLevel());
        
        String extraSpace = "";
        int hp = court.getPlayerCopy().getHP();
        if (hp < 10) {
            extraSpace = "    ";
        } else if (hp < 100) {
            extraSpace = "  ";
        }
        hpLabel.setText("HP: " + extraSpace + hp + " / " + Player.HP);
        
        timeLabel.setText("Time until next wave: " + court.getTimeLeft());
    }

}
