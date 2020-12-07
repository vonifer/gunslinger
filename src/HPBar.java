import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

/*
 * Contains the HP bar. Used in the Play page.
 */
@SuppressWarnings("serial")
public class HPBar extends JComponent {
    
    private final Court court;
    private static final int BAR_WIDTH = 300;
    private static final int BAR_HEIGHT = 40;
    
    /*
     * Creates the JComponent containing the HP bar.
     */
    HPBar(Court court) {
        this.court = court;
        setPreferredSize(new Dimension(BAR_WIDTH, BAR_HEIGHT));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    /*
     * Draws the HP bar. If the HP is less than 100, the rest of the bar is colored pink.
     * 
     * The HP part of the bar is colored green if the HP is above 50, yellow if the HP is between
     * 26 and 50 (inclusive), and red if the HP is 25 or less.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int cutoff = BAR_WIDTH * court.getPlayerCopy().getHP() / Player.HP;
        
        g.setColor(Color.PINK);
        g.fillRect(0, 0, BAR_WIDTH, BAR_HEIGHT);
        if (cutoff > 0.5 * BAR_WIDTH) {
            g.setColor(Color.GREEN);
        } else if (cutoff > 0.25 * BAR_WIDTH) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        if (cutoff > 0) {
            g.fillRect(0, 0, cutoff, BAR_HEIGHT);
        }
    }
    
}
