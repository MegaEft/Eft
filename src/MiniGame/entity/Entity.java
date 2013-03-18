/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;

import java.util.*;
import MiniGame.world.World;
import MiniGame.Camera;
/**
 *
 * @author Ty
 */
public abstract class Entity {
    public enum EntityType {
        GenericEntity, PlayerEntity, EnemyEntity, ProjectileEntity
    }
    private static int nextId = 0;
    protected static Random random = new Random();

    protected int id;
    protected float x, y, z;
    
    protected Vector3f acceleration = new Vector3f(0, 0, 0);
    protected Vector3f velocity = new Vector3f(0, 0, 0);
    
    protected final World world;
    public static Image shadowImage;
    protected static final Color shadowColorMult = new Color(0, 0, 0, 0.5f);
    private boolean removed;
    protected EntityType entityType = EntityType.GenericEntity;
    
    public abstract boolean update(GameContainer slickContainer, int deltaMS);
    public abstract void render(GameContainer slickContainer, Graphics g, Camera camera);
    
    public Entity(World world) {
        this.world = world;
        this.id = ++nextId;
    }

    public Entity(World world, float x, float z) {
        this(world);
        this.x = x;
        this.z = z;
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public EntityType getType() {
        return entityType;
    }

    public boolean isFixedPosition() {
        return false;
    }
    
    public boolean isRemoved() {
        return removed;
    }
    
    public void setRemoved() {
        this.removed = true;
    }
    
    public boolean collidesWith(Entity other) {
        return other != this;
    }

    public float getCollisionRadius() {
        return 16;
    }

    public float getEntityHeight() {
        return 32.0f;
    }

    public boolean isOutOfWorld() {
        return x < 0 || z < 0 || x > world.getWidth() || z > world.getHeight();
    }

    protected void onCollide(Entity entity) {
        
    }
    
    public void renderGroundLayer(GameContainer slickContainer, Graphics g, Camera camera) {
        if (y < 0) {
            return;
        }
        float shadowScale = (float) Math.pow(1 - Math.min(y, 400) / 400, 2);
        int halfShadowX = (int) (((float) shadowImage.getWidth() / 2.0f) * shadowScale);
        int halfShadowY = (int) (((float) shadowImage.getHeight() / 2.0f) * shadowScale);
        g.drawImage(shadowImage, x - camera.getX() - halfShadowX, z - camera.getY() - halfShadowY, x - camera.getX() + halfShadowX, z - camera.getY() + halfShadowY, 0, 0, 32, 32, shadowColorMult);
    }
    
    public float perspectiveDistanceToSqr(Entity other) {
        float dx = x - other.x;
        float dz = (z - other.z) * 2;
        return dx * dx + dz * dz;
    }
    
    public void checkCollisions(List<Entity> entities) {

        float thisRadius = getCollisionRadius();
        for (Entity e : world.getEntities()) {
            if (collidesWith(e) && e.collidesWith(this)) {
                float radius = thisRadius + e.getCollisionRadius();
                if (perspectiveDistanceToSqr(e) < radius * radius) {
                    onCollide(e);
                }
            }
        }
    }
}
