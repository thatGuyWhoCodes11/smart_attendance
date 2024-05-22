package com.example.projectwork;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;



public class History extends AppCompatActivity {

    String courseVal;
    String uid;
    EditText datePicker;
    LinearLayout studentList;
    private static final String TAG = "HistoryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        uid = getIntent().getExtras().getString("uid");
        courseVal = getIntent().getExtras().getString("course");

        datePicker = findViewById(R.id.date_picker);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }


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
                        datePicker.setText(formattedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void fetchAttendanceForDate(String dateKey) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/" + uid + "/subjects/" + courseVal + "/attendance_taken/" + dateKey);
        Log.d(TAG, "Fetching data from path: users/" + uid + "/subjects/" + courseVal + "/attendance_taken/" + dateKey);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Data snapshot received: " + dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    displayAttendance(dataSnapshot);
                } else {
                    Log.d(TAG, "No data found for this date");
                    Toast.makeText(History.this, "No attendance data found for this date", Toast.LENGTH_SHORT).show();
                    studentList.removeAllViews(); // Clear previous data if no data found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(History.this, "Failed to fetch data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void displayAttendance(DataSnapshot dataSnapshot) {
        studentList.removeAllViews(); // Clear previous data
        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
            String studentName = studentSnapshot.getValue(String.class);
            TextView studentView = new TextView(this);
            studentView.setText(studentName);
            studentList.addView(studentView);
        }
    }
}

