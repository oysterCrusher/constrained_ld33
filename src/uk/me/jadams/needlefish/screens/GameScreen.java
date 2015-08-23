package uk.me.jadams.needlefish.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import uk.me.jadams.needlefish.Assets;
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
        engine.addSystem(new EnemyBulletSystem(world, engine));
        engine.addSystem(new CollisionSystem());

        engine.addEntity(player);

        b2dDebugRenderer = new Box2DDebugRenderer();

        B2DObjectFactory.ai(engine, world);
    }

    @Override
    public void show()
    {
        camera.setToOrtho(false, 192, 108);
//        camera.position.x = 192 / 2;
//        camera.position.y = 0;
//        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta)
    {
        world.step(delta, 6, 2);
        wallCollisions();
        engine.update(delta);

        batch.begin();

        playerExplode.render(batch, delta);
        drawWalls();
        Box2DSprite.draw(batch, world);

        batch.end();
//        b2dDebugRenderer.render(world, camera.combined);
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

    private void drawWalls()
    {
        Array<Fixture> fixtures = walls.getFixtureList();
        for (Fixture fixture : fixtures)
        {
            ChainShape shape = (ChainShape) fixture.getShape();

            int nVertices = shape.getVertexCount();

            if (nVertices <= 1)
            {
                return;
            }

            Vector2 lastVertex = new Vector2();
            shape.getVertex(0, lastVertex);
            
            for (int i = 1; i < nVertices; i++)
            {
                Vector2 vertex = new Vector2();
                shape.getVertex(i, vertex);

                float w = Math.abs(vertex.x - lastVertex.x);
                float h = Math.abs(vertex.y - lastVertex.y);
                
                float l = (float) Math.sqrt(w * w + h * h);

                float angle = 270 - (float) (Math.atan2((vertex.x - lastVertex.x), (vertex.y - lastVertex.y)) / Math.PI * 180);

                batch.draw(Assets.wall, vertex.x, vertex.y, 0, 0, l, 0.7f, 1, 1, angle, 0, 0, 1, 1, false, false);
                
                float vx = vertex.x + (vertex.x - lastVertex.x) / l * 0.3f;
                float vy = vertex.y + (vertex.y - lastVertex.y) / l * 0.3f;
                batch.draw(Assets.vertex, vertex.x - 0.9f, vertex.y - 0.9f, 1.8f, 1.8f);

                lastVertex.x = vertex.x;
                lastVertex.y = vertex.y;
                
                
            }
        }
    }
}
