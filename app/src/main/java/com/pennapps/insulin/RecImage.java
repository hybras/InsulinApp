package com.pennapps.insulin;

import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;

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



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            RadioGroup rg = findViewById(R.id.foodChoice);
            int i = 0;
//            try {
                for (String label : new String[]{"peas","pie","pasta"}) {
                    RadioButton button = new RadioButton(getApplicationContext());
                    button.setText(label);
                    rg.addView(button, i++);
                }
//            } catch (IOException io) {
//            }


        }
    }

    public void enter(View view) {
        RadioGroup rg = findViewById(R.id.foodChoice);
        RadioButton rad = findViewById(rg.getCheckedRadioButtonId());
        String selected = rad.getText().toString();
        Snackbar.make(view, selected + " has " + 50 + " grams of carbs", Snackbar.LENGTH_LONG).show();
//        ApiRequest fin = new ApiRequest((carbs -> Snackbar.make(view, "This meal has " + carbs + " grams of carbs", Snackbar.LENGTH_LONG).show()));
//        fin.execute(selected);
    }

}
