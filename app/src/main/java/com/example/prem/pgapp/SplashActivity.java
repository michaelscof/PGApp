package com.example.prem.pgapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_ACTIVITY_TIMEOUT=2500;
    private int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar=getSupportActionBar();
        DatabaseUtil.getDatabase();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        actionBar.hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {
                    final String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Intent intent;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("Owners").hasChild(user))
                                    flag =0;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(flag==1)
                        intent=new Intent(getApplicationContext(),ViewPG.class);
                    else
                       intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(new Intent(intent));
                    finish();
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(new Intent(intent));
                    finish();
                }
            }
        },SPLASH_ACTIVITY_TIMEOUT);
    }
}
