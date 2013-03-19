/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;

import MiniGame.Camera;
import MiniGame.world.World;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;

/**
 *
 * @author Ty
 */
public class Tank extends HealthEntity {
    private long currentLifeTimeMS = 0;
    private long lastShotFiredTimeMS = 0;
    private Image upperTank= new Image("res/actors/upperTank.png");
    private Image lowerTank= new Image("res/actors/lowerTank.png");
    private Input userInput;
    private double angleToTurn;
    private float tankX,tankY,xDist,yDist, mouseX,mouseY;
    private String mousex,mousey,ang,s_tankX,s_tankY;
    
    @Override
    public boolean update(GameContainer slickContainer, int deltaMS){
        try{
            Input input = slickContainer.getInput();
            this.setInput(slickContainer.getInput());
            this.direction();
            this.tankMovement(slickContainer.getInput(), deltaMS);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    
    @Override
    public void render(GameContainer slickContainer, Graphics g, Camera camera){
        g.drawString(this.drawInfomration(), 100, 10);
        this.drawTank();
    }
    
    public Tank(World world, float x, float z) throws SlickException {
        super(world, x, z);
        upperTank.setCenterOfRotation(38,38);
        lowerTank.setCenterOfRotation(49,32);
        tankX=100;
        tankY=100;
        lowerTank.draw(tankX,tankY);
        upperTank.draw(tankX,tankY);
    }
    
    private void setInput(Input userI){
        mouseX=userI.getMouseX();
        mouseY=userI.getMouseY();
    }
    
    private void direction(){
        xDist=mouseX-tankX;
        yDist=mouseY-tankY;
        angleToTurn=Math.toDegrees(Math.atan2(yDist,xDist));
    }
    
    private void drawTank(){
        lowerTank.draw(tankX-9,tankY+6);
        upperTank.draw(tankX,tankY);
        upperTank.setRotation((float)angleToTurn);
    }
    
    private String drawInfomration(){
        mousex=String.valueOf(mouseX);
        mousey=String.valueOf(mouseY);
        ang=String.valueOf(angleToTurn);
        s_tankX=String.valueOf(tankX);
        s_tankY=String.valueOf(tankY);
        return "Mouse X: "+mousex+"Mouse Y: "+mousey+" Angle: "+ang+"\nCurrent X: "+s_tankX+" Current Y: "+s_tankY;
    }
    
    private void tankMovement(Input userI, int delta){
        if(userI.isKeyDown(Input.KEY_SPACE)){
            if (currentLifeTimeMS > lastShotFiredTimeMS + getFireDelay()) {
                lastShotFiredTimeMS = currentLifeTimeMS;
                double angle = Math.atan2(yDist,xDist);
                //Vector3f shootDir = new Vector3f((float) Math.cos(angle - Math.PI * .05), (float) Math.sin(angle - Math.PI * .05), 0);
                Vector3f shootDir = new Vector3f((float) mouseX, (float) mouseY, 0);
                shootBullet(yDist, xDist, 0, shootDir); 
                System.out.println("Tank Shot");
            }
        }
        if(userI.isKeyDown(Input.KEY_A)){
            lowerTank.rotate(-.04f*delta);
        }
        if(userI.isKeyDown(Input.KEY_D)){
            lowerTank.rotate(.04f*delta);
         
        }
        if(userI.isKeyDown(Input.KEY_W)){
            float hip=.04f*delta;
            tankX+=hip*Math.sin(Math.toRadians(lowerTank.getRotation()));
            tankY-=hip*Math.cos(Math.toRadians(lowerTank.getRotation()));
        }
        if(userI.isKeyDown(Input.KEY_S)){
            float hip=-.04f*delta;
            tankX+=hip*Math.sin(Math.toRadians(lowerTank.getRotation()));
            tankY-=hip*Math.cos(Math.toRadians(lowerTank.getRotation()));        
        }
    }
    
//     private void shootBullet(float tx, float tz, int shootArm) {
//        Vector3f projectileDirection = new Vector3f(playerDirection.x, 0, playerDirection.y);
//        shootBullet(tx, tz, shootArm, projectileDirection);
//    }

    private void shootBullet(float tx, float tz, int shootArm, Vector3f projectileDirection) {
        double targetDistance = Math.sqrt((x - tx) * (x - tx) + (z - tz) * (z - tz));
        float shootHeight = y;
        projectileDirection.y = -shootHeight / ((float) targetDistance + 40.0f);
        float bulletSpeed = 600;
        Projectile bullet = new Projectile(world, tankX , tankY , 0 , projectileDirection, bulletSpeed);
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
