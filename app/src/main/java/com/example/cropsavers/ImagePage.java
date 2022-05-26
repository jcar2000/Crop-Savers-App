package com.example.cropsavers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cropsavers.ml.MobileAppleModel;
import com.example.cropsavers.ml.MobileCherryModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class ImagePage extends AppCompatActivity {
    private static final String TAG = "myapp";
    private Button homeButton;
    private Button infoButton;
    private Button backButton;
    private Button predictButton;

    private TextView errorText;
    String species = null;

    // Define the pic id
    private static final int pic_id = 123;
    Button camera_open_id;
    ImageView click_image_id;

    Interpreter interpreter;
    Bitmap photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);
        
        
        Intent selectedSpecies = getIntent();
        species = selectedSpecies.getStringExtra("species");
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

        Button uploadButton = findViewById(R.id.uploadPhotoButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoLibrary();
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
        // create an instance of the
        // intent of the type image
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(i, 124);
    }


    public void openPredictionsPage() {
        click_image_id = findViewById(R.id.Image);
        errorText = findViewById(R.id.noPhotoErrorText);
        if (click_image_id.getDrawable() == null) {
            errorText.setText("No photo added... Please add a photo");
        }
        else {

            try {
                float[] resultsArray = null;

                // process image
                Bitmap bitmap = Bitmap.createScaledBitmap(photo, 224, 224, false);
                ByteBuffer byteBuffer = convertBitmapToByteBuffer(bitmap);
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                inputFeature0.loadBuffer(byteBuffer);

                if (species.contentEquals("Apple")) {
                    MobileAppleModel model = MobileAppleModel.newInstance(this);
                    MobileAppleModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                }
                else if (species.contentEquals("Cherry")) {
                    MobileCherryModel model = MobileCherryModel.newInstance(this);
                    MobileCherryModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                }

                else {
                    MobileAppleModel model = MobileAppleModel.newInstance(this);
                    MobileAppleModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                }

                for (int i = 0; i < resultsArray.length; i++) {
                    Log.d("myapp", String.valueOf(resultsArray[i]));
                }

                Intent intent = new Intent(this, ResultsPage.class);
                //convert bitmap image to byteArray
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bs);
                intent.putExtra("predictionImage", bs.toByteArray());
                intent.putExtra("predictions", resultsArray);
                intent.putExtra("species", species);
                startActivity(intent);

            } catch (IOException e) {
                errorText.setText("error");
            }
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
            Bitmap img = (Bitmap) data.getExtras()
                    .get("data");
            photo = img;
            click_image_id.setBackground(null);
            // Set the image in imageview for display
            click_image_id.setImageBitmap(img);
        }
        else if (requestCode == 124)
        {
            try {
                if(data != null){
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    // update the preview image in the layout
                    Bitmap img = null;
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    photo = img;
                    click_image_id.setBackground(null);
                    click_image_id.setImageBitmap(img);
                }
            }
            catch (IOException e) {}
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bp) {
        ByteBuffer imgData = ByteBuffer.allocateDirect(Float.BYTES*224*224*3);
        imgData.order(ByteOrder.nativeOrder());
        Bitmap bitmap = Bitmap.createScaledBitmap(bp,224,224,true);
        int [] intValues = new int[224*224];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        // Convert the image to floating point.
        int pixel = 0;

        for (int i = 0; i < 224; ++i) {
            for (int j = 0; j < 224; ++j) {
                final int val = intValues[pixel++];

                imgData.putFloat(((val>> 16) & 0xFF) / 255.f);
                imgData.putFloat(((val>> 8) & 0xFF) / 255.f);
                imgData.putFloat((val & 0xFF) / 255.f);
            }
        }
        return imgData;
    }
}