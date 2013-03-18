/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame;
import org.newdawn.slick.AppGameContainer;
/**
 *
 * @author Ty
 */
public class MainClass {
    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new StateController());
        app.setDisplayMode(1280, 720, false);
        app.start();
    }
}
