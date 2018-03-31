package com.example.prem.pgapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    private EditText changePassword, confirmPassword;
    String password, cpassword;
    private Button submitButton;
    public String oldpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        changePassword=findViewById(R.id.changePassword);
        confirmPassword=findViewById(R.id.confirmPassword);
        submitButton=findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, OwnerHomeDrawer.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validatePassword() {
        if (TextUtils.isEmpty(password) || password.length() < 6 || !password.equals(cpassword))
            return false;
        return true;
    }

    private void changePassword() {
        password = changePassword.getText().toString().trim();
        cpassword = confirmPassword.getText().toString().trim();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!validatePassword()) {
            Toast.makeText(this,
                    "Invalid password , please enter valid password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        final String email = user.getEmail();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/password");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oldpassword=dataSnapshot.getValue(String.class);
                System.out.println(oldpassword);
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldpassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        databaseReference.setValue(password);
                                        Toast.makeText(getApplicationContext(), "Password has been updated",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to update password",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println(oldpassword);

    }
    @Override
    public void onClick(View v) {
        changePassword();
    }
}
