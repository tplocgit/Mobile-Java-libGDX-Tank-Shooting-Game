package com.mygdx.game;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;



public class AndroidInterface implements FirebaseInterface {

    private FirebaseDatabase database;
    private DatabaseReference myDbRef;
    public String TAG = "Android Interface";

    public AndroidInterface() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void writePlayerTankVal(String room, String userId, Tank playerTank) {
        Tank pT = playerTank;
        // Write a message to the database
        //myDbRef.child("player1").child(userId).setValue(playerTank);
        myDbRef = database.getReference();
        myDbRef.child("TankGame").child(room).child("Player").child(userId).child("x").setValue(pT.x);
        myDbRef.child("TankGame").child(room).child("Player").child(userId).child("y").setValue(pT.y);



        myDbRef.child("TankGame").child(room).child("Player").child(userId).child("firepower").setValue(pT.firepower);
        myDbRef.child("TankGame").child(room).child("Player").child(userId).child("shield").setValue(pT.shield);
        //myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletMag").setValue(pT.bulletMag);
        myDbRef.child("TankGame").child(room).child("Player").child(userId).child("direction").setValue(pT.direction);

        ArrayList<Bullet> bulletArrayList = pT.getBullets();
        for (int i = 0; i < bulletArrayList.size(); i++) {
            if (bulletArrayList.get(i).x < Graphic.TILE_SIZE * (Graphic.NUMBER_OF_HEIGHT_TILE - 1) && bulletArrayList.get(i).x > 0 &&
                    bulletArrayList.get(i).y < Graphic.TILE_SIZE * (Graphic.NUMBER_OF_WIDTH_TILE - 1) && bulletArrayList.get(i).y > 0) {
                myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletList")
                        .child(Integer.toString(i)).child("x").setValue(bulletArrayList.get(i).x);
                myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletList")
                        .child(Integer.toString(i)).child("y").setValue(bulletArrayList.get(i).y);
                myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletList")
                        .child(Integer.toString(i)).child("direction").setValue(bulletArrayList.get(i).getDirection());
            }
            /*else {
                myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletList")
                        .child(Integer.toString(i)).removeValue();
                myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletList")
                        .child(Integer.toString(i)).child("x").setValue(null);
                myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletList")
                        .child(Integer.toString(i)).child("y").setValue(null);
                myDbRef.child("TankGame").child(room).child("Player").child(userId).child("bulletList")
                        .child(Integer.toString(i)).child("direction").setValue(null);
            }*/
        }

        //myDbRef.child("player1").child(userId).child("currentTankTextureRegion").setValue(pT.currentTankTextureRegion);
        /*for (TextureRegion textureRegion : pT.tankTextureRegions) {
            myDbRef.child("player1").child(userId).child("currentTankTextureRegion").child("textureRegion").setValue(textureRegion);
        }*/
        /*movementSpeed, int firepower,
        int shield, float bulletWidth, float bulletHeight,
        float bulletMovementSpeed, float timeBetweenShots, int direction,
        TextureRegion[] tankTextureRegions, TextureRegion initTankTextureRegion,
                TextureRegion[] bulletTextureRegions, TextureRegion initBulletTextureRegion,
                TextureRegion shieldTextureRegion*/
    }

    @Override
    // Read from the database
    public void setValEventListener(final String target, final Tank tank) {
        myDbRef = database.getReference(target);
        myDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Tank value = dataSnapshot.getValue(Tank.class);


                Log.d(TAG, "Key of target " + target + " is: " + key);
                //Log.d(TAG, "Value of target " + target + " is: " + value);
                /*Log.d(TAG, "x of target " + target + " is: " + value.hitBox.x);
                Log.d(TAG, "y of target " + target + " is: " + value.hitBox.y);
                Log.d(TAG, "height of target " + target + " is: " + value.hitBox.height);
                Log.d(TAG, "width of target " + target + " is: " + value.hitBox.width);
                Log.d(TAG, "firepower of target " + target + " is: " + value.firepower);
                Log.d(TAG, "direction of target " + target + " is: " + value.direction);
                Log.d(TAG, "bulletMag of target " + target + " is: " + value.bulletMag);
                Log.d(TAG, "shield of target " + target + " is: " + value.shield);*/
                tank.x = value.x;
                tank.y = value.y;
                //tank.hitBox = value.hitBox;

                tank.firepower = value.firepower;
                tank.direction = value.direction;
                //tank.bulletMag = value.bulletMag;
                tank.shield = value.shield;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value of target" + target, error.toException());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void deletePlayerNode(final String key) {
        deleteNode(database.getReference().child("Player").getPath().toString(), key);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void deleteBullet(final String targetKey) {
        deleteNode(database.getReference().child("Player").child("bulletList").getPath().toString(), targetKey);
    }

    @Override
    public void deleteNode(final String parentPath, final String targetKey) {
        final DatabaseReference ref = database.getReference().child(parentPath);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(targetKey)) {
                    // run some code
                    Log.w(TAG, "Key \"" + targetKey + "\" not found.");
                    return;
                }
                Log.w(TAG, "OK");
                ref.child(targetKey).setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to delete \"" + targetKey + "\n of path \"" + parentPath + "\".", error.toException());
            }
        });
    }
}
