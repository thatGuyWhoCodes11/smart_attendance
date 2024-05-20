package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        emailEditText = findViewById(R.id.loginEmail); // Updated to reference the email input
        passwordEditText = findViewById(R.id.password_logIn);
        mAuth = FirebaseAuth.getInstance();
    }

    public void handleSignIn(View view) {
        Intent intent = new Intent(this, CourseSelection.class);
        String email = emailEditText.getText().toString(); // Updated to use the email variable
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                        FirebaseUser user=mAuth.getCurrentUser();
                        assert user != null;
                        intent.putExtra("uid",user.getUid());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Wrong email or password.", Toast.LENGTH_SHORT).show();
                        Log.d("handleSignInException", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }

    public void handleRequestRegister(View view) {
        Intent nav = new Intent(this, Register.class);
        startActivity(nav);
    }
    public void handleForgotPassword(View view){
        Intent nav = new Intent(this,PasswordRecovery.class);
        startActivity(nav);
    }
}
