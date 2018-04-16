package com.example.prem.pgapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonLogin;
    private EditText editTextEmail,editTextPassword;
    private TextView forgotPassword,newUser;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Spinner loginSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonLogin=findViewById(R.id.buttonLogin);
        loginSpinner=findViewById(R.id.loginSpinner);
        forgotPassword=findViewById(R.id.forgotPassword);
        newUser=findViewById(R.id.NewUser);
        progressDialog=new ProgressDialog(this);
        buttonLogin.setOnClickListener(this);
        newUser.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }
    private void userLogin()
    {
        String email=editTextEmail.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("User Logging In");
        progressDialog.show();
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Intent intent;
                    if(loginSpinner.getSelectedItemId()==0)
                        intent=new Intent(LoginActivity.this,OwnerHomeDrawer.class);
                    else
                        intent=new Intent(LoginActivity.this,ViewPG.class);
                    intent.putExtra("password",password);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(new Intent(intent));
                    finish();
                    return;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view==buttonLogin)
            userLogin();
        if(view==forgotPassword)
        {
            String email=editTextEmail.getText().toString().trim();
            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.setMessage("Sending email..");
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(view==newUser)
        {
            Intent intent=new Intent(getApplicationContext(),FirstPage.class);
            startActivity(intent);
            finish();
        }
    }
}
