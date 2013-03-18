package MiniGame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
/**
 *
 * @author Ty
 */
public class StateController extends StateBasedGame {
    public StateController() {
        super("MiniGame");
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        addState(new MainGame());
    }

}
