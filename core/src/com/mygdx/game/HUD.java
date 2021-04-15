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
    int score, life, enemyCount;
    Vector2 position;
    boolean debugMode = true;

    public HUD(int score, int life, int enemyCount, Vector2 initPosition, boolean debugMode) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, GameScreen.batch);
        this.debugMode = debugMode;
        this.score = score;
        this.life = life;
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

        font.draw(batch, "Enemy", 100, 450, 20, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", enemyCount), 100, 400,
                20, Align.left, false);

        if(debugMode) {
            font.draw(batch, "Position", 100, 350, 20, Align.left, false);
            font.draw(batch, this.position.toString(), 100, 300,
                    20, Align.left, false);
        }

        font.draw(batch, "Score", 400, 450, 20, Align.center, false);
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), 400, 400,
                20,  Align.center, false);

        font.draw(batch, "Life", 700, 450, 20, Align.right, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", life), 700, 400,
                20, Align.right, false);

        if (this.life <= 0)
            font.draw(batch, "YOU DIED", 400, 250, 20, Align.center, false);
        batch.end();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}