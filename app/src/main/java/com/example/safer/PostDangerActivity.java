package com.example.safer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class PostDangerActivity extends AppCompatActivity {
    public static final String TAG = "PostDangerActivity";

    private ImageView mBack;
    private TextView mPickFromMap;
    private EditText mTime, mLocation, mDescription;
    private FloatingActionButton mPost, mProfile;

    private final int REQUEST_CODE = 20;



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
        mProfile = (FloatingActionButton) findViewById(R.id.profileBtn);


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
                startActivityForResult(intent, REQUEST_CODE);
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
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    rootNode = FirebaseDatabase.getInstance();
                    dangerReference = rootNode.getReference("Danger");
                    userReference = rootNode.getReference("Users");

                    Random random = new Random();
                    IdGenerator idGenerator = new IdGenerator(random);
                    String danger_id = idGenerator.nextId();
                    DangerHelperClass dangerClass = new DangerHelperClass(strTime, strDescript, strLocation);
                    DangerList dangerList = new DangerList();

                    dangerList.PushDanger(danger_id);
                    dangerReference.child(danger_id).setValue(dangerClass);


                    userReference.child(user_id).child("dangers").child(danger_id).setValue(true);


                }

            }

        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDangerActivity.this, ProfileActivity.class);
                startActivity(intent);
                return;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent subintent) {
        super.onActivityResult(requestCode, resultCode, subintent);
        Log.i(TAG, "onActivityResult: caled");
        if(resultCode == RESULT_OK) {
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
}