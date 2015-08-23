package uk.me.jadams.needlefish.systems.aimovement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import uk.me.jadams.needlefish.components.AIMovementTrackPlayerComponent;
import uk.me.jadams.needlefish.components.BodyComponent;

public class TrackPlayerSystem extends IteratingSystem
{
    private final ComponentMapper<BodyComponent> bodyMap;
    
    private final Body player;

    @SuppressWarnings("unchecked")
    public TrackPlayerSystem(Body player)
    {
        super(Family.all(BodyComponent.class, AIMovementTrackPlayerComponent.class).get());
        
        this.player = player;
        
        bodyMap = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        Body body = bodyMap.get(entity).body;
        
        float desired_vx = player.getPosition().x - body.getPosition().x;
        float desired_vy = player.getPosition().y - body.getPosition().y;
        
        Vector2 desired_v = new Vector2(desired_vx, desired_vy);
        desired_v.nor();
        desired_v = desired_v.scl(15);

//        desired_v.sub(body.getLinearVelocity());
//        desired_v.scl(body.getMass());

//        body.applyLinearImpulse(desired_v, body.getPosition(), true);
        
        body.setLinearVelocity(desired_v);
    }

}
