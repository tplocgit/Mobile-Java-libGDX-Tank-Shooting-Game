package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen implements Screen {
    public static final int BUTTON_DEFAULT_PAD_VERTICAL = 20;
    public static final int BUTTON_DEFAULT_PAD_HORIZONTAL = 110;
    public static final int TABLE_ROW_DEFAULT_PAD_VERTICAL = 30;
    public static final float LABEL_TITLE_FONT_SCALE = 10;
    public static final int LABEL_TITLE_PAD_BOTTOM = 50;
    public static final float BUTTON_LABEL_FONT_SCALE = 2.5f;
    private TankShootingGame parent;
    private Stage stage;



    public MainMenuScreen(TankShootingGame game) {
        parent = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
// Create a buttonsTable that fills the screen. Everything else will go inside this buttonsTable.
        Table container = new Table();
        container.setFillParent(true);
//        container.setDebug(true);
        Table buttonsTable = new Table();
//        buttonsTable.setDebug(true);
        stage.addActor(container);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal(Graphic.SKIN_PATH));


        Label tile = new Label("Tank Shooting Game", skin);
        tile.setFontScale(LABEL_TITLE_FONT_SCALE);
        //create buttons
        TextButton pveButton = new TextButton("PVE", skin);
        TextButton pvpButton = new TextButton("PVP", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        pveButton.getLabel().setFontScale(BUTTON_LABEL_FONT_SCALE);
        pveButton.pad(
                BUTTON_DEFAULT_PAD_VERTICAL, BUTTON_DEFAULT_PAD_HORIZONTAL,
                BUTTON_DEFAULT_PAD_VERTICAL,BUTTON_DEFAULT_PAD_HORIZONTAL
        );
        pvpButton.getLabel().setFontScale(BUTTON_LABEL_FONT_SCALE);
        pvpButton.pad(
                BUTTON_DEFAULT_PAD_VERTICAL, BUTTON_DEFAULT_PAD_HORIZONTAL,
                BUTTON_DEFAULT_PAD_VERTICAL,BUTTON_DEFAULT_PAD_HORIZONTAL
        );
        exitButton.getLabel().setFontScale(BUTTON_LABEL_FONT_SCALE);
        exitButton.pad(
                BUTTON_DEFAULT_PAD_VERTICAL, BUTTON_DEFAULT_PAD_HORIZONTAL,
                BUTTON_DEFAULT_PAD_VERTICAL,BUTTON_DEFAULT_PAD_HORIZONTAL
        );
        //add buttons to buttonsTable
//        buttonsTable.add(tile);
//        buttonsTable.row().pad(TABLE_ROW_DEFAULT_PAD_VERTICAL, 0, 0, 0);
        buttonsTable.add(pveButton).uniform().fillX();
        buttonsTable.row().padTop(TABLE_ROW_DEFAULT_PAD_VERTICAL);
        buttonsTable.add(pvpButton).uniform().fillX();
        buttonsTable.row().padTop(TABLE_ROW_DEFAULT_PAD_VERTICAL);
        buttonsTable.add(exitButton).uniform().fillX();

        container.add(tile).padBottom(LABEL_TITLE_PAD_BOTTOM);
        container.row();
        container.add(buttonsTable);

        // create button listeners
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        pveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(TankShootingGame.PVE_SCREEN);
            }
        });

        pvpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(TankShootingGame.PVP_SCREEN);
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
