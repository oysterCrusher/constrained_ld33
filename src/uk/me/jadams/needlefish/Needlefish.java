package uk.me.jadams.needlefish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uk.me.jadams.needlefish.screens.GameScreen;

public class Needlefish extends Game
{
    SpriteBatch batch;
    OrthographicCamera camera;
    Texture texture;
    Sprite sprite;

    @Override
    public void create()
    {
        Assets.load();
        
        batch = new SpriteBatch();
        camera = new OrthographicCamera(192, 108);
        camera.position.x = 192 / 2;
        camera.position.y = 108 / 2;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        texture = Assets.bg;
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        
        GameScreen gameScreen = new GameScreen(camera, batch);
        this.setScreen(gameScreen);
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(texture, 0, 0, 192, 108, 0, 0, 1, 1080 / 32);
        batch.end();
        
        super.render();
    }
}
