package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
//    protected Rectangle hitBox;



//    public GameObject(float xPos, float yPos, float width, float height) {
//        super(xPos, yPos, width, height);
//        this.hitBox = new Rectangle(this);
//    }
//
//    public GameObject(Rectangle hitBox) {
//        super(hitBox);
//        this.hitBox = new Rectangle(this);
//
//    }

//    public static Rectangle calculateHitBox(Tank texture, float hbWidth, float hbHeight) {
//        float hbX, hbY;
//        hbX = texture.getX() + (texture.getWidth() - hbWidth) / 2f;
//        hbY = texture.getY() + (texture.getHeight() - hbHeight) / 2f;
//        return new Rectangle(hbX, hbY, hbWidth, hbHeight);
//    }



//    public void rotateHitBox90Deg() {
//        float prevWidth = this.hitBox.getWidth(), prevHeight = this.hitBox.getHeight();
//        this.hitBox.setWidth(prevHeight);
//        this.hitBox.setHeight(prevWidth);
//    }


    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2();
    private Vector2 origin = new Vector2();
    private Vector2 velocity = new Vector2();
    private float speed = 0;
    private boolean collidable = false;
    private boolean blockable = false;
    private boolean isBlocked = false;

    private Rectangle hitBox = new Rectangle();

    private float rotation = 0;

    private Sprite sprite = null;

    //contructor

    public GameObject() {
        PvEScreen.getInstance().getGameObjectList().add(this);
    }

    public static void Destroy(GameObject gameObject){
        PvEScreen.getInstance().getGameObjectList().remove(gameObject);
    }



//    Graphic.TILE_SIZE, Graphic.TILE_SIZE
    public void draw(Batch batch){

        if(sprite != null){
            sprite.setBounds(position.x - sprite.getWidth() / 2f, position.y - sprite.getHeight() / 2f,
                    sprite.getWidth(), sprite.getHeight());
            sprite.draw(batch);
        }
    }

    public void update(){
        if(movable()){
            position.x += speed * velocity.x;
            position.y += speed * velocity.y;
        }
        CheckCollideGameObject();
    }

    public void setImage(TextureRegion textureRegion) {
        this.sprite = new Sprite(textureRegion);
        hitBox.setWidth(textureRegion.getRegionWidth());
        hitBox.setHeight(textureRegion.getRegionHeight());
        origin.x = hitBox.width / 2f;
        origin.y = hitBox.height / 2f;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Rectangle getHitBox() {
        this.hitBox.x = position.x - hitBox.width/2f ;
        this.hitBox.y = position.y - hitBox.height/2f;
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity.nor();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean isBlockable() {
        return blockable;
    }

    public void setBlockable(boolean blockable) {
        this.blockable = blockable;
    }

    //    public Rectangle getNextLeftHitBox(float dt) {
//        return new Rectangle(this.getHitBox().x - this.getSpeed() * dt, this.getHitBox().y, this.getHitBox().width, this.getHitBox().height);
//    }
//
//    public Rectangle getNextRightHitBox(float dt) {
//        return new Rectangle(this.getHitBox().x + this.getSpeed() * dt, this.getHitBox().y, this.getHitBox().width, this.getHitBox().height);
//    }
//
//    public Rectangle getNextUpHitBox(float dt) {
//        return new Rectangle(this.getHitBox().x, this.getHitBox().y + this.getSpeed() * dt, this.getHitBox().width, this.getHitBox().height);
//    }
//
//    public Rectangle getNextDownHitBox(float dt) {
//        return new Rectangle(this.getHitBox().x, this.getHitBox().y - this.getSpeed() * dt, this.getHitBox().width, this.getHitBox().height);
//    }

    public Rectangle getNextMoveHitBox() {

        this.hitBox.x = position.x - hitBox.width/2f ;
        this.hitBox.y = position.y - hitBox.height/2f;

        return new Rectangle(this.hitBox.x + speed * velocity.x, this.hitBox.y + speed * velocity.y,
                this.hitBox.width, this.hitBox.height);
    }

    private boolean movable(){
        if(this.blockable){
            //colli wall
            for(RectangleMapObject objectRectangle : PvEScreen.getInstance().mapObjects.getByType(RectangleMapObject.class)) {
                Rectangle objectBounds = objectRectangle.getRectangle();
                if(getNextMoveHitBox().overlaps(objectBounds)) {
                    return false;
                }
            }
            //check tank colli
            for (GameObject gameObject : PvEScreen.getInstance().getGameObjectList()){
                if(gameObject.isBlockable() && getNextMoveHitBox().overlaps(gameObject.getHitBox()) && !gameObject.equals(this)){
                    return false;
                }
            }
        }
        return true;
    }

    protected void onCollided(GameObject gameObject){

    }

    private void CheckCollideGameObject(){
//        check tank colli
        if(collidable){
            for (GameObject gameObject : PvEScreen.getInstance().getGameObjectList()){
                if(gameObject.isCollidable() && getNextMoveHitBox().overlaps(gameObject.getHitBox()) && !gameObject.equals(this)){
                    onCollided(gameObject);
                }
            }
        }

    }
}
