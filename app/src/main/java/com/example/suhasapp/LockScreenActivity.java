package com.example.suhasapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LockScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        TextView warningText = findViewById(R.id.warning_text);
        Button homeButton = findViewById(R.id.home_button);

        warningText.setText("This app is banned and cannot be used.");

        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(homeIntent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent back button from dismissing the lock screen
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(homeIntent);
    }
}