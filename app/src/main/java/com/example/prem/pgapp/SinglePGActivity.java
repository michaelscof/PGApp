package com.example.prem.pgapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglePGActivity extends AppCompatActivity implements View.OnClickListener{
    private String pgkey=null;
    private ImageView imageViewPG,imageViewAC,imageViewLaundry,imageViewFood,imageViewMaid,imageViewWifi;
    private TextView textViewName,textViewPrice,textViewLocation,textViewType;
    private ImageButton imageButtonLocation;
    private Button buttonCall;
    private DatabaseReference databaseReference;
    private String contact,landmark;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pg);
        pgkey=getIntent().getExtras().getString("pgkey");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        imageViewPG=findViewById(R.id.imageViewPG);
        textViewName=findViewById(R.id.textViewPGName);
        textViewLocation=findViewById(R.id.textViewPGLocation);
        textViewPrice=findViewById(R.id.textViewPGPrice);
        textViewType=findViewById(R.id.viewPGType);
        imageButtonLocation=findViewById(R.id.imageButtonLocation);
        imageViewAC=findViewById(R.id.showAC);
        imageViewFood=findViewById(R.id.showFood);
        imageViewLaundry=findViewById(R.id.showLaundry);
        imageViewMaid=findViewById(R.id.showMaid);
        imageViewWifi=findViewById(R.id.showWifi);
        buttonCall=findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(this);
        imageButtonLocation.setOnClickListener(this);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("PGs/"+pgkey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean ac=dataSnapshot.child("ac").getValue(Boolean.class);
                boolean food=dataSnapshot.child("food").getValue(Boolean.class);
                boolean wifi=dataSnapshot.child("wifi").getValue(Boolean.class);
                boolean laundry=dataSnapshot.child("laundry").getValue(Boolean.class);
                boolean maid=dataSnapshot.child("maid").getValue(Boolean.class);
                boolean boys=dataSnapshot.child("boys").getValue(Boolean.class);
                if(ac)
                    imageViewAC.setImageResource(R.drawable.ic_ac);
                else
                    imageViewAC.setImageResource(R.drawable.ic_no_ac);
                if(food)
                    imageViewFood.setImageResource(R.drawable.ic_food);
                else
                    imageViewFood.setImageResource(R.drawable.ic_no_food);
                if(maid)
                    imageViewMaid.setImageResource(R.drawable.ic_maid);
                else
                    imageViewMaid.setImageResource(R.drawable.ic_no_maid);
                if(laundry)
                    imageViewLaundry.setImageResource(R.drawable.ic_laundry);
                else
                    imageViewLaundry.setImageResource(R.drawable.ic_no_laundry);
                if(wifi)
                    imageViewWifi.setImageResource(R.drawable.ic_wifi);
                else
                    imageViewWifi.setImageResource(R.drawable.ic_no_wifi);
                if(boys)
                    textViewType.setText("Boys");
                else
                    textViewType.setText("Girls");
                String name=dataSnapshot.child("name").getValue(String.class);
                String location=dataSnapshot.child("location").getValue(String.class);
                int price=dataSnapshot.child("price").getValue(Integer.class);
                textViewName.setText(name);
                textViewLocation.setText(location);
                textViewPrice.setText("INR "+String.valueOf(price));
                String imagePath=dataSnapshot.child("image").getValue(String.class);
                Picasso.with(getBaseContext()).load(Uri.parse(imagePath)).into(imageViewPG);
                contact=dataSnapshot.child("contact").getValue(String.class);
                landmark=dataSnapshot.child("landmark").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
            if(v==buttonCall)
            {
                Uri number = Uri.parse("tel:"+contact);
                Intent callIntent = new Intent(Intent.ACTION_DIAL,number);
                startActivity(callIntent);
            }
            if(v==imageButtonLocation)
            {
                Intent intent=new Intent(getApplicationContext(),MapsActivityUser.class);
                intent.putExtra("landmark",landmark);
                startActivity(new Intent(intent));
                finish();
            }
    }
}
