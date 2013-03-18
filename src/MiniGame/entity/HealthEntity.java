/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;
import org.newdawn.slick.*;
import MiniGame.Camera;
import MiniGame.gui.HealthBar;
import MiniGame.world.World;
/**
 *
 * @author Ty
 */
public class HealthEntity extends Entity {

    private static final Color hurtColor = new Color(1, .3f, .1f, 1.0f);

    protected HealthBar healthBar;
    protected float health;
    protected int hurtTimer;

    public HealthEntity(World world, float x, float z) {
        super(world, x, z);

        health = getMaxHealth();
        healthBar = new HealthBar(this);
    }

    @Override
    public boolean update(GameContainer slickContainer, int deltaMS) {
        healthBar.update(slickContainer, deltaMS);
        hurtTimer -= deltaMS;
        return health > 0;
    }

    @Override
    public void render(GameContainer slickContainer, Graphics g, Camera camera) {
        healthBar.render(slickContainer, g, camera);
    }

    public void hurt(float damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        hurtTimer = 200;
    }

    public void heal(float amount) {
        health += amount;
        if (health > getMaxHealth()) {
            health = getMaxHealth();
        }
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return 100.0f;
    }

    protected boolean isRecentlyHurt() {
        return hurtTimer > 0;
    }

    protected Color getHurtColor() {
        if (isRecentlyHurt()) {
            return hurtColor;
        }
        return Color.white;
    }
}
