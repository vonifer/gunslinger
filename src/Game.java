import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

/**
 * Main class for running the game. Defines the frame and creates a CardLayout for the GUI.
 * Only two cards (Menu and Instructions) are created first, but more are added later.
 */

public class Game implements Runnable {
    private static JFrame frame = new JFrame("Gunslinger");
    private static JPanel cards = new JPanel(new CardLayout());
    
    /*
     * Runs the application and sets up the frame and CardLayout.
     */
    public void run() {
        frame.setLocation(360, 50);
        frame.add(cards);
        
        addCard(new Menu(), "Menu");
        addCard(new Instructions(), "Instructions");
        
        frame.setPreferredSize(new Dimension(650, 650));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        showCard("Menu");
    }
    
    /*
     * Shows a certain card contained within the CardLayout.
     */
    public static void showCard(String name) {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, name);
    }
    
    public static void addCard(JPanel panel, String name) {
        cards.add(panel, name);
    }
    
    public static void removeCard(JPanel panel) {
        cards.remove(panel);
    }
    
    /*
     * Closes the window. (used for the Quit button in Menu)
     */
    public static void quit() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
    
    /*
     * Invokes the Game constructor to begin the game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
