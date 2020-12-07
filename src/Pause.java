import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Page that is displayed when the pause button (see Play) is clicked. Shows the current score and
 * level, and includes buttons for resuming, restarting, or returning to the main menu (which quits
 * the current game).
 */
@SuppressWarnings("serial")
public class Pause extends Page {  
    private Play play;
    
    private JLabel scoreLabel;
    private JLabel levelLabel;
    
    /*
     * Sets up the title, score and level labels, and resume/restart/main menu buttons.
     */
    Pause(Play play) {
        this.play = play;
        
        setLayout(new GridLayout(5, 1));
        
        makeLabel("Game Paused", FONT_SIZE_LARGE, null, SwingConstants.CENTER);
        Page gameState = new Page();
        gameState.setLayout(new GridLayout(1, 2));
        scoreLabel = gameState.makeLabel("Score: " + play.getScore(), FONT_SIZE_MEDIUM, null, 
                SwingConstants.CENTER);        
        levelLabel = gameState.makeLabel("Level: " + play.getLevel(), FONT_SIZE_MEDIUM, null, 
                SwingConstants.CENTER);
        add(gameState);
        
        JButton resume = makeButton("Resume", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        JButton restart = makeButton("Restart", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        JButton mainMenu = makeButton("Main Menu", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        
        resume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.showCard("Play");
                play.toggleCourt();
            }
        });
        
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove();
                Game.addCard(new Play(), "Play");
                Game.showCard("Play");
            }
        });
        
        mainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.showCard("Menu");
                remove();
            }
        });
    }
    
    /*
     * Removes the current Pause and Play cards from the overall collection of cards. Used when
     * restarting the game or exiting to the main menu.
     */
    private void remove() {
        Game.removeCard(this);
        Game.removeCard(play);
    }
    
    /*
     * Repaints the score and level labels.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        scoreLabel.setText("Score: " + play.getScore());
        levelLabel.setText("Level: " + play.getLevel());
    }
}
