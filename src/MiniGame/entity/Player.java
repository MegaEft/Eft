/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;

import MiniGame.Camera;
import MiniGame.world.World;
import org.newdawn.slick.Input;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;

/**
 *
 * @author Ty
 */
public class Player extends HealthEntity {
    Image PlayerImage = null;
    float x = 400;
    float y = 300;
    float scale = 1.0f;
    int boost = 2;
    public Player(World world, float x, float z) throws SlickException {
        super(world, x, z);
        PlayerImage = new Image("res/actors/plane.png");        
    }
    @Override
    public boolean update(GameContainer slickContainer, int deltaMS){
        Input input = slickContainer.getInput(); 
        if(input.isKeyDown(Input.KEY_A)){
            PlayerImage.rotate(-0.2f * deltaMS);
        } 
        if(input.isKeyDown(Input.KEY_D)) {
            PlayerImage.rotate(0.2f * deltaMS);
        } 
        if(input.isKeyDown(Input.KEY_W)){
            float hip = 0.4f * deltaMS; 
            float rotation = PlayerImage.getRotation(); 
            if(input.isKeyDown(Input.KEY_LSHIFT)){
                x+= hip * Math.sin(Math.toRadians(rotation)) * boost;
                y-= hip * Math.cos(Math.toRadians(rotation)) * boost;
            }
            else{
                x+= hip * Math.sin(Math.toRadians(rotation));
                y-= hip * Math.cos(Math.toRadians(rotation));
            }
        }
        if(input.isKeyDown(Input.KEY_S)){
            float hip = 0.4f * deltaMS; 
            float rotation = PlayerImage.getRotation(); 
            x-= hip * Math.sin(Math.toRadians(rotation));
            y+= hip * Math.cos(Math.toRadians(rotation));
        } 
        if(input.isKeyDown(Input.KEY_2)) {
            scale += (scale >= 5.0f) ? 0 : 0.1f;
            PlayerImage.setCenterOfRotation(PlayerImage.getWidth()/2.0f*scale, PlayerImage.getHeight()/2.0f*scale);
        }
        if(input.isKeyDown(Input.KEY_1)) {
            scale -= (scale <= 1.0f) ? 0 : 0.1f;
            PlayerImage.setCenterOfRotation(PlayerImage.getWidth()/2.0f*scale, PlayerImage.getHeight()/2.0f*scale);
        }
        return true;
    }
    
    @Override
    public void render(GameContainer slickContainer, Graphics g, Camera camera) {
        PlayerImage.draw(x, y, scale);
    }
}
