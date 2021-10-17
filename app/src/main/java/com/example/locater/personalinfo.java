package com.example.locater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class personalinfo extends AppCompatActivity {

    EditText name, emailId, phoneNumber;
    Button savebtn, backButton;
    String _getNumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        _getNumber = getIntent().getStringExtra("phone number");

        phoneNumber = findViewById(R.id.phone);
        emailId = findViewById(R.id.email);
        name = findViewById(R.id.name);
        savebtn = findViewById(R.id.saveinfo);
        backButton = findViewById(R.id.backbutton);

        phoneNumber.setText(_getNumber);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = firebaseUser.getUid();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(user_id);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mainpage.class));
                finish();
            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = phoneNumber.getText().toString().trim();
                String emailAcc = emailId.getText().toString().trim();
                String usersname = name.getText().toString().trim();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(TextUtils.isEmpty(number)){
                            phoneNumber.setError("Please Enter Phone Number!");
                        }
                        else if(TextUtils.isEmpty(emailAcc)){
                            emailId.setError("Please enter email!");
                        }
                        else if(TextUtils.isEmpty(usersname)){
                            name.setError("Please Enter User Name!");
                        }
                        else {
                            UserInfo userInfo = new UserInfo();

                            userInfo.setName(usersname);
                            userInfo.setEmailID(emailAcc);
                            userInfo.setPhoneNumber(number);
                            userInfo.setUserid(user_id);

                            databaseReference.setValue(userInfo);

                            startActivity(new Intent(getApplicationContext(), mainpage.class));
                        }
                        DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("users").child("phone");

                        databaseReference1.setValue(number);


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Failed to add data " + error, Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}

