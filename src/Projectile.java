import java.awt.*;

/*
 * Describes projectiles, which can be shot by either the player or a ranged enemy.
 */
public class Projectile {
    private int px;
    private int py;
    
    // The "ends" of the projectile are represented as doubles to maximize accuracy.
    // The px and py fields are rounded forms of the initX and initY fields.
    private double initX;
    private double initY;
    private double endX;
    private double endY;
    
    // Angle from horizontal (0 to 2pi)
    private final double direction;
    
    // Indicates whether the projectile was fired by a ranged enemy or not
    private final boolean enemy;
    
    private final int damage;
    private final Court court;
    
    public static final int LENGTH = 30;
    public static final int SPEED = 15;
    public static final Color PLAYER_COLOR = Color.GREEN;
    public static final Color ENEMY_COLOR = Color.RED;
    
    // Constructs a projectile for a given position, direction, enemy boolean, damage, and court.
    Projectile(int px, int py, double direction, boolean enemy, int damage, Court court) {
        this.px = px;
        this.py = py;
        this.direction = direction;
        this.enemy = enemy;
        this.damage = damage;
        this.court = court;
        
        this.initX = px;
        this.initY = py;
        this.endX = px + Math.cos(direction) * LENGTH;
        this.endY = py + Math.sin(direction) * LENGTH;
    }
    
    /*
     * Moves the projectile (i.e. its ends) in the appropriate direction based on SPEED and LENGTH,
     * then calls the update method.
     */
    public void move() {     
        initX += Math.cos(direction) * SPEED;
        initY += Math.sin(direction) * SPEED;
        endX = initX + Math.cos(direction) * LENGTH;
        endY = initY + Math.sin(direction) * LENGTH;
                
        px = (int) Math.round(initX);
        py = (int) Math.round(initY);
        update();
    }
    
    /*
     * Checks the state of the projectile after it has moved. Removes the projectile from the game
     * state if it has hit a wall, an enemy (if it was fired by the player), or the player (if it
     * was fired by a ranged enemy). For the latter two cases, decreases the enemy or player's HP
     * accordingly.
     * 
     * Note that an enemy projectile is not affected if another enemy stands in its path.
     */
    private void update() {
        if (hitWall()) {
            court.removeProjectile(this);
        }
        for (Entity e : court.getEnemiesAsEntities()) {
            if (!enemy && hitEntity(e)) {
                e.setHP(Math.max(e.getHP() - damage, 0));
                court.removeProjectile(this);
            }
        }
        if (enemy && hitEntity(court.getPlayerCopy())) {
            court.setPlayerHP(Math.max(court.getPlayerCopy().getHP() - damage, 0));
            court.removeProjectile(this);
        }
    }
    
    /*
     * Determines if the projectile has hit a wall (i.e. if its far end is out of bounds).
     */
    public boolean hitWall() {
        return (endX >= Court.COURT_WIDTH || endX < 0 || endY >= Court.COURT_HEIGHT || endY < 0);
    }
    
    /*
     * Determines if the projectile has hit an entity.
     */
    public boolean hitEntity(Entity e) {
        for (int i = 0; i <= LENGTH; i++) {
            int x = (int) Math.round(px + Math.cos(direction) * i);
            int y = (int) Math.round(py + Math.sin(direction) * i);

            if (e.getInitialized() && x >= e.getPx() && x <= e.getPx() + e.getWidth()
                && y >= e.getPy() && y <= e.getPy() + e.getHeight()) {
                return true;
            }
        }
        return false;
    }
    
    /*
     *  The following four getter methods are used for testing.
     */
    
    public double getDirection() {
        return direction;
    }
    
    public int getPx() {
        return px;
    }
    
    public int getPy() {
        return px;
    }
    
    public boolean enemyOrNot() {
        return enemy;
    }
    
    /*
     * Draws the projectile as a short line segment between its ends.
     * 
     * Projectiles shot by a ranged enemy are red, while those shot by the player are green.
     */
    public void draw(Graphics g) {
        if (enemy) {
            g.setColor(ENEMY_COLOR);
        } else {
            g.setColor(PLAYER_COLOR);
        }
        g.drawLine(px, py, (int) Math.round(endX), (int) Math.round(endY));
    }
}
