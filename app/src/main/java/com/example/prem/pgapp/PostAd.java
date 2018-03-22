package com.example.prem.pgapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class PostAd extends AppCompatActivity implements View.OnClickListener {
    private Button postAdSubmit;
    private EditText editTextName,editTextAddress,editTextLocation,editTextLandmark,editTextMobile,editTextSeater,editTextPrice;
    private RadioButton radioButtonBoys,radioButtonGirls;
    private CheckBox checkBoxAc,checkBoxLaundry,checkBoxWiFi,checkBoxMaid,checkBoxFood;
    private int pgcount,seater,price;
    private boolean ac,maid,laundry,wifi,food,boys,girls;
    private FirebaseUser firebaseUser;
    private String name,address,location,landmark,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);
        postAdSubmit=findViewById(R.id.postAdSubmit);
        editTextName=findViewById(R.id.postAdName);
        editTextAddress=findViewById(R.id.postAdAddress);
        editTextLandmark=findViewById(R.id.postAdLandmark);
        editTextLocation=findViewById(R.id.postAdLocation);
        editTextMobile=findViewById(R.id.postAdMobile);
        editTextSeater=findViewById(R.id.postAdSeater);
        editTextPrice=findViewById(R.id.postAdPrice);
        radioButtonBoys=findViewById(R.id.postAdRadioBoys);
        radioButtonGirls=findViewById(R.id.postAdRadioGirls);
        checkBoxAc=findViewById(R.id.postAdAc);
        checkBoxFood=findViewById(R.id.postAdFood);
        checkBoxLaundry=findViewById(R.id.postAdLaundry);
        checkBoxMaid=findViewById(R.id.postAdMaid);
        checkBoxWiFi=findViewById(R.id.postAdWifi);
        postAdSubmit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {
            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Owners");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pgcount=dataSnapshot.child(firebaseUser.getUid().toString()).child("pgCount").getValue(Integer.class);
                    pgcount=pgcount+1;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    public void getValues()
    {
        ac=checkBoxAc.isChecked();
        food=checkBoxFood.isChecked();
        wifi=checkBoxWiFi.isChecked();
        laundry=checkBoxLaundry.isChecked();
        maid=checkBoxMaid.isChecked();
        boys=radioButtonBoys.isChecked();
        girls=radioButtonGirls.isChecked();
        seater=Integer.parseInt(editTextSeater.getText().toString());
        price=Integer.parseInt(editTextPrice.getText().toString());
        contact = editTextMobile.getText().toString();
        name=editTextName.getText().toString();
        landmark=editTextLandmark.getText().toString();
        location=editTextLocation.getText().toString();
        address=editTextAddress.getText().toString();
    }
    public void postAd()
    {
        getValues();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("PGs");
        PostAdDB postAdDB=new PostAdDB(name,address,location,landmark,"1234",seater,price,contact,boys,girls,ac,wifi,food,maid,laundry);
        databaseReference.push().setValue(postAdDB);
        databaseReference=FirebaseDatabase.getInstance().getReference("Owners");
        OwnerPGCount ownerPGCount=new OwnerPGCount(pgcount);
        databaseReference.child(firebaseUser.getUid()).setValue(ownerPGCount);
    }
    @Override
    public void onClick(View view) {
        if(view==postAdSubmit)
        {
            postAd();
        }
    }
    public void Facilities(View view)
    {

    }
}
