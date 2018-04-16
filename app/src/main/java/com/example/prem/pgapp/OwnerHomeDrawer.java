package com.example.prem.pgapp;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.security.acl.Owner;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerHomeDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView ownerName,ownerEmail,noOfPGs,headerName,headerEmail;
    private ImageView headerPic,ownerPic;
    private DatabaseReference databaseReference;
    private String password;
    private String image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //password=getIntent().getExtras().getString("Password");
        //Toast.makeText(this,password,Toast.LENGTH_LONG).show();
        final String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ownerName=findViewById(R.id.ownerName);
        noOfPGs=findViewById(R.id.ownerProperties);
        ownerPic=findViewById(R.id.ownerImageView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        headerPic=header.findViewById(R.id.imageViewUser);
        headerName=header.findViewById(R.id.textViewName);
        headerEmail=header.findViewById(R.id.textViewEmail);
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Profile");
        databaseReference1.keepSynced(true);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(user))
                    {
                       image1=dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("image").getValue(String.class);
                        Uri filePath=Uri.parse(image1);
                        Glide.with(getApplicationContext()).load(filePath).into(ownerPic);
                        Picasso.with(getBaseContext()).load(filePath).resize(200,200).transform(new CircleTransform()).centerCrop().into(headerPic);
                    }
                    else
                    {
                        headerPic.setImageResource(R.mipmap.ic_launcher_round);
                        ownerPic.setImageResource(R.mipmap.ic_launcher_round);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child(user).child("email").getValue(String.class);
                headerEmail.setText(email);
                String name = dataSnapshot.child(user).child("name").getValue(String.class);
                headerName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(user!=null)
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("Users/"+user);
            databaseReference.keepSynced(true);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    ownerName.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference= FirebaseDatabase.getInstance().getReference("Owners/");
            databaseReference.keepSynced(true);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user)) {
                            int pgCount = dataSnapshot.child(user).child("pgCount").getValue(Integer.class);
                            noOfPGs.setText("No of Properties : " + pgCount);
                        }
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
                Snackbar.make(view, "App developed by Prem Kagrani", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(), OwnerHomeDrawer.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
