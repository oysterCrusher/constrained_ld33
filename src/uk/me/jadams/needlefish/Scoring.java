package uk.me.jadams.needlefish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Scoring
{
    private float highScore;

    private float time;

    public Scoring()
    {
        start();
        
        Preferences prefs = Gdx.app.getPreferences("needlefish");
        highScore = prefs.getFloat("score", 0);
    }

    public void start()
    {
        time = 0;
    }
    
    public void end()
    {
        if (time > highScore)
        {
            highScore = time;
            Preferences prefs = Gdx.app.getPreferences("needlefish");
            prefs.putFloat("score", time);
            prefs.flush();
        }
    }

    public float getScore()
    {
        return time;
    }

    public String getHighScoreString()
    {
        return format(highScore);
    }

    public void update(float delta)
    {
        time += delta;
    }

    public String getScoreString()
    {
        return format(time);
    }
    
    private String format(float score)
    {
        int secs = (int) score;
        int mils = (int) (score * 10) % 10;
        return secs + "." + mils;
    }
}
