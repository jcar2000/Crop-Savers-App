package com.example.cropsavers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagePage extends AppCompatActivity {
    private Button homeButton;
    private Button infoButton;
    private Button backButton;
    private Button predictButton;

    private TextView errorText;

    // Define the pic id
    private static final int pic_id = 123;
    Button camera_open_id;
    ImageView click_image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);


        Intent selectedSpecies = getIntent();
        String species = selectedSpecies.getStringExtra("species");
        ((TextView)findViewById(R.id.header)).setText(species.concat(" Detection"));


        camera_open_id = findViewById(R.id.takePhotoButton);
        click_image_id = findViewById(R.id.Image);

        camera_open_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // Create the camera_intent ACTION_IMAGE_CAPTURE
                // it will open the camera for capture the image
                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);

                // Start the activity with camera_intent,
                // and request pic id
                startActivityForResult(camera_intent, pic_id);
            }
        });


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

        // button logic for back button
        infoButton = findViewById(R.id.backButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        // button logic for photo upload button
        infoButton = findViewById(R.id.uploadPhotoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoLibrary();
            }
        });

        // button logic for predict button
        infoButton = findViewById(R.id.predictButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPredictionsPage();
            }
        });
        ////////////////////////////////////////////////////////////////////////////
    }


    public void openHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openInfoPage() {
        Intent intent = new Intent(this, InfoPage.class);
        startActivity(intent);
    }

    public void goBack() {
        finish();
    }

    public void openPhotoLibrary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivity(intent);
    }


    public void openPredictionsPage() {
        click_image_id = findViewById(R.id.Image);
        errorText = findViewById(R.id.noPhotoErrorText);
        if (click_image_id.getDrawable() == null) {
            errorText.setText("No photo added... Please add a photo");
        }
        else {
            Intent intent = new Intent(this, ResultsPage.class);
            startActivity(intent);
        }
    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            // BitMap is data structure of image file
            // which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");
            click_image_id.setBackground(null);
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
        }
    }
}