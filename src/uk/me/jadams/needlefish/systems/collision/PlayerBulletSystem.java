package uk.me.jadams.needlefish.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import uk.me.jadams.needlefish.CollisionData;
import uk.me.jadams.needlefish.FixtureTypes;
import uk.me.jadams.needlefish.Particles;
import uk.me.jadams.needlefish.Utils;
import uk.me.jadams.needlefish.components.BodyComponent;
import uk.me.jadams.needlefish.components.InputComponent;

public class PlayerBulletSystem extends IteratingSystem
{
    private final ComponentMapper<BodyComponent> bodyMap;

    private final Particles deathParticles;

    @SuppressWarnings("unchecked")
    public PlayerBulletSystem(Particles deathParticles)
    {
        super(Family.all(BodyComponent.class, InputComponent.class).get());

        this.deathParticles = deathParticles;

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
                    if (collisonData.getAgainst() == FixtureTypes.BULLET)
                    {
                        deathParticles.start(body.getPosition().x, body.getPosition().y);

                        // TODO - Kill/damage the player.
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
}
