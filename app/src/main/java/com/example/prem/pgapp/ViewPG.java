package com.example.prem.pgapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPG extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    RecyclerView recyclerView;
    private ImageView imageViewCust;
    private NavigationView navigationView;
    private TextView textViewEmail,textViewName;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Query query;
    public String user;
    FirebaseRecyclerAdapter<PostAdDB,PGHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pg);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(this, user, Toast.LENGTH_SHORT).show();
        View header = navigationView.getHeaderView(0);
        imageViewCust = findViewById(R.id.imageViewCust);
        //imageViewCust.setImageBitmap();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/" + user);
        textViewEmail = (TextView) header.findViewById(R.id.textViewEmail1);
        textViewName = (TextView) header.findViewById(R.id.textViewName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue(String.class);
                textViewEmail.setText(email);
                String name = dataSnapshot.child("name").getValue(String.class);
                textViewName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewCust);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference databaseReferencePG = FirebaseDatabase.getInstance().getReference("PGs");
        query = databaseReferencePG.orderByKey();
        FirebaseRecyclerOptions<PostAdDB> options =
                new FirebaseRecyclerOptions.Builder<PostAdDB>()
                        .setQuery(query, PostAdDB.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                holder.setName(model.getName());
                holder.setAddress(model.getAddress());
                holder.setContact(model.getContact());
                holder.setLandmark(model.getLandmark());
                holder.setImage(getBaseContext(),model.getImage());
            }

            @Override
            public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.pg_list_layout, parent, false);
                return new PGHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
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
        getMenuInflater().inflate(R.menu.view_pg, menu);
        menu.findItem(R.id.buttonAdd).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.buttonAdd) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_boys) {


        } else if (id == R.id.nav_girls) {

        } else if (id == R.id.nav_logout) {
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(intent));
            FirebaseAuth.getInstance().signOut();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}