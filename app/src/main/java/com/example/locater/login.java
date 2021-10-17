package com.example.locater;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class login extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phone;
    Button signinbtn;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.ccp);
        phone = findViewById(R.id.editTextPhone);
        signinbtn = findViewById(R.id.login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        if (user != null){
            startActivity(new Intent(getApplicationContext(), mainpage.class));
        }


            signinbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String _phoneNumber = phone.getText().toString().trim();

                    String _getPhoneNumber = "+"+countryCodePicker.getFullNumber()+_phoneNumber;


                    if(TextUtils.isEmpty(_getPhoneNumber)){
                        Toast.makeText(getApplicationContext(), "Please Enter Your Number" , Toast.LENGTH_SHORT);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), verifyphonenumber.class);
                        intent.putExtra("phone number", _getPhoneNumber);
                        startActivity(intent);
                   }


                }
            });
        }

    }