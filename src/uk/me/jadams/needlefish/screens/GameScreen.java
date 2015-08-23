package uk.me.jadams.needlefish.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import uk.me.jadams.needlefish.Assets;
import uk.me.jadams.needlefish.B2DObjectFactory;
import uk.me.jadams.needlefish.CollisionData;
import uk.me.jadams.needlefish.FixtureTypes;
import uk.me.jadams.needlefish.GameStage;
import uk.me.jadams.needlefish.MenuStage;
import uk.me.jadams.needlefish.MyContactListener;
import uk.me.jadams.needlefish.Needlefish;
import uk.me.jadams.needlefish.Particles;
import uk.me.jadams.needlefish.Scoring;
import uk.me.jadams.needlefish.Utils;
import uk.me.jadams.needlefish.components.BodyComponent;
import uk.me.jadams.needlefish.components.InputComponent;
import uk.me.jadams.needlefish.systems.EnemySpawnSystem;
import uk.me.jadams.needlefish.systems.InputSystem;
import uk.me.jadams.needlefish.systems.PlayerShootingSystem;
import uk.me.jadams.needlefish.systems.TrackPlayerSystem;
import uk.me.jadams.needlefish.systems.collision.EnemyBulletSystem;
import uk.me.jadams.needlefish.systems.collision.PlayerCollisionSystem;

public class GameScreen implements Screen
{
    private final OrthographicCamera camera;

    private final OrthographicCamera uiCamera;

    private final SpriteBatch batch;

    private PooledEngine engine;

    private Body walls;

    private World world;

    private Entity player;

    private Particles playerExplode;
    private Particles enemyExplode;
    private Particles bulletWall;

    private final Scoring scoring;

    private final GameStage gameStage;
    private final MenuStage menuStage;
    private Stage currentStage;

    private boolean justOver;
    private boolean isOver;

    private InputSystem inputSystem;
    private TrackPlayerSystem trackPlayerSystem;
    private PlayerCollisionSystem playerCollisionSystem;
    private EnemySpawnSystem enemySpawnSystem;
    private PlayerShootingSystem playerShootingSystem;

    public GameScreen(Needlefish needlefish, OrthographicCamera camera, SpriteBatch batch, Scoring scoring)
    {
        this.camera = camera;
        this.batch = batch;
        this.scoring = scoring;

        uiCamera = new OrthographicCamera(1920, 1080);

        gameStage = new GameStage(uiCamera, batch);
        menuStage = new MenuStage(needlefish, uiCamera, batch);
    }

    @Override
    public void show()
    {
        isOver = false;
        justOver = false;

        scoring.start();

        engine = new PooledEngine();

        camera.setToOrtho(false, 192, 108);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        gameStage.setHighScore(scoring.getHighScoreString());
        currentStage = gameStage;
        Gdx.input.setInputProcessor(currentStage);

        world = new World(new Vector2(0, 0), true);

        playerExplode = new Particles("effect_player_explode.p");
        enemyExplode = new Particles("effect_enemy_explode.p");
        bulletWall = new Particles("effect_bullet_wall.p");

        MyContactListener contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        Body playerBody = B2DObjectFactory.newPlayer(world);

        walls = B2DObjectFactory.bounds(world);

        player = engine.createEntity();
        player.add(new BodyComponent(playerBody));
        player.add(new InputComponent());

        inputSystem = new InputSystem(camera);
        engine.addSystem(inputSystem);

        trackPlayerSystem = new TrackPlayerSystem(playerBody);
        engine.addSystem(trackPlayerSystem);

        playerCollisionSystem = new PlayerCollisionSystem(this, world, engine, playerExplode);
        engine.addSystem(playerCollisionSystem);

        engine.addSystem(new EnemyBulletSystem(world, engine, enemyExplode));

        enemySpawnSystem = new EnemySpawnSystem(world, engine);
        engine.addSystem(enemySpawnSystem);

        playerShootingSystem = new PlayerShootingSystem(world, playerBody);
        engine.addSystem(playerShootingSystem);

        engine.addEntity(player);
    }

    @Override
    public void render(float delta)
    {
        delta = 1 / 60f;

        world.step(delta, 6, 2);

        wallCollisions();

        engine.update(delta);

        batch.begin();

        if (!isOver)
        {
            scoring.update(delta);
            gameStage.setScore(scoring.getScoreString());
        }

        // Render particle effects.
        playerExplode.render(batch, delta);
        enemyExplode.render(batch, delta);
        bulletWall.render(batch, delta);

        drawWalls();
        Box2DSprite.draw(batch, world);

        batch.end();

        currentStage.act(delta);
        currentStage.draw();

        if (justOver)
        {
            scoring.end();
            menuStage.setHighScore(scoring.getHighScoreString());
            menuStage.setLastScore(scoring.getScoreString());
            currentStage = menuStage;
            Gdx.input.setInputProcessor(currentStage);
            stopSystems();
            justOver = false;
        }
    }

    @Override
    public void resize(int width, int height)
    {
        // TODO - something here to get the world camera nicely resized.

        gameStage.getViewport().update(width, height, true);
        menuStage.getViewport().update(width, height, true);
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
        gameStage.dispose();
        menuStage.dispose();
        world.dispose();
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
                        float x = collisonData.getX();
                        float y = collisonData.getY();
                        bulletWall.start(x, y);
                        Assets.wallBounce.play(0.6f);
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

                batch.draw(Assets.wall, vertex.x, vertex.y, 0, 0, l, 1f, 1, 1, angle, 0, 0, 1, 1, false, false);

                batch.draw(Assets.vertex, vertex.x - 1.2f, vertex.y - 1.2f, 2.4f, 2.4f);

                lastVertex.x = vertex.x;
                lastVertex.y = vertex.y;
            }
        }
    }

    public void over()
    {
        isOver = true;
        justOver = true;
    }

    public void stopSystems()
    {
        engine.removeSystem(inputSystem);
        engine.removeSystem(trackPlayerSystem);
        engine.removeSystem(playerCollisionSystem);
        engine.removeSystem(enemySpawnSystem);
        engine.removeSystem(playerShootingSystem);
    }
}
