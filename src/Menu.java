import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Page that contains the menu, from which the user may play the game, view the instructions, check
 * the current leaderboard, or quit.
 */
@SuppressWarnings("serial")
public class Menu extends Page {   
    
    /*
     * Sets up the title and four buttons with appropriate action listeners.
     * 
     * Note that clicking "Play" starts a new game (i.e. new instance of Play), and likewise for
     * "Leaderboard." This is not the case for "Instructions" as that Page stays constant.
     */
    Menu() {        
        setLayout(new GridLayout(5, 1));
        
        makeLabel("Gunslinger", "Cooper Black", FONT_SIZE_LARGE, null, SwingConstants.CENTER);
        
        JButton play = makeButton("Play", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        JButton instr = makeButton("Instructions", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        JButton lb = makeButton("Leaderboard", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        JButton quit = makeButton("Quit", FONT_SIZE_MEDIUM, null, BUTTON_NORMAL);
        
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.addCard(new Play(), "Play");
                Game.showCard("Play");
            }
        });
        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.showCard("Instructions");
            }
        });
        lb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.addCard(new Leaderboard(), "Leaderboard");
                Game.showCard("Leaderboard");
            }
        });
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.quit();
            }
        });
        
    }
}
