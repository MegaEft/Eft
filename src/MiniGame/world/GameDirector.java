/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.world;
import java.util.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import MiniGame.entity.*;

/**
 * 
 * @author Ty
 */
public class GameDirector {
    private World gameWorld;

    public GameDirector(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void init(GameContainer container) {
     
    }

    public void update(GameContainer container, int deltaMS) {

    }

    public void renderGUI(GameContainer container, Graphics g) {

    }
    
    public boolean isWon() {
        return false;
    }
}
