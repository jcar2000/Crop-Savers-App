package com.example.cropsavers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResultsPage extends AppCompatActivity {
    private Button homeButton;
    private Button infoButton;

    // Arrays for predictions and their corresponding labels
    private float[] predictionsArray;
    private String[] predictionLabels = new String[10];

    // ON PAGE CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);

        // convert passed image from last screen back to bitmap for displaying
        if(getIntent().hasExtra("predictionImage")) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("predictionImage"), 0, getIntent().getByteArrayExtra("predictionImage").length);
            ImageView imageView = findViewById(R.id.resultsImage);
            imageView.setImageBitmap(bitmap);
        }

        // Display Prediction Probabilities
        displayPredictions();

        //Buttons //////////////////////////////////////////////////////////////////
        // button logic for home button
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        // button logic for info button
        infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoPage();
            }
        });
        ///////////////////////////////////////////////////////////////////////////
    }

    public void openHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openInfoPage() {
        Intent intent = new Intent(this, InfoPage.class);
        startActivity(intent);
    }

    // function to get prediction labels and display ordered probabilities on the results page
    private void displayPredictions() {
        // get species type from last screen and set label file name with it
        Intent previousPage = getIntent();
        String species = previousPage.getStringExtra("species");
        String labelFileName = species + "_Labels.txt";

        // get labels from text file
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(labelFileName)));
            for (int i = 0; i < 10; i++) {
                String label = reader.readLine();
                predictionLabels[i] = label;
            }
        }
        catch (IOException e) {
            Log.d("myapp", "Label file not found");
        }

        // sort the label/probabilities arrays and set them as text on the UI
        if(getIntent().hasExtra("predictions")) {
            predictionsArray = sortPredictions(getIntent().getFloatArrayExtra("predictions"));
        }

        String fullPredictionString = "";
        for (int i = 0; i<3; i++) {
            String data = String.format("%s: %1.2f", predictionLabels[i], predictionsArray[i]) + "%";
            if (i == 2) {
                fullPredictionString = fullPredictionString + data;
            } else {
                fullPredictionString = fullPredictionString + data + "\n";
            }
        }
        TextView predictionText = findViewById(R.id.Prediction1);
        predictionText.setText(fullPredictionString);
    }


    // function to sort both the predictions array and labels array in desc order of probability
    private float[] sortPredictions(float[] predictArray) {
        for (int i = 0; i < predictArray.length; i++) {
            for (int j = i + 1; j < predictArray.length; j++) {
                float tmp = 0;
                String ltmp = null;
                if (predictArray[i] < predictArray[j]) {
                    tmp = predictArray[i];
                    ltmp = predictionLabels[i];

                    predictArray[i] = predictArray[j];
                    predictionLabels[i] = predictionLabels[j];

                    predictArray[j] = tmp;
                    predictionLabels[j] = ltmp;
                }
            }
        }

        for (int i = 0; i < predictArray.length; i++) {
            predictArray[i] = predictArray[i] * 100.0f;
        }
        return predictArray;
    }
}