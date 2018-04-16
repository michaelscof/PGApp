package com.example.prem.pgapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.DrmManagerClient;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.security.acl.Owner;
import java.util.Map;

public class PostAd extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 123;
    private Button postAdSubmit;
    private ImageButton imageButtonPG;
    private EditText editTextName,editTextAddress,editTextLocation,editTextLandmark,editTextMobile,editTextSeater,editTextPrice;
    private RadioButton radioButtonBoys,radioButtonGirls;
    private CheckBox checkBoxAc,checkBoxLaundry,checkBoxWiFi,checkBoxMaid,checkBoxFood;
    private int pgcount,seater,price;
    private boolean ac,maid,laundry,wifi,food,boys,girls;
    private FirebaseUser firebaseUser;
    private String name,address,location,landmark,contact,uniqueid,imagePath;
    private Uri filePath;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);
        storageReference= FirebaseStorage.getInstance().getReference();
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
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_pg, menu);
        menu.findItem(R.id.buttonAdd).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuLogout:
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new Intent(intent));
                finish();
                FirebaseAuth.getInstance().signOut();
                return true;
            case android.R.id.home:
                intent = new Intent(this, OwnerHomeDrawer.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.buttonAdd:
                getValues();
                if(filePath!=null)
                {
                    final ProgressDialog progressDialog=new ProgressDialog(this);
                    progressDialog.setMessage("Uploading profile photo!!");
                    progressDialog.show();
                    StorageReference picReference=storageReference.child("PGs/"+uniqueid);
                    picReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri downloadUrl=taskSnapshot.getDownloadUrl();
                            imagePath=downloadUrl.toString();
                            System.out.println("Image path:"+imagePath);
                            PostAdDB postAdDB=new PostAdDB(name,address,location,landmark,imagePath,seater,price,contact,boys,girls,ac,wifi,food,maid,laundry,firebaseUser.getUid());
                            databaseReference.child(uniqueid).setValue(postAdDB);
                            databaseReference=FirebaseDatabase.getInstance().getReference("Owners");
                            OwnerPGCount ownerPGCount=new OwnerPGCount(pgcount);
                            databaseReference.child(firebaseUser.getUid()).setValue(ownerPGCount);
                            Intent intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),"Ad posted",Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
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
            default:
                return super.onOptionsItemSelected(item);
        }
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
        databaseReference=FirebaseDatabase.getInstance().getReference("PGs");
        uniqueid=databaseReference.push().getKey();
    }
    public void postAd()
    {

    }
    @Override
    public void onClick(View view) {
        if(view==imageButtonPG)
        {
            showFileChooser();
        }
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
    private void uploadFile()
    {
    }
    private void showFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    public void Facilities(View view)
    {

    }
    @Override
    public void onBackPressed() {
        intent = new Intent(getApplicationContext(), OwnerHomeDrawer.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
