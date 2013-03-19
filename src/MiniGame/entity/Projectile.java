/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import MiniGame.world.*;
import MiniGame.Camera;
import MiniGame.util.OffsetImage;
/**
 *
 * @author Ty
 */
public class Projectile extends Entity{
    protected int damage;
    protected Vector3f direction;
    protected float speed;
    protected float blastRadius;
    protected Image bulletImage;
    Vector2f bulletOffset;
    
    protected Projectile(World world, float x, float y, float z, Vector3f direction, float speed) {
        super(world, x, z);
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        entityType = EntityType.ProjectileEntity;
        try{
            bulletImage = new OffsetImage("res/actors/bullet1.png");
            bulletOffset = new Vector2f(bulletImage.getWidth() / 2, bulletImage.getHeight() / 2);
        }
        catch (SlickException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean update(GameContainer slickContainer, int deltaMS) {
        x += direction.x * speed * deltaMS * .001f;
        y += direction.y * speed * deltaMS * .001f;
        z += direction.z * speed * deltaMS * .001f;
        return true;
    }
    
    protected void renderSelf(GameContainer slickContainer, Graphics g, Camera camera, float yOff) {
        double angleDegrees = Math.atan2(direction.z, direction.x) * 180.0 / Math.PI;
        g.pushTransform();
        g.rotate(x - camera.getX(), z - camera.getY() - yOff, (float) angleDegrees + 90);
        g.drawImage(bulletImage, x - camera.getX() - bulletOffset.x, z - camera.getY() - bulletOffset.y - yOff);
        g.popTransform();
    }

    @Override
    public void render(GameContainer slickContainer, Graphics g, Camera camera) {
        bulletImage.setColor(0, 1, 1, 1, 1);
        bulletImage.setColor(1, 1, 1, 1, 1);
        bulletImage.setColor(2, 1, 1, 1, 1);
        bulletImage.setColor(3, 1, 1, 1, 1);
        renderSelf(slickContainer, g, camera, y);
    }
    
    protected void onDeath() {
        float bulletY = Math.max(0, y);
        //world.addParticle(new AnimationParticle(world, x, bulletY, z, AnimationParticle.bulletImpactAnimation));
    }
    
    @Override
    public float getCollisionRadius() {
        return 8;
    }

    @Override
    public float getEntityHeight() {
        return 10.0f;
    }
}
