package uk.me.jadams.needlefish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

import uk.me.jadams.needlefish.Assets;
import uk.me.jadams.needlefish.Needlefish;

public class MenuScreen implements Screen
{
    private final Needlefish needlefish;

    private final Stage stage;

    public MenuScreen(Needlefish needlefish, OrthographicCamera camera, SpriteBatch batch)
    {
        this.needlefish = needlefish;

        stage = new Stage(new ScreenViewport(camera), batch);
    }

    @Override
    public void show()
    {
        Label title = new Label("Constrained", new LabelStyle(Assets.titleFont, Color.valueOf("F26937")));

        Drawable buttonDrawable = new BaseDrawable();

        TextButton playButton = new TextButton("Play", new TextButtonStyle(buttonDrawable, buttonDrawable, buttonDrawable, Assets.titleFont));
        playButton.getStyle().fontColor = Color.valueOf("F26937");
        playButton.getStyle().overFontColor = Color.valueOf("C8C8C8");
        playButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Assets.buttonPress.play();
                needlefish.setScreen(needlefish.gameScreen);
            }
        });

        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        Table titleTable = new Table();
        titleTable.center();

        titleTable.add(title);

        mainTable.add(new Table()).expand();
        mainTable.row();
        mainTable.add(titleTable).expand();
        mainTable.row();
        mainTable.add(playButton).expand();
        mainTable.row();
        mainTable.add(new Table()).expand();

        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta)
    {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }

}
