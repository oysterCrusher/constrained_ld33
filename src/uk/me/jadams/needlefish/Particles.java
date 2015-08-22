package uk.me.jadams.needlefish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Particles
{
    private ParticleEffect effect;

    private ParticleEffectPool effectPool;

    private Array<PooledEffect> effects;

    public Particles(String fileName)
    {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(fileName), Gdx.files.internal(""));
        effectPool = new ParticleEffectPool(effect, 5, 75);
        effects = new Array<PooledEffect>();
    }

    public void start(float x, float y)
    {
        PooledEffect effect = effectPool.obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }

    public void start(float x, float y, float angleMin, float angleMax)
    {
        PooledEffect effect = effectPool.obtain();
        effect.setPosition(x, y);

        for (ParticleEmitter e : effect.getEmitters())
        {
            e.getAngle().setHigh(angleMin, angleMax);
            e.getAngle().setLow(angleMin, angleMax);
        }

        effects.add(effect);
    }

    public void render(SpriteBatch batch, float delta)
    {
        for (PooledEffect e : effects)
        {
            e.draw(batch, delta);
        }

        for (PooledEffect e : effects)
        {
            if (e.isComplete())
            {
                effects.removeValue(e, true);
                e.free();
            }
        }
    }

    public void dispose()
    {
        effect.dispose();
    }
}
