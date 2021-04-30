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

import java.util.Locale;

public class HUD {
    Viewport viewport;
    Stage stage;
    OrthographicCamera cam;
    BitmapFont font;
    float life, movementSpeed;
    int score, enemyCount, firepower, shield;
    Vector2 position;
    boolean debugMode = true;
    boolean isPvp = false;

    public HUD(int score, float life, int firepower, int shield, float movementSpeed, int enemyCount,
               Vector2 initPosition, boolean debugMode, SpriteBatch batch) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        //stage = new Stage(viewport, GameScreen.batch);
        stage = new Stage(viewport, batch);
        this.debugMode = debugMode;
        this.score = score;
        this.life = life;
        this.movementSpeed = movementSpeed;
        this.firepower = firepower;
        this.shield = shield;
        this.enemyCount = enemyCount;
        this.position = initPosition;

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void draw(SpriteBatch batch){
        //stage.draw();
        batch.begin();

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
        font.draw(batch, String.format(Locale.getDefault(), "%02d", shield), 450, 40,
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

    void update(Tank tank) {
        this.movementSpeed = tank.movementSpeed;
        this.life = tank.life;
        this.position.x = tank.getX();
        this.position.y = tank.getY();
        this.firepower = tank.firepower;
        this.shield = tank.shield;
    }
}