package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navi;
    private String uid;
    private String instructorFullName;
    private TextView instructorName_TV;
    private TextView instructorName_nav;
    private String courseVal;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        if(getIntent().getExtras() == null || getIntent().getExtras().getString("uid") == null){
            Toast.makeText(this, "please login again", Toast.LENGTH_SHORT).show();
            finish();
        }
        uid = getIntent().getExtras().getString("uid");
        courseVal = getIntent().getExtras().getString("course");
        prepareHomeScreen();
        instructorName_TV =findViewById(R.id.instructor_name);
        drawerLayout = findViewById(R.id.drawer_layout);
        navi = findViewById(R.id.navigation_view);
        View header=navi.getHeaderView(0);
        instructorName_nav = header.findViewById(R.id.nav_header_text);
        Log.d("TAG", instructorName_nav.getText().toString());
        navi.setNavigationItemSelectedListener(this::onMenuItemSelected);
    }

    private void prepareHomeScreen() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/"+uid);
        db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("TAG", task.getResult().toString());
                    for (DataSnapshot ds : task.getResult().getChildren())
                        if (ds.getKey().equalsIgnoreCase("fullname"))
                            instructorFullName = ds.getValue(String.class);
                    instructorName_TV.setText(instructorName_TV.getText().toString().replace("instructor", instructorFullName));
                    instructorName_nav.setText(instructorName_nav.getText().toString().replace("Instructor", instructorFullName));
                }
                else
                    Toast.makeText(HomePage.this, "fail to save to firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean onMenuItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.nav_register)
            startActivity(new Intent(this,RegisterStudents.class).putExtra("uid",uid).putExtra("course",courseVal));
        else if(item.getItemId()==R.id.nav_attendance)
            startActivity(new Intent(this,TakeAttendance.class).putExtra("uid",uid).putExtra("course",courseVal));
        else if(item.getItemId()==R.id.nav_history)
            Toast.makeText(this, "TODO CREATE HISTORY", Toast.LENGTH_SHORT).show();
        else if(item.getItemId()==R.id.nav_edit_account)
            Toast.makeText(this, "TODO CREATE EDIT ACCOUNT", Toast.LENGTH_SHORT).show();
        return true;
    }

    // Method to handle the click event
    public void openDrawerMenu(View view) {
        drawerLayout.openDrawer(GravityCompat.END); // Use END to match your layout_gravity
    }

    // Handle back press to close the drawer if it's open
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) { // Use END to match your layout_gravity
            drawerLayout.closeDrawer(GravityCompat.END); // Use END to match your layout_gravity
        } else {
            super.onBackPressed();
        }
    }
}
