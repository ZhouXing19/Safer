package com.example.safer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDangerActivity extends AppCompatActivity {
    private ImageView mBack;
    private TextView mPickFromMap;
    private EditText mTime, mLocation, mDescription;
    private FloatingActionButton mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_danger);

        mBack = (ImageView) findViewById(R.id.back);
        mPickFromMap = (TextView) findViewById(R.id.pickupfrommap);
        mTime = (EditText) findViewById(R.id.time);
        mLocation = (EditText) findViewById(R.id.location);
        mDescription = (EditText) findViewById(R.id.descript);
        mSubmit = (FloatingActionButton) findViewById(R.id.submitButton);



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
                startActivity(intent);
                finish();
                return;
            }
        });

      //  --------- TODO --------
//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String strLocation = mLocation.getText().toString();
//                String strTime = mLocation.getText().toString();
//                String strDescript = mDescription.getText().toString();
//
//                if(strLocation.replaceAll("//s", "" ).equalsIgnoreCase("")
//                        || strTime.replaceAll("//s", "" ).equalsIgnoreCase("")
//                        || strDescript.replaceAll("//s", "").equalsIgnoreCase("")){
//                    Toast.makeText(PostDangerActivity.this, "Complete the info!", Toast.LENGTH_SHORT).show();
//                }else{
//                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Danger");
//                    // ------------ save the data into firebase ----------
//
//
//                }
//
//            }
//        });
    }
}