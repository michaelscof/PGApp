package com.example.prem.pgapp;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.net.URI;
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
    private MaterialSearchView searchView;
    private Query query;
    public String user,image1,pass;
    FirebaseRecyclerAdapter<PostAdDB,PGHolder> adapter;
    FirebaseRecyclerAdapter<PostAdDB,PGHolder> adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pg);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //pass=getIntent().getExtras().getString("password");
        //Toast.makeText(this, user, Toast.LENGTH_SHORT).show();
        View header = navigationView.getHeaderView(0);
        imageViewCust = header.findViewById(R.id.imageViewCust);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/" + user);
        textViewEmail = header.findViewById(R.id.textViewEmail1);
        textViewName =  header.findViewById(R.id.textViewName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "App developed by Prem Kagrani", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Profile/"+FirebaseAuth.getInstance().getUid());
        databaseReference1.keepSynced(true);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    image1 = dataSnapshot.child("image").getValue(String.class);
                    Uri filePath = Uri.parse(image1);
                    Picasso.with(getBaseContext()).load(filePath).resize(200, 200).transform(new CircleTransform()).centerCrop().into(imageViewCust);
                } else
                    imageViewCust.setImageResource(R.mipmap.ic_launcher_round);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue(String.class);
                textViewEmail.setText(email);
                String name = dataSnapshot.child("name").getValue(String.class);
                textViewName.setText(name);
                String password=dataSnapshot.child("password").getValue(String.class);
              /*  if(pass!=password)
                {
                    databaseReference.child("password").setValue(String.class);
                }
                */
//                final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                final StorageReference picReference=storageReference.child("UsersProfilePic/"+email);
//                Picasso.with(getBaseContext()).load(image1).resize(200,200).transform(new CircleTransform()).centerCrop().into(imageViewCust);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewCust);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference databaseReferencePG = FirebaseDatabase.getInstance().getReference("PGs");
        databaseReferencePG.keepSynced(true);
        query = databaseReferencePG.orderByKey();
        FirebaseRecyclerOptions<PostAdDB> options =
                new FirebaseRecyclerOptions.Builder<PostAdDB>()
                        .setQuery(query, PostAdDB.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                final String pgkey=getRef(position).getKey();
                holder.setName(model.getName());
                holder.setAddress(model.getLocation());
                holder.setType(model.isBoys());
                holder.setLandmark("\u20B9"+ model.getPrice());
                holder.setImage(getBaseContext(),model.getImage());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                        intent.putExtra("pgkey",pgkey);
                        startActivity(new Intent(intent));
                        finish();
                        return;
                    }
                });
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
        getMenuInflater().inflate(R.menu.view_pg, menu);
        menu.findItem(R.id.buttonAdd).setVisible(false);
        menu.findItem(R.id.menuLogout).setVisible(false);
        /*MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);*/
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
            Intent intent=new Intent(this,EditProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_boys) {
            DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
            query = databaseReferencePG1.orderByChild("boys").equalTo(true);
            FirebaseRecyclerOptions<PostAdDB> options1 =
                    new FirebaseRecyclerOptions.Builder<PostAdDB>()
                            .setQuery(query, PostAdDB.class)
                            .build();
            adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                @Override
                protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                    final String pgkey=getRef(position).getKey();
                    holder.setName(model.getName());
                    holder.setAddress(model.getAddress());
                    holder.setType(model.isBoys());
                    holder.setLandmark("\u20B9"+ model.getPrice());
                    holder.setImage(getBaseContext(),model.getImage());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                            intent.putExtra("pgkey",pgkey);
                            startActivity(new Intent(intent));
                            finish();
                            return;
                        }
                    });
                }

                @Override
                public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pg_list_layout, parent, false);
                    return new PGHolder(view);
                }
            };
            adapter1.startListening();
            recyclerView.setAdapter(adapter1);
        } else if (id == R.id.nav_girls) {
            DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
            query = databaseReferencePG1.orderByChild("girls").equalTo(true);
            FirebaseRecyclerOptions<PostAdDB> options1 =
                    new FirebaseRecyclerOptions.Builder<PostAdDB>()
                            .setQuery(query, PostAdDB.class)
                            .build();
            adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                @Override
                protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                    holder.setName(model.getName());
                    final String pgkey=getRef(position).getKey();
                    holder.setAddress(model.getAddress());
                    holder.setType(model.isBoys());
                    holder.setLandmark("\u20B9"+ model.getPrice());
                    holder.setImage(getBaseContext(),model.getImage());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                            intent.putExtra("pgkey",pgkey);
                            startActivity(new Intent(intent));
                            finish();
                            return;
                        }
                    });
                }

                @Override
                public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pg_list_layout, parent, false);
                    return new PGHolder(view);
                }
            };
            adapter1.startListening();
            recyclerView.setAdapter(adapter1);


        } else if (id == R.id.nav_logout) {
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(intent));
            finish();
            FirebaseAuth.getInstance().signOut();
        }
        else if(id==R.id.nav_ac)
        {
            DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
            query = databaseReferencePG1.orderByChild("ac").equalTo(true);
            FirebaseRecyclerOptions<PostAdDB> options1 =
                    new FirebaseRecyclerOptions.Builder<PostAdDB>()
                            .setQuery(query, PostAdDB.class)
                            .build();
            adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                @Override
                protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                    holder.setName(model.getName());
                    holder.setAddress(model.getAddress());
                    holder.setType(model.isBoys());
                    final String pgkey=getRef(position).getKey();
                    holder.setLandmark("\u20B9"+ model.getPrice());
                    holder.setImage(getBaseContext(),model.getImage());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                            intent.putExtra("pgkey",pgkey);
                            startActivity(new Intent(intent));
                            finish();
                            return;
                        }
                    });
                }

                @Override
                public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pg_list_layout, parent, false);
                    return new PGHolder(view);
                }
            };
            adapter1.startListening();
            recyclerView.setAdapter(adapter1);
        }
        else if(id==R.id.nav_food)
        {
            DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
            query = databaseReferencePG1.orderByChild("food").equalTo(true);
            FirebaseRecyclerOptions<PostAdDB> options1 =
                    new FirebaseRecyclerOptions.Builder<PostAdDB>()
                            .setQuery(query, PostAdDB.class)
                            .build();
            adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                @Override
                protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                    holder.setName(model.getName());
                    holder.setAddress(model.getAddress());
                    holder.setType(model.isBoys());
                    final String pgkey=getRef(position).getKey();
                    holder.setLandmark("\u20B9"+ model.getPrice());
                    holder.setImage(getBaseContext(),model.getImage());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                            intent.putExtra("pgkey",pgkey);
                            startActivity(new Intent(intent));
                            finish();
                            return;
                        }
                    });
                }

                @Override
                public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pg_list_layout, parent, false);
                    return new PGHolder(view);
                }
            };
            adapter1.startListening();
            recyclerView.setAdapter(adapter1);
        }
        else if(id==R.id.nav_Maid)
        {
            DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
            query = databaseReferencePG1.orderByChild("maid").equalTo(true);
            FirebaseRecyclerOptions<PostAdDB> options1 =
                    new FirebaseRecyclerOptions.Builder<PostAdDB>()
                            .setQuery(query, PostAdDB.class)
                            .build();
            adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                @Override
                protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                    holder.setName(model.getName());
                    holder.setAddress(model.getAddress());
                    holder.setType(model.isBoys());
                    final String pgkey=getRef(position).getKey();
                    holder.setLandmark("\u20B9"+ model.getPrice());
                    holder.setImage(getBaseContext(),model.getImage());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                            intent.putExtra("pgkey",pgkey);
                            startActivity(new Intent(intent));
                            finish();
                            return;
                        }
                    });
                }

                @Override
                public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pg_list_layout, parent, false);
                    return new PGHolder(view);
                }
            };
            adapter1.startListening();
            recyclerView.setAdapter(adapter1);
        }
        else if(id==R.id.nav_wifi)
        {
            DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
            query = databaseReferencePG1.orderByChild("wifi").equalTo(true);
            FirebaseRecyclerOptions<PostAdDB> options1 =
                    new FirebaseRecyclerOptions.Builder<PostAdDB>()
                            .setQuery(query, PostAdDB.class)
                            .build();
            adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                @Override
                protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                    holder.setName(model.getName());
                    holder.setAddress(model.getAddress());
                    holder.setType(model.isBoys());
                    holder.setLandmark("\u20B9"+ model.getPrice());
                    final String pgkey=getRef(position).getKey();
                    holder.setImage(getBaseContext(),model.getImage());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                            intent.putExtra("pgkey",pgkey);
                            startActivity(new Intent(intent));
                            finish();
                            return;
                        }
                    });
                }

                @Override
                public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pg_list_layout, parent, false);
                    return new PGHolder(view);
                }
            };
            adapter1.startListening();
            recyclerView.setAdapter(adapter1);
        }
        else if(id==R.id.nav_all)
        {
            adapter.startListening();
            recyclerView.setAdapter(adapter);
        }
        else if (id==R.id.nav_change_password)
        {
            Intent intent=new Intent(this,ChangePassword.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.nav_laundry)
        {
            DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
            query = databaseReferencePG1.orderByChild("laundry").equalTo(true);
            FirebaseRecyclerOptions<PostAdDB> options1 =
                    new FirebaseRecyclerOptions.Builder<PostAdDB>()
                            .setQuery(query, PostAdDB.class)
                            .build();
            adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                @Override
                protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                    holder.setName(model.getName());
                    holder.setAddress(model.getAddress());
                    holder.setType(model.isBoys());
                    holder.setLandmark("\u20B9"+ model.getPrice());
                    holder.setImage(getBaseContext(),model.getImage());
                    final String pgkey=getRef(position).getKey();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                            intent.putExtra("pgkey",pgkey);
                            startActivity(new Intent(intent));
                            finish();
                            return;
                        }
                    });
                }

                @Override
                public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pg_list_layout, parent, false);
                    return new PGHolder(view);
                }
            };
            adapter1.startListening();
            recyclerView.setAdapter(adapter1);
        }
        else if(id==R.id.nav_location)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText editTextLocation = new EditText(getApplicationContext());
            editTextLocation.setGravity(Gravity.CENTER);
            alert.setTitle("Enter Location");
            alert.setView(editTextLocation);
            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
                    String loc=editTextLocation.getText().toString();
                    query = databaseReferencePG1.orderByChild("location").equalTo(loc);
                    FirebaseRecyclerOptions<PostAdDB> options1 =
                            new FirebaseRecyclerOptions.Builder<PostAdDB>()
                                    .setQuery(query, PostAdDB.class)
                                    .build();
                    adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                        @Override
                        protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                            holder.setName(model.getName());
                            holder.setAddress(model.getLocation());
                            holder.setType(model.isBoys());
                            holder.setLandmark("\u20B9"+ model.getPrice());
                            holder.setImage(getBaseContext(),model.getImage());
                            final String pgkey=getRef(position).getKey();
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                                    intent.putExtra("pgkey",pgkey);
                                    startActivity(new Intent(intent));
                                    finish();
                                    return;
                                }
                            });
                        }

                        @Override
                        public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.pg_list_layout, parent, false);
                            return new PGHolder(view);
                        }
                    };
                    adapter1.startListening();
                    recyclerView.setAdapter(adapter1);
                }
            });
            alert.show();
        }
        else if(id==R.id.nav_price)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText editTextPrice = new EditText(getApplicationContext());
            editTextPrice.setHeight(200);
            editTextPrice.setWidth(340);
            editTextPrice.setGravity(Gravity.CENTER);
            alert.setTitle("Enter your Budget");
            alert.setView(editTextPrice);
            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    DatabaseReference databaseReferencePG1=FirebaseDatabase.getInstance().getReference("PGs");
                    double budget=Double.parseDouble(editTextPrice.getText().toString());
                    query = databaseReferencePG1.orderByChild("price").endAt(budget);
                    FirebaseRecyclerOptions<PostAdDB> options1 =
                            new FirebaseRecyclerOptions.Builder<PostAdDB>()
                                    .setQuery(query, PostAdDB.class)
                                    .build();
                    adapter1=new FirebaseRecyclerAdapter<PostAdDB, PGHolder>(options1) {
                        @Override
                        protected void onBindViewHolder(@NonNull PGHolder holder, int position, @NonNull PostAdDB model) {
                            holder.setName(model.getName());
                            holder.setAddress(model.getAddress());
                            holder.setType(model.isBoys());
                            holder.setLandmark("\u20B9"+ model.getPrice());
                            holder.setImage(getBaseContext(),model.getImage());
                            final String pgkey=getRef(position).getKey();
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getApplicationContext(),SinglePGActivity.class);
                                    intent.putExtra("pgkey",pgkey);
                                    startActivity(new Intent(intent));
                                    finish();
                                    return;
                                }
                            });
                        }

                        @Override
                        public PGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.pg_list_layout, parent, false);
                            return new PGHolder(view);
                        }
                    };
                    adapter1.startListening();
                    recyclerView.setAdapter(adapter1);
                }
            });
            alert.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}