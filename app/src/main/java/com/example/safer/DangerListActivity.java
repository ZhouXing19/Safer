package com.example.safer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.safer.models.Danger;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DangerListActivity extends AppCompatActivity {

    ArrayList<Danger> dangers;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_list);
        dangers = new ArrayList<>();

        // TODO: add real data
        Danger temp = new Danger("robbery", "a student was approached by an armed individual",
                "some user", 1604698894);
        dangers.add(temp);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvDangers);

        // Create adapter passing in the sample user data
        DangerAdapter adapter = new DangerAdapter(dangers);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_danger_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_danger_list:
                        return true;
                    case R.id.action_map:
                        // do something here
                        intent = new Intent(DangerListActivity.this, MainMapActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_post_danger:
                        // do something here
                        intent = new Intent(DangerListActivity.this, PostDangerActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_profile:
                        intent = new Intent(DangerListActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                    default: return true;
                }
            }
        });
    }
}