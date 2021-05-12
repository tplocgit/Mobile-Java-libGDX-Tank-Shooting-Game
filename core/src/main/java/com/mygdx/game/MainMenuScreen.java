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
import com.mygdx.game.network.AssetManager;

public class MainMenuScreen implements Screen {
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
        Skin skin = new Skin(Gdx.files.internal(AssetManager.getInstance().SKIN_PATH));


        Label title = new Label("Tank Shooting Game", skin);
        title.setFontScale(Graphic.LABEL_TITLE_FONT_SCALE);
        //create buttons
        TextButton pveButton = new TextButton("PVE", skin);
        TextButton pvpButton = new TextButton("PVP", skin);
        TextButton clientButton = new TextButton("Client", skin);
        pveButton.getLabel().setFontScale(Graphic.BUTTON_LABEL_FONT_SCALE);
        pveButton.pad(
                Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL,
                Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL
        );
        pvpButton.getLabel().setFontScale(Graphic.BUTTON_LABEL_FONT_SCALE);
        pvpButton.pad(
                Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL,
                Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL
        );
        clientButton.getLabel().setFontScale(Graphic.BUTTON_LABEL_FONT_SCALE);
        clientButton.pad(
                Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL,
                Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL
        );
        //add buttons to buttonsTable
//        buttonsTable.add(tile);
//        buttonsTable.row().pad(TABLE_ROW_DEFAULT_PAD_VERTICAL, 0, 0, 0);
        buttonsTable.add(pveButton).uniform().fillX();
        buttonsTable.row().padTop(Graphic.TABLE_ROW_DEFAULT_PAD_VERTICAL);
        buttonsTable.add(pvpButton).uniform().fillX();
        buttonsTable.row().padTop(Graphic.TABLE_ROW_DEFAULT_PAD_VERTICAL);
        buttonsTable.add(clientButton).uniform().fillX();

        container.add(title).padBottom(Graphic.LABEL_TITLE_PAD_BOTTOM);
        container.row();
        container.add(buttonsTable);

        // create button listeners
        clientButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(TankShootingGame.CONNECT_SERVER_SCREEN);
    //                Gdx.app.exit();
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
                parent.changeScreen(TankShootingGame.CREATE_ROOM_SCREEN);
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
