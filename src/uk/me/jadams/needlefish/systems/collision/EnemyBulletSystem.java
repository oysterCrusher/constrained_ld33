package uk.me.jadams.needlefish.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import uk.me.jadams.needlefish.Assets;
import uk.me.jadams.needlefish.CollisionData;
import uk.me.jadams.needlefish.FixtureTypes;
import uk.me.jadams.needlefish.Particles;
import uk.me.jadams.needlefish.Utils;
import uk.me.jadams.needlefish.components.AIMovementTrackPlayerComponent;
import uk.me.jadams.needlefish.components.BodyComponent;
import uk.me.jadams.needlefish.screens.GameScreen;

public class EnemyBulletSystem extends IteratingSystem
{
    private final ComponentMapper<BodyComponent> bodyMap;

    private final GameScreen game;

    private final Engine engine;
    
    private final Particles effect;

    @SuppressWarnings("unchecked")
    public EnemyBulletSystem(GameScreen game, Engine engine, Particles effect)
    {
        super(Family.all(BodyComponent.class, AIMovementTrackPlayerComponent.class).get());

        this.game = game;
        this.engine = engine;
        this.effect = effect;

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
                        game.destroyBody(body);
                        engine.removeEntity(entity);
                        game.destroyBody(collisonData.getOtherBody());

                        effect.start(body.getPosition().x, body.getPosition().y);
                        
                        Assets.enemyExplode.play();
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
