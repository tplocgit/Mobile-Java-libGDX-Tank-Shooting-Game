package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class CreateRoomScreen implements Screen {
    public static final int TABLE_ROW_DEFAULT_PAD_VERTICAL = 30;
    private TankShootingGame parent;
    private Stage stage;

    @Override
    public void show() {
        Table optionTable = new Table();
        optionTable.setDebug(true);
        optionTable.setFillParent(true);
        stage.addActor(optionTable);

        Skin skin = new Skin(Gdx.files.internal(Graphic.SKIN_PATH));
        Label nameLabel = new Label("Name: ", skin);
        TextField nameTextField = new TextField("Enter room's name ...", skin);

        optionTable.add(nameLabel).fillX();
        optionTable.add(nameTextField).fillX();
        optionTable.row().padBottom(TABLE_ROW_DEFAULT_PAD_VERTICAL);
        
        Label pwdLabel = new Label("Password: ", skin);
        TextField pwdTextField = new TextField("Enter room's password ...", skin);
        pwdTextField.setDisabled(true);

        optionTable.add(pwdLabel).fillX();
        optionTable.add(pwdTextField).fillX();
        optionTable.row();

        CheckBox pwdCheckbox = new CheckBox("Use password: ",skin);
        optionTable.add();
        optionTable.add(pwdCheckbox).align(Align.left);
        optionTable.row().padBottom(TABLE_ROW_DEFAULT_PAD_VERTICAL);

        HorizontalGroup buttonsGroup = new HorizontalGroup();
        TextButton backButton = new TextButton("Back", skin);
        TextButton createButton = new TextButton("Create", skin);

        buttonsGroup.addActor(backButton);
        buttonsGroup.addActor(createButton);
        buttonsGroup.align(Align.center);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        optionTable.add();
        optionTable.add(buttonsGroup);
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

    public CreateRoomScreen() {
    }

    public CreateRoomScreen(TankShootingGame parent) {
        this.parent = parent;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }
}
