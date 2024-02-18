package com.example.projectwork;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFinal extends AppCompatActivity {
    DatabaseReference db;
    EditText email_ET;
    EditText DOB_ET;
    EditText gender_ET;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_final);
        email_ET = findViewById(R.id.email);
        DOB_ET = findViewById(R.id.DOB);
        gender_ET = findViewById(R.id.gender);
    }
    public void handeSendRegister(View view){
        Bundle extras=getIntent().getExtras();
        String instructorID;
        String fullName;
        String password;
        String email=email_ET.toString();
        String DOB=DOB_ET.getText().toString();
        String gender=gender_ET.getText().toString();
        if(extras != null){
            instructorID=extras.getString("instructorID");
            fullName=extras.getString("fullName");
            password=extras.getString("password");
            db = FirebaseDatabase.getInstance().getReference("users");
            Map<String, Object> data = new HashMap<>();
            data.put("instructorID",instructorID);
            data.put("fullName",fullName);
            data.put("password",password);
            data.put("email",email);
            data.put("DOB",DOB);
            data.put("gender",gender);
            db.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(RegisterFinal.this, "saved!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterFinal.this, "not saved", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
            });
        }
    }
}