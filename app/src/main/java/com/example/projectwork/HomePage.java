package com.example.projectwork;

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

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    private NavigationView navi;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage); // Ensure this matches your layout file name

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navi = findViewById(R.id.navigation_view);
        navi.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("TAG", "onNavigationItemSelected: testttttttttttttttttttttttttttttt");
        return false;
    }
}
