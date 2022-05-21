package com.example.cropsavers;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InfoPage extends AppCompatActivity {

    // ON PAGE CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);

        // Logic for back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> goBack());
    }

    public void goBack() {
        finish();
    }
}