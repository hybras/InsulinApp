package com.pennapps.insulin;

import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.Lists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RecImage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        OpenCam();
        EditText foodText = findViewById(R.id.foodChoice);
        foodText.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
        foodText.setOnEditorActionListener((view,key,event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            String foodString = foodText.getText().toString();
            ApiRequest food = new ApiRequest(
                    (i) -> {
                        int TDD = getIntent().getIntExtra("TDD", 50);
                        int targetBG = getIntent().getIntExtra("targetBG", 120);
                        boolean insulinType = getIntent().getBooleanExtra("insulinType", false);
                        Meal m = new Meal(insulinType,TDD,targetBG);
                        m.setMealCHO(i);
                        m.setCurrentBG(50);
                        int insulin = m.insulin();
                        Snackbar.make(getCurrentFocus(), "You need " + insulin + " grams insulin",Snackbar.LENGTH_LONG).show();
                    }
            );
            food.execute(foodString);
            return  true;
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static File lastPhoto = null;

    public void OpenCam() {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            String imageFileName = timeStamp;
//            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            try {
//                File image = File.createTempFile(
//                        imageFileName,  /* prefix */
//                        ".jpg",         /* suffix */
//                        storageDir      /* directory */
//                );
//                lastPhoto = image;
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.pennapps.insulin",
//                        image);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            } catch (IOException io) {}
//        } else Toast.makeText(getApplicationContext(),"Couldn't find a camera app",Toast.LENGTH_LONG).show();
        onActivityResult(REQUEST_IMAGE_CAPTURE,RESULT_OK,null);
    }


}
