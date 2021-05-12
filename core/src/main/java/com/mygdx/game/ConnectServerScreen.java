package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.network.AssetManager;
import com.mygdx.game.network.GameClient;
import com.mygdx.game.network.GameServer;

import java.io.IOException;

public class ConnectServerScreen implements Screen {

    private static ConnectServerScreen instance;
    private TankShootingGame parent;
    private Stage stage;
    private Table serverButtonTable;
    private ScrollPane scrollPane;
    private Table outerTable;
    private Table parentTable;
    private final Skin buttonSkin = new Skin(Gdx.files.internal(AssetManager.getInstance().SKIN_PATH));;

    public static ConnectServerScreen getInstance(){
        return instance;
    }

    @Override
    public void show() {
        // Components
        serverButtonTable = new Table();
        parentTable = new Table();
        parentTable.setFillParent(true);
        outerTable = new Table();
        scrollPane = new ScrollPane(serverButtonTable);
//        Table rightSideTable = new Table();
        stage.addActor(parentTable);

//        parentTable.center();
        Label title = new Label("Finding server", buttonSkin);
        title.setFontScale(5);
        parentTable.add(title).uniform().padBottom(50);
        parentTable.row();

        outerTable.add(scrollPane);
        parentTable.add(outerTable);
//        parentTable.row().padBottom(100);

        TextButton backButton = new TextButton("Back", buttonSkin);
        backButton.setWidth(backButton.getMaxWidth());
        backButton.setHeight(50);
        outerTable.add(backButton).height(150).width(300).pad(50);



        //listener
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(TankShootingGame.MENU_SCREEN);
            }
        });
    }

    private TextButton createButton(String buttonName, String ip, int port, Skin skin){
        serverButtonTable.row().padBottom(Graphic.TABLE_ROW_DEFAULT_PAD_VERTICAL);
        TextButton textButton = new TextButton(buttonName, skin);
        textButton.setWidth(textButton.getMaxWidth());
        textButton.setHeight(50);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    GameClient.getInstance().connectToServer(ip, GameServer.TCP_PORT, parent);
                } catch (IOException e) {
                    System.out.println("Failed to connect to server at: " + ip);
                }
            }
        });
        return textButton;
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


    public ConnectServerScreen(TankShootingGame parent) {
        this.stage = new Stage();
        this.parent = parent;
        instance = this;
        Gdx.input.setInputProcessor(stage);
        GameClient.getInstance().searchingForServers();
    }

    public void addServerToList(String serverName, String ip, int port) {
        TextButton textButton = createButton(serverName, ip, port, buttonSkin);
        serverButtonTable.add(textButton).height(200).width(400);
    }
}
