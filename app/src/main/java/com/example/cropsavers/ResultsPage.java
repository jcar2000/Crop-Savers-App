package com.example.cropsavers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class ResultsPage extends AppCompatActivity {

    // Arrays for predictions and their corresponding labels
    private float[] predictionsArray;
    private final String[] predictionLabelsAndDesc = new String[15];

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
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> openHomePage());

        // button logic for info button
        Button infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> openInfoPage());

        // button logic for back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

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
        String labelFileName = species + "_Labels_Descriptions.txt";

        // get labels from text file
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(labelFileName)));
            for (int i = 0; i < 15; i++) {
                String fullLine = reader.readLine();
                predictionLabelsAndDesc[i] = fullLine;
            }
        }
        catch (IOException e) {
            Log.d("myapp", "Label file not found");
        }

        // sort the label/probabilities arrays and set them as text on the UI
        if(getIntent().hasExtra("predictions")) {
            predictionsArray = sortPredictions(getIntent().getFloatArrayExtra("predictions"));
        }

        StringBuilder fullPredictionString = new StringBuilder();
        for (int i = 0; i<3; i++) {
            String[] labelDescriptionArr = predictionLabelsAndDesc[i].split(";",3);
            String data = String.format(Locale.getDefault(), "%s: %1.2f", labelDescriptionArr[0], predictionsArray[i]) + "%";
                if (i == 0) {
                    String topDescription = labelDescriptionArr[1];
                    String[] newLineDescArr = topDescription.split("\\\\n",0);
                    StringBuilder fullDesc = new StringBuilder();
                    for (String s : newLineDescArr) {
                        fullDesc.append(s).append("\n\n");
                    }
                    TextView description = findViewById(R.id.descriptionText);
                    description.setText(fullDesc);

                    TextView nameDescription = findViewById(R.id.descriptionNameText);
                    nameDescription.setText(labelDescriptionArr[0]);

                    String topLink = labelDescriptionArr[2];
                    TextView linkTextView = findViewById(R.id.linkText);
                    linkTextView.setText("Learn more at: " + topLink);

                }
                if (i == 1) {
                    String topDescription = labelDescriptionArr[1];
                    String[] newLineDescArr = topDescription.split("\\\\n",0);
                    StringBuilder fullDesc = new StringBuilder();
                    for (String s : newLineDescArr) {
                        fullDesc.append(s).append("\n\n");
                    }
                    TextView description = findViewById(R.id.descriptionText1);
                    description.setText(fullDesc);

                    TextView nameDescription = findViewById(R.id.descriptionNameText1);
                    nameDescription.setText(labelDescriptionArr[0]);

                    String topLink = labelDescriptionArr[2];
                    TextView linkTextView = findViewById(R.id.linkText2);
                    linkTextView.setText("Learn more at: " + topLink);
                }

                if (i == 2) {
                    String topDescription = labelDescriptionArr[1];
                    String[] newLineDescArr = topDescription.split("\\\\n",0);
                    StringBuilder fullDesc = new StringBuilder();
                    for (String s : newLineDescArr) {
                        fullDesc.append(s).append("\n\n");
                    }
                    TextView description = findViewById(R.id.descriptionText2);
                    description.setText(fullDesc);

                    TextView nameDescription = findViewById(R.id.descriptionNameText2);
                    nameDescription.setText(labelDescriptionArr[0]);

                    String topLink = labelDescriptionArr[2];
                    TextView linkTextView = findViewById(R.id.linkText3);
                    linkTextView.setText("Learn more at: " + topLink);
                }
                fullPredictionString.append(data).append("\n");
        }
        TextView predictionText = findViewById(R.id.Prediction1);
        predictionText.setText(fullPredictionString.toString());
    }


    // function to sort both the predictions array and labels array in desc order of probability
    private float[] sortPredictions(float[] predictArray) {
        for (int i = 0; i < predictArray.length; i++) {
            for (int j = i + 1; j < predictArray.length; j++) {
                float tmp;
                String ltmp;
                if (predictArray[i] < predictArray[j]) {
                    tmp = predictArray[i];
                    ltmp = predictionLabelsAndDesc[i];

                    predictArray[i] = predictArray[j];
                    predictionLabelsAndDesc[i] = predictionLabelsAndDesc[j];

                    predictArray[j] = tmp;
                    predictionLabelsAndDesc[j] = ltmp;
                }
            }
        }

        for (int i = 0; i < predictArray.length; i++) {
            predictArray[i] = predictArray[i] * 100.0f;
        }
        return predictArray;
    }
}