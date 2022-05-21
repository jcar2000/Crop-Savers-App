package com.example.cropsavers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SpeciesPage extends AppCompatActivity {

    // ON PAGE CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_page);

        // Logic for back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> goBack());
    }

    // Pass selected species type to image page depending on card clicked
    public void cardClick(View view) {
        Intent intent = new Intent(this, ImagePage.class);
        switch (view.getId()) {
            case (R.id.appleCard):
                intent.putExtra("species", "Apple");
                break;
            case (R.id.cherryCard):
                intent.putExtra("species", "Cherry");
                break;
            case (R.id.cornCard):
                intent.putExtra("species", "Corn");
                break;
            case (R.id.grapeCard):
                intent.putExtra("species", "Grape");
                break;
            case (R.id.peachCard):
                intent.putExtra("species", "Peach");
                break;
            case (R.id.pepperCard):
                intent.putExtra("species", "Pepper");
                break;
            case (R.id.potatoCard):
                intent.putExtra("species", "Potato");
                break;
            case (R.id.strawberryCard):
                intent.putExtra("species", "Strawberry");
                break;
            case (R.id.tomatoCard):
                intent.putExtra("species", "Tomato");
                break;
        }
        startActivity(intent);
    }

    public void goBack() {
        finish();
    }
}