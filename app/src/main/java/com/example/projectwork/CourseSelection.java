package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CourseSelection extends AppCompatActivity {
    Spinner Courses;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_selection);
        Courses=findViewById(R.id.coursesSpinner);

    }



    public void handleContinue(View view){
        try {
            String courseVal = Courses.getSelectedItem().toString();
            Bundle extras = new Bundle();
            extras.putString("course", courseVal);

            Intent intent = new Intent(this, HomePage.class);
            intent.putExtras(extras);
            startActivity(intent);
        } catch (NullPointerException e) {          // in case no course is selected
            // This will catch cases where getSelectedItem() returns null.
            Toast.makeText(this, "Please select a course.", Toast.LENGTH_LONG).show();
        }
    }


}