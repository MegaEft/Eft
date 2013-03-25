/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniGame.world;
import java.util.*;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import MiniGame.entity.*;
import MiniGame.gui.*;
import MiniGame.Camera;
import MiniGame.util.BlockMap;
import MiniGame.util.Block;
import MiniGame.gui.ShortcutCommands;
/**
 *
 * @author Ty
 */
public class World {

    private List<Entity> entities = new ArrayList<Entity>();
    private List<Entity> newEntities = new ArrayList<Entity>();
    private List<Entity> particles = new ArrayList<Entity>();
    private List<Entity> newParticles = new ArrayList<Entity>();
    private GameDirector gameDirector;
    private Player player;
    private Tank tank;
    private ShortcutCommands shortCuts;
    private Image backgroundImage;
    private final Camera camera;
    private Starfield starfield;
    private float width;
    private float height;
    private float WorldCenterZ;
    private float WorldCutZ;
    private long gameTime;
    public BlockMap map;

    private boolean gameOver;
    private boolean isVictory;
    private int winTime;

    public World(Camera camera) {
        this.camera = camera;
        gameDirector = new GameDirector(this);
    }

    public void init(GameContainer container) throws SlickException {
        this.starfield = new Starfield((int) width, (int) height);
        Entity.shadowImage = new Image("res/actors/shadow.png");
        //backgroundImage = new Image("res/bg/earth.jpg");
        shortCuts=new ShortcutCommands();
        addEntity(tank = new Tank(this, 500, 300, 200));
        addEntity(player = new Player(this, container.getWidth() / 2 + 10, container.getHeight() / 2));
        this.camera.setConstraints(0, 0, width - container.getWidth(), height - container.getHeight());
        this.camera.centerOnConstraints();
        gameDirector.init(container);
        map = new BlockMap("res/map/basemap.tmx");
    }

    public void update(GameContainer container, int deltaMS) {
        gameTime += deltaMS;
        gameDirector.update(container, deltaMS);
        starfield.update(container, deltaMS);
        shortCuts.keyPressed(container.getInput());
        
        updateEntityList(container, deltaMS, entities, newEntities, true);
        updateEntityList(container, deltaMS, particles, newParticles, false);

        if (player.isRemoved()) {
            gameOver = true;
            isVictory = false;
        } else if (gameDirector.isWon()) {
            winTime += deltaMS;
            if (winTime > 4000) {
                gameOver = true;
                isVictory = true;
            }
        }
        shortCuts.update(container, deltaMS);
    }

    private void updateEntityList(GameContainer container, int deltaMS, List<Entity> ents, List<Entity> newEnts, boolean checkCollisions) {
        Iterator<Entity> e = ents.iterator();
        while (e.hasNext()) {
            Entity entity = e.next();
            if (!entity.update(container, deltaMS) || entity.isRemoved()) {
                entity.setRemoved();
                e.remove();
            } else if (checkCollisions) {
                entity.checkCollisions(entities);
            }
        }
        ents.addAll(newEnts);
        newEnts.clear();
    }

    public void render(GameContainer container, Graphics g) {
        
        g.setColor(Color.black);
        BlockMap.tmap.render(0,0);
        g.drawRect(0, 0, container.getWidth(), container.getHeight());
        //starfield.render(container, g, camera);
        ArrayList<Entity> renderableEntities = new ArrayList<Entity>();
        renderableEntities.addAll(entities);
        renderableEntities.addAll(particles);
        Collections.sort(renderableEntities, entitySorter);
        // render entities that are "under" the pizza
        Iterator<Entity> iterator = renderableEntities.iterator();
        while (iterator.hasNext()) {
            Entity r = iterator.next();
            if (r.getY() < 0 && r.getZ() < WorldCutZ) {
                r.render(container, g, camera);
                iterator.remove();
            }
        }
        //g.drawImage(backgroundImage, -camera.getX(), -camera.getY(), -camera.getX() + backgroundImage.getWidth(), -camera.getY() + backgroundImage.getHeight(), 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());

        for (Entity r : renderableEntities) {
            r.renderGroundLayer(container, g, camera);
        }
        for (Entity r : renderableEntities) {
            r.render(container, g, camera);
        }
        shortCuts.render(container, g);
    }

    public long getGameTime() {
        return gameTime;
    }

    public void addEntity(Entity e) {
        newEntities.add(e);
    }

    public void addParticle(Entity e) {
        newParticles.add(e);
    }

    public Camera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesInRange(float x, float z, float range) {
        float rangeSqr = range * range;
        ArrayList<Entity> results = new ArrayList<Entity>();

        for (Entity e : entities) {
            if (!e.isRemoved()) {
                float dx = e.getX() - x;
                float dz = e.getZ() - z;
                float distSqr = dx * dx + dz * dz;
                if (distSqr < rangeSqr) {
                    results.add(e);
                }
            }
        }
        return results;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private class EntityZedSorter implements Comparator<Entity> {
        @Override
        public int compare(Entity a, Entity b) {
            if (a.getY() < 0 && b.getY() >= 0 && a.getZ() < WorldCutZ) {
                return -1;
            } else if (a.getY() >= 0 && b.getY() < 0 && b.getZ() < WorldCutZ) {
                return 1;
            }
            if (a.getZ() < b.getZ()) {
                return -1;
            }
            if (a.getZ() > b.getZ()) {
                return 1;
            }
            return 0;
        }
    }

    private final EntityZedSorter entitySorter = new EntityZedSorter();

    public int getNumberOfEnimies() {
        int numOfEnemies = 0;
        Iterator<Entity> e = entities.iterator();
        while (e.hasNext()) {
            Entity entity = e.next();
            if (entity.getType() == Entity.EntityType.EnemyEntity) {
                numOfEnemies++;
            }
        }
        return numOfEnemies;
    }

    public boolean isVictory() {
        return isVictory;
    }
}
