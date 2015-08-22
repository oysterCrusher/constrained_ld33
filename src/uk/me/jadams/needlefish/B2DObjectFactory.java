package uk.me.jadams.needlefish;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class B2DObjectFactory
{
    private static final short GROUP_BULLET = -1;

    private B2DObjectFactory()
    {

    }

    public static Body newPlayer(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(192 / 2, 108 / 2);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(1.5f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1f;
        fixDef.restitution = 0;
        fixDef.friction = 0;

        body.createFixture(fixDef);
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
        bodyDef.position.set(10, 10);

        Body body = world.createBody(bodyDef);

        ChainShape shape = new ChainShape();

        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(0, 0);
        vertices[1] = new Vector2(172, 0);
        vertices[2] = new Vector2(172, 88);
        vertices[3] = new Vector2(0, 88);

        shape.createLoop(vertices);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1f;

        body.createFixture(fixDef);

        shape.dispose();

        return body;
    }

    public static Body projectile(World world, float x, float y, float angle)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        
        float sx = (float) (x + Math.cos(angle) * 2);
        float sy = (float) (y + Math.sin(angle) * 2);
        bodyDef.position.set(sx, sy);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1f;
        fixDef.restitution = 1;
        fixDef.friction = 0;
        fixDef.filter.groupIndex = GROUP_BULLET;

        body.createFixture(fixDef);
        body.setFixedRotation(true);
        shape.dispose();

        Box2DSprite sprite = new Box2DSprite(Assets.bullet);
        body.setUserData(sprite);

        float vx = (float) (Math.cos(angle) * 10);
        float vy = (float) (Math.sin(angle) * 10);

        body.setLinearVelocity(vx, vy);
        return body;
    }
}
