package uk.me.jadams.needlefish.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import uk.me.jadams.needlefish.B2DObjectFactory;

public class EnemySpawnSystem extends EntitySystem
{
    private static final float TIMER_TRACKING_CORE = 1f;
    
    private float timer;
    
    private final World world;
    
    private final PooledEngine engine;
    
    private float timerTracking;
    
    public EnemySpawnSystem(World world, PooledEngine engine)
    {
        super();
        
        this.world = world;
        this.engine = engine;
        
        this.reset();
    }

    public final void reset()
    {
        timer = 0;
        timerTracking = TIMER_TRACKING_CORE;
    }

    @Override
    public void update(float deltaTime)
    {
        timer += deltaTime;

        timerTracking -= deltaTime;
        
        if (timerTracking <= 0)
        {
            Vector2 position = randomSpawnPoint();
            B2DObjectFactory.ai(engine, world, position);
            
            timerTracking = TIMER_TRACKING_CORE / ((timer + 60) / 150);
        }
    }
    
    private final Vector2 randomSpawnPoint()
    {
        Vector2 pos = new Vector2();
        
        float r = (float) (Math.random() * (192 + 108 + 192 + 108));
        
        if (r < 192)
        {
            pos.x = r;
            pos.y = -10;
            return pos;
        }
        else if (r < 192 + 108)
        {
            pos.x = 202;
            pos.y = r - 192;
            return pos;
        }
        else if (r < 192 + 108 + 192)
        {
            pos.x = r - (192 + 108);
            pos.y = 118;
            return pos;
        }
        else
        {
            pos.x = -10;
            pos.y = r - (192 + 108 + 192);
            return pos;
        }
            
    }
}
