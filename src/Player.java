import java.awt.Color;
import java.awt.Graphics;

/*
 * Describes a player, which is the entity controlled by the game's user. A Court has exactly one
 * associated Player. The player can move freely (based on user input) and attack by shooting
 * projectiles.
 */
public class Player extends Entity {
    
    public static final int SIZE = 20;
    public static final int DAMAGE = 1;
    public static final int PLAYER_SPEED = 8;
    public static final int HP = 100;
    
    private static final Color PLAYER_COLOR = Color.BLACK;
    
    private int targetX;
    private int targetY;
    
    /*
     * Creates the player at its default position at the center of the Court.
     */
    Player(Court court) {
        super(0, 0, 0, 0, SIZE, SIZE, HP, court, Court.COURT_WIDTH, Court.COURT_HEIGHT);
        this.setInitialized(true);

        int initX = (Court.COURT_WIDTH - this.getWidth()) / 2;
        int initY = (Court.COURT_HEIGHT - this.getHeight()) / 2;
        this.setPx(initX);
        this.setPy(initY);
    }
    
    /*
     * Creates a copy of the Player (used for encapsulation).
     */
    public Player clone() {
        Player clone = new Player(this.getCourt());
        clone.setPx(this.getPx());
        clone.setPy(this.getPy());
        clone.setHP(this.getHP());
        return clone;
    }
    
    /*
     * Sets the target of the player. This method is used to create any projectiles shot by the
     * player, and is invoked by the mouse listener in Court.
     */
    public void setTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }
    
    /*
     * Draws the player as a black square.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(PLAYER_COLOR);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
    
    /*
     * Shoots a projectile toward the player's target coordinates and adds it to the game state.
     */
    @Override
    public void attack() {
        int centerX = this.getPx() + this.getWidth() / 2;
        int centerY = this.getPy() + this.getHeight() / 2;
        double direction = Math.atan2(targetY - centerY, targetX - centerX);
        
        Court court = this.getCourt();
        Projectile proj = new Projectile(centerX, centerY, direction, false, 1, court);
        court.addProjectile(proj);
    }
}
