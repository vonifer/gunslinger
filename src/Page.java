import java.awt.*;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/*
 * Extension of JPanel that provides various useful static fields and methods.
 * 
 * In this game, every "card" that is shown is an extension of Page. The cards in this game are
 * Menu, Instructions, Leaderboard, Play, Pause, and GameOver. Pages are also sometimes used as 
 * components of these cards in order to facilitate button/label creation.
 */
@SuppressWarnings("serial")
public class Page extends JPanel {    
    public static final int BORDER_SPACE = 10;
    public static final String DEFAULT_FONT = "Rockwell";
    public static final int FONT_SIZE_LARGE = 80;
    public static final int FONT_SIZE_MEDIUM = 50;
    public static final int FONT_SIZE_SMALL = 30;
    public static final Dimension BUTTON_NORMAL = new Dimension(400, 80);
    public static final Dimension BUTTON_SMALL = new Dimension(120, 40);
    
    /*
     * Adds a JPanel enclosing a single JButton to the invoking Page.
     */
    public JButton makeButton(String name, String font, 
            int fontSize, Object constraints, Dimension buttonSize) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(
                BORDER_SPACE, BORDER_SPACE, BORDER_SPACE, BORDER_SPACE));
        
        JButton b = new JButton(name);
        b.setFont(new Font(font, Font.PLAIN, fontSize));
        
        b.setPreferredSize(buttonSize);
        
        panel.add(b);
        if (constraints != null) {
            add(panel, constraints);
        } else {
            add(panel);
        }
        
        return b;
    }
    
    /*
     * Uses default font if font argument is not given.
     */
    public JButton makeButton(String name, int fontSize, Object constraints, Dimension buttonSize) {
        return makeButton(name, DEFAULT_FONT, fontSize, constraints, buttonSize);
    }
    
    /*
     * Adds a JPanel enclosing a single JLabel to the invoking Page.
     */
    public JLabel makeLabel(String name, String font, 
            int fontSize, Object constraints, int alignment) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(
                BORDER_SPACE, BORDER_SPACE, BORDER_SPACE, BORDER_SPACE));
        
        JLabel l = new JLabel(name, alignment);
        l.setFont(new Font(font, Font.PLAIN, fontSize));
                
        panel.add(l);
        
        if (constraints != null) {
            add(panel, constraints);
        } else {
            add(panel);
        }
        
        return l;
    }
    
    /*
     * Uses default font if font argument is not given.
     */
    public JLabel makeLabel(String name, int fontSize, Object constraints, int alignment) {
        return makeLabel(name, DEFAULT_FONT, fontSize, constraints, alignment);
    }
    
    /*
     * Adds a JPanel enclosing multiple JLabels to the invoking Page. The default font is assumed.
     */
    public List<JLabel> makeLabels(List<String> lines, int fontSize, 
            Object constraints, int alignment) {
        JPanel panel = new JPanel(new GridLayout(lines.size(), 1));
        panel.setBorder(BorderFactory.createEmptyBorder(
                BORDER_SPACE, BORDER_SPACE, BORDER_SPACE, BORDER_SPACE));
        List<JLabel> labels = new LinkedList<>();
        
        for (String line : lines) {
            JLabel l = new JLabel(line, alignment);
            l.setFont(new Font(DEFAULT_FONT, Font.PLAIN, fontSize));
            panel.add(l);
            labels.add(l);
        }
        
        if (constraints != null) {
            add(panel, constraints);
        } else {
            add(panel);
        }
        
        return labels;
    }
}
