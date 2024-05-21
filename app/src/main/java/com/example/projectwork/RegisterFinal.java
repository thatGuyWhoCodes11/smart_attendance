package com.example.projectwork;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
public class RegisterFinal extends AppCompatActivity {
    DatabaseReference db;
    FirebaseAuth mAuth;
    EditText email_ET;
    EditText DOB_ET;
    EditText subject_ET;
    Spinner gender_ET;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_final);

        subject_ET = findViewById(R.id.pick_subject);
        email_ET = findViewById(R.id.email);
        DOB_ET = findViewById(R.id.DOB);

        //setup the gender spinner
        gender_ET = findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_ET.setAdapter(adapter);


        //setup date picker inside dob edit text
        DOB_ET = findViewById(R.id.DOB);
        DOB_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }

    // date picker function
    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Format the date in dd/MM/yyyy
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                        // Set the formatted date in the DOB_ET EditText
                        DOB_ET.setText(formattedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }



    public void handelSendRegister(View view){
        Intent nav = new Intent(this,SignInActivity.class);
        Bundle extras=getIntent().getExtras();
        String instructorID;
        String fullName;
        String password;
        String subjectsUnfiltered = subject_ET.getText().toString();
        String email = email_ET.getText().toString();
        String DOB=DOB_ET.getText().toString();
        String gender=gender_ET.getSelectedItem().toString();
        // Regex pattern for email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(subjectsUnfiltered.isEmpty()){
            Toast.makeText(this, "Subjects are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DOB.isEmpty()) {
            Toast.makeText(this, "Date of Birth is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if(extras != null){
            ArrayList<String> subjects = new ArrayList<>(Arrays.asList(subjectsUnfiltered.trim().split(",")));
            instructorID=extras.getString("instructorID");
            fullName=extras.getString("fullName");
            password=extras.getString("password");
            mAuth = FirebaseAuth.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("instructorID",instructorID);
            data.put("fullName",fullName);
            data.put("DOB",DOB);
            data.put("gender",gender);
            Map<String,Object> subjectHash = new HashMap<>();
            for(String subject:subjects)
                subjectHash.put(subject,"");
            data.put("subjects",subjectHash);
            //create a new authentication instance with email and password
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterFinal.this, "success!", Toast.LENGTH_SHORT).show();
                        handleDatabase(data,nav);
                    }else {
                        Toast.makeText(RegisterFinal.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleDatabase(Map<String,Object> data,Intent nav) {
        Bundle extras=getIntent().getExtras();
        db = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        //save the user credentials except email and password
        db.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(RegisterFinal.this, "Account created", Toast.LENGTH_SHORT).show();
                nav.putExtras(extras);
                startActivity(nav);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterFinal.this, "Registration failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }
}