package com.example.prem.pgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnerHomeDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView ownerName,ownerEmail,noOfPGs;
    private ImageView ownerPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ownerName=findViewById(R.id.ownerName);
        ownerEmail=findViewById(R.id.ownerEmail);
        noOfPGs=findViewById(R.id.ownerProperties);
        ownerPic=findViewById(R.id.ownerImageView);
        if(user!=null)
        {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users/"+user);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    ownerEmail.setText(email);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    ownerName.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference= FirebaseDatabase.getInstance().getReference("Owners/"+user);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int pgCount = dataSnapshot.child("pgCount").getValue(Integer.class);
                    noOfPGs.setText("No of Properties : " + pgCount);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.owner_home_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_post_ad) {
            Intent intent=new Intent(getApplicationContext(),PostAd.class);
            startActivity(new Intent(intent));
            finish();
        }
        else if (id == R.id.nav_view_properties)
        {
            Intent intent=new Intent(getApplicationContext(),ViewProperties.class);
            startActivity(new Intent(intent));
            finish();
        }
        else if (id == R.id.nav_edit_profile) {
            Intent intent=new Intent(getApplicationContext(),EditProfile.class);
            startActivity(new Intent(intent));
            finish();
        } else if (id == R.id.nav_change_password) {
            Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
            startActivity(new Intent(intent));
            finish();
        }
        else if (id == R.id.nav_logout) {
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(intent));
            finish();
            FirebaseAuth.getInstance().signOut();

        }
        else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
