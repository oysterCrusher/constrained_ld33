package uk.me.jadams.needlefish;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameStage extends Stage
{
    private static final LabelStyle LABEL_STYLE = new LabelStyle(Assets.titleFont, Color.valueOf("8DA7BEFF"));

    private final Label highScoreField;

    private final Label scoreField;

    public GameStage(OrthographicCamera camera, SpriteBatch batch)
    {
        super(new ScreenViewport(camera), batch);

        // High score label.
        Label highScoreLabel = new Label("Best:", LABEL_STYLE);
        highScoreLabel.setFontScale(0.5f);
        highScoreField = new Label("0.0", LABEL_STYLE);
        highScoreField.setFontScale(0.5f);

        // Score label.
        Label scoreLabel = new Label("Score:", LABEL_STYLE);
        scoreLabel.setFontScale(0.5f);
        scoreField = new Label("0.0", LABEL_STYLE);
        scoreField.setFontScale(0.5f);

        // Lay it all out in a table.
        Table table = new Table();
        table.setFillParent(true);
        table.add(scoreLabel).top().pad(20);
        table.add(scoreField).expand().left().top().pad(20);
        table.add(highScoreLabel).top().right().pad(20);
        table.add(highScoreField).right().top().pad(20);

        this.addActor(table);
    }

    public void setHighScore(String highScore)
    {
        highScoreField.setText(highScore);
    }

    public void setScore(String score)
    {
        scoreField.setText(score);
    }
}
