package uk.me.jadams.needlefish.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import uk.me.jadams.needlefish.B2DObjectFactory;
import uk.me.jadams.needlefish.components.BodyComponent;
import uk.me.jadams.needlefish.components.InputComponent;

public class InputSystem extends IteratingSystem
{
    private final ComponentMapper<BodyComponent> bodyMap;

    private final OrthographicCamera camera;
    
    private final World world;

    @SuppressWarnings("unchecked")
    public InputSystem(OrthographicCamera camera, World world)
    {
        super(Family.all(BodyComponent.class, InputComponent.class).get());

        this.camera = camera;
        this.world = world;

        bodyMap = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        BodyComponent body = bodyMap.get(entity);

        float vx = 0;
        float vy = 0;

        if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D))
        {
            vx = -35;
        }
        else if (Gdx.input.isKeyPressed(Keys.D) && !Gdx.input.isKeyPressed(Keys.A))
        {
            vx = 35;
        }
        else
        {
            vx = 0;
        }

        if (Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.S))
        {
            vy = 35;
        }
        else if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.W))
        {
            vy = -35;
        }
        else
        {
            vy = 0;
        }

        body.body.setLinearVelocity(vx, vy);

        int mx = Gdx.input.getX();
        int my = Gdx.input.getY();
        Vector3 mc = new Vector3(mx, my, 0);

        float x = body.body.getPosition().x;
        float y = body.body.getPosition().y;

        camera.unproject(mc);
        mc.sub(x, y, 0);

        Vector2 mr = new Vector2(mc.x, mc.y);
        float angle = (float) (mr.angle() / 360 * 2 * Math.PI);
        body.body.setTransform(x, y, angle);
        
        if (Gdx.input.isKeyJustPressed(Keys.SPACE))
        {
            B2DObjectFactory.projectile(world, x, y, angle);
        }
    }
}
