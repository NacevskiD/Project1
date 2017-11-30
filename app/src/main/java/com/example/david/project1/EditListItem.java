package com.example.david.project1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 11/29/2017.
 */

public class EditListItem extends AppCompatActivity{

    private ImageView picture;
    private EditText desc;
    private Button finish;
    private Bitmap thumbnail;
    private static int TAKE_PICTURE_CODE = 1;
    private String mImagePath;
    private Bitmap mImage;
    private static final int SAVE_IMAGE_REQUEST_CODE = 1001;
    static String SEND_DESCRIPTION = "description";
    static String SEND_PICTURE = "picture";
    static String SEND_DATE = "date";
    static String IMAGE_FILE_PATH_KEY = "image filepath key";
    private String date;
    private boolean edit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_list_item);
        picture = (ImageView) findViewById(R.id.fresh_picture);
        desc = (EditText) findViewById(R.id.add_desc);
        finish = (Button) findViewById(R.id.finish_button);

        if (edit){
            desc.setText(getIntent().getStringExtra(MainActivity.EDIT_DESC));
            Bitmap pic = getIntent().getParcelableExtra(MainActivity.EDIT_PIC);
            picture.setImageBitmap(pic);
            edit = false;
        }

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent();
                goBack.putExtra(SEND_DESCRIPTION,desc.getText().toString());
                goBack.putExtra(SEND_PICTURE,thumbnail);
                goBack.putExtra(SEND_DATE,date);
                setResult(RESULT_OK,goBack);
                finish();
            }
        });


    }

    private void takePicture(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (pictureIntent.resolveActivity(getPackageManager()) == null){
            Toast.makeText(this,"You don't have a camera",Toast.LENGTH_LONG).show();

        }
        else {
            date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            String imageFileName = "Custom_picture__" + date;
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File imageFile = null;
            Uri imageFileUri = null;
            try{
                imageFile = File.createTempFile(imageFileName,".jpg",storageDirectory);
                mImagePath = imageFile.getAbsolutePath();
                Log.i("test",mImagePath);
               // imageFileUri = FileProvider.getUriForFile(this,"com.example.david.project1",imageFile);

            }
            catch (IOException ioe){
                return;
            }

           // pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageFileUri);
            startActivityForResult(pictureIntent,TAKE_PICTURE_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == TAKE_PICTURE_CODE && resultCode == RESULT_OK){
            thumbnail = data.getParcelableExtra("data");
            picture.setImageBitmap(thumbnail);

        }
    }
}
