package com.zybooks.mobile2app;

import static java.lang.Math.random;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseActivity extends AppCompatActivity {

    // Variables
    private static final String TAG = "PrintLog";
    public MaterialToolbar top_menu_toolbar;
    public FloatingActionButton fab_button;
    public RecyclerView recyclerView;
    public List<TableRowData> tableData;
    ConstraintLayout mActivityDatabase;
    Intent intentToLoginActivity, intentFromAddItemActivity, intentToNotificationActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_database);
        Log.d(TAG, "activity_database Started");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_database), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /*  TODO: Create the top menu ActionBar and enable the Back button
        * *****************************************************************************************/
        // Find the top menu ActionBar id from the activity_database.xml layout
        top_menu_toolbar = findViewById(R.id.topAppBar);
        // Set up the top menu ActionBar with the MaterialToolbar
        setSupportActionBar(top_menu_toolbar);
        // Enable the Back button in the ActionBar
        ActionBar actionBarBackButton = getSupportActionBar();
        if (actionBarBackButton != null) {
            actionBarBackButton.setDisplayShowHomeEnabled(true);
            actionBarBackButton.setDisplayHomeAsUpEnabled(true);
        }
        Log.d(TAG, "In Database Activity: Top Menu bar created successfully");

        /* ****************************************************************************************/
        /* TODO: Handler for the bottom navigation bar.
            Setup the bottom navigation view with the navigation controller
        * *****************************************************************************************/

        //  Find the NavHostFragment from the activity_database.xml layout
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        // Update the bottom navigation view with the navigation controller
        bottomNav.setSelectedItemId(R.id.nav_database);

        // Handle navigation item clicks from the navigation bar
        bottomNav.setOnItemSelectedListener(item -> {
            // Get the id of the item that triggered the event
            int itemId = item.getItemId();
            mActivityDatabase = findViewById(R.id.activity_database);

            // TODO: Go to Login Activity from DatabaseActivity
            if (itemId == R.id.nav_login) {
                intentToLoginActivity = new Intent(DatabaseActivity.this, LoginActivity.class);

                // Put an extra data to the intent call to log the user out
                intentToLoginActivity.putExtra("loggedIn", false);

                Log.d(TAG, "In Database Activity: Logged out button clicked");

                // Start new activity, pass the extra variable, and end current activity
                startActivity(intentToLoginActivity);
                finish();
                return true;

            // TODO: Go to Database Activity from DatabaseActivity
            } else if (itemId == R.id.nav_database) {
                Toast.makeText(this, "On Database screen", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "In Database Activity: Database button clicked");
                return true;

            // FIXME: Activate for notification fragment as added
            // Go to Notification Activity from DatabaseActivity
            }  else if (itemId == R.id.nav_notification) {
                intentToNotificationActivity = new Intent(DatabaseActivity.this, NotificationActivity.class);

                Log.d(TAG, "In Database Activity: Notification button clicked");

                // Start new activity and end current activity
                startActivity(intentToNotificationActivity);
                finish();
                return true;
            }
            return false;
        });
        Log.d(TAG, "In Database Activity: Bottom navigation bar initialized successfully");

        /* ****************************************************************************************/
        /* TODO: Handler for the floating action button in the bottom navigation bar
        * *****************************************************************************************/
        // Find the floating action button id from the activity_database.xml layout
        fab_button = findViewById(R.id.fab);
        // Set up the floating action button with the click listener
        fab_button.setOnClickListener(view -> {

            Log.d(TAG, "In Database Activity: FAB button clicked");
            // Todo: Action when clicked: Create an Intent to start the AddItemActivity view
            startActivity(new Intent(this, AddItemActivity.class));
        });
        Log.d(TAG, "In Database Activity: FAB button created");

        /* ****************************************************************************************/
        /*  TODO: Handler for the RecyclerView to inflate the database content
         * ****************************************************************************************/
        // Find the RecyclerView by its ID in the activity_database.xml layout
        recyclerView = findViewById(R.id.tableRecyclerView);

        // Set a layout through the RecyclerView linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a new ArrayList to store the table data row by row
        tableData = new ArrayList<>();
        // Create Random object (To be removed as database connected)
        Random random = new Random();

        // Add data to the tableData list (To be removed as database connected)
        for (int i = 0; i < 5; i++) {
            int qty = random.nextInt(100);
            String sku = "A1B2" + qty;
            String itemName = "Item " + (i + 1);
            tableData.add(new TableRowData(sku, itemName, qty));
        }

        /* *****************************************************************************************
         *  TODO: Check if additional data was retrieved from AddItemActivity through an intent pass.
         *   If so, add it to the tableData list.
         * ****************************************************************************************/
        // Catch the Intent and extract the extras (SKU, Name, Quantity) if intent came from
        // AddItemActivity and carries the extras
        intentFromAddItemActivity = getIntent();
        if (intentFromAddItemActivity != null && intentFromAddItemActivity.hasExtra("itemSku")) {
            String itemImage = intentFromAddItemActivity.getStringExtra("itemImage");
            String itemSku = intentFromAddItemActivity.getStringExtra("itemSku");
            String itemName = intentFromAddItemActivity.getStringExtra("itemName");
            int itemQuantity = intentFromAddItemActivity.getIntExtra("itemQuantity", 0);

            // Log the data and check it was retrieved correctly
            Log.d(TAG, "Intent Received from AddItemActivity - Image: "
                    + itemImage + ", SKU: " + itemSku + ", Name: "
                    + itemName + ", Quantity: " + itemQuantity);

            // Add the retrieved data to the table data list in the database before it sent to RecyclerView
            if (itemSku != null && itemName != null) {
                tableData.add(new TableRowData(itemImage, itemSku, itemName, itemQuantity));
            }
        }

        /* ****************************************************************************************/
        /*  TODO: Set the adapter for the RecyclerView with delete click listener
         * ****************************************************************************************/
        TableAdapter adapter = new TableAdapter(tableData, (position, rowData) -> {
            tableData.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            recyclerView.getAdapter().notifyItemRangeChanged(position, tableData.size());
        });

        /* ****************************************************************************************/
        /*  TODO: Initialize the RecyclerView
         * ****************************************************************************************/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "In Database Activity: Recycler view created and populated");
    }
    // -----------------------      onCreate ends here      -----------------------


    /* *********************************************************************************************
     *  TODO: Populate the menu in the ActionBar
     * ********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // tell Android to use your res/menu/app_bar_menu.xml to populate the toolbar menu.
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        Log.d(TAG, "In Database Activity: Top app_bar_menu inflated");
        return true;
    }
    /* *********************************************************************************************
     *  TODO: Handle the app_bar_menu navigation bar from the top menu. Implements functionality for:
     *   1. Back button, 2. Settings button, 3. About button
     * ********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mActivityDatabase = findViewById(R.id.activity_database);

        // TODO: Handle the Back button from the top menu action bar
        if (id == android.R.id.home) {
            Log.d(TAG, "In Database Activity: Back button clicked");
            finish();
            return true;

        // TODO: Handle the Settings button from the top menu action bar
        } else if (id == R.id.menu_bar_settings_button) {
            Snackbar.make(mActivityDatabase, "* Settings - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Database Activity: Settings button clicked");
            return true;

        // TODO: Handle the About button from the top menu action bar
        } else if (id == R.id.menu_bar_about_button) {
            Snackbar.make(mActivityDatabase, "* About - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Database Activity: About button clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}