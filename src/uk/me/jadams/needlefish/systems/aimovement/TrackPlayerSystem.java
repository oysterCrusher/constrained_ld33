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
        
        float vx = player.getPosition().x - body.getPosition().x;
        float vy = player.getPosition().y - body.getPosition().y;
        
        Vector2 v = new Vector2(vx, vy);
        v.nor();
        v = v.scl(20);

        body.setLinearVelocity(v);
    }

}
