package uk.me.jadams.needlefish;

public class Scoring
{
    private int score;
    
    private float timer;
    
    public Scoring()
    {
        reset();
    }
    
    public void reset()
    {
        score = 0;
        timer = 0;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public float getTimer()
    {
        return timer;
    }
    
    public void add(int score)
    {
        this.score += score;
    }

    public void update(float delta)
    {
        timer += delta;
    }

    public CharSequence getTimeString()
    {
        int mins = (int) timer / 60;
        int secs = (int) timer % 60;
        int mils = (int) (timer * 10) % 10;
        return mins + ":" + secs + ":" + mils;
    }
}
