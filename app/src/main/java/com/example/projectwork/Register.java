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
        Intent nav = new Intent(this, RegisterFinal.class);
        Bundle extras = new Bundle();

        String instructorIDValue = instructorID.getText().toString().trim();
        String fullNameValue = fullName.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();
        String repeatPasswordValue = repeatPassword.getText().toString().trim();

        if(passwordValue.length()<6){
            Toast.makeText(this, "password must be 6 or more characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (instructorIDValue.isEmpty()) {
            Toast.makeText(this, "Instructor ID is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fullNameValue.isEmpty()) {
            Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (repeatPasswordValue.isEmpty()) {
            Toast.makeText(this, "Repeat password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordValue.equals(repeatPasswordValue)) {
            Toast.makeText(this, "Make sure passwords match", Toast.LENGTH_SHORT).show();
            return;
        }

        extras.putString("instructorID", instructorIDValue);
        extras.putString("fullName", fullNameValue);
        extras.putString("password", passwordValue);
        nav.putExtras(extras);
        startActivity(nav);
    }
}
