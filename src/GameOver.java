import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
/*
 * Page that is displayed when the game has ended (i.e. the player runs out of HP). Shows the 
 * final score and level, and includes buttons for playing again or returning to the main menu.
 * If the score is sufficiently high, the updateLeaderboard method is called (see Leaderboard).
 * 
 * This page is very similar to the Pause page.
 */
@SuppressWarnings("serial")
public class GameOver extends Page {  
    private Play play;
    
    private static final int UPDATE_DELAY = 100;
    
    private JLabel scoreLabel;
    private JLabel levelLabel;
    
    /*
     * Sets up the title, score and level labels, and play again/main menu buttons.
     */
    GameOver(Play play) {
        this.play = play;
        
        setLayout(new GridLayout(5, 1));
        
        makeLabel("Game Over!", FONT_SIZE_LARGE, null, SwingConstants.CENTER);
        
        Page gameState = new Page();
        gameState.setLayout(new GridLayout(1, 2));
        scoreLabel = gameState.makeLabel("Score: " + play.getScore(), FONT_SIZE_MEDIUM, null, 
                SwingConstants.CENTER);        
        levelLabel = gameState.makeLabel("Level: " + play.getLevel(), FONT_SIZE_MEDIUM, null, 
                SwingConstants.CENTER);
        add(gameState);
        
        JButton playAgain = makeButton("Play Again", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        JButton mainMenu = makeButton("Main Menu", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        
        // Calls updateLeaderboard after some delay (so that the other parts of the page can load).
        GameOver go = this;
        Timer timer = new Timer(UPDATE_DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leaderboard.updateLeaderboard(play.getScore(), play.getLevel(), go);
            }
        });
        timer.setRepeats(false);
        timer.start();
        
        playAgain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove();
                Game.addCard(new Play(), "Play");
                Game.showCard("Play");
            }
        });
        
        mainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove();
                Game.showCard("Menu");
            }
        });
    }
    
    /*
     * Removes the current Game Over and Play cards from the overall collection of cards. Used when
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
