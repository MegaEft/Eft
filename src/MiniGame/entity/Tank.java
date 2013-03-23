/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;

import MiniGame.Camera;
import MiniGame.world.World;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author Ty
 */
public class Tank extends HealthEntity {
    private long currentLifeTimeMS = 0;
    private long lastShotFiredTimeMS = 0;
    private long lastTreadCreated = 0; 
    private Image upperTank = new Image("res/actors/upperTank.png");
    private Image lowerTank = new Image("res/actors/lowerTank.png");
    private Image im = new Image("res/actors/treads.png");
    private double angleToTurn;
    private float tankX,tankY,xDist,yDist, mouseX,mouseY;
    private String mousex,mousey,ang,s_tankX,s_tankY;
    
    @Override
    public boolean update(GameContainer slickContainer, int deltaMS){
        try{
            currentLifeTimeMS += deltaMS;
            this.setInput(slickContainer.getInput());
            this.direction();
            this.tankMovement(slickContainer.getInput(), deltaMS);
        }
        catch (Exception e){
            System.out.println("Tank Error");
            return false;
        }
        return true;
    }
    
    @Override
    public void render(GameContainer slickContainer, Graphics g, Camera camera){
        g.drawString(this.drawInfomration(), 100, 10);
        this.drawTank();
        g.draw(this.entityPoly);
    }
    
    public Tank(World world, float x, float y, float z) throws SlickException {
        super(world, x, z);
        upperTank.setCenterOfRotation(15,upperTank.getHeight()/2);
        lowerTank.setCenterOfRotation(lowerTank.getWidth()/2,lowerTank.getHeight()/2);
        
        if(x<=-1 || x>1000){
            tankX=500;
        }else
            tankX=x;
        
        if(y<=-1 || y>1000){
            tankX=300;
        }else
            tankY=y;
        
        this.entityPoly =  new Polygon(new float[]{tankX,tankY,tankX+lowerTank.getWidth(),tankY,tankX+lowerTank.getWidth(),tankY+lowerTank.getHeight(),tankX,tankY+lowerTank.getHeight()});
        updatePoly(tankX,tankY);
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
        lowerTank.draw(tankX,tankY);
        upperTank.draw(tankX+15,tankY+lowerTank.getCenterOfRotationY()/2);
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
    
    private void tankMovement(Input userI, int delta) throws SlickException{
        if(userI.isKeyDown(Input.KEY_LEFT)){
            lowerTank.rotate(-.15f*delta);
        }
        if(userI.isKeyDown(Input.KEY_RIGHT)){
            lowerTank.rotate(.15f*delta);         
        }
        if(userI.isKeyDown(Input.KEY_UP)){
            float hip=.15f*delta;
            tankX+=hip*Math.sin(Math.toRadians(lowerTank.getRotation()));
            tankY-=hip*Math.cos(Math.toRadians(lowerTank.getRotation()));
            updatePoly(tankX,tankY);
            if (wallCollision(this.entityPoly)){
                    tankY+= hip * Math.cos(Math.toRadians(lowerTank.getRotation()));
                    tankX-= hip * Math.sin(Math.toRadians(lowerTank.getRotation()));
                    updatePoly(tankX,tankY);
            }    
            createTread(delta);
        }
        if(userI.isKeyDown(Input.KEY_DOWN)){
            float hip=-.15f*delta;
            tankX+=hip*Math.sin(Math.toRadians(lowerTank.getRotation()));
            tankY-=hip*Math.cos(Math.toRadians(lowerTank.getRotation()));
            updatePoly(tankX,tankY);
            if (wallCollision(this.entityPoly)){
                tankY-= hip * Math.cos(Math.toRadians(lowerTank.getRotation()));
                tankX+= hip * Math.sin(Math.toRadians(lowerTank.getRotation()));
                updatePoly(tankX,tankY);
            }    
            createTread(delta);
        }
        if(userI.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
            if (this.currentLifeTimeMS > this.lastShotFiredTimeMS + getFireDelay()) {
                this.lastShotFiredTimeMS = this.currentLifeTimeMS;
                Vector3f shootDir = new Vector3f((float) (Math.sin(Math.PI/180 * (angleToTurn+90))),(float) (Math.cos(Math.PI/180 * (angleToTurn+90))), 0);
                shootBullet(tankX+38, -tankY-38, 0, shootDir);
            }
        }
    }

    private void shootBullet(float tx, float tz, int shootArm, Vector3f projectileDirection) {
        float bulletSpeed = 600;
        Projectile bullet = new Projectile(world, tx , tz , 0 , projectileDirection, bulletSpeed);
        world.addEntity(bullet);
        
        
    }
    
    public int getFireDelay() {
        int level = 1;
        if (level > 3) {
            level = 3;
        }
        return 250 - (25 * level);
    }
    public void createTread(int delta){
        if(this.currentLifeTimeMS>this.lastTreadCreated){
            this.lastTreadCreated=50+this.currentLifeTimeMS;
            try {
                
                TrailEntity trail = new TrailEntity(world,tankX,tankY,0f,im,delta,lowerTank.getRotation(),lowerTank.getCenterOfRotationX(),lowerTank.getCenterOfRotationY(),lowerTank.getWidth(),2500);
                world.addEntity(trail);
            } catch (SlickException ex) {
                System.out.println("Tread Error "+ex);
            }
            
        }
    }
}
