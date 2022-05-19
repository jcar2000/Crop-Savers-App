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
    private static final String TAG = "myapp";
    private Button homeButton;
    private Button infoButton;

    private float[] predictionsArray;
    private String[] predictionLabels = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);

        // convert image back to bitmap for displaying
        if(getIntent().hasExtra("predictionImage")) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("predictionImage"), 0, getIntent().getByteArrayExtra("predictionImage").length);
            ImageView imageView = findViewById(R.id.resultsImage);
            imageView.setImageBitmap(bitmap);
        }

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

            if(getIntent().hasExtra("predictions")) {
                predictionsArray = sortPredictions(getIntent().getFloatArrayExtra("predictions"));
            }

            String fullPredictionString = "";
            for (int i = 0; i<3; i++) {
                String data = String.format("%s: %1.4f", predictionLabels[i], predictionsArray[i]) + "%";
                if (i == 2) {
                    fullPredictionString = fullPredictionString + data;
                } else {
                    fullPredictionString = fullPredictionString + data + "\n";
                }
            }
            TextView predictionText = findViewById(R.id.Prediction1);
            predictionText.setText(fullPredictionString);

        }
        catch (IOException e) {
            Log.d("myapp", Log.getStackTraceString(e));
        }



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
    }

    public void openHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openInfoPage() {
        Intent intent = new Intent(this, InfoPage.class);
        startActivity(intent);
    }

    private float[] sortPredictions(float[] predictArray) {
        for (int i = 0; i < predictArray.length; i++) {
            for (int j = i + 1; j < predictArray.length; j++) {
                float tmp = 0;
                String ltmp;
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