package com.example.locater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class verifyphonenumber extends AppCompatActivity {

    private TextView textNumber;
    private Button verifybtn, backbtn;
    private PinView pin;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private String mverificationId;
    String _getNumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private static final String TAG = "MAIN_TAG";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyphonenumber);

        _getNumber = getIntent().getStringExtra("phone number");

        textNumber = findViewById(R.id.phonenumber);
        backbtn = findViewById(R.id.backbutton);
        pin = findViewById(R.id.otp);
        verifybtn = findViewById(R.id.verify);
        progressBar = findViewById(R.id.pBar);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        textNumber.setText(_getNumber);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        //Used when phone number is authenticated
                signInWithPhoneAuthCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onCodeSent(@NonNull String verifcationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verifcationId, token);
                //Code sent to user's number
                mverificationId = verifcationId;
                forceResendingToken = token;

                Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT);

            }
        };

        sendVerificationCode(_getNumber);

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pin.getText().toString();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(getApplicationContext(), "Please Enter the code..." , Toast.LENGTH_SHORT);
                }
                else{
                    verifyPhoneNumberWithCode(mverificationId, code);
                }

            }
        });

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String phone = mAuth.getCurrentUser().getPhoneNumber();

                Intent intent = new Intent(getApplicationContext(), mainpage.class);
                intent.putExtra("phone number", phone);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Log in Failed" , Toast.LENGTH_SHORT);
            }
        });

    }

    private void sendVerificationCode(String getNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(getNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    public void resendcode(View view) {
        progressBar.setVisibility(View.VISIBLE);
        resendVerificationCode(_getNumber, forceResendingToken);
    }

    private void resendVerificationCode(String getNumber, PhoneAuthProvider.ForceResendingToken token) {
        Toast.makeText(getApplicationContext(), "Resending Code" , Toast.LENGTH_SHORT);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(getNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks).setForceResendingToken(token)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        progressBar.setVisibility(View.GONE);
    }
}