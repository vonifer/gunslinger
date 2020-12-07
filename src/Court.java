import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.HashSet;
import java.util.Set;

/*
 * Contains the main state of the game.
 * 
 * As a reminder, the game consists of a player attempting to shoot as many enemies as possible
 * before inevitably dying. The enemies (either melee or ranged) arrive in waves.
 */
@SuppressWarnings("serial")
public class Court extends JPanel {
    
    private Play play;
    
    private int score = 0;
    private boolean playing = false;
    
    public static final int COURT_WIDTH = 400;
    public static final int COURT_HEIGHT = 400;
    
    // Milliseconds per tick
    public static final int TICK_INTERVAL = 35;
    
    // Seconds per wave (i.e. before next one starts)
    public static final int WAVE_INTERVAL = 10;
    
    // For a constant level, each wave contains more enemies than the previous.
    // When a new level is reached, the enemy count (i.e. wave number) resets, but each enemy 
    // does more damage.
    public static final int WAVES_PER_LEVEL = 3;
    
    private Timer tickTimer;
    private Timer waveTimer;
    private int timeLeft = WAVE_INTERVAL;
    
    private Player player;
    private Set<Enemy> enemies;
    private Set<Projectile> projectiles;
    
    // For a level number m and a wave number n (1-3, inclusive), the current wave contains
    // a total of 2n enemies (n melee and n ranged). Each enemy does m damage.
    private int levelNumber = 1;
    private int waveNumber;
    
    /*
     * Sets up the initial game state.
     */
    Court(Play play) {
        this.play = play;
        setPreferredSize(new Dimension(COURT_WIDTH, COURT_HEIGHT));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        setupEntitiesProjectilesAndTimers();
        setupKeysAndMouse();
    }
    
    /*
     * Creates a new player, Set of enemies, and Set of projectiles.
     */
    private void setupEntitiesProjectilesAndTimers() {
        player = new Player(this);
        enemies = new HashSet<>();
        projectiles = new HashSet<>();
        
        tickTimer = new Timer(TICK_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        tickTimer.start();
        
        // Begins first wave, then starts the wave timer.
        newWave();
        waveTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (playing) {
                    timeLeft--;
                    if (timeLeft <= 0) {
                        timeLeft = WAVE_INTERVAL;
                        newWave();
                    }
                }
            }
        });
        
        waveTimer.start();
    }
    
    /*
     * Sets up keyboard and mouse controls. As a reminder, the WASD keys are used to move the
     * player, and a mouse press causes the player to shoot a projectile towards the location of the
     * mouse press event.
     */
    private void setupKeysAndMouse() {
        final int ifw = JComponent.WHEN_IN_FOCUSED_WINDOW;
        getInputMap(ifw).put(KeyStroke.getKeyStroke("W"), "UP");
        getInputMap(ifw).put(KeyStroke.getKeyStroke("A"), "LEFT");
        getInputMap(ifw).put(KeyStroke.getKeyStroke("S"), "DOWN");
        getInputMap(ifw).put(KeyStroke.getKeyStroke("D"), "RIGHT");
        getActionMap().put("UP", new VelocityAction(null, -Player.PLAYER_SPEED));
        getActionMap().put("LEFT", new VelocityAction(-Player.PLAYER_SPEED, null));
        getActionMap().put("DOWN", new VelocityAction(null, Player.PLAYER_SPEED));
        getActionMap().put("RIGHT", new VelocityAction(Player.PLAYER_SPEED, null));
        
        Action stop = new VelocityAction(0, 0);
        getInputMap(ifw).put(KeyStroke.getKeyStroke("released W"), "UP released");
        getInputMap(ifw).put(KeyStroke.getKeyStroke("released A"), "LEFT released");
        getInputMap(ifw).put(KeyStroke.getKeyStroke("released S"), "DOWN released");
        getInputMap(ifw).put(KeyStroke.getKeyStroke("released D"), "RIGHT released");
        getActionMap().put("UP released", stop);
        getActionMap().put("LEFT released", stop);
        getActionMap().put("DOWN released", stop);
        getActionMap().put("RIGHT released", stop);
        
        addMouseListener(new ShootingListener());
    }
    
    /*
     * Defines an Action class that sets the velocity of the player. Used to set up the keyboard
     * controls above.
     */
    private class VelocityAction extends AbstractAction {
        private Integer vx;
        private Integer vy;
        VelocityAction(Integer vx, Integer vy) {
            this.vx = vx;
            this.vy = vy;
        }
        
        public void actionPerformed(ActionEvent e) {
            if (vx != null) {
                player.setVx(vx);
            }
            if (vy != null) {
                player.setVy(vy);
            }
        }
    }
    
    /*
     * Defines a mouse listener class that causes the player to shoot a projectile when the mouse
     * is pressed.
     */
    private class ShootingListener extends MouseAdapter implements MouseListener {
        public void mousePressed(MouseEvent e) {
            player.setTarget(e.getX(), e.getY());
            player.attack();
        }
    }
    
    /*
     * Updates the game state (if the game is currently active). Moves all projectiles, then moves
     * the player, then moves all entities.
     */
    void tick() {
        if (playing) {
            
            // Create set of all Entities in the game.
            Set<Entity> entities = new HashSet<>();
            entities.add(player);
            entities.addAll(getEnemiesAsEntities());
            
            // Moves all projectiles.
            for (Projectile p : new HashSet<Projectile>(projectiles)) {
                p.move();
            }
            
            // Moves the player. If the player has no more HP, then the game is stopped, and the 
            // Game Over card is shown.
            player.move(entities);
            if (player.getHP() <= 0) {
                toggle();
                Game.addCard(new GameOver(play), "Game Over");
                Game.showCard("Game Over");
            }
            
            // Moves all enemies. If an enemy has no more HP, then it is removed from the game
            // state, and the score is incremented by the current level number.
            for (Enemy e : new HashSet<Enemy>(enemies)) {
                if (e.getInitialized()) {
                    e.move(entities);
                    if (e.getHP() <= 0) {
                        enemies.remove(e);
                        score += levelNumber;
                    } else {
                        e.attack();
                    }
                } else {
                    e.initialize();
                }
            }
            
            // Resets the wave timer and starts a new wave of enemies if there are no enemies left.
            // If the current wave is entirely defeated before the next wave begins, the score is
            // incremented by the number of seconds left until the start of the next wave. 
            if (enemies.isEmpty()) {
                score += timeLeft;
                timeLeft = WAVE_INTERVAL;
                newWave();
            }
            
            // Repaints this component.
            play.repaint();
        }        
    }
    
    // For a level number m and wave number, creates a wave consisting of 2n enemies (n melee and n
    // n ranged), each of which does m damage.
    private void newWave() {
        if (waveNumber == WAVES_PER_LEVEL) {
            waveNumber = 1;
            levelNumber++;
        } else {
            waveNumber++;
        }
        
        for (int i = 0; i < waveNumber; i++) {
            (new MeleeEnemy(this, levelNumber)).spawn();
            (new RangedEnemy(this, levelNumber)).spawn();
        }
    }
    
    /*
     * Switches the game state between active and inactive (represented by the boolean "playing").
     */
    public void toggle() {
        playing = !playing;
    }
    
    /*
     * The following methods are getter/setter methods for certain private fields.
     */
    
    public int getScore() {
        return score;
    }
    
    public boolean getPlaying() {
        return playing;
    }
    
    public Set<Entity> getEnemiesAsEntities() {
        Set<Entity> entities = new HashSet<>();
        for (Enemy e : enemies) {
            entities.add(e);
        }
        return entities;
    }
    
    public Set<Enemy> getEnemies() {
        return new HashSet<Enemy>(enemies);
    }
    
    public Set<Projectile> getProjectiles() {
        return new HashSet<Projectile>(projectiles);
    }
    
    public void setPlayerHP(int i) {
        player.setHP(i);
    }
    
    public Player getPlayerCopy() {
        return player.clone();
    }
    
    public void setPlayerVx(int i) {
        player.setVx(i);
    }
    
    public void setPlayerVy(int i) {
        player.setVy(i);
    }
    
    public int getTimeLeft() {
        return timeLeft;
    }
    
    public int getLevel() {
        return levelNumber;
    }
    
    /*
     * The following methods are used to add/remove enemies/projectiles.
     */
    
    public void addEnemy(Enemy e) {
        enemies.add(e);
    }
    
    public void removeEnemy(Enemy e) {
        enemies.remove(e);
    }
    
    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }
    
    public void removeProjectile(Projectile p) {
        projectiles.remove(p);
    }
    
    /*
     * Turns off the tick and wave timers. Used for testing.
     */
    public void turnOffTimers() {
        tickTimer.stop();
        waveTimer.stop();
    }
    
    /*
     * Repaints the Court, all enemies, the player, and all projectiles.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Enemy e : enemies) {
            e.draw(g);
        }
        player.draw(g);
        for (Projectile p : projectiles) {
            p.draw(g);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
}
