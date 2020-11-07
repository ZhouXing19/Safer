package com.example.safer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.safer.models.Danger;

import java.util.ArrayList;

public class DangerListActivity extends AppCompatActivity {

    ArrayList<Danger> dangers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_list);

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
    }
}