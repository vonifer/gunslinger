import java.awt.*;
import java.util.Set;

/*
 * Describes all entities in the game, i.e. players and enemies. (see Player and Enemy subclasses)
 */
public abstract class Entity {
    private int px;
    private int py;
    private int vx;
    private int vy;
    private int width;
    private int height;
    
    private int maxX;
    private int maxY;
    
    private int hp;
    
    /* 
     * An entity can only move when it has been initialized. This field is only used for enemies.
     * See Enemy for more info.
     */
    private boolean initialized;
    
    private Court court;
    
    // Creates a new Entity with the given fields. The court width and height are used to determine
    // the maximum possible x- and y-coordinates of the Entity.
    Entity(int px, int py, int vx, int vy, int width, int height, int hp, Court court,
            int courtWidth, int courtHeight) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.court = court;
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }
    
    /*
     * The following methods are getter/setter methods for certain private fields.
     */
    
    public int getPx() {
        return px;
    }
    
    public int getPy() {
        return py;
    }
    
    public void setPx(int px) {
        this.px = px;
    }
    
    public void setPy(int py) {
        this.py = py;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getVx() {
        return vx;
    }
    
    public int getVy() {
        return vy;
    }
    
    public void setVx(int vx) {
        this.vx = vx;
    }
    
    public void setVy(int vy) {
        this.vy = vy;
    }
    
    public int getMaxX() {
        return maxX;
    }
    
    public int getMaxY() {
        return maxY;
    }
    
    public int getHP() {
        return hp;
    }
    
    public void setHP(int hp) {
        this.hp = hp;
    }
    
    public boolean getInitialized() {
        return initialized;
    }
    
    public void setInitialized(boolean b) {
        initialized = b;
    }
    
    /*
     * Returns a reference to the court itself. Only used in subclasses.
     */
    protected Court getCourt() {
        return court;
    }
    
    /*
     * Moves the entity. A set of all other entities in the game must be given so that the method
     * can check whether the entity can move at all.
     */
    public void move(Set<Entity> entities) {
        if (initialized) {
            
            // For an entity that is an enemy, calls the updateVelocity method to change the enemy's
            // direction.
            if (this instanceof Enemy) {
                ((Enemy) this).updateVelocity();
            }
            
            // Moves the entity.
            px += vx;
            py += vy;
            
            // Calls the clip and clipEntity methods to adjust the entity's position from its "raw"
            // form. This prevents the entity from moving into a wall or another entity.
            for (Entity that : entities) {
                if (this != that && intersects(that)) {
                    clip();
                    clipEntity(that);
                    that.clipEntity(this);
                    clip();
                    return;
                }
            }
            
            clip();
        }
    }
    
    /*
     * Adjusts the entity's position if if its "raw" position is out of bounds.
     */
    private void clip() {
        if (px < 0 || px > maxX) {
            px = Math.min(Math.max(0, px), maxX);
        }
        if (py < 0 || py > maxY) {
            py = Math.min(Math.max(0, py), maxY);
        }
    }
    
    /*
     * Adjusts the entity's position if if its "raw" position conflicts with the position of another
     * entity.
     */

    private void clipEntity(Entity that) {
        while (intersects(that)) {
            if (vx > 0) {
                this.px--;
            } else if (vx < 0) {
                this.px++;
            } else {
                if (that.vx > 0) {
                    that.px--;
                } else if (that.vx < 0) {
                    that.px++;
                } 
            }
            if (vy > 0) {
                this.py--;
            } else if (vy < 0) {
                this.py++;
            } else {
                if (that.vy > 0) {
                    that.py--;
                } else if (that.vy < 0) {
                    that.py++;
                }
            }
        }
    }
    
    /*
     * Determines if the invoking entity's position conflicts with that of the argued entity.
     */
    public boolean intersects(Entity that) {
        return (this.px + this.width > that.px
            && this.py + this.height > that.py
            && that.px + that.width > this.px 
            && that.py + that.height > this.py);
    }
    
    /*
     * Determines if the invoking entity's position will conflict with that of the argued entity,
     * i.e. after one tick.
     */
    public boolean willIntersect(Entity that) {
        int thisNextX = this.px + this.vx;
        int thisNextY = this.py + this.vy;
        int thatNextX = that.px + that.vx;
        int thatNextY = that.py + that.vy;
    
        return (thisNextX + this.width > thatNextX
            && thisNextY + this.height > thatNextY
            && thatNextX + that.width > thisNextX 
            && thatNextY + that.height > thisNextY);
    }
    
    /*
     * Draws the entity.
     */
    public abstract void draw(Graphics g);
    
    /*
     * Causes the entity to attack.
     */
    public abstract void attack();
}
