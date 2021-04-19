package com.mygdx.game;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.mygdx.game.GameScreen.*;

public class AndroidInterface implements FirebaseInterface{
    private FirebaseDatabase database;
    private DatabaseReference myDbRef;
    public String TAG = "Android Interface";

    public AndroidInterface() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void writePlayerTankVal(String userId, PlayerTank playerTank) {
        PlayerTank pT = playerTank;
        // Write a message to the database
        myDbRef = database.getReference();
        myDbRef.child("player1").child(userId).setValue(playerTank);
        /*myDbRef.child("player1").child(userId).child("movementSpeed").setValue(PLAYER_INITIAL_MOVERMENT_SPEED);
        myDbRef.child("player1").child(userId).child("firepower").setValue(pT.firepower);
        myDbRef.child("player1").child(userId).child("shield").setValue(pT.firepower);
        myDbRef.child("player1").child(userId).child("bulletWidth").setValue(pT.bulletWidth);
        myDbRef.child("player1").child(userId).child("bulletHeight").setValue(pT.bulletHeight);
        myDbRef.child("player1").child(userId).child("bulletMovementSpeed").setValue(pT.bulletMovementSpeed);
        myDbRef.child("player1").child(userId).child("timeBetweenShots").setValue(pT.timeBetweenShots);
        myDbRef.child("player1").child(userId).child("direction").setValue(pT.direction);*/
        /*movementSpeed, int firepower,
        int shield, float bulletWidth, float bulletHeight,
        float bulletMovementSpeed, float timeBetweenShots, int direction,
        TextureRegion[] tankTextureRegions, TextureRegion initTankTextureRegion,
                TextureRegion[] bulletTextureRegions, TextureRegion initBulletTextureRegion,
                TextureRegion shieldTextureRegion*/
    }

    @Override
    // Read from the database
    public void readValFromDb(final String target) {
        myDbRef = database.getReference(target);
        myDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Tank value = dataSnapshot.getValue(Tank.class);
                Log.d(TAG, "Value of target " + target + " is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value of target" + target, error.toException());
            }
        });
    };
}
