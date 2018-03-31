package com.example.prem.pgapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_ACTIVITY_TIMEOUT=3500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar=getSupportActionBar();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        actionBar.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {
                    final String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Intent intent;
                    if(databaseReference.child("Owners").child(String.valueOf(user))==null)
                        intent=new Intent(getApplicationContext(),ViewPG.class);
                    else
                        intent=new Intent(getApplicationContext(),PostAd.class);
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
