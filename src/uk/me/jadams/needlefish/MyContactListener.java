package uk.me.jadams.needlefish;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener
{

    @Override
    public void beginContact(Contact contact)
    {

    }

    @Override
    public void endContact(Contact contact)
    {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        float x = contact.getWorldManifold().getPoints()[0].x;
        float y = contact.getWorldManifold().getPoints()[0].y;

        CollisionData collisionDataA = getCollisionData(contact.getFixtureA());
        CollisionData collisionDataB = getCollisionData(contact.getFixtureB());

        if (collisionDataA == null || collisionDataB == null)
        {
            return;
        }

        collisionDataA.setCollision(collisionDataB.myType, 0, x, y);
        collisionDataB.setCollision(collisionDataA.myType, 0, x, y);
    }

    private CollisionData getCollisionData(Fixture fixture)
    {
        Object userData = fixture.getUserData();

        if (userData instanceof CollisionData)
        {
            return (CollisionData) userData;
        }

        return null;
    }
}
