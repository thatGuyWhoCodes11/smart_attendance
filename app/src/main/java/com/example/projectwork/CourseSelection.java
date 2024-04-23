package com.example.projectwork;

import android.os.Bundle;
import android.widget.Spinner;

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
    public void handleContinue(){
        Bundle extra = new Bundle();
        String courseVal=Courses.getSelectedItem().toString();
        extra.putString("course",courseVal);
    }
}