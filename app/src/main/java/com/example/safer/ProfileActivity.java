package com.example.safer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    TextView usernameTv, emailTv;
//    FloatingActionButton logoutBtn;
    ExtendedFloatingActionButton logoutBtn;
    ImageView avatarIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTv = (TextView) findViewById(R.id.usernameTv);
        emailTv = (TextView) findViewById(R.id.emailTv);
//        logoutBtn = (FloatingActionButton) findViewById(R.id.logoutBtn);
        logoutBtn = (ExtendedFloatingActionButton) findViewById(R.id.logoutBtn);
        avatarIv = (ImageView) findViewById(R.id.avatarIv);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            usernameTv.setText(signInAccount.getDisplayName());
            emailTv.setText(signInAccount.getEmail());

            String avatarUrl = signInAccount.getPhotoUrl().toString();
//            Toast.makeText(ProfileActivity.this, avatarUrl, Toast.LENGTH_LONG).show();
            // Load an image using Picasso library
            if(avatarUrl!=null){
                Picasso.with(getApplicationContext())
                        .load(avatarUrl)
                        .into(avatarIv);

                // Load an image using Glide library
                Glide.with(getApplicationContext())
                        .load(avatarUrl)
                        .into(avatarIv);
            }

        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainMapActivity.class);
                startActivity(intent);
            }
        });
    }
}

