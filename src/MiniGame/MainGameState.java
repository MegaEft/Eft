/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame;
/**
 *
 * @author Ty
 */

import MiniGame.world.World;
import org.newdawn.slick.*; 
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;
public class MainGameState extends BasicGameState{
    public static final int ID = 2;
    Image land = null;
    private Camera camera;
    private World gameWorld;
    
    public MainGameState(){
    }
    
    void startGame(GameContainer container) throws SlickException {
        camera = new Camera();
        gameWorld = new World(camera);
        gameWorld.init(container);
    }
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        startGame(container);
    }
 
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
        
    }
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int deltaMS) throws SlickException {
        if (gameWorld != null) {
            camera.update(deltaMS);
            gameWorld.update(gc, deltaMS);
        }
    }
 
    public void render(GameContainer container, StateBasedGame sbg, Graphics g)  throws SlickException {
        if (gameWorld != null) {
            gameWorld.render(container, g);
        }
    }    
}
