package uk.me.jadams.needlefish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets
{
    public static Texture bg;
    public static Texture player;
    public static Texture bullet;
    public static Texture enemy;
    public static Texture wall;
    public static Texture vertex;
    public static BitmapFont titleFont;
    
    private static Texture loadTexture(String file)
    {
        return new Texture(Gdx.files.internal(file));
    }

    private static BitmapFont loadFont(String file)
    {
        return new BitmapFont(Gdx.files.internal(file));
    }

    public static void load()
    {
        bg = loadTexture("bg.png");
        player = loadTexture("player.png");
        bullet = loadTexture("bullet.png");
        enemy = loadTexture("enemy.png");
        wall = loadTexture("wall.png");
        vertex = loadTexture("vertex.png");
        
        titleFont = loadFont("bpdotssquares.fnt");
    }
}
