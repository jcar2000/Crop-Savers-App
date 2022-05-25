package com.example.cropsavers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cropsavers.ml.MobileAppleModel;
import com.example.cropsavers.ml.MobileCherryModel;
import com.example.cropsavers.ml.MobileCornModel;
import com.example.cropsavers.ml.MobileGrapeModel;
import com.example.cropsavers.ml.MobilePeachModel;
import com.example.cropsavers.ml.MobilePepperModel;
import com.example.cropsavers.ml.MobilePotatoModel;
import com.example.cropsavers.ml.MobileStrawberryModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImagePage extends AppCompatActivity {
    // Define species string
    String species = null;

    // Define photo and display imageview variables
    Bitmap photo = null;
    ImageView selectedImageDisplay;
    TextView errorText;

    // ON PAGE CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);
        
        // get selected species type from last screen and set title
        Intent selectedSpecies = getIntent();
        species = selectedSpecies.getStringExtra("species");
        ((TextView)findViewById(R.id.header)).setText(species.concat(" Detection"));

        // set variables for image display imageview and error text below it
        selectedImageDisplay = findViewById(R.id.Image);
        errorText = findViewById(R.id.noPhotoErrorText);

        //Buttons //////////////////////////////////////////////////////////////////
        // button logic for home button
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> openHomePage());

        // button logic for info button
        Button infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> openInfoPage());

        // button logic for back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> goBack());

        // button logic for photo upload button
        Button uploadButton = findViewById(R.id.uploadPhotoButton);
        uploadButton.setOnClickListener(v -> openPhotoLibrary());

        // button logic for predict button
        Button predictButton = findViewById(R.id.predictButton);
        predictButton.setOnClickListener(v -> openPredictionsPage());

        Button takePhotoButton = findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(v -> openCameraFunction());
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
        // create an intent for a photo library instance to select an image
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // open the intent and pass the selected image and request code
        startActivityForResult(i, 2);
    }

    private void openCameraFunction() {
        // create an intent for a camera instance to take a new photo
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // open the intent and pass the taken photo and request code
        startActivityForResult(camera_intent, 1);
    }

    // function to pass image into the required model and pass the predicted
    // probabilities to the results page
    public void openPredictionsPage() {
        if (selectedImageDisplay.getDrawable() == null) {
            errorText.setText("No photo added... Please add a photo");
        }
        else {
            float[] resultsArray = null;

            // process bitmap image into a ByteBuffer and then into a TensorBuffer
            ByteBuffer byteBuffer = convertBitmapToByteBuffer(photo);
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // use correct ml model for the selected crop species
            try {
                if (species.contentEquals("Apple")) {
                    MobileAppleModel model = MobileAppleModel.newInstance(this);
                    MobileAppleModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else if (species.contentEquals("Cherry")) {
                    MobileCherryModel model = MobileCherryModel.newInstance(this);
                    MobileCherryModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else if (species.contentEquals("Corn")) {
                    MobileCornModel model = MobileCornModel.newInstance(this);
                    MobileCornModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else if (species.contentEquals("Grape")) {
                    MobileGrapeModel model = MobileGrapeModel.newInstance(this);
                    MobileGrapeModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else if (species.contentEquals("Peach")) {
                    MobilePeachModel model = MobilePeachModel.newInstance(this);
                    MobilePeachModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else if (species.contentEquals("Pepper")) {
                    MobilePepperModel model = MobilePepperModel.newInstance(this);
                    MobilePepperModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else if (species.contentEquals("Potato")) {
                    MobilePotatoModel model = MobilePotatoModel.newInstance(this);
                    MobilePotatoModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else if (species.contentEquals("Strawberry")) {
                    MobileStrawberryModel model = MobileStrawberryModel.newInstance(this);
                    MobileStrawberryModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                } else {
                    MobileAppleModel model = MobileAppleModel.newInstance(this);
                    MobileAppleModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    resultsArray = outputFeature0.getFloatArray();
                    model.close();
                }
            } catch (Exception e) {
                Log.d("myapp", "error with model predictions");
            }

            // add required information to results page intent and start activity
            Intent intent = new Intent(this, ResultsPage.class);
            //convert bitmap image to byteArray to be able to pass to results page
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, bs);
            intent.putExtra("predictionImage", bs.toByteArray());
            intent.putExtra("predictions", resultsArray);
            intent.putExtra("species", species);
            startActivity(intent);
        }
    }

    // This method will help to retrieve the images
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);

        // Camera Function
        if (requestCode == 1) {
            try {
                Bitmap img = (Bitmap) data.getExtras().get("data");
                photo = img;
                // Set the image in imageview for display
                selectedImageDisplay.setBackground(null);
                selectedImageDisplay.setImageBitmap(img);
                errorText.setText("");
            }
            catch (Exception  e) {
                Log.d("myapp", "error with camera function");
            }
        }

        // Photo Library
        else if (requestCode == 2)
        {
            try {
                if(data != null){
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    // update the preview image in the layout
                    Bitmap img;
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    photo = img;
                    // Set the image in imageview for display
                    selectedImageDisplay.setBackground(null);
                    selectedImageDisplay.setImageBitmap(img);
                    errorText.setText("");
                }
            }
            catch (Exception  e) {
                Log.d("myapp", "error with photo library");
            }
        }
    }


    // function to convert an image bitmap into a normalized bytebuffer to be used as an input for the ml model
    private ByteBuffer convertBitmapToByteBuffer(Bitmap bp) {
        ByteBuffer imgData = ByteBuffer.allocateDirect(Float.BYTES*224*224*3); // allocate required memory
        imgData.order(ByteOrder.nativeOrder());
        Bitmap bitmap = Bitmap.createScaledBitmap(bp,224,224,true); // scale the bitmap dimensions to required size for model input
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