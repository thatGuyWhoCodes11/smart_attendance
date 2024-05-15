package com.example.projectwork;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomePage extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage); // Ensure this matches your layout file name

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
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
