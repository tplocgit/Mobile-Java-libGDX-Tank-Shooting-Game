package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.network.GameClient;

public class ConnectServerScreen implements Screen {

    private static ConnectServerScreen instance;
    private Stage stage;
    private Table serverButtonTable;
    private ScrollPane scrollPane;
    private Table outerTable;
    private final Skin buttonSkin = new Skin(Gdx.files.internal(Graphic.SKIN_PATH));;

    public static ConnectServerScreen getInstance(){
        return instance;
    }

    @Override
    public void show() {
        // Components
        Actor actor = new Actor();
//        ScrollPane layout = new ScrollPane(actor);
//        layout.setFillParent(true);
//        layout.cancelTouchFocus();
//        layout.setFadeScrollBars(true);

        serverButtonTable = new Table();
        outerTable = new Table();
        outerTable.setFillParent(true);
        scrollPane = new ScrollPane(serverButtonTable);
        outerTable.add(scrollPane);

        stage.addActor(outerTable);

        serverButtonTable.add(createButton("huhuorigin", "haa", 90, buttonSkin)).height(200).width(400);
        serverButtonTable.add(createButton("huhu", "haa", 90, buttonSkin)).height(200).width(400);
        serverButtonTable.add(createButton("huhu", "haa", 90, buttonSkin)).height(200).width(400);
        serverButtonTable.add(createButton("huhu", "haa", 90, buttonSkin)).height(200).width(400);
        serverButtonTable.add(createButton("huhu", "haa", 90, buttonSkin)).height(200).width(400);

//        layout.setActor(serverButtonTable);

//        TextButton textButton2 = new TextButton("okay", buttonSkin);
//        textButton2.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("here ConectSereverScree 44");
//            }
//        });
//


    }

    private TextButton createButton(String buttonName, String ip, int port, Skin skin){
        serverButtonTable.row().padBottom(Graphic.TABLE_ROW_DEFAULT_PAD_VERTICAL);
        TextButton textButton = new TextButton(buttonName, skin);
        textButton.setWidth(textButton.getMaxWidth());
        textButton.setHeight(50);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Connect to " + ip);
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

    public ConnectServerScreen() {
        this.stage = new Stage();
        instance = this;
        Gdx.input.setInputProcessor(stage);
        GameClient.getInstance().searchingForServers();
    }

    public ConnectServerScreen(TankShootingGame parent) {

    }

    public void addServerToList(String serverName, String ip, int port) {
        TextButton textButton = createButton(serverName, ip, port, buttonSkin);
        serverButtonTable.add(textButton).height(200).width(400);
    }
}
