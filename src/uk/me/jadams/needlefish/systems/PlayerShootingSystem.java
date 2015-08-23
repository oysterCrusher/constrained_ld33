package uk.me.jadams.needlefish.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import uk.me.jadams.needlefish.B2DObjectFactory;

public class PlayerShootingSystem extends EntitySystem
{
    private static final float FIRE_PERIOD_CORE = 0.8f;

    private final World world;

    private final Body player;

    private float timerFire = 0;
    
    private float timer;

    public PlayerShootingSystem(World world, Body player)
    {
        super();

        this.world = world;
        this.player = player;
        
        this.reset();
    }
    
    public void reset()
    {
        timer = 0;
        timerFire = 5;
    }

    @Override
    public void update(float deltaTime)
    {
        timer += deltaTime;
        timerFire -= deltaTime;

        if (timerFire < 0)
        {
            float x = player.getPosition().x;
            float y = player.getPosition().y;
            float a = player.getAngle();

            B2DObjectFactory.projectile(world, x, y, a);
            
            timerFire = FIRE_PERIOD_CORE / ((timer + 60) / 150);;
        }
    }
}
