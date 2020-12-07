import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/*
 * Page that contains the top 10 scores, along with the players' names, levels reached, and date
 * of achievement. This information is stored in the leaderboard.txt file in a "~"-separated format.
 */
@SuppressWarnings("serial")
public class Leaderboard extends Page {    
    
    private static final String PATH_TO_LEADERBOARD = "files/leaderboard.txt";
    private static final int FONT_SIZE_LEADERBOARD = 26;
    
    /*
     * Sets up the title, scores and main menu button.
     * 
     * The scores Page (used to display the top 10 scores) uses a GridBagLayout so that the first
     * and last (fourth) columns of the "table" take up the most space.
     */
    Leaderboard() {
        setLayout(new BorderLayout());
        
        makeLabel("Leaderboard", FONT_SIZE_LARGE, BorderLayout.NORTH, SwingConstants.CENTER);
        
        // Sets up the scores page with the appropriate constraints.
        Page scoresPage = new Page();
        scoresPage.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int[] weights = new int[]{1, 0, 0, 1};
        
        // Displays the leaderboard column-by-column. 
        // The columns are (from left to right): Name, Score, Level, and Date.
        for (List<String> col : getScores()) {
            c.weightx = (double) weights[getScores().indexOf(col)];
            scoresPage.makeLabels(col, FONT_SIZE_LEADERBOARD, c, SwingConstants.CENTER);
        }
        
        add(scoresPage, BorderLayout.CENTER);
        
        JButton mainMenu = makeButton("Main Menu", FONT_SIZE_MEDIUM, 
                BorderLayout.SOUTH, BUTTON_NORMAL);
        mainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.showCard("Menu");
            }
        });
    }
    
    /*
     * For a List containing m Lists of n elements, returns the transpose of the top-level List,
     * i.e. a List containing n Lists of m elements.
     * 
     * This is used to convert the leaderboard table from a list of its rows (as obtained from the 
     * file) to a list of its columns (which is how it is displayed in the GUI).
     */
    private static <T> List<List<T>> transposeList(List<List<T>> rows) {
        int size = rows.get(0).size();
        List<List<T>> columns = new ArrayList<List<T>>();
        
        for (int i = 0; i < size; i++) {
            List<T> column = new ArrayList<T>();
            for (List<T> row : rows) {
                column.add(row.get(i));
            }
            columns.add(column);
        }
        
        return columns;
    }
    
    /*
     * Reads the leaderboard.txt file and returns its contents as a List of List of Strings.
     * Each List<String> element represents a row of the file, containing the fields for a single
     * score entry (i.e. Name, Score, Level, Date). 
     */
    private static List<List<String>> getScores() {
        List<List<String>> rows = new ArrayList<>();
        
        try {
            Path path = FileSystems.getDefault().getPath(PATH_TO_LEADERBOARD);
            List<String> allLines = Files.readAllLines(path);
            for (String line : allLines) {
                rows.add(Arrays.asList(line.split("~")));
            }
                        
        } catch (IOException e) {
            System.out.println("There was an error opening the scores file.");
        }
                
        return transposeList(rows);
    }
    
    /*
     * Updates the leaderboard (i.e. rewrites the leaderboard.txt file) if the given score is higher
     * than the current 10th highest score. (If so, a JDialog is created to ask for the player's
     * name.) This method is invoked in the GameOver page.
     */
    public static void updateLeaderboard(int score, int level, JComponent comp) {
        List<String> scores = getScores().get(1);
        if (score > Integer.valueOf(scores.get(scores.size() - 1))) {
            int index = 0;
            for (int i = scores.size() - 1; i > 0; i--) {
                if (score > Integer.valueOf(scores.get(i))) {
                    index = i;
                }
            }
            String name = askForName(comp);
            List<List<String>> lines = transposeList(getScores());            
            List<String> newEntry = Arrays.asList(name, "" + score, "" + level, getDate());
            lines.add(index, newEntry);
            lines.remove(lines.size() - 1);
            
            try {
                Writer w = new FileWriter(PATH_TO_LEADERBOARD);
                BufferedWriter bw = new BufferedWriter(w);
                for (List<String> line : lines) {
                    if (lines.indexOf(line) > 0) {
                        bw.newLine();
                    }
                    for (String field : line) {
                        if (line.indexOf(field) > 0) {
                            bw.write("~");
                        }
                        bw.write(field);
                    }
                }
                bw.close();
            } catch (IOException e) {
                System.out.println("There was an error updating the leaderboard.");
            }
        }
    }
    
    /*
     * Creates a JDialog that asks for the player's name. (see updateLeaderboard)
     */
    private static String askForName(JComponent comp) {
        String s = null;
        String error = "";
        while (isInvalidName(s)) {
            s = (String)JOptionPane.showInputDialog(
                   comp, error + "New top 10 score! For record keeping purposes, please enter\n"
                           + "your name (alphanumeric characters only, max 20 characters):",
                   "New top 10 score!", JOptionPane.PLAIN_MESSAGE, null, null, "Player");
            if (isInvalidName(s)) {
                error = "Invalid name!\n";
            }
        }        
        return s;
    }
    
    /*
     * Determines if the given String is invalid. A valid string is nonempty, consists only of
     * alphanumeric characters, and is at most 20 characters long.
     */
    private static boolean isInvalidName(String s) {
        return (s == null || !Pattern.matches(".*[^\\s].*", s) || 
                Pattern.matches(".*[^\\w].*", s) || s.length() > 20);
    }
    
    /*
     * Returns the current date as a String of the format "month day year". Ex: Jan 01 2020
     */
    private static String getDate() {
        List<String> fields = Arrays.asList(Calendar.getInstance().getTime().toString().split(" "));
        fields = new ArrayList<>(fields);
        fields.remove(4);
        fields.remove(3);
        fields.remove(0);
        String date = "";
        for (String s : fields) {
            if (fields.indexOf(s) > 0) {
                date += " ";
            }
            date += s;
        }
        return date;
    }

}
