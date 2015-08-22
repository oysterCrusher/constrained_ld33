package uk.me.jadams.needlefish.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import uk.me.jadams.needlefish.B2DObjectFactory;
import uk.me.jadams.needlefish.CollisionData;
import uk.me.jadams.needlefish.FixtureTypes;
import uk.me.jadams.needlefish.MyContactListener;
import uk.me.jadams.needlefish.Particles;
import uk.me.jadams.needlefish.Utils;
import uk.me.jadams.needlefish.components.BodyComponent;
import uk.me.jadams.needlefish.components.InputComponent;
import uk.me.jadams.needlefish.systems.CollisionSystem;
import uk.me.jadams.needlefish.systems.InputSystem;
import uk.me.jadams.needlefish.systems.aimovement.TrackPlayerSystem;
import uk.me.jadams.needlefish.systems.collision.EnemyBulletSystem;
import uk.me.jadams.needlefish.systems.collision.PlayerBulletSystem;

public class GameScreen implements Screen
{
    private final OrthographicCamera camera;

    private final SpriteBatch batch;

    private final PooledEngine engine;

    private final Body walls;

    private final World world;

    private final Entity player;

    private final Box2DDebugRenderer b2dDebugRenderer;
    
    private final Particles playerExplode;

    public GameScreen(OrthographicCamera camera, SpriteBatch batch)
    {
        this.camera = camera;
        this.batch = batch;

        engine = new PooledEngine();

        world = new World(new Vector2(0, 0), true);

        playerExplode = new Particles("effect_player_explode.p");

        MyContactListener contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        Body playerBody = B2DObjectFactory.newPlayer(world);

        walls = B2DObjectFactory.bounds(world);

        player = engine.createEntity();
        player.add(new BodyComponent(playerBody));
        player.add(new InputComponent());

        engine.addSystem(new InputSystem(camera, world));
        engine.addSystem(new TrackPlayerSystem(playerBody));
        engine.addSystem(new PlayerBulletSystem(playerExplode));
        engine.addSystem(new EnemyBulletSystem());
        engine.addSystem(new CollisionSystem());

        engine.addEntity(player);

        b2dDebugRenderer = new Box2DDebugRenderer();

        B2DObjectFactory.ai(engine, world);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        world.step(delta, 6, 2);
        wallCollisions();
        engine.update(delta);

        batch.begin();
        
        Box2DSprite.draw(batch, world);
        playerExplode.render(batch, delta);
        batch.end();
        b2dDebugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }

    private void wallCollisions()
    {
        Array<Fixture> fixtures = walls.getFixtureList();

        for (Fixture fixture : fixtures)
        {
            CollisionData collisonData = Utils.getCollisionData(fixture);

            if (collisonData != null)
            {
                if (!collisonData.isProcessed())
                {
                    if (collisonData.getAgainst() == FixtureTypes.BULLET)
                    {
                        // TODO - Particle effect?
                    }

                    collisonData.markProcessed();
                }
            }
            else
            {
                System.out.println("body missing collision data.");
            }
        }
    }
}
