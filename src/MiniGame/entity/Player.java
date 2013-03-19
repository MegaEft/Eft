/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;

import MiniGame.Camera;
import MiniGame.world.World;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import MiniGame.util.Block;
import MiniGame.util.BlockMap;

/**
 *
 * @author Ty
 */
public class Player extends HealthEntity {
    private long lastShotFiredTimeMS = 0;
    private long currentLifeTimeMS = 0;
    private Image PlayerImage = null;
    private Polygon playerPoly;
    private float x = 600;
    private float y = 300;
    private float z = 0;
    private float scale = 1.0f;
    private int boost = 2;
    
    public Player(World world, float x, float z) throws SlickException {
        super(world, x, z);
        PlayerImage = new Image("res/actors/plane.png");
        playerPoly = new Polygon(new float[]{x,y,x+PlayerImage.getWidth(),y,x+PlayerImage.getWidth(),y+PlayerImage.getHeight(),x,y+PlayerImage.getHeight()});
    }
    @Override
    public boolean update(GameContainer slickContainer, int deltaMS){
        currentLifeTimeMS += deltaMS;
        
        Input input = slickContainer.getInput(); 
        if(input.isKeyDown(Input.KEY_A)){
            PlayerImage.rotate(-0.2f * deltaMS);
        } 
        if(input.isKeyDown(Input.KEY_D)) {
            PlayerImage.rotate(0.2f * deltaMS);
        }
        //shoot bullets!
        if(input.isKeyDown(Input.KEY_SPACE)){
            if (currentLifeTimeMS > lastShotFiredTimeMS + getFireDelay()) {
                lastShotFiredTimeMS = currentLifeTimeMS;

                float rotation = PlayerImage.getRotation();
                Vector3f shootDir = new Vector3f((float) (Math.sin(Math.PI/180 * (rotation))),(float) (Math.cos(Math.PI/180 * (rotation))), 0);
                shootBullet(0, 0, 1, shootDir);
            }
        }
        if(input.isKeyDown(Input.KEY_W)){
            float hip = 0.4f * deltaMS; 
            float rotation = PlayerImage.getRotation(); 
            if(input.isKeyDown(Input.KEY_LSHIFT)){
                x+= hip * Math.sin(Math.toRadians(rotation)) * boost;
                y-= hip * Math.cos(Math.toRadians(rotation)) * boost;
                playerPoly.setY(y);
                playerPoly.setX(x);
                /*if (entityCollisionWith()){
                    y+= hip * Math.cos(Math.toRadians(rotation)) * boost;
                    x-= hip * Math.sin(Math.toRadians(rotation)) * boost;
                    playerPoly.setY(y);
                    playerPoly.setX(x);
                }*/
            }
            else{
                x+= hip * Math.sin(Math.toRadians(rotation));
                y-= hip * Math.cos(Math.toRadians(rotation));
                playerPoly.setY(y-PlayerImage.getHeight()/2);
                playerPoly.setX(x-PlayerImage.getWidth()/2);
                if (entityCollisionWith()){
                    y+= hip * Math.cos(Math.toRadians(rotation));
                    x-= hip * Math.sin(Math.toRadians(rotation));
                    playerPoly.setY(y-PlayerImage.getHeight()/2);
                    playerPoly.setX(x-PlayerImage.getWidth()/2);
                }
            }
        }
        if(input.isKeyDown(Input.KEY_S)){
            float hip = 0.4f * deltaMS; 
            float rotation = PlayerImage.getRotation(); 
            x-= hip * Math.sin(Math.toRadians(rotation));
            y+= hip * Math.cos(Math.toRadians(rotation));
            playerPoly.setY(y-PlayerImage.getHeight()/2);
            playerPoly.setX(x-PlayerImage.getWidth()/2);
            if (entityCollisionWith()){
                y-= hip * Math.cos(Math.toRadians(rotation));
                x+= hip * Math.sin(Math.toRadians(rotation));
                playerPoly.setY(y-PlayerImage.getHeight()/2);
                playerPoly.setX(x-PlayerImage.getWidth()/2);
            }
        } 
        if(input.isKeyDown(Input.KEY_2)) {
            scale += (scale >= 5.0f) ? 0 : 0.1f;
            PlayerImage.setCenterOfRotation(PlayerImage.getWidth()/2.0f*scale, PlayerImage.getHeight()/2.0f*scale);
        }
        if(input.isKeyDown(Input.KEY_1)) {
            scale -= (scale <= 1.0f) ? 0 : 0.1f;
            PlayerImage.setCenterOfRotation(PlayerImage.getWidth()/2.0f*scale, PlayerImage.getHeight()/2.0f*scale);
        }
        Camera camera = world.getCamera();
        camera.setTargetX(x);
        camera.setTargetY(y);
        return true;
    }
    
    public boolean entityCollisionWith(){
        for (int i = 0; i < BlockMap.entities.size();i++){
            Block entity1 = (Block) BlockMap.entities.get(i);
            if (playerPoly.intersects(entity1.poly)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void render(GameContainer slickContainer, Graphics g, Camera camera) {
        PlayerImage.draw(x - PlayerImage.getWidth()/2, y - PlayerImage.getHeight()/2, scale);
        //remove this to get rid of the black box
        g.draw(playerPoly);
    }
    
    private void shootBullet(float tx, float tz, int shootArm) {
        Vector3f projectileDirection = new Vector3f(PlayerImage.getRotation(), 0, PlayerImage.getRotation());
        shootBullet(tx, tz, shootArm, projectileDirection);
    }

    private void shootBullet(float tx, float tz, int shootArm, Vector3f projectileDirection) {
        double targetDistance = Math.sqrt((x - tx) * (x - tx) + (z - tz) * (z - tz));
        float shootHeight = y;
        //projectileDirection.y = -shootHeight / ((float) targetDistance + 40.0f);
        float bulletSpeed = 800;
        Projectile bullet = new Projectile(world, x , -y , 0 , projectileDirection, bulletSpeed);
        world.addEntity(bullet);
    }
    
    public int getFireDelay() {
        int level = 1;
        if (level > 3) {
            level = 3;
        }
        return 250 - (25 * level);
    }
}
