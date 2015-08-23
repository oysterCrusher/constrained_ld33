package uk.me.jadams.needlefish;

import com.badlogic.gdx.physics.box2d.Body;

public class CollisionData
{
    public final FixtureTypes myType;

    private boolean processed;
    
    private FixtureTypes against;
    
    private Body otherBody;
    
    private float impulse;
    
    private float x;
    
    private float y;
    
    public CollisionData(FixtureTypes myType)
    {
        this.myType = myType;

        processed = true;
        against = null;
        x = 0;
        y = 0;
    }
    
    public void setCollision(FixtureTypes against, Body body, float impulse, float x, float y)
    {
        this.against = against;
        this.impulse = impulse;
        this.otherBody = body;
        this.x = x;
        this.y = y;
        
        processed = false;
    }
    
    public boolean isProcessed()
    {
        return processed;
    }
    
    public FixtureTypes getAgainst()
    {
        return against;
    }
    
    public float getImpulse()
    {
        return impulse;
    }
    
    public Body getOtherBody()
    {
        return otherBody;
    }

    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }

    public void markProcessed()
    {
        processed = true;
    }
}
