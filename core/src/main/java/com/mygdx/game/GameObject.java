package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.network.AssetManager;
import com.mygdx.game.network.GameServer;
import com.mygdx.game.network.PvPScreen;
import gameservice.GameService;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameObject {

    public static CopyOnWriteArrayList<GameObject> gameObjectList = new CopyOnWriteArrayList<>();

    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2(1,1);
    private Vector2 origin = new Vector2();
    private Vector2 velocity = new Vector2();
    private float speed = 0;
    private boolean collidable = false;
    private boolean blockable = false;
    private boolean movable = false;

    private GameService.Texture textureID = GameService.Texture.TEXTURE_ATLAS;

    private Rectangle hitBox = new Rectangle();

    private float rotation = 0;

    private Sprite sprite = null;

    //contructor

    public GameObject() {
        gameObjectList.add(this);
    }

    public static void Destroy(GameObject gameObject){
        gameObjectList.remove(gameObject);
    }

    public static void ClearObjectList() {
        gameObjectList.clear();
    }

//    Graphic.TILE_SIZE, Graphic.TILE_SIZE
    public void draw(Batch batch){

        if(sprite != null){
            sprite.setBounds(position.x - sprite.getWidth() / 2f, position.y - sprite.getHeight() / 2f,
                    sprite.getWidth(), sprite.getHeight());
            sprite.setScale(scale.x, scale.y);
            sprite.draw(batch);
        }
    }

    public void update(){
        UpdateCollideGameObject();
        if(movable){
            position.x += speed * velocity.x;
            position.y += speed * velocity.y;
        }
    }

    public void setImage(TextureRegion textureRegion) {
        this.sprite = new Sprite(textureRegion);
        hitBox.setWidth(textureRegion.getRegionWidth());
        hitBox.setHeight(textureRegion.getRegionHeight());
        origin.x = hitBox.width / 2f;
        origin.y = hitBox.height / 2f;
        this.textureID = AssetManager.getInstance().getIDFromTexture(textureRegion);
    }

    public void setSize(float width, float height){
        if (sprite != null) {
            sprite.setSize(width, height);
        }
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

    public GameService.Texture getTextureID() {
        return textureID;
    }

    public void setTextureID(GameService.Texture textureID) {
        this.textureID = textureID;
    }

    public Rectangle getNextMoveHitBox() {

        this.hitBox.x = position.x - hitBox.width/2f ;
        this.hitBox.y = position.y - hitBox.height/2f;

        return new Rectangle(this.hitBox.x + speed * velocity.x, this.hitBox.y + speed * velocity.y,
                this.hitBox.width, this.hitBox.height);
    }


    protected void onCollided(GameObject gameObject){

    }

    protected void onMapCollided(Rectangle mapRec) {

    }

    //Check if the objet can move or not
    private void UpdateCollideGameObject(){
//        check tank colli
        movable = true;
        MapObjects currentMapObjects = GameScreen.getInstance().mapObjects;

        if(GameScreen.getInstance() instanceof PvPScreen) {
            currentMapObjects = GameServer.getInstance().getMapObjects();
        }

        if(collidable){
            for(RectangleMapObject objectRectangle : currentMapObjects.getByType(RectangleMapObject.class)) {
                Rectangle objectBounds = objectRectangle.getRectangle();
                if(getNextMoveHitBox().overlaps(objectBounds)) {
                    if (blockable){
                        movable = false;
                    }
                    onMapCollided(objectBounds);
                }
            }

            for (GameObject gameObject : gameObjectList){
                if(gameObject.isCollidable() && getNextMoveHitBox().overlaps(gameObject.getHitBox()) && !gameObject.equals(this)){
                    if (blockable && gameObject.isBlockable()) {
                        movable = false;
                    }
                    onCollided(gameObject);
                }
            }

        }

    }

    public GameService.GameObject getServiceObject(){
        GameService.GameObject.Builder gameObjectBuilder = GameService.GameObject.newBuilder()
                .setPosition(GameService.Vector2.newBuilder()
                    .setX(position.x)
                    .setY(position.y))
                .setScale(GameService.Vector2.newBuilder()
                    .setX(scale.x)
                    .setY(scale.y))
                .setOrigin(GameService.Vector2.newBuilder()
                    .setX(origin.x)
                    .setY(origin.y))
                .setVelocity(GameService.Vector2.newBuilder()
                    .setX(velocity.x)
                    .setY(velocity.y))
                .setSpeed(speed);

        if(textureID != null) {
            gameObjectBuilder.setTexture(textureID);
        }

        return gameObjectBuilder.build();
    }

}
