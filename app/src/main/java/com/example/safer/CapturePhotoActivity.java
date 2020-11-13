package com.example.safer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;

public class CapturePhotoActivity extends AppCompatActivity {

    public static final String TAG = "CapturePhotoActivity";
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;
    String takenImagePath;
    Uri imageUri;
    String myUri = "";

    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView closeIv, imageIv;
    TextView postTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);

        closeIv = findViewById(R.id.closeIv);
        imageIv = findViewById(R.id.imageIv);
        postTv = findViewById(R.id.postTv);

        storageReference = FirebaseStorage.getInstance().getReference("posts");

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CapturePhotoActivity.this, PostDangerActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        postTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CapturePhotoActivity.this, "Should post!", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();

        takenImagePath = intent.getStringExtra("takenImagePath");
        if (takenImagePath != null){
            imageIv.setImageBitmap(BitmapFactory.decodeFile(takenImagePath));
        } else {
            Toast.makeText(this, "Taken Image is not passed to CapturePhotoActivity", Toast.LENGTH_SHORT).show();
        }

//        CropImage.activity()
//                .setAspectRatio(1, 1)
//                .start(CapturePhotoActivity.this);

        postTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CapturePhotoActivity.this, "Image Confirmed", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }


}