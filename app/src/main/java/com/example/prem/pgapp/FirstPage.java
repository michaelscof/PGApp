package com.example.prem.pgapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirstPage extends AppCompatActivity implements OnClickListener {
    private Button buttonRegister;
    private EditText editTextEmail,editTextPassword,editTextName,editTextMobile;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView oldUser;
    private FirebaseDatabase firebaseDatabase;
    private Spinner loginSpinner;
    public String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        firebaseDatabase=FirebaseDatabase.getInstance();
        buttonRegister=(Button)findViewById(R.id.buttonRegister);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextMobile=(EditText)findViewById(R.id.editTextMobile);
        buttonRegister.setOnClickListener(this);
        oldUser=findViewById(R.id.OldUser);
        oldUser.setOnClickListener(this);
        loginSpinner=findViewById(R.id.loginSpinner);
        progressDialog=new ProgressDialog(this);
    }
    private void saveCredentials()
    {
       /* String password=editTextPassword.getText().toString().trim();
        String email=editTextEmail.getText().toString().trim();
        final SaveCredentialsDB saveCredentialsDB=new SaveCredentialsDB(email,password);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
           DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
           ref.child("Users").child(user.getUid()).setValue(saveCredentialsDB);
        }*/
        /*FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        Map<String, String> userData = new HashMap<String, String>();
        userData.put("Email", email);
        userData.put("Password", password);
        ref = ref.child(email);
        usersRef.setValue(userData);*/
    }
    private void registerUser()
    {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6)
        {
            Toast.makeText(this,"Minimum password length should be 6!!",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User..");
        progressDialog.show();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Registration Successfull
                    progressDialog.dismiss();
                    Toast.makeText(FirstPage.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent=new Intent(getApplicationContext(),ViewPG.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(new Intent(intent));
                    //SaveCredentials to firebase database
                    String password=editTextPassword.getText().toString().trim();
                    String email=editTextEmail.getText().toString().trim();
                    String name=editTextName.getText().toString().trim();
                    String mobile=editTextMobile.getText().toString().trim();
                    final SaveCredentialsDB saveCredentialsDB=new SaveCredentialsDB(email,password,name,mobile);
                    final OwnerPGCount ownerPGCount=new OwnerPGCount(0);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("Users").child(user.getUid()).setValue(saveCredentialsDB);
                    }
                }
                else
                {
                    //Registration Unsuccessfull
                    //Log.i("Response","Failed to create user!!"+task.getException().getLocalizedMessage());
                    progressDialog.dismiss();
                    Toast.makeText(FirstPage.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
            if(view==buttonRegister){
            if(loginSpinner.getSelectedItemId()==1){
            registerUser();
            }
            if(loginSpinner.getSelectedItemId()==0)
            {
                   registerUserAsOwner();
            }
            saveCredentials();}
            if(view==oldUser)
            {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
    }

    private void registerUserAsOwner() {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6)
        {
            Toast.makeText(this,"Minimum password length should be 6!!",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User..");
        progressDialog.show();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Registration Successfull
                    progressDialog.dismiss();
                    Toast.makeText(FirstPage.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent=null;
                    if(loginSpinner.getSelectedItemId()==1)
                        intent=new Intent(getApplicationContext(),ViewPG.class);
                    if(loginSpinner.getSelectedItemId()==0)
                        intent=new Intent(getApplicationContext(),OwnerHomeDrawer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(new Intent(intent));
                    //SaveCredentials to firebase database
                    String password=editTextPassword.getText().toString().trim();
                    String email=editTextEmail.getText().toString().trim();
                    String name=editTextName.getText().toString().trim();
                    String mobile=editTextMobile.getText().toString().trim();
                    final SaveCredentialsDB saveCredentialsDB=new SaveCredentialsDB(email,password,name,mobile);
                    final OwnerPGCount ownerPGCount=new OwnerPGCount(0);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("Users").child(user.getUid()).setValue(saveCredentialsDB);
                        ref.child("Owners").child(user.getUid()).setValue(ownerPGCount);
                    }
                }
                else
                {
                    //Registration Unsuccessfull
                    progressDialog.dismiss();
                    Toast.makeText(FirstPage.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
