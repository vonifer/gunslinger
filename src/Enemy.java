import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Describes common properties of enemies in the game. The two concrete subclasses of Enemy are
 * MeleeEnemy and RangedEnemy.
 * 
 * The Court creates an enemy by first spawning the enemy and then initializing it. After
 * constructing an enemy, spawning it sets its position to a random position and adds it to the 
 * Court's Set of entities. The enemy is initialized over the course of 20 ticks, after which it is
 * free to move and attack.
 */
public abstract class Enemy extends Entity {
    
    public static final int SIZE = 20;
    public static final int HP = 1;
    
    public static final int TICKS_TO_INITIALIZE = 20;
    
    private int initializationStage = 0;
    
    /* 
     * Sets initial position to (0, 0), but this is changed in the spawn method below.
     */
    Enemy(Court court) {
        super(0, 0, 0, 0, SIZE, SIZE, HP, court, Court.COURT_WIDTH, Court.COURT_HEIGHT);
        this.setInitialized(false);
    }
    
    /*
     * Randomizes enemy position and makes sure that there are no preexisting entities there.
     * The enemy is then added to the game state (i.e. the Court's Set<Enemy> field).
     */
    public void spawn() {
        Court court = this.getCourt();
        Set<Entity> entities = court.getEnemiesAsEntities();
        entities.add(court.getPlayerCopy());
        boolean validLocation = false;
        int initX = 0;
        int initY = 0;
        
        while (!validLocation) {
            initX = ThreadLocalRandom.current().nextInt(0, this.getMaxX());
            initY = ThreadLocalRandom.current().nextInt(0, this.getMaxY());
            
            this.setPx(initX);
            this.setPy(initY);
            
            validLocation = true;
            for (Entity e : entities) {
                if (this.intersects(e)) {
                    validLocation = false;
                }
            }
        }
                        
        court.addEnemy(this);
    }
    
    /*
     * Spawns at an enemy at the given location, which must be valid. Used only for testing.
     */
    public void spawn(int px, int py) {
        Court court = this.getCourt();
        Set<Entity> entities = court.getEnemiesAsEntities();
        entities.add(court.getPlayerCopy());
        this.setPx(px);
        this.setPy(py);
        for (Entity e : entities) {
            if (this.intersects(e)) {
                throw new IllegalArgumentException(
                        "Another entity already exists at the given point.");
            }
        }
        court.addEnemy(this);
    }
    
    /*
     * For an uninitialized enemy, increments the initializationStage by 1 each tick. After 20
     * ticks, the enemy becomes initialized and can then move and attack.
     */
    public void initialize() {
        initializationStage++;
        if (initializationStage >= TICKS_TO_INITIALIZE) {
            this.setInitialized(true);
        }
    }
    
    /*
     * Getter method for initialization stage.
     */
    public int getInitStage() {
        return initializationStage;
    }
    
    /*
     * Changes the velocity of the enemy according to its expected behavior. This method is called
     * at the beginning of the move method in Entity, and it only applies to enemies.
     * 
     * For melee enemies, the velocity will be updated to target the player's new position.
     * For ranged enemies, the velocity may be changed to a new random velocity.
     */
    public abstract void updateVelocity();
}
