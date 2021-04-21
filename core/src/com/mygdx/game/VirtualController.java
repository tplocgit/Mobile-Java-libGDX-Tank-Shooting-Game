package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by brentaureli on 10/23/15.
 */
public class VirtualController {
    Viewport viewport;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed, crossHairPressed, arrowPressed;
    OrthographicCamera cam;
    public static final int CONTROLLER_BUTTON_DEFAULT_SIZE = 70;
    public static final int CONTROLLER_BUTTON_MEDIUM_SIZE = 120;
    public static final Image UP_IMAGE = new Image(new Texture("Controller/up-arrow.png"));
    public static final Image DOWN_IMAGE = new Image(new Texture("Controller/down-arrow.png"));
    public static final Image LEFT__IMAGE = new Image(new Texture("Controller/left-arrow.png"));
    public static final Image RIGHT_IMAGE = new Image(new Texture("Controller/right-arrow.png"));
    public static final Image CROSSHAIR__IMAGE = new Image(new Texture("Controller/focus.png"));



    public VirtualController(SpriteBatch batch) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        //stage = new Stage(viewport, GameScreen.batch);
        stage = new Stage(viewport, batch);
        stage.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = true;
                        arrowPressed = true;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = true;
                        arrowPressed = true;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        arrowPressed = true;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        arrowPressed = true;
                        break;
                    case Input.Keys.Z:
                        crossHairPressed = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = false;
                        arrowPressed = false;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = false;
                        arrowPressed = false;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        arrowPressed = false;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        arrowPressed = false;
                        break;
                    case Input.Keys.Z:
                        crossHairPressed = false;
                        break;
                }
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();

        Image upImg = UP_IMAGE;
        upImg.setSize(CONTROLLER_BUTTON_DEFAULT_SIZE, CONTROLLER_BUTTON_DEFAULT_SIZE);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image downImg = DOWN_IMAGE;
        downImg.setSize(CONTROLLER_BUTTON_DEFAULT_SIZE, CONTROLLER_BUTTON_DEFAULT_SIZE);
        downImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        Image rightImg = RIGHT_IMAGE;
        rightImg.setSize(CONTROLLER_BUTTON_DEFAULT_SIZE, CONTROLLER_BUTTON_DEFAULT_SIZE);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = LEFT__IMAGE;
        leftImg.setSize(CONTROLLER_BUTTON_DEFAULT_SIZE, CONTROLLER_BUTTON_DEFAULT_SIZE);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image crossHairImage = CROSSHAIR__IMAGE;
        crossHairImage.setSize(CONTROLLER_BUTTON_MEDIUM_SIZE, CONTROLLER_BUTTON_MEDIUM_SIZE);
        crossHairImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                crossHairPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                crossHairPressed = false;
            }
        });

        table.add();
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        table.add();

        Table crossHairTable = new Table();
        crossHairTable.setPosition(740, 80);
        //crossHairTable.right().center();
        crossHairTable.add();
        crossHairTable.add(crossHairImage).size(crossHairImage.getWidth(), crossHairImage.getHeight());
        crossHairTable.add();

        stage.addActor(table);
        stage.addActor(crossHairTable);
    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}