package com.example.safer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.LatLng;

import org.joda.time.Hours;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;


public class PostDangerActivity extends AppCompatActivity {
    public static final String TAG = "PostDangerActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final int PICK_FROM_MAP_REQUEST_CODE = 20;

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 55;
    String[] CATEGORIES = {"Robbery", "Fighting", "Gun-carrying", "Carjacking"};

    private String user_id = "";
    private String danger_id = "";
    private String imageUrl = "", videoUrl = "";

    private ImageView mBack;
    private TextView mPickFromMap;
    private EditText mTime, mLocation, mDescription, mTitle;
    private TextInputLayout mCategory;
    private FloatingActionButton mPost;
    private ExtendedFloatingActionButton pictureBtn, videoBtn, pickDate, pickTime;
    private File photoFile, videoFile;
    private AutoCompleteTextView mAutoCompleteCategory;
    private Uri videoUri;
    public String photoFileName = "photo.jpg";
    public String videoFileName = "video.mp4";
    private String mTimeZone;
    public Bitmap takenImage;
    private String timestamp;

    private Calendar rightNow = Calendar.getInstance();
    private int currentHourIn24Format;
    private int currentMinutes;

    private String strDate = "";
    private StringBuilder sbTime = new StringBuilder();
    private String wholeTimeStr = "";

    private double pickedLat, pickedLng;

    private StorageReference mStorageRef;

    FirebaseDatabase rootNode;
    DatabaseReference dangerReference, userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_danger);

        mTimeZone = "[" + TimeZone.getDefault().getID() + "] ";


        mBack = (ImageView) findViewById(R.id.back);
        mPickFromMap = (TextView) findViewById(R.id.pickupfrommap);
        mTime = (EditText) findViewById(R.id.time);
        mLocation = (EditText) findViewById(R.id.location);
        mDescription = (EditText) findViewById(R.id.descript);
        mCategory = (TextInputLayout) findViewById(R.id.category_layout);
        mTitle = (EditText) findViewById(R.id.titleEt);
        mPost = (FloatingActionButton) findViewById(R.id.postButton);
        pictureBtn = (ExtendedFloatingActionButton) findViewById(R.id.pictureBtn);
        videoBtn = (ExtendedFloatingActionButton) findViewById(R.id.videoBtn);
        pickDate = (ExtendedFloatingActionButton) findViewById(R.id.datePicker);
        pickTime = (ExtendedFloatingActionButton) findViewById(R.id.timePicker);
        mAutoCompleteCategory = (AutoCompleteTextView) findViewById(R.id.category);

        mTime.setText(mTimeZone);


        Random random = new Random();
        IdGenerator idGenerator = new IdGenerator(random);
        danger_id = idGenerator.nextId();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDangerActivity.this, MainMapActivity.class);
                startActivity(intent);
                return;
            }
        });

        mPickFromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDangerActivity.this, PickLocationActivity.class);
                startActivityForResult(intent, PICK_FROM_MAP_REQUEST_CODE);
            }
        });


        // -------------------------------------- Date Picker --------------------------------------
        MaterialDatePicker.Builder datePickBuilder = MaterialDatePicker.Builder.datePicker();
        datePickBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = datePickBuilder.build();
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                strDate = materialDatePicker.getHeaderText();
                wholeTimeStr = getWholeTimeStr(mTimeZone, strDate, sbTime);
                mTime.setText(wholeTimeStr);
            }
        });

        // -------------------------------------- Time Picker --------------------------------------

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                currentMinutes = rightNow.get(Calendar.MINUTE);

                final MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(currentHourIn24Format)
                        .setMinute(currentMinutes)
                        .build();

                materialTimePicker.show(getSupportFragmentManager(), "fragment_tag");
                // Don't touch -- it's magic
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View dialog) {
                        String newHour = String.valueOf(materialTimePicker.getHour());
                        String newMinute = String.valueOf(materialTimePicker.getMinute());
                        PostDangerActivity.this.onTimeSet(newHour, newMinute);

                        wholeTimeStr = getWholeTimeStr(mTimeZone, strDate, sbTime);
                        mTime.setText(wholeTimeStr);
                    }
                });
            }
        });







        // -------------------------------------- Dropdown Menu --------------------------------------

        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), R.layout.list_item, CATEGORIES);

        mAutoCompleteCategory.setAdapter(adapter);



        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strTime = mTime.getText().toString();
                String strDescript = mDescription.getText().toString();
                String strLocation = mLocation.getText().toString();
                String strTitle = mTitle.getText().toString();
                String strCategory = mAutoCompleteCategory.getText().toString();

                if (strLocation.replaceAll("//s", "").equalsIgnoreCase("")
                        || strTime.replaceAll("//s", "").equalsIgnoreCase("")
                        || strDescript.replaceAll("//s", "").equalsIgnoreCase("")
                        || strDate.equals("") || sbTime.toString().equals("")) {
                    Toast.makeText(PostDangerActivity.this, "Complete the info!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onClick: " + strLocation);

                    rootNode = FirebaseDatabase.getInstance();
                    dangerReference = rootNode.getReference("Danger");
                    userReference = rootNode.getReference("Users");
                    Log.i(TAG, "onClick: strDate:" + strDate);
                    Log.i(TAG, "onClick: sbTime:" + sbTime);
                    timestamp = new GetNumericDate(strDate, sbTime.toString()).transform();
                    Log.i(TAG, "onClick: timestamp:" + timestamp);


                    DangerHelperClass dangerClass = new DangerHelperClass(strTime,
                                                                          timestamp,
                                                                          strDescript,
                                                                          strLocation,
                                                                          imageUrl,
                                                                          videoUrl,
                                                                          strCategory,
                                                                          strTitle,
                                                                          user_id,
                                                                          pickedLat,
                                                                          pickedLng);
                    DangerList dangerList = new DangerList();

                    dangerList.PushDanger(danger_id);
                    dangerReference.child(danger_id).setValue(dangerClass);
                    userReference.child(user_id).child("dangers").child(danger_id).setValue(true);

                    Toast.makeText(PostDangerActivity.this, "Danger Posted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostDangerActivity.this, MainMapActivity.class);
                    startActivity(intent);
                }
            }

        });

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoCamera();
            }
        });

    }

    private void onTimeSet(String newHour, String newMinute) {
        sbTime = new StringBuilder();
        sbTime.append(newHour);
        sbTime.append(":");
        sbTime.append(newMinute);
    }

    private String getWholeTimeStr(String mTimeZone, String strDate, StringBuilder sbTime){
        return  mTimeZone + " " + strDate + " " + sbTime.toString();
    }


    private void launchVideoCamera() {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = getFileUri(videoFileName, "video");
            Uri fileProvider = FileProvider.getUriForFile(PostDangerActivity.this, "com.example.safer", videoFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a File reference for future access
        photoFile = getFileUri(photoFileName, "image");

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(PostDangerActivity.this, "com.example.safer", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    // Returns the File for a photo stored on disk given the fileName
    public File getFileUri(String fileName, String mediaType) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir;
        switch (mediaType){
            case "image":
                mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
                break;
            case "video":
                mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), TAG);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mediaType);
        }
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        // /storage/emulated/0/Android/data/com.example.safer/files/Pictures/PostDangerActivity/photo.jpg
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
                pickedLat = selectedLocation[0];
                pickedLng = selectedLocation[1];
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
                                                Toast.makeText(PostDangerActivity.this, "Picture Taken!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostDangerActivity.this, "Failed to save image to cloud: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                videoUri = subintent.getData();
                mStorageRef = FirebaseStorage.getInstance().getReference();

                StorageReference videoRef = mStorageRef.child("Safer/video/" + user_id + "/" + danger_id + "_0.mp4");

                videoRef.putFile(videoUri)
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
                                                videoUrl = uri.toString();
                                                Log.i(TAG, "onSuccess: saved video to url: " + videoUrl);
                                                Toast.makeText(PostDangerActivity.this, "Video uploaded!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostDangerActivity.this, "Failed to save video to cloud: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                Toast.makeText(this, "Fail to take video", Toast.LENGTH_SHORT).show();
            }
        }


    }
}