package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
    }
    public void handleRegister(View view) {
        //todo handle signin requests with firebase
    }
    public void handleRequestRegister(View view){
        Intent nav =new Intent(this,Register.class);
        startActivity(nav);
    }
}
