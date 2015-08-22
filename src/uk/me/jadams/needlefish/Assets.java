package uk.me.jadams.needlefish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets
{
    public static Texture bg;
    public static Texture player;
    
    private static Texture loadTexture(String file)
    {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load()
    {
        bg = loadTexture("bg.png");
        player = loadTexture("player.png");
    }
}
