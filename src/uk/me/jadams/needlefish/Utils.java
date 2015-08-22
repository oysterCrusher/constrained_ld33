package uk.me.jadams.needlefish;

import com.badlogic.gdx.physics.box2d.Fixture;

public class Utils
{
    public static CollisionData getCollisionData(Fixture fixture)
    {
        Object userData = fixture.getUserData();

        if (userData instanceof CollisionData)
        {
            return (CollisionData) userData;
        }

        return null;
    }
}
