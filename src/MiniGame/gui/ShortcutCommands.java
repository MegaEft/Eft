/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.gui;
import org.newdawn.slick.*;
/**
 *
 * @author Josh
 */
public class ShortcutCommands {
    protected boolean isPPressed=false;
    protected boolean isRPressed=false;
    protected String message="PAUSED";
    
    public ShortcutCommands(){}
    
    public void keyPressed(Input keyPressed){
        if(keyPressed.isKeyPressed(Input.KEY_P)){
            if(this.isPPressed==true)
                this.isPPressed=false;
            else
                this.isPPressed=true;
        }

    }
    public void update(GameContainer slickContainer, int deltaMS) {
       if(this.isPPressed==true)
           slickContainer.pause();
       else
           slickContainer.resume();
    }
    public void render(GameContainer slickContainer, Graphics g){
        if(this.isPPressed==true){
            g.setColor(Color.white);
            g.drawString(this.message, slickContainer.getScreenWidth()/2 - message.length(),slickContainer.getScreenHeight()/2);
        }
    }
    
}
