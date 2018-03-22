package com.example.prem.pgapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostAd extends AppCompatActivity implements View.OnClickListener {
    private Button buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);
      //  buttonAdd=(Button)findViewById(R.id.seaterplus);
        //buttonAdd.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.findItem(R.id.buttonAdd).setVisible(true);
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}
