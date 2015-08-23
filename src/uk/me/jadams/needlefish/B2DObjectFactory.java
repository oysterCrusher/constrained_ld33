package uk.me.jadams.needlefish;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import uk.me.jadams.needlefish.components.AIMovementTrackPlayerComponent;
import uk.me.jadams.needlefish.components.BodyComponent;

public class B2DObjectFactory
{
    private static final short CATEGORY_PLAYER = 0x0001;
    private static final short CATEGORY_BULLET = 0x0002;
    private static final short CATEGORY_AI = 0x0004;
    private static final short CATEGORY_WALL = 0x0008;
    
    private static final short MASK_BULLET = CATEGORY_AI | CATEGORY_WALL | CATEGORY_PLAYER;
    private static final short MASK_AI = CATEGORY_BULLET | CATEGORY_PLAYER | CATEGORY_AI;

    private B2DObjectFactory()
    {

    }

    public static Body newPlayer(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Maybe should be KinematicBody?
        bodyDef.position.set(192 / 2, 108 / 2);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(1.5f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.1f;
        fixDef.restitution = 0;
        fixDef.friction = 0;
        fixDef.filter.categoryBits = CATEGORY_PLAYER;

        Fixture fixture = body.createFixture(fixDef);
        fixture.setUserData(new CollisionData(FixtureTypes.PLAYER));
        body.setFixedRotation(true);
        shape.dispose();

        Box2DSprite sprite = new Box2DSprite(Assets.player);
        body.setUserData(sprite);

        return body;
    }

    public static Body bounds(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        ChainShape shape = new ChainShape();

        Vector2[] vertices = new Vector2[6];
        vertices[0] = new Vector2(40, 40);
        vertices[1] = new Vector2(192 / 2, 10);
        vertices[2] = new Vector2(192 - 40, 40);
        vertices[3] = new Vector2(192 - 40, 68);
        vertices[4] = new Vector2(192 / 2, 98);
        vertices[5] = new Vector2(40, 68);

        shape.createLoop(vertices);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.1f;
        fixDef.filter.categoryBits = CATEGORY_WALL;

        Fixture fixture = body.createFixture(fixDef);
        fixture.setUserData(new CollisionData(FixtureTypes.WALL));

        shape.dispose();
        
        return body;
    }

    public static Body projectile(World world, float x, float y, float angle)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        
        float sx = (float) (x + Math.cos(angle) * 3);
        float sy = (float) (y + Math.sin(angle) * 3);
        bodyDef.position.set(sx, sy);

        Body body = world.createBody(bodyDef);
        body.setBullet(true);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.4f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.1f;
        fixDef.restitution = 1;
        fixDef.friction = 0;
        fixDef.filter.categoryBits = CATEGORY_BULLET;
        fixDef.filter.maskBits = MASK_BULLET;
        
        Fixture fixture = body.createFixture(fixDef);
        fixture.setUserData(new CollisionData(FixtureTypes.BULLET));

        body.setFixedRotation(true);
        shape.dispose();

        Box2DSprite sprite = new Box2DSprite(Assets.bullet);
        body.setUserData(sprite);

        float vx = (float) (Math.cos(angle) * 30);
        float vy = (float) (Math.sin(angle) * 30);

        body.setLinearVelocity(vx, vy);
        return body;
    }
    
    public static Body ai(PooledEngine engine, World world, Vector2 pos)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(pos);
        bodyDef.angle = (float) (Math.PI / 2f);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(1.5f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.1f;
        fixDef.restitution = 0;
        fixDef.friction = 0;
        fixDef.filter.categoryBits = CATEGORY_AI;
        fixDef.filter.maskBits = MASK_AI;

        Fixture fixture = body.createFixture(fixDef);
        fixture.setUserData(new CollisionData(FixtureTypes.AI));
        body.setFixedRotation(true);
        shape.dispose();

        body.setLinearVelocity(10, 0);

        Box2DSprite sprite = new Box2DSprite(Assets.enemy);
        body.setUserData(sprite);
        
        Entity aiEntity = engine.createEntity();
        aiEntity.add(new BodyComponent(body));
        aiEntity.add(new AIMovementTrackPlayerComponent());
        engine.addEntity(aiEntity);

        return body;
    }
}
