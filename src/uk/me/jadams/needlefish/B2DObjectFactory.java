package uk.me.jadams.needlefish;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class B2DObjectFactory
{
    private static final BodyDef bodyDef = new BodyDef();

    private B2DObjectFactory()
    {
        
    }
    
    public static Body newPlayer(World world)
    {
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(192 / 2, 108 / 2);
        
        Body body = world.createBody(bodyDef);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(1.5f);
        
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1f;
        
        body.createFixture(fixDef);
        body.setFixedRotation(true);
        shape.dispose();

        Box2DSprite sprite = new Box2DSprite(Assets.player);
        body.setUserData(sprite);

        return body;
    }
    
    public static Body newSquare(World world)
    {
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(10, 10);
        
        Body body = world.createBody(bodyDef);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(2f);
        
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1f;
        
        body.createFixture(fixDef);
        
        shape.dispose();
        
        return body;
    }
}
