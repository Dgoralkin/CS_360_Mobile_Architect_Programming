package com.zybooks.mobile2app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    // Variables for the class
    private static final String TAG = "PrintLog";
    private static String mTempUsername = "";
    private static String mTempPassword = "";
    private static boolean loggedIn = false;
    // Variables for the widgets
    private EditText mUserName, mPassword;
    private Button mLoginButton;
    private ConstraintLayout mActivityLogin;
    private TextView loginTempTextOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityLoginContainer),
                (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate: Activity Login started...");      // Log a message to the console

        // Retrieve the "loggedIn" boolean from the bottom_nav_menu.xml Intent and log user out
        loggedIn = getIntent().getBooleanExtra("loggedIn", false);
        Log.d(TAG, "Login status: " + loggedIn);

        // Identify the EditTexts in the layout file
        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        // Identify the Button in the layout file
        mLoginButton = findViewById(R.id.loginButton);
        // Identify the ConstraintLayout in the layout file
        mActivityLogin = findViewById(R.id.activityLoginContainer);
        // Identify the TextView in the layout file
        loginTempTextOutput = findViewById(R.id.loginTempTextOutput);

        // Add a TextWatcher to the username and password EditTexts
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if both username and password fields are not empty
                String usernameInput = mUserName.getText().toString().trim();
                String passwordInput = mPassword.getText().toString().trim();

                // Enable the login button if both fields are not empty
                mLoginButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        // Attach the same watcher to both username and password fields
        // and trigger the onTextChanged method when the user types in the fields
        // and enable the login button if both fields are not empty
        mUserName.addTextChangedListener(loginTextWatcher);
        mPassword.addTextChangedListener(loginTextWatcher);
    }


    /* *********************************************************************************************
    *  This method is called when the user clicks the "Login" button
    *  It retrieves the user input from the username and password EditTexts
    * and displays it in the temporary TextView
    * *********************************************************************************************/
    public void LoginClick(View view) {
        // If view exists
        if (view != null) {
            Log.d(TAG, "Login button clicked");                 // Log a message to the console

            // get user input
            String mUserNameInput = mUserName.getText().toString();
            String mPasswordInput = mPassword.getText().toString();
            Log.d(TAG, "User Login Input: " + mUserNameInput + " / " + mPasswordInput);

            // TODO: modify to check if the EditTexts mUserName and mPassword exist in the database
            if (mUserNameInput.equals(mTempUsername) && mPasswordInput.equals(mTempPassword)) {

                // Show the loginTempTextOutput TextView and set its text
                loginTempTextOutput.setVisibility(View.VISIBLE);
                String mOutputMessage = getString(R.string.welcome) + mUserNameInput + "!";

                // Display the user input in the TextView
                loginTempTextOutput.setText(mOutputMessage);

                // Set the loggedIn variable to true
                loggedIn = true;
                Log.d(TAG, "User Login Status: " + loggedIn);

                // Start the Database activity view
                startActivity(new Intent(this, DatabaseActivity.class));

            } else {
                // TODO: implement login logic if username or password is incorrect (database connection...)
                Snackbar.make(mActivityLogin, getString(R.string.invalid_username_password),
                        Snackbar.LENGTH_SHORT).show();
                loginTempTextOutput.setVisibility(View.VISIBLE);
                loginTempTextOutput.setText(R.string.login_failed_message);
                return;
            }
        }
    }


    /* *********************************************************************************************
     *  TODO: Implement the RegisterClick method
     * ********************************************************************************************/
    public void RegisterClick(View view) {
        // If view exists
        if (view != null) {
            Log.d(TAG, "Register button clicked");              // Log a message to the console

            // get user input
            String mUserNameInput = mUserName.getText().toString();
            String mPasswordInput = mPassword.getText().toString();
            Log.d(TAG, "User Register Input: " + mUserNameInput + "/" + mPasswordInput);

            if (!mUserNameInput.trim().isEmpty() && !mPasswordInput.trim().isEmpty()) {
                // FIXME: implement user registration logic in the user database.
                // Set the temporary username and password variables
                mTempUsername = mUserNameInput;
                mTempPassword = mPasswordInput;
                // Mark the user as logged in
                loginTempTextOutput.setVisibility(View.VISIBLE);
                loginTempTextOutput.setText(R.string.register_success_message);
                Toast.makeText(this, R.string.register_success_message,
                        Toast.LENGTH_SHORT).show();
            } else {
                // TODO: implement register logic if username or password is incorrect (database connection...)
                loginTempTextOutput.setVisibility(View.VISIBLE);
                loginTempTextOutput.setText(R.string.register_failed_message);
            }
        }
    }
}