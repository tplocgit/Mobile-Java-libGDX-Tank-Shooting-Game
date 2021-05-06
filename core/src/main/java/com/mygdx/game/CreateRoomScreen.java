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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.network.GameClient;
import com.mygdx.game.network.GameServer;
import com.mygdx.game.network.GameServerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class CreateRoomScreen implements Screen {

    private TankShootingGame parent;

    private Stage stage;

    @Override
    public void show() {

        // Components
        Table layout = new Table();
        Skin skin = new Skin(Gdx.files.internal(Graphic.SKIN_PATH));
        Label title = new Label("Create Room", skin);
        Table optionTable = new Table();
        Label nameLabel = new Label("Name: ", skin);
        TextField nameTextField = new TextField("", skin);
        Label pwdLabel = new Label("Password: ", skin);
        TextField pwdTextField = new TextField("", skin);

        Table buttonsGroup = new Table();
        TextButton backButton = new TextButton("Back", skin);
        TextButton createButton = new TextButton("Create", skin);

        // Setting
        TextField.TextFieldStyle tfStyle = skin.get(TextField.TextFieldStyle.class);
        tfStyle.font.getData().scale(Graphic.TEXT_FIELD_LABEL_FONT_SCALE);
        layout.setFillParent(true);
        stage.addActor(layout);
        title.setFontScale(Graphic.LABEL_TITLE_FONT_SCALE);
        nameTextField.setMessageText("Name ...");
        nameTextField.setMaxLength(20);
        pwdTextField.setMessageText("Password ...");
        pwdTextField.setMaxLength(20);

        backButton.getLabel().setFontScale(Graphic.BUTTON_LABEL_FONT_SCALE);
        createButton.getLabel().setFontScale(Graphic.BUTTON_LABEL_FONT_SCALE);
        buttonsGroup.align(Align.center);

        // adding
        layout
            .add(title)
            .fillX()
            .uniform();
        layout.row().padBottom(Graphic.LABEL_TITLE_PAD_BOTTOM);
        optionTable
                .add(nameLabel)
                .fillX();
        optionTable
                .add(nameTextField)
                .width(Graphic.TEXT_FIELD_WIDTH)
                .height(Graphic.TEXT_FIELD_HEIGHT)
                .pad(Graphic.TEXT_FIELD_PAD);
        optionTable.row().padBottom(Graphic.TABLE_ROW_DEFAULT_PAD_VERTICAL);
        optionTable.add(pwdLabel).fillX();
        optionTable
                .add(pwdTextField)
                .width(Graphic.TEXT_FIELD_WIDTH)
                .height(Graphic.TEXT_FIELD_HEIGHT)
                .pad(Graphic.TEXT_FIELD_PAD);

        optionTable.row().padBottom(Graphic.TABLE_ROW_DEFAULT_PAD_VERTICAL);
        buttonsGroup
                .add(backButton)
                .pad(Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL, Graphic.BUTTON_DEFAULT_PAD_VERTICAL);
        buttonsGroup
                .add(createButton)
                .pad(Graphic.BUTTON_DEFAULT_PAD_VERTICAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL, Graphic.BUTTON_DEFAULT_PAD_HORIZONTAL, Graphic.BUTTON_DEFAULT_PAD_VERTICAL);
        optionTable.add();
        optionTable.add(buttonsGroup).fill();
        layout.add(optionTable);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(TankShootingGame.MENU_SCREEN);
            }
        });

        createButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameServerListener gameServerListener = new GameServerListener() {
                    @Override
                    public void onReady(int port) {
                        System.out.println("Server online at port: " + port);
                    }
                };

                GameServer gameServer = new GameServer(nameTextField.getText(), gameServerListener);
            }
        });

//        createButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
////            try {
////                new GameServer(nameLabel.getText().toString());
////                GameServer.getInstance().Start();
////                parent.changeScreen(TankShootingGame.PVE_SCREEN);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//                System.out.println("herehehe");
//                try {
//                    GameServer gameServer = new GameServer();
//                    System.out.println("server on");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

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
