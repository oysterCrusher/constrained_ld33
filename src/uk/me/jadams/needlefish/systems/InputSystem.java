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

import uk.me.jadams.needlefish.components.BodyComponent;
import uk.me.jadams.needlefish.components.InputComponent;

public class InputSystem extends IteratingSystem
{
    private final ComponentMapper<BodyComponent> bodyMap;

    private final OrthographicCamera camera;

    @SuppressWarnings("unchecked")
    public InputSystem(OrthographicCamera camera)
    {
        super(Family.all(BodyComponent.class, InputComponent.class).get());

        this.camera = camera;

        bodyMap = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        BodyComponent body = bodyMap.get(entity);

        float vx = 0;
        float vy = 0;

        if (Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))
        {
            vx = -25;
        }
        else if (Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT))
        {
            vx = 25;
        }
        else
        {
            vx = 0;
        }

        if (Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN))
        {
            vy = 25;
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN) && !Gdx.input.isKeyPressed(Keys.UP))
        {
            vy = -25;
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
    }
}
