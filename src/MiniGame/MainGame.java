/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame;
/**
 *
 * @author Ty
 */

import org.newdawn.slick.*; 
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;
public class MainGame extends BasicGameState{
    public static final int ID = 2;
    Image land = null;
    Image plane = null;
    float x = 400;
    float y = 300;
    float scale = 1.0f;
    int boost = 2;
    
    @Override
    public int getID() {
        return ID;
    }
 
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
        land = new Image("res/bg/earth.jpg");
        plane = new Image("res/actors/plane.png");
    }
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int deltaMS) throws SlickException {
        Input input = gc.getInput();
 
        if(input.isKeyDown(Input.KEY_A)){
            plane.rotate(-0.2f * deltaMS);
        } 
        if(input.isKeyDown(Input.KEY_D)) {
            plane.rotate(0.2f * deltaMS);
        } 
        if(input.isKeyDown(Input.KEY_W)){
            float hip = 0.4f * deltaMS; 
            float rotation = plane.getRotation(); 
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
            float rotation = plane.getRotation(); 
            x-= hip * Math.sin(Math.toRadians(rotation));
            y+= hip * Math.cos(Math.toRadians(rotation));
        } 
        if(input.isKeyDown(Input.KEY_2)) {
            scale += (scale >= 5.0f) ? 0 : 0.1f;
            plane.setCenterOfRotation(plane.getWidth()/2.0f*scale, plane.getHeight()/2.0f*scale);
        }
        if(input.isKeyDown(Input.KEY_1)) {
            scale -= (scale <= 1.0f) ? 0 : 0.1f;
            plane.setCenterOfRotation(plane.getWidth()/2.0f*scale, plane.getHeight()/2.0f*scale);
        }
    }
 
    public void render(GameContainer container, StateBasedGame sbg, Graphics g)  throws SlickException {
        land.draw(0, 0);
        plane.draw(x, y, scale);
    }    
}
