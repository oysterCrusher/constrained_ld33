package uk.me.jadams.needlefish.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import uk.me.jadams.needlefish.B2DObjectFactory;
import uk.me.jadams.needlefish.components.BodyComponent;
import uk.me.jadams.needlefish.components.InputComponent;
import uk.me.jadams.needlefish.systems.InputSystem;

public class GameScreen implements Screen
{
    private final OrthographicCamera camera;

    private final SpriteBatch batch;

    private final PooledEngine engine;

    private final World world;

    private final Entity player;

    private final Box2DDebugRenderer b2dDebugRenderer;

    public GameScreen(OrthographicCamera camera, SpriteBatch batch)
    {
        this.camera = camera;
        this.batch = batch;

        engine = new PooledEngine();

        world = new World(new Vector2(0, 0), true);

        Body playerBody = B2DObjectFactory.newPlayer(world);
        
        B2DObjectFactory.bounds(world);

        player = engine.createEntity();
        player.add(new BodyComponent(playerBody));
        player.add(new InputComponent());

        // Entity randomSquare = engine.createEntity();
        // randomSquare.add(new BodyComponent(randomSquareBody));

        engine.addSystem(new InputSystem(camera, world));
        // engine.addSystem(new RenderingSystem(mrigal.batch, camera));

        engine.addEntity(player);
        // engine.addEntity(randomSquare);

        b2dDebugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        world.step(delta, 6, 2);

        engine.update(delta);

        batch.begin();
        Box2DSprite.draw(batch, world);
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
}
