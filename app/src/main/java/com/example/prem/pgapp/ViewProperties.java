package com.example.prem.pgapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
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

public class ViewProperties extends AppCompatActivity {
    private Intent intent;
    private Query query;
    public int pgCount;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<PostAdDB,PGHolder> adapter,adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_properties);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewOwner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference=FirebaseDatabase.getInstance().getReference("Owners/"+FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pgCount=dataSnapshot.child("pgCount").getValue(Integer.class);
                pgCount--;
                System.out.println("PGCount:"+pgCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final DatabaseReference databaseReferencePG = FirebaseDatabase.getInstance().getReference("PGs");
        query = databaseReferencePG.orderByChild("ownerid").equalTo(FirebaseAuth.getInstance().getUid());
        FirebaseRecyclerOptions<PostAdDB> options =
                new FirebaseRecyclerOptions.Builder<PostAdDB>()
                        .setQuery(query,PostAdDB.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                final String pgkey=getRef(position).getKey();
                holder.setName(model.getName());
                holder.setAddress(model.getLocation());
                holder.setType(model.isBoys());
                holder.setLandmark(model.getLandmark());
                holder.setImageOwner(getBaseContext(),model.getImage());
                holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       alertDialogBuilder.setTitle("Delete PG?").setMessage("Are you sure about this?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       databaseReferencePG.child(pgkey).removeValue();
                                       databaseReference.child("pgCount").setValue(pgCount);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                });
                holder.buttonModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),ModifyAd.class);
                        intent.putExtra("pgkey",pgkey);
                        startActivity(new Intent(intent));
                        finish();
                    }
                });
            }
            @Override
            public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.properties_list_layout, parent, false);
                return new PGHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                intent = new Intent(this, OwnerHomeDrawer.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
