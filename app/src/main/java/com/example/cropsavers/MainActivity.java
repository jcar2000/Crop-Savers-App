package com.example.cropsavers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // ON PAGE CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buttons /////////////////////////////////////////////////////
        // button logic for begin button
        Button beginButton = findViewById(R.id.beginButton);
        beginButton.setOnClickListener(v -> openSelectPage());

        // button logic for begin button
        Button infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> openInfoPage());
        ////////////////////////////////////////////////////////////////
    }

    public void openSelectPage() {
        Intent intent = new Intent(this, SpeciesPage.class);
        startActivity(intent);
    }

    public void openInfoPage() {
        Intent intent = new Intent(this, InfoPage.class);
        startActivity(intent);
    }
}