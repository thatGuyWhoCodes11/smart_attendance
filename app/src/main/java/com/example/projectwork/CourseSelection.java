package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CourseSelection extends AppCompatActivity {
    Spinner courses_sp;
    DatabaseReference database;
    String uid;
    ArrayList<String> subjectsArray = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_selection);
        courses_sp=findViewById(R.id.coursesSpinner);
        Intent pastIntent=getIntent();
        uid = pastIntent.getStringExtra("uid");
        prepareSubjectSpinner();
    }

    private void prepareSubjectSpinner() {
        database=FirebaseDatabase.getInstance().getReference("users/"+uid + "/subjects");
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren())
                        subjectsArray.add(ds.getKey());
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_text_view,subjectsArray);
                    courses_sp.setAdapter(arrayAdapter);
                }
                else
                    Toast.makeText(CourseSelection.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleContinue(View view){
        try {
            String courseVal = courses_sp.getSelectedItem().toString();
            Bundle extras = new Bundle();
            extras.putString("course", courseVal);
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtras(extras);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } catch (NullPointerException e) {          // in case no course is selected
            // This will catch cases where getSelectedItem() returns null.
            Toast.makeText(this, "Please select a course.", Toast.LENGTH_LONG).show();
        }
    }
}