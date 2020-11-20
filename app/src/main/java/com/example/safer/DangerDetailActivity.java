package com.example.safer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.safer.models.Danger;

import org.parceler.Parcels;

import java.util.Date;

public class DangerDetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvDescription;

    private ImageView btnBack;
    private ImageView ivDanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.tvTime);
        tvAddress = findViewById(R.id.tvAddress);
        tvDescription = findViewById(R.id.tvDescription);
        btnBack = findViewById(R.id.btnBack);
        ivDanger = findViewById(R.id.ivDanger);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // go back to prev screen
            }
        });

        // populate data
        Danger danger = (Danger) Parcels.unwrap(getIntent().getParcelableExtra(DangerAdapter.KEY_DANGER));
        tvTitle.setText(danger.getTitle());
        // unix time to human-readable date
        Date date = new java.util.Date(danger.getUnixTime());
        tvTime.setText(date.toString());
        tvAddress.setText(danger.getAddress());
        tvDescription.setText(danger.getDescription());
        Glide.with(this).load(danger.getImgUrl()).into(ivDanger);
    }
}