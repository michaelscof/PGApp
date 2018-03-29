package com.example.prem.pgapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SinglePGActivity extends AppCompatActivity {
    private String pgkey=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pg);
        pgkey=getIntent().getExtras().getString("pgkey");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                Intent intent = new Intent(this, ViewPG.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                //Uri number = Uri.parse("tel:7668109336");
                //Intent callIntent = new Intent(Intent.ACTION_DIAL,number);
                //startActivity(callIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
