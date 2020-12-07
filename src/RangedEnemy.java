import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Describes a ranged enemy, one of the two kinds of enemies in the game. Like the player, a ranged
 * enemy can also shoot projectiles, and its movement is random, making it difficult for the player
 * to hit.
 */
public class RangedEnemy extends Enemy {
    
    private static final Color RANGED_COLOR = Color.YELLOW;
    private static final double PROBABILITY_CHANGE_VELOCITY = 0.1;
    
    public static final int RANGED_SPEED = 3;
    public static final int TICKS_PER_ATTACK = 10;

    private int damage;
    private int attackStage;
    
    /*
     * Creates a new RangedEnemy with the given damage, and changes its velocity so that it
     * immediately begins moving upon initialization.
     */
    RangedEnemy(Court court, int damage) {
        super(court);
        
        this.damage = damage;
        
        changeVx();
        changeVy();
    }
    
    /*
     * Creates a copy of the invoking RangedEnemy.
     */
    public RangedEnemy clone() {
        RangedEnemy clone = new RangedEnemy(this.getCourt(), damage);
        clone.setPx(this.getPx());
        clone.setPy(this.getPy());
        return clone;
    }
    
    /*
     * Draws the ranged enemy as a yellow square. Note that, during the process of initialization,
     * the color is initially clear and becomes more opaque over the course of initialization,
     * creating a fade-in effect.
     */
    @Override
    public void draw(Graphics g) {
        if (this.getInitialized()) {
            g.setColor(RANGED_COLOR);
        } else {
            int red = RANGED_COLOR.getRed();
            int green = RANGED_COLOR.getGreen();
            int blue = RANGED_COLOR.getBlue();
            int alpha = (int) ((float) getInitStage() / Enemy.TICKS_TO_INITIALIZE * 255);
            g.setColor(new Color(red, green, blue, alpha));
        }
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
    
    /*
     * Shoots a projectile towards the player once the invoking ranged enemy is at the appropriate
     * attack stage. The projectile is added to the game state (i.e. the Court's Set<Projectile>
     * field.) In this case, TICKS_PER_ATTACK is set to 10, so a ranged enemy attacks once every 10 
     * ticks.
     * 
     * Note that the projectile is fired from the center of the enemy and targets the center of
     * the player.
     */
    @Override
    public void attack() {
        attackStage++;
        if (attackStage >= TICKS_PER_ATTACK) {
            attackStage = 0;
            
            int centerX = this.getPx() + this.getWidth() / 2;
            int centerY = this.getPy() + this.getHeight() / 2;
            
            Court court = this.getCourt();
            Player p = court.getPlayerCopy();
            int pCenterX = p.getPx() + p.getWidth() / 2;
            int pCenterY = p.getPy() + p.getHeight() / 2;
            
            double direction = Math.atan2(pCenterY - centerY, pCenterX - centerX);

            Projectile proj = new Projectile(centerX, centerY, direction, true, damage, court);
            court.addProjectile(proj);
        }
    }
    
    /*
     * For a certain probability (currently set to 0.1), changes the x- and/or y-velocities of the
     * ranged enemy.
     */
    @Override
    public void updateVelocity() {
        if (Math.random() < PROBABILITY_CHANGE_VELOCITY) {
            changeVx();
        }
        if (Math.random() < PROBABILITY_CHANGE_VELOCITY) {
            changeVy();
        }
    }
    
    /*
     * Changes the x-velocity of the ranged enemy randomly between left, stationary, and right.
     */
    private void changeVx() {
        this.setVx(RANGED_SPEED * ThreadLocalRandom.current().nextInt(-1, 2));
    }
    
    /*
     * Changes the y-velocity of the ranged enemy randomly between up, stationary, and down.
     */
    private void changeVy() {
        this.setVy(RANGED_SPEED * ThreadLocalRandom.current().nextInt(-1, 2));
    }
}
