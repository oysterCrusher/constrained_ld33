package uk.me.jadams.needlefish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    public static Sound wallBounce;
    public static Sound enemyExplode;
    public static Sound buttonPress;
    
    private static Texture loadTexture(String file)
    {
        return new Texture(Gdx.files.internal(file));
    }

    private static BitmapFont loadFont(String file)
    {
        return new BitmapFont(Gdx.files.internal(file));
    }
    
    private static Sound loadSound(String file)
    {
        return Gdx.audio.newSound(Gdx.files.internal(file));
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
        
        wallBounce = loadSound("wall_bounce.wav");
        enemyExplode = loadSound("enemy_explode.wav");
        buttonPress = loadSound("button_press.wav");
    }
}
