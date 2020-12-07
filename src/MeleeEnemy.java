import java.awt.Color;
import java.awt.Graphics;

/*
 * Describes a melee enemy, one of the two kinds of enemies in the game. A melee enemy can only
 * attack the player when adjacent to it and therefore always moves (i.e. updates velocity) in the
 * player's direction.
 */
public class MeleeEnemy extends Enemy {
    
    private static final Color MELEE_COLOR = Color.RED;
    
    public static final int MELEE_SPEED = 10;
    public static final int TICKS_PER_ATTACK = 10;

    private int damage;
    private int attackStage;
    
    /*
     * Creates a new MeleeEnemy with the given damage.
     */
    MeleeEnemy(Court court, int damage) {
        super(court);

        this.damage = damage;
    }
    
    /*
     * Creates a copy of the invoking MeleeEnemy.
     */
    public MeleeEnemy clone() {
        MeleeEnemy clone = new MeleeEnemy(this.getCourt(), damage);
        clone.setPx(this.getPx());
        clone.setPy(this.getPy());
        return clone;
    }
    
    /*
     * Draws the melee enemy as a red square. Note that, during the process of initialization,
     * the color is initially clear and becomes more opaque over the course of initialization,
     * creating a fade-in effect.
     */
    @Override
    public void draw(Graphics g) {
        if (this.getInitialized()) {
            g.setColor(MELEE_COLOR);
        } else {
            int red = MELEE_COLOR.getRed();
            int green = MELEE_COLOR.getGreen();
            int blue = MELEE_COLOR.getBlue();
            int alpha = (int) ((float) getInitStage() / Enemy.TICKS_TO_INITIALIZE * 255);
            g.setColor(new Color(red, green, blue, alpha));
        }
        
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
    
    /*
     * Attacks the player if the invoking melee enemy is at the appropriate attack stage (and is
     * adjacent to the player). In this case, TICKS_PER_ATTACK is set to 10, so a melee enemy 
     * attacks once every 10 ticks.
     */
    @Override
    public void attack() {
        attackStage++;
        if (attackStage >= TICKS_PER_ATTACK) {
            attackStage = 0;
            Court court = this.getCourt();
            Player p = court.getPlayerCopy();
            if (isAdjacentX(p) || isAdjacentY(p)) {
                court.setPlayerHP(Math.max(court.getPlayerCopy().getHP() - damage, 0));
            }
        }
    }
    
    /*
     * Updates the melee enemy's velocity so that it always moves in the direction of the player.
     * If the melee enemy is already adjacent to the player, then it will not move at all.
     */
    @Override
    public void updateVelocity() {
        Player p = this.getCourt().getPlayerCopy();
        
        double direction = Math.atan2(p.getPy() - this.getPy(), p.getPx() - this.getPx());
        setVx((int) (Math.cos(direction) * MELEE_SPEED));
        setVy((int) (Math.sin(direction) * MELEE_SPEED));
        
        if (isAdjacentX(p) || isAdjacentY(p)) {
            setVx(0);
            setVy(0);
        }
    }
    
    /*
     * The following two methods determine adjacency in the x- and y-directions.
     */
    
    private boolean isAdjacentX(Entity that) {
        return ((that.getPx() + that.getWidth() == this.getPx() || 
                this.getPx() + this.getWidth() == that.getPx()) &&
                this.getPy() < that.getPy() + that.getHeight() && 
                that.getPy() < this.getPy() + this.getHeight());
    }
    
    private boolean isAdjacentY(Entity that) {
        return ((that.getPy() + that.getHeight() == this.getPy() || 
                this.getPy() + this.getHeight() == that.getPy()) &&
                this.getPx() < that.getPx() + that.getWidth() && 
                that.getPx() < this.getPx() + this.getWidth());
    }
}
