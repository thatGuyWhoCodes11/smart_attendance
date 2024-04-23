package com.example.projectwork;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class PasswordRecovery extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText mail_ed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        mail_ed=findViewById(R.id.recover_mail);
        mAuth=FirebaseAuth.getInstance();
    }
    public void handleSendEmail(View view) {
        String mailVal=mail_ed.getText().toString();
        mAuth.sendPasswordResetEmail(mailVal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PasswordRecovery.this,"success",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(PasswordRecovery.this,"failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
