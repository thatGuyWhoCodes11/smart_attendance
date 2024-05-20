package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    private NavigationView navi;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras() == null){
            Toast.makeText(this, "please login again", Toast.LENGTH_SHORT).show();
            finish();
        }
        uid = getIntent().getExtras().getString("UID");
        setContentView(R.layout.homepage); // Ensure this matches your layout file name
        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navi = findViewById(R.id.navigation_view);
        navi.setNavigationItemSelectedListener(this::onMenuItemSelected);
    }

    private boolean onMenuItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.nav_register)
            startActivity(new Intent(this,RegisterStudents.class).putExtra("uid",uid));
        else if(item.getItemId()==R.id.nav_attendance)
            startActivity(new Intent(this,TakeAttendance.class).putExtra("uid",uid));
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
