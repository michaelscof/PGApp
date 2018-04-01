package com.example.prem.pgapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ModifyAd extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 123;
    private ImageButton imageButtonPG;
    private Uri filePath;
    private EditText editTextName,editTextAddress,editTextLocation,editTextLandmark,editTextMobile,editTextSeater,editTextPrice;
    private RadioButton radioButtonBoys,radioButtonGirls;
    private String name;
    private String address;
    private String location;
    private String landmark;
    private String contact;
    private String imagePath;
    private String pgkey;
    private DatabaseReference databaseReference;
    private CheckBox checkBoxAc,checkBoxLaundry,checkBoxWiFi,checkBoxMaid,checkBoxFood;
    private int seater,price;
    private boolean ac,maid,laundry,wifi,food,boys,girls;
    private StorageReference picReference,storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ad);
        storageReference= FirebaseStorage.getInstance().getReference();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        imageButtonPG=findViewById(R.id.imageButtonPG);
        imageButtonPG.setOnClickListener(this);
        pgkey = getIntent().getExtras().getString("pgkey");
        databaseReference= FirebaseDatabase.getInstance().getReference("PGs/"+pgkey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ac=dataSnapshot.child("ac").getValue(Boolean.class);
                food=dataSnapshot.child("food").getValue(Boolean.class);
                maid=dataSnapshot.child("maid").getValue(Boolean.class);
                wifi=dataSnapshot.child("wifi").getValue(Boolean.class);
                laundry=dataSnapshot.child("laundry").getValue(Boolean.class);
                boys=dataSnapshot.child("boys").getValue(Boolean.class);
                girls=dataSnapshot.child("girls").getValue(Boolean.class);
                seater=dataSnapshot.child("seater").getValue(Integer.class);
                price=dataSnapshot.child("price").getValue(Integer.class);
                name=dataSnapshot.child("name").getValue(String.class);
                address=dataSnapshot.child("address").getValue(String.class);
                contact=dataSnapshot.child("contact").getValue(String.class);
                location=dataSnapshot.child("location").getValue(String.class);
                landmark=dataSnapshot.child("landmark").getValue(String.class);
                imagePath=dataSnapshot.child("image").getValue(String.class);
                filePath=Uri.parse(imagePath);
                checkBoxAc.setChecked(ac);
                checkBoxFood.setChecked(food);
                checkBoxLaundry.setChecked(laundry);
                checkBoxMaid.setChecked(maid);
                checkBoxWiFi.setChecked(wifi);
                radioButtonBoys.setChecked(boys);
                radioButtonGirls.setChecked(girls);
                editTextAddress.setText(address);
                editTextName.setText(name);
                editTextLandmark.setText(landmark);
                editTextLocation.setText(location);
                editTextMobile.setText(contact);
                editTextPrice.setText(String.valueOf(price));
                editTextSeater.setText(String.valueOf(seater));
                Picasso.with(getApplicationContext()).load(filePath).resize(200,200).into(imageButtonPG);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        databaseReference= FirebaseDatabase.getInstance().getReference("PGs");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_pg, menu);
        menu.findItem(R.id.buttonAdd).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                final Intent intent = new Intent(this, ViewProperties.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.buttonAdd:
                getValues();
                if(filePath!=null)
                {
                    final ProgressDialog progressDialog=new ProgressDialog(this);
                    progressDialog.setMessage("Updating PG pic!!");
                    progressDialog.show();
                    picReference=storageReference.child("PGs/"+pgkey);
                    picReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri downloadUrl=taskSnapshot.getDownloadUrl();
                            imagePath=downloadUrl.toString();
                            System.out.println("Image path:"+imagePath);
                            Toast.makeText(getApplicationContext(),"PG Details modified",Toast.LENGTH_SHORT).show();
                            PostAdDB postAdDB=new PostAdDB(name,address,location,landmark,imagePath,seater,price,contact,boys,girls,ac,wifi,food,maid,laundry,FirebaseAuth.getInstance().getUid());
                            databaseReference.child(pgkey).setValue(postAdDB);
                            Intent intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"PG Details modified",Toast.LENGTH_SHORT).show();
                                    PostAdDB postAdDB=new PostAdDB(name,address,location,landmark,imagePath,seater,price,contact,boys,girls,ac,wifi,food,maid,laundry,FirebaseAuth.getInstance().getUid());
                                    databaseReference.child(pgkey).setValue(postAdDB);
                                    Intent intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Upload failed!!",Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageButtonPG.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v==imageButtonPG)
            showFileChooser();
    }
    private void showFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }
}
