package com.zybooks.mobile2app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class AddItemActivity extends AppCompatActivity {

    // Variables for the class
    private static final String TAG = "PrintLog";
    public MaterialToolbar top_menu_toolbar;
    public FloatingActionButton fab_button;
    public ConstraintLayout mAddItemToDatabase;
    public EditText skuEditText, nameEditText, quantityEditText;
    public Button addItemButton;
    Intent intentToLoginActivity, intentToNotificationActivity, intentToDatabaseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addItemToDatabase), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Prompt log debugging message
        Log.d(TAG, "activity_add_item Started");

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
        Log.d(TAG, "In Add Item Activity: Top Menu bar created and initialized successfully");


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
            mAddItemToDatabase = findViewById(R.id.addItemToDatabase);

            // TODO: Go to Login Activity from AddItemActivity
            if (itemId == R.id.nav_login) {
                intentToLoginActivity = new Intent(AddItemActivity.this, LoginActivity.class);

                // Put an extra data to the intent call to log the user out
                intentToLoginActivity.putExtra("loggedIn", false);

                // Start new activity, pass the extra variable, and end current activity
                startActivity(intentToLoginActivity);
                finish();
                Log.d(TAG, "In Add Item Activity: logged out button clicked");
                return true;

            // TODO: Go to Database Activity from AddItemActivity
            } else if (itemId == R.id.nav_database) {
                intentToDatabaseActivity = new Intent(AddItemActivity.this, DatabaseActivity.class);
                // Start new activity, pass the extra variable, and end current activity
                Log.d(TAG, "In Add Item Activity: Database button clicked");

                // Start new activity and end current activity
                startActivity(intentToDatabaseActivity);
                finish();
                return true;

            // FIXME: Activate for notification fragment as added
            // Go to Notification Activity from AddItemActivity
            } else if (itemId == R.id.nav_notification) {
                intentToNotificationActivity = new Intent(AddItemActivity.this, NotificationActivity.class);

                Log.d(TAG, "In Add Item Activity: Notification button clicked");

                // Start new activity and end current activity
                startActivity(intentToNotificationActivity);
                finish();
                return true;
            }
            return false;
        });
        Log.d(TAG, "In Add Item Activity: Bottom navigation bar initialized successfully");


        /* ****************************************************************************************/
        /* TODO: Handler for the floating action button in the bottom navigation bar
         * *****************************************************************************************/
        // Find the floating action button id from the activity_add_item.xml layout
        fab_button = findViewById(R.id.fab);
        // Set up the floating action button with the click listener
        fab_button.setOnClickListener(view -> {

            Log.d(TAG, "In Add Item Activity: FAB button clicked");
            // Todo: Action when clicked: Create an Intent to start the AddItemActivity view
            startActivity(new Intent(this, AddItemActivity.class));
        });
        Log.d(TAG, "In Add Item Activity: FAB button created");


        /* ****************************************************************************************/
        /* TODO: Handler for reading the EditTexts in the activity_add_item.xml layout
         *  This method will be called when the user clicks the "Add Item" button and after all
         *  the EditTexts have been entered.
         * ****************************************************************************************/
        // Todo: Get references to all three EditTexts and the Add Item Button
        skuEditText = findViewById(R.id.addUsername);
        nameEditText = findViewById(R.id.addItemName);
        quantityEditText = findViewById(R.id.addItemQuantity);
        addItemButton = findViewById(R.id.addItemButton);

        // TODO: TextWatcher to check fields
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if all fields are filled
                String sku = skuEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String quantity = quantityEditText.getText().toString().trim();

                // Enable the Add Item button if all fields are filled
                addItemButton.setEnabled(!sku.isEmpty() && !name.isEmpty() && !quantity.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        // Link the watcher to all three fields so all three fields are checked
        skuEditText.addTextChangedListener(inputWatcher);
        nameEditText.addTextChangedListener(inputWatcher);
        quantityEditText.addTextChangedListener(inputWatcher);


        /* ****************************************************************************************/
        /* TODO: Add click listener to retrieve values from the EditTexts in the activity_add_item.xml
             and add them to the database
         * ****************************************************************************************/
        addItemButton = findViewById(R.id.addItemButton);
        // Set the click listener for the Add Item button
        addItemButton.setOnClickListener(v -> {
            String sku = skuEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String quantityStr = quantityEditText.getText().toString().trim();

            // Validate the quantity input is valid and convert it to an integer.
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(AddItemActivity.this, "Quantity must be a number",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Invalid quantity input in AddItemActivity @+id/addItemQuantity: "
                        + quantityStr);
                return;
            }

            // FIXME: Forward item to DatabaseActivity through an intent with extra data
            //  to add the item to database after it is implemented
            intentToDatabaseActivity = new Intent(AddItemActivity.this, DatabaseActivity.class);
            intentToDatabaseActivity.putExtra("itemSku", sku);
            intentToDatabaseActivity.putExtra("itemName", name);
            intentToDatabaseActivity.putExtra("itemQuantity", quantity);

            Log.d(TAG, "Item forwarded from Add Item Activity form to Database Activity: " +
                    " - SKU: " + sku + ", Name: " + name + ", Quantity: " + quantity);

            // Start new activity, pass the extra variable, and end current activity
            startActivity(intentToDatabaseActivity);
            finish();
        });
    }
    // -----------------------      onCreate ends here      -----------------------


    /* *********************************************************************************************
     *  TODO: Handle the app_bar_menu navigation bar from the top menu.
     *   Implements functionality for the Back button only
     * ********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // TODO: Handle the Back button from the top menu action bar
        if (id == android.R.id.home) {
            Log.d(TAG, "In Add Item Activity: Back button clicked");
            finish();
            return true;

            // TODO: Handle the Settings button from the top menu action bar
        }
        return super.onOptionsItemSelected(item);
    }
}