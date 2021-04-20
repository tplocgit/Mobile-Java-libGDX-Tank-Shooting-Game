package com.mygdx.game;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AndroidInterface implements FirebaseInterface {

    private FirebaseDatabase database;
    private DatabaseReference myDbRef;
    public String TAG = "Android Interface";

    public AndroidInterface() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void writePlayerTankVal(String userId, Tank playerTank) {
        Tank pT = playerTank;
        // Write a message to the database
        //myDbRef.child("player1").child(userId).setValue(playerTank);
        myDbRef = database.getReference();
        myDbRef.child("Player").child(userId).child("hitBox").setValue(pT.hitBox);
        myDbRef.child("Player").child(userId).child("firepower").setValue(pT.firepower);
        myDbRef.child("Player").child(userId).child("shield").setValue(pT.shield);
        myDbRef.child("Player").child(userId).child("bulletMag").setValue(pT.bulletMag);
        myDbRef.child("Player").child(userId).child("movementSpeed").setValue(pT.movementSpeed);
        myDbRef.child("Player").child(userId).child("timeBetweenShots").setValue(pT.timeBetweenShots);
        myDbRef.child("Player").child(userId).child("direction").setValue(pT.direction);
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
    public void readPlayerTankVal(final String target) {
        myDbRef = database.getReference(target);
        myDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Tank value = dataSnapshot.getValue(Tank.class);
                Log.d(TAG, "Key of target " + target + " is: " + key);
                Log.d(TAG, "Value of target " + target + " is: " + value);
                Log.d(TAG, "x of target " + target + " is: " + value.hitBox.x);
                Log.d(TAG, "y of target " + target + " is: " + value.hitBox.y);
                Log.d(TAG, "height of target " + target + " is: " + value.hitBox.height);
                Log.d(TAG, "width of target " + target + " is: " + value.hitBox.width);
                Log.d(TAG, "firepower of target " + target + " is: " + value.firepower);
                Log.d(TAG, "direction of target " + target + " is: " + value.direction);
                Log.d(TAG, "bulletMag of target " + target + " is: " + value.bulletMag);
                Log.d(TAG, "movementSpeed of target " + target + " is: " + value.movementSpeed);
                Log.d(TAG, "shield of target " + target + " is: " + value.shield);
                Log.d(TAG, "timeBetweenShots of target " + target + " is: " + value.timeBetweenShots);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value of target" + target, error.toException());
            }
        });
    };
}