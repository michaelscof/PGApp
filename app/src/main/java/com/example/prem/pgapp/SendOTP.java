package com.example.prem.pgapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextMobile,editTextOTP;
    private Button sendOTP,verifyOTP;
    private String codeSent;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        editTextMobile=findViewById(R.id.editTextMobile);
        editTextOTP=findViewById(R.id.editTextOTP);
        sendOTP=findViewById(R.id.sendOTP);
        verifyOTP=findViewById(R.id.verifyOTP);
        sendOTP.setOnClickListener(this);
        verifyOTP.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v==sendOTP)
        {
            getOTP();
        }
        if(v==verifyOTP)
        {
            verificationOTP();
        }
    }

    private void verificationOTP() {
        String codeEntered=editTextOTP.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeEntered);
        if(codeEntered.isEmpty())
        {
            editTextOTP.setError("Please Enter OTP");
            editTextOTP.requestFocus();
            return;
        }
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Incorrect OTP entered",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void getOTP() {
        String number = editTextMobile.getText().toString();
        if (number.isEmpty()) {
            editTextMobile.setError("Phone number required");
            editTextMobile.requestFocus();
            return;
        }
        if (number.length() != 10) {
            editTextMobile.setError("Phone number invalid");
            editTextMobile.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent=s;
            }
        };
}
