package uk.me.jadams.needlefish;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuStage extends Stage
{
    private static final LabelStyle LABEL_STYLE = new LabelStyle(Assets.titleFont, Color.valueOf("8DA7BEFF"));
    
    private final Label highScoreField;
    
    private final Label lastScoreField;

    public MenuStage(final Needlefish needlefish, OrthographicCamera camera, SpriteBatch batch)
    {
        super(new ScreenViewport(camera), batch);

        // High score label.
        Label highScoreLabel = new Label("Best:", LABEL_STYLE);
        highScoreField = new Label("0.0", LABEL_STYLE);

        // Last score label.
        Label lastScoreLabel = new Label("Last:", LABEL_STYLE);
        lastScoreField = new Label("0.0", LABEL_STYLE);

        // A Play button.
        Drawable buttonDrawable = new BaseDrawable();
        TextButtonStyle buttonStyle = new TextButtonStyle(buttonDrawable, buttonDrawable, buttonDrawable, Assets.titleFont);
        TextButton playButton = new TextButton("Play", buttonStyle);
        playButton.getStyle().fontColor = Color.valueOf("E26D5C");
        playButton.getStyle().overFontColor = Color.BLACK;

        playButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                needlefish.setScreen(needlefish.gameScreen);
            }
        });

        // Put it all in a table.
        Table table = new Table();
        table.setFillParent(true);
        table.add(highScoreLabel).pad(10).expandX().right();
        table.add(highScoreField).pad(10).expandX().left();
        table.row();
        table.add(lastScoreLabel).pad(10).expandX().right();
        table.add(lastScoreField).pad(10).expandX().left();
        table.row();
        table.add(playButton).colspan(2).pad(60);
        
        this.addActor(table);
    }
    
    public void setHighScore(String highScore)
    {
        highScoreField.setText(highScore);
    }

    public void setLastScore(String score)
    {
        lastScoreField.setText(score);
    }
}
