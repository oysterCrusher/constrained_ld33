package uk.me.jadams.needlefish.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import uk.me.jadams.needlefish.CollisionData;
import uk.me.jadams.needlefish.Utils;
import uk.me.jadams.needlefish.components.BodyComponent;

public class CollisionSystem extends IteratingSystem
{
    private final ComponentMapper<BodyComponent> bodyMap;

    @SuppressWarnings("unchecked")
    public CollisionSystem()
    {
        super(Family.all(BodyComponent.class).get());

        bodyMap = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        Body body = bodyMap.get(entity).body;
        
        Array<Fixture> fixtures = body.getFixtureList();
        
        for (Fixture fixture : fixtures)
        {
            CollisionData collisonData = Utils.getCollisionData(fixture);
            
            if (collisonData != null)
            {
                if (!collisonData.isProcessed())
                {
                    System.out.println(collisonData.myType + " collided with " + collisonData.getAgainst());
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
