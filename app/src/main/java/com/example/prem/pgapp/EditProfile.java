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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditProfile extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_IMAGE_REQUEST = 123;
    private DatabaseReference databaseReference;
    private Intent intent;
    private FirebaseUser firebaseUser;
    private EditText editProfileName,editProfileAge,editProfileOccupation,editProfileContact,editProfileEmail,editProfileAddress;
    private RadioButton radioButtonMale,radioButtonFemale,radioButtonSingle,radioButtonMarried;
    private ImageView imageViewProfile;
    private Uri filePath;
    private String name,occupation,address,imagePath,contact,email;
    private boolean single,married,male,female;
    private int age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editProfileAddress=findViewById(R.id.editProfileAddress);
        editProfileName=findViewById(R.id.editProfileName);
        editProfileAge=findViewById(R.id.editProfileAge);
        editProfileContact=findViewById(R.id.editProfileContact);
        editProfileEmail=findViewById(R.id.editProfileEmail);
        editProfileOccupation=findViewById(R.id.editProfileOccupation);
        radioButtonFemale=findViewById(R.id.editProfileRadioGirls);
        radioButtonMale=findViewById(R.id.editProfileRadioBoys);
        radioButtonMarried=findViewById(R.id.editProfileRadioMarried);
        radioButtonSingle=findViewById(R.id.editProfileRadioSingle);
        imageViewProfile=findViewById(R.id.imageViewUser);
        imageViewProfile.setOnClickListener(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child(firebaseUser.getUid()).child("name").getValue(String.class);
                String email=dataSnapshot.child(firebaseUser.getUid()).child("email").getValue(String.class);
                String contact=dataSnapshot.child(firebaseUser.getUid()).child("mobile").getValue(String.class);
                editProfileEmail.setText(email);
                editProfileContact.setText(contact);
                editProfileName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference=FirebaseDatabase.getInstance().getReference("Profile");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(firebaseUser.getUid()))
                {
                    age=dataSnapshot.child(firebaseUser.getUid()).child("age").getValue(Integer.class);
                    male=dataSnapshot.child(firebaseUser.getUid()).child("male").getValue(Boolean.class);
                    female=dataSnapshot.child(firebaseUser.getUid()).child("female").getValue(Boolean.class);
                    single=dataSnapshot.child(firebaseUser.getUid()).child("single").getValue(Boolean.class);
                    married=dataSnapshot.child(firebaseUser.getUid()).child("married").getValue(Boolean.class);
                    String imagePath=dataSnapshot.child(firebaseUser.getUid()).child("image").getValue(String.class);
                    String occupation=dataSnapshot.child(firebaseUser.getUid()).child("occupation").getValue(String.class);
                    String address=dataSnapshot.child(firebaseUser.getUid()).child("address").getValue(String.class);
                    editProfileAge.setText(String.valueOf(age));
                    editProfileAddress.setText(address);
                    editProfileOccupation.setText(occupation);
                    radioButtonFemale.setChecked(female);
                    radioButtonMale.setChecked(male);
                    radioButtonMarried.setChecked(married);
                    System.out.println(imagePath);
                    radioButtonSingle.setChecked(true);
                    if(imagePath!=null){
                    filePath=Uri.parse(imagePath);
                    Picasso.with(getApplicationContext()).load(filePath).into(imageViewProfile);
                }}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_pg, menu);
        menu.findItem(R.id.buttonAdd).setVisible(true);
        return true;
    }

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if(result)
            Toast.makeText(getApplicationContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
        return result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.menuLogout:
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new Intent(intent));
                finish();
                FirebaseAuth.getInstance().signOut();
                return true;
            case android.R.id.home:
                databaseReference=FirebaseDatabase.getInstance().getReference("Owners");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid()))
                            intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                        else
                            intent=new Intent(getApplicationContext(),ViewPG.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            case R.id.buttonAdd:
                    getValues();
                    if(isEmptyField(editProfileAddress))
                        return true;
                    if(isEmptyField(editProfileAge))
                        return true;
                    if(isEmptyField(editProfileContact))
                        return true;
                    if(isEmptyField(editProfileEmail))
                        return true;
                    if(isEmptyField(editProfileName))
                        return true;
                    if(isEmptyField(editProfileOccupation))
                        return true;
                    if(!radioButtonFemale.isChecked() && !radioButtonMale.isChecked()){
                        Toast.makeText(getApplicationContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                        return true;}
                    if(!radioButtonMarried.isChecked() && !radioButtonSingle.isChecked()){
                        Toast.makeText(getApplicationContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                        return true;}
                databaseReference=FirebaseDatabase.getInstance().getReference("Profile");
                databaseReference.keepSynced(true);
                if(filePath!=null)
                {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    final String email=user.getEmail().toString();
                    final ProgressDialog progressDialog=new ProgressDialog(this);
                    progressDialog.setMessage("Updating profile!!");
                    progressDialog.show();
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final StorageReference picReference=storageReference.child("UsersProfilePic/"+email);
                    picReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_SHORT).show();
                            picReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                    imagePath = downloadUri.toString();
                                    ProfileUser profileUser=new ProfileUser(name,address,contact,occupation,email,imagePath,single,married,male,female,age);
                                    databaseReference=FirebaseDatabase.getInstance().getReference("Profile");
                                    databaseReference.child(firebaseUser.getUid()).setValue(profileUser);
                                    databaseReference=FirebaseDatabase.getInstance().getReference("Owners");
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(firebaseUser.getUid()))
                                                intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                                            else
                                                intent=new Intent(getApplicationContext(),ViewPG.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_LONG).show();
                                    ProfileUser profileUser=new ProfileUser(name,address,contact,occupation,email,imagePath,single,married,male,female,age);
                                    databaseReference=FirebaseDatabase.getInstance().getReference("Profile");
                                    databaseReference.child(firebaseUser.getUid()).setValue(profileUser);
                                    databaseReference=FirebaseDatabase.getInstance().getReference("Owners");
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(firebaseUser.getUid()))
                                                intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                                            else
                                                intent=new Intent(getApplicationContext(),ViewPG.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
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

    @Override
    public void onClick(View v) {
        if(v==imageViewProfile)
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
                imageViewProfile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }
    public void getValues()
    {
        single=radioButtonSingle.isChecked();
        married=radioButtonMarried.isChecked();
        male=radioButtonMale.isChecked();
        female=radioButtonFemale.isChecked();
        age=Integer.parseInt(editProfileAge.getText().toString());
        contact = editProfileContact.getText().toString();
        name=editProfileName.getText().toString();
        occupation=editProfileOccupation.getText().toString();
        email=editProfileEmail.getText().toString();
        address=editProfileAddress.getText().toString();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            intent = new Intent();
        }
        super.startActivityForResult(intent, requestCode);
    }
    @Override
    public void onBackPressed() {
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Owners");
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid()))
                    intent = new Intent(getApplicationContext(), OwnerHomeDrawer.class);
                else
                    intent = new Intent(getApplicationContext(), ViewPG.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onBackPressed();
    }
}
