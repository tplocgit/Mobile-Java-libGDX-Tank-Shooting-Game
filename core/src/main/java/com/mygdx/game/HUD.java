package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.objects.PlayerTank;

import java.util.Locale;

public class HUD {
    Viewport viewport;
    Stage stage;
    OrthographicCamera cam;
    BitmapFont font;
    float life, movementSpeed, shield;
    int score, enemyCount, firepower;
    Vector2 position;
    boolean debugMode = false;
    boolean isPvp = false;
    SpriteBatch batch;
    private GameScreen gameScreen;

    private static HUD instance;



    public HUD(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        batch = gameScreen.getBatch();
        cam = gameScreen.getCamera();
        viewport = new FitViewport(800, 480, cam);
        //stage = new Stage(viewport, GameScreen.batch);
        stage = new Stage(viewport, batch);
        this.score = 0;
//        this.update();
        this.enemyCount = 0;

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        instance = this;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public float getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public static HUD getInstance() {
        return instance;
    }

    public void draw(){
//        stage.draw();
        batch.begin();

//        PlayerTank pTank = gameScreen.getPlayerTank();

        font.draw(batch, "Enemy", 100, 475, 20, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", enemyCount), 100, 435,
                20, Align.left, false);

        if(debugMode) {
            font.draw(batch, "Position", 100, 350, 20, Align.left, false);
            font.draw(batch, this.position.toString(), 100, 300,
                    20, Align.left, false);
        }

        font.draw(batch, "Score", 400, 475, 20, Align.center, false);
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), 400, 435,
                20,  Align.center, false);

        font.draw(batch, "Power", 720, 475, 20, Align.right, false);
        font.draw(batch, String.format(Locale.getDefault(), "%2d", firepower), 700, 435,
                20, Align.right, false);
        font.draw(batch, "Speed", 720, 400, 20, Align.right, false);
        font.draw(batch, String.format(Locale.getDefault(), "%.0f", movementSpeed), 700, 360,
                20, Align.right, false);

        font.draw(batch, "Life", 300, 80, 20, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%.0f", life), 450, 80,
                20, Align.right, false);
        font.draw(batch, "%", 485, 80,
                20, Align.right, false);
        font.draw(batch, "Shield", 300, 40, 20, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%.0f", shield), 450, 40,
                20, Align.right, false);
        font.draw(batch, "%", 485, 40,
                20, Align.right, false);

        if (this.life <= 0)
            font.draw(batch, "YOU DIED", 400, 200, 20, Align.center, false);

        if (isPvp && enemyCount <= 0)
            font.draw(batch, "YOU WIN", 400, 200, 20, Align.center, false);

        batch.end();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

//    public void update() {
//        this.movementSpeed = gameScreen.getPlayerTank().getSpeed();
//        this.life = gameScreen.getPlayerTank().getLife();
//        this.position = gameScreen.getPlayerTank().getPosition();
//        this.firepower = gameScreen.getPlayerTank().getFirepower();
//        this.shield = gameScreen.getPlayerTank().getShield();
//        this.score = gameScreen.getPlayerTank().getGainedScore();
//    }



    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setShield(float shield) {
        this.shield = shield;
    }

    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}