package uk.me.jadams.needlefish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uk.me.jadams.needlefish.screens.GameScreen;
import uk.me.jadams.needlefish.screens.MenuScreen;

public class Needlefish extends Game
{
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture texture;
    
    public GameScreen gameScreen;

    @Override
    public void create()
    {
        Assets.load();
        
        batch = new SpriteBatch();
        camera = new OrthographicCamera(1920, 1080);
        camera.position.x = 1920 / 2;
        camera.position.y = 1080 / 2;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        texture = Assets.bg;
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        
        MenuScreen menuScreen = new MenuScreen(this, camera, batch);
        gameScreen = new GameScreen(camera, batch);
        this.setScreen(menuScreen);
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(texture, 0, 0, camera.viewportWidth, camera.viewportHeight, 0, 0, 1, 1080 / 32);
        batch.end();
        
        super.render();
    }
}
