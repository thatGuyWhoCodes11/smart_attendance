package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    EditText instructorID;
    EditText fullName;
    EditText password;
    EditText repeatPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        instructorID = findViewById(R.id.instructorID_register);
        fullName = findViewById(R.id.fullName);
        password = findViewById(R.id.password_register);
        repeatPassword = findViewById(R.id.repeatPassword);
    }
    public void handleNextRegister(View view){
        Intent nav = new Intent(this,RegisterFinal.class);
        Bundle extras = new Bundle();
        if(!password.getText().toString().equals(repeatPassword.toString())){
            Toast.makeText(this, "make sure passwords match", Toast.LENGTH_SHORT).show();
            return;
        }
        extras.putString("instructorID",instructorID.getText().toString());
        extras.putString("fullName",fullName.getText().toString());
        extras.putString("password",password.getText().toString());
        nav.putExtras(extras);
        startActivity(nav);
    }
}
