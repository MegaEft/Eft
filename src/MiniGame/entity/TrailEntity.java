/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.entity;
import org.newdawn.slick.*;
import MiniGame.world.*;
import MiniGame.Camera;
import org.newdawn.slick.Graphics;
/**
 *
 * @author A Swell Kinda Guy
 */
public class TrailEntity extends Entity {
    protected float rotation;
    protected long currentLifeTimeMs = 0;
    protected long endLifeTimeMs = 0;
    protected Image trail =null;
    protected float trailX;
    protected float trailY;
    protected float direction;
    protected float distance;
    protected float centerOfSpawnObjX;
    protected float centerOfSpawnObjY;
    protected Color fadeColor;
    /*
     *
     * @throws SlickException
     */
    protected TrailEntity(World world, float x, float y, float z,Image image,int deltaMS, float rotation,float objCenterX, float objCenterY,float distance,int lifespan) throws SlickException {
        super(world,x,z);
        this.endLifeTimeMs=lifespan+deltaMS;
        
        this.trailX= x;
        this.trailY= y;
     
        this.centerOfSpawnObjX=objCenterX;
        this.centerOfSpawnObjY=objCenterY;
       
        this.trail = image;
        this.distance=distance;
        this.direction=rotation;
        this.fadeColor = new Color(1f,1f,1f,1f);
        
        this.entityType = EntityType.TrailEntity;
    }
    @Override
    public boolean update(GameContainer gc,int deltaMS){
       
       if( (currentLifeTimeMs+=deltaMS)>endLifeTimeMs )
            this.fadeColor.a-=deltaMS*.0005f;
       
       if(fadeColor.a<=0)
            this.setRemoved();
        
        return true;
    }
    @Override
    public void render(GameContainer gc, Graphics g, Camera camera){
       
        g.rotate(this.trailX+centerOfSpawnObjX, this.trailY+centerOfSpawnObjY, this.direction);
        g.drawImage(this.trail, this.trailX, this.trailY,this.fadeColor);
        g.drawImage(this.trail, this.trailX+distance-this.trail.getWidth(), this.trailY,this.fadeColor);
        g.rotate(this.trailX+centerOfSpawnObjX, this.trailY+centerOfSpawnObjY, -this.direction);
    }
}
