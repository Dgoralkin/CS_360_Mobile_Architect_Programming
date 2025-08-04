package com.zybooks.mobile2app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    // Variables
    private static final String TAG = "PrintLog";
    public MaterialToolbar top_menu_toolbar;
    public ActionBar actionBarBackButton;
    public FloatingActionButton fab_button;
    public RecyclerView recyclerView;
    public List<TableRowData> tableData;
    ConstraintLayout mActivityNotification;
    Intent intentToDatabaseActivity;
    // Set a constant for the SMS permission request code tag
    private static final int SMS_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        Log.d(TAG, "activity_notification Started");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityNotification), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /* TODO: The checkSmsPermission() method checks for the SMS permission and query the
            user for permission if permission has not been granted.
        * *****************************************************************************************/
        // Check for SMS permission automatically as activity starts
        checkSmsPermission();

        // Check for SMS permission manually upon button click and user request
        // Find the button from the activity_notification.xml layout
        Button btnCheckSmsStatus = findViewById(R.id.btnCheckSmsStatus);
        btnCheckSmsStatus.setOnClickListener(v -> checkSmsPermission());


        /*  TODO: Create the top menu Notification Activity and enable the Back button
         * *****************************************************************************************/
        // Find the top menu ActionBar id from the activity_database.xml layout
        top_menu_toolbar = findViewById(R.id.topAppBar);
        // Set up the top menu ActionBar with the MaterialToolbar
        setSupportActionBar(top_menu_toolbar);

        // Enable the Back button in the ActionBar
        ActionBar actionBarBackButton = getSupportActionBar();
        if (actionBarBackButton != null) {
            actionBarBackButton.setDisplayShowHomeEnabled(true);
            // Hide the Back button in the ActionBar
            actionBarBackButton.setDisplayHomeAsUpEnabled(false);
        }
        Log.d(TAG, "In Notification Activity: Top Menu bar created successfully");


        /* ****************************************************************************************/
        /* TODO: Handler for the bottom navigation bar.
            Setup the bottom navigation view with the navigation controller
        * *****************************************************************************************/
        //  Find the NavHostFragment from the activity_database.xml layout
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        // Update the bottom navigation view with the navigation controller
        bottomNav.setSelectedItemId(R.id.nav_notification);

        // Handle navigation item clicks from the navigation bar
        bottomNav.setOnItemSelectedListener(item -> {
            // Get the id of the item that triggered the event
            int itemId = item.getItemId();
            mActivityNotification = findViewById(R.id.ActivityNotification);

            // TODO: Go to Login Activity from Notification Activity
            if (itemId == R.id.nav_login) {
                Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);

                // Put an extra data to the intent call to log the user out
                intent.putExtra("loggedIn", false);

                // Start new activity, pass the extra variable, and end current activity
                startActivity(intent);
                finish();
                Log.d(TAG, "In Notification Activity: Logged out button clicked");
                return true;

                // TODO: Go to Database Activity from Notification Activity
            } else if (itemId == R.id.nav_database) {
                intentToDatabaseActivity = new Intent(NotificationActivity.this, DatabaseActivity.class);

                // Log the prompt for debugging
                Log.d(TAG, "In Notification Activity: Database button clicked");

                // Start new activity and end current activity
                startActivity(intentToDatabaseActivity);
                finish();
                return true;

                // Go to Notification Activity from Notification Activity
            }  else if (itemId == R.id.nav_notification) {
                Toast.makeText(this, "On Notification screen", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "In Notification Activity: Notification button clicked");
            }
            return false;
        });
        Log.d(TAG, "In Notification Activity: Bottom navigation bar initialized successfully");
    }
    // -----------------------      onCreate ends here      -----------------------


    /* *********************************************************************************************
     *  TODO: Populate the menu in the ActionBar
     * ********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // tell Android to use your res/menu/app_bar_menu.xml to populate the toolbar menu.
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        Log.d(TAG, "In Notification Activity: Top app_bar_menu inflated");
        return true;
    }
    /* *********************************************************************************************
     *  TODO: Handle the app_bar_menu navigation bar from the top menu. Implements functionality for:
     *   1. Back button, 2. Settings button, 3. About button
     * ********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mActivityNotification = findViewById(R.id.ActivityNotification);

        // TODO: Handle the Back button from the top menu action bar
        if (id == android.R.id.home) {
            Log.d(TAG, "In Notification Activity: Back button clicked");
            finish();
            return true;

            // TODO: Handle the Settings button from the top menu action bar to display a toast
        } else if (id == R.id.menu_bar_settings_button) {
            Snackbar.make(mActivityNotification, "* Settings - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Notification Activity: Settings button clicked");
            return true;

            // TODO: Handle the About button from the top menu action bar to display a toast
        } else if (id == R.id.menu_bar_about_button) {
            Snackbar.make(mActivityNotification, "* About - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Notification Activity: About button clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* ********************************************************************************************/
    /* TODO: This method checks for the SMS permission. It will query the user for permission if
        permissions are not granted. Upon results, it will update the UI accordingly.
     * ********************************************************************************************/
    private void checkSmsPermission() {
        // Find the SMS permission status text view from the activity_notification.xml layout
        TextView smsStatusText = findViewById(R.id.smsStatusText);
        Log.d(TAG, "In Notification Activity: Checking for SMS permission...");

        // Check for SMS permission. If not granted, request it.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // If permission NOT granted - update the UI to request permission
            smsStatusText.setText("SMS permission not granted. Requesting...");

            // Request the permission from user by showing the system permission dialog and
            // automatically calling the onRequestPermissionsResult() method to update the UI
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE);

        } else {
            // If permission exists - update the UI to show permission granted
            Log.d(TAG, "In Notification Activity: SMS permission already exists");

            // Permission already granted
            smsStatusText.setText(R.string.sms_permission_already_granted);
        }
    }
    /* *********************************************************************************************
     *  TODO: Automatic call by the Android system as response to ActivityCompat.requestPermissions()
     *   and user's response to output the permission results to the UI.
     * ********************************************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check for the SMS permission request code to find the correct permission result
        if (requestCode == SMS_PERMISSION_CODE) {
            TextView smsStatusText = findViewById(R.id.smsStatusText);
            // If permission granted -> update the UI accordingly
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                smsStatusText.setText(R.string.sms_permission_granted);
                Log.d(TAG, "In Notification Activity: SMS permission granted");
            } else {
                // If permission denied -> update the UI accordingly
                smsStatusText.setText(R.string.sms_permission_denied);
                Log.d(TAG, "In Notification Activity: SMS permission denied");
            }
        }
    }
}