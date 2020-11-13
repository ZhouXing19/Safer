package com.example.safer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class PostDangerActivity extends AppCompatActivity {
    public static final String TAG = "PostDangerActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final int PICK_FROM_MAP_REQUEST_CODE = 20;
    private static final int POST_PHOTO_REQUEST_CODE = 33;

    private String user_id = "";
    private String danger_id = "";
    private String imageUrl;

    private ImageView mBack;
    private TextView mPickFromMap;
    private EditText mTime, mLocation, mDescription;
    private FloatingActionButton mPost;
    private ExtendedFloatingActionButton pictureBtn, videoBtn;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    public Bitmap takenImage;

    private StorageReference mStorageRef;

    FirebaseDatabase rootNode;
    DatabaseReference dangerReference, userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_danger);

        mBack = (ImageView) findViewById(R.id.back);
        mPickFromMap = (TextView) findViewById(R.id.pickupfrommap);
        mTime = (EditText) findViewById(R.id.time);
        mLocation = (EditText) findViewById(R.id.location);
        mDescription = (EditText) findViewById(R.id.descript);
        mPost = (FloatingActionButton) findViewById(R.id.postButton);
        pictureBtn = (ExtendedFloatingActionButton) findViewById(R.id.pictureBtn);
        videoBtn = (ExtendedFloatingActionButton) findViewById(R.id.videoBtn);

        Random random = new Random();
        IdGenerator idGenerator = new IdGenerator(random);
        danger_id = idGenerator.nextId();

        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPickFromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDangerActivity.this, PickLocationActivity.class);
                startActivityForResult(intent, PICK_FROM_MAP_REQUEST_CODE);
            }
        });


        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strTime = mTime.getText().toString();
                String strDescript = mDescription.getText().toString();
                String strLocation = mLocation.getText().toString();

                if (strLocation.replaceAll("//s", "").equalsIgnoreCase("")
                        || strTime.replaceAll("//s", "").equalsIgnoreCase("")
                        || strDescript.replaceAll("//s", "").equalsIgnoreCase("")) {
                    Toast.makeText(PostDangerActivity.this, "Complete the info!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onClick: " + strLocation);


                    rootNode = FirebaseDatabase.getInstance();
                    dangerReference = rootNode.getReference("Danger");
                    userReference = rootNode.getReference("Users");

                    DangerHelperClass dangerClass = new DangerHelperClass(strTime, strDescript, strLocation, imageUrl);
                    DangerList dangerList = new DangerList();

                    dangerList.PushDanger(danger_id);
                    dangerReference.child(danger_id).setValue(dangerClass);
                    userReference.child(user_id).child("dangers").child(danger_id).setValue(true);

                }
            }

        });

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });



    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(PostDangerActivity.this, "com.example.safer", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }



    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent subintent) {
        super.onActivityResult(requestCode, resultCode, subintent);
        Log.i(TAG, "onActivityResult: caled");

        if (requestCode == PICK_FROM_MAP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = subintent.getExtras();
                double[] selectedLocation = extras.getDoubleArray("SelectedLocation");
                Log.i(TAG, "onActivityResult: " + Arrays.toString(selectedLocation));
                Toast.makeText(this, "Selected Location successfully!", Toast.LENGTH_SHORT).show();

                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(selectedLocation[0], selectedLocation[1], 1);
                    String selectedAddress = addresses.get(0).getAddressLine(0);
                    mLocation.setText(selectedAddress);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "Get Address failed");
                }

            }
        }

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Log.i(TAG, "onActivityResult: CAPTURE_IMAGE_ACTIVITY back");
                Intent intent = new Intent(PostDangerActivity.this, CapturePhotoActivity.class);
                // Pass the image to the new intent
                intent.putExtra("takenImagePath", photoFile.getAbsolutePath());
                startActivityForResult(intent, POST_PHOTO_REQUEST_CODE);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == POST_PHOTO_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                mStorageRef = FirebaseStorage.getInstance().getReference();
                Uri photoUri = Uri.fromFile(photoFile);
                StorageReference imageRef = mStorageRef.child("Safer/images/" + user_id + "/" + danger_id + "_0.jpg");

                imageRef.putFile(photoUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // Get a URL to the uploaded content
                                                imageUrl = uri.toString();
                                                Log.i(TAG, "onSuccess: saved image to url: " + imageUrl);
                                            }
                                        });
                                    }
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostDangerActivity.this, "Failed to save to cloud: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
        }




    }
}