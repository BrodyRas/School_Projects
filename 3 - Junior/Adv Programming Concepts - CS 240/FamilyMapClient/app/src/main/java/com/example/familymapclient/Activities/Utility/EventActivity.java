package com.example.familymapclient.Activities.Utility;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.familymapclient.Activities.Main.LoginFragment;
import com.example.familymapclient.Activities.Main.MapFragment;
import com.example.familymapclient.Models.DataCache;
import com.example.familymapclient.R;
import com.google.android.gms.maps.GoogleMap;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends AppCompatActivity {
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Iconify.with(new FontAwesomeModule());
        DataCache dataCache = DataCache.getInstance();
        dataCache.setInEventActivity(true);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.event_map);

        dataCache.setClickedMarker(dataCache.getMyMarker(dataCache.getClickedEvent().getEventID()));
        if (fragment == null) {
            fragment = new MapFragment();
            fm.beginTransaction()
                    .add(R.id.event_map, fragment)
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataCache.getInstance().setInEventActivity(false);
    }
}
