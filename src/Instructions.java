import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.LinkedList;
import java.util.List;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * Page that contains the instructions. The instructions are loaded from the files/instruction.txt 
 * file.
 */
@SuppressWarnings("serial")
public class Instructions extends Page {
    
    private final static int LINE_LENGTH = 47;
    private final static String PATH_TO_INSTRUCTIONS = "files/instructions.txt";
    private final static int FONT_SIZE_INSTRUCTIONS = 26;
    
    /*
     * Sets up the title, instructions, and main menu button.
     */
    Instructions() {
        setLayout(new BorderLayout());
        
        makeLabel("Instructions", FONT_SIZE_LARGE, BorderLayout.NORTH, SwingConstants.CENTER);
        
        // Creates a list of "lines" to be displayed, using the getInstructions and splitEqually
        // helper methods (along with the appropriate character limit)
        List<String> text = splitEqually(getInstructions(), LINE_LENGTH);
        text.add("Controls: WASD - move, click - shoot");
        
        makeLabels(text, FONT_SIZE_INSTRUCTIONS, 
                BorderLayout.CENTER, SwingConstants.LEFT);
        
        JButton mainMenu = makeButton("Main Menu", FONT_SIZE_MEDIUM, BorderLayout.SOUTH, 
                BUTTON_NORMAL);
        mainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.showCard("Menu");
            }
        });
    }
    
    /*
     * Returns the given String as a list of substrings; the length of each substring is at most the
     * given character limit. Each substring only contains whole words, so any word that cannot be 
     * entirely appended to a substring is instead used to begin the next substring.
     */
    private static List<String> splitEqually(String text, int limit) {
        String[] words = text.split(" ");
        
        List<String> lines = new LinkedList<>();
        String currentLine = "";
        for (String word : words) {
            if (word.length() > limit) {
                System.out.println("Error: The word \"" + word + "\" is longer than the given "
                        + "limit.");
                return lines;
            }
            if (currentLine.length() == 0) {
                currentLine = word;
            } else if (currentLine.length() + word.length() <= limit) {
                currentLine += (" " + word);
                if (word.equals(words[words.length - 1])) {
                    lines.add(currentLine);
                }
            } else {
                lines.add(currentLine);
                currentLine = word;
            }
        }
        return lines;
    }
    
    /*
     * Reads the instructions.txt file and returns its contents as a single String.
     */
    private String getInstructions() {
        try {
            Path path = FileSystems.getDefault().getPath(PATH_TO_INSTRUCTIONS);
            List<String> allLines = Files.readAllLines(path);
            String instructions = "";
            for (String s : allLines) {
                instructions += s;
            }
            return instructions;
        } catch (IOException e) {
            return "There was an error opening the instructions file.";
        }
    }
}
