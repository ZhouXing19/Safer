package com.example.safer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.safer.models.Danger;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DangerListActivity extends AppCompatActivity {

    ArrayList<Danger> dangers;

    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_list);
        dangers = new ArrayList<>();

        // TODO: add real data
        Danger temp = new Danger("robbery", "a student was approached by an armed individual",
                "some user", 1604698894, "", "466 Maple Street");
        dangers.add(temp);
        temp = new Danger("arrest", "man with gun arrested",
                "some user", 1604698999, "", "9999 King East Ave");
        dangers.add(temp);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvDangers);

        // Create adapter passing in the sample user data
        final DangerAdapter adapter = new DangerAdapter(this, dangers);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                // TODO
//                adapter.clear();

                Danger temp = new Danger("more data", "a student was approached by an armed individual",
                        "some user", 1604698894, "", "466 Maple Street");
                dangers.add(temp);
                adapter.addAll(dangers);

                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Fragment navigation
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