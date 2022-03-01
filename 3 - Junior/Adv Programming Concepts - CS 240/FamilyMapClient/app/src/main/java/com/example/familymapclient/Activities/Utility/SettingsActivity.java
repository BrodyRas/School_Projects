package com.example.familymapclient.Activities.Utility;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.familymapclient.Models.Client;
import com.example.familymapclient.Models.DataCache;
import com.example.familymapclient.R;

import Results.RegisterResult;

public class SettingsActivity extends AppCompatActivity {
    private Spinner lifeStorySpinner;
    private Spinner familyTreeSpinner;
    private Spinner spouseSpinner;

    private int NO_LOGOUT = 6;
    private int YES_LOGOUT = 7;

    private static final int RED_POSITION = 0;
    private static final int CYAN_POSITION = 1;
    private static final int YELLOW_POSITION = 2;
    private static final int GREEN_POSITION = 3;
    private static final int BLUE_POSITION = 4;
    private static final int NORMAL_POSITION = 0;
    private static final int HYBRID_POSITION = 1;
    private static final int SATELLITE_POSITION = 2;
    private static final int TERRAIN_POSITION = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Get references to relevant views
        lifeStorySpinner = findViewById(R.id.life_story_spinner);
        familyTreeSpinner = findViewById(R.id.family_tree_spinner);
        spouseSpinner = findViewById(R.id.spouse_spinner);
        Spinner mapSpinner = findViewById(R.id.map_spinner);

        Switch lifeStorySwitch = findViewById(R.id.life_story_switch);
        Switch familyTreeSwitch = findViewById(R.id.family_tree_switch);
        Switch spouseSwitch = findViewById(R.id.spouse_switch);

        DataCache dataCache = DataCache.getInstance();

        lifeStorySwitch.setChecked(dataCache.getLifeStorySwitch());
        familyTreeSwitch.setChecked(dataCache.getFamilyTreeSwitch());
        spouseSwitch.setChecked(dataCache.getSpouseSwitch());

        lifeStorySpinner.setEnabled(dataCache.getLifeStorySwitch());
        familyTreeSpinner.setEnabled(dataCache.getFamilyTreeSwitch());
        spouseSpinner.setEnabled(dataCache.getSpouseSwitch());

        Button syncButton = findViewById(R.id.sync_button);
        Button logoutButton = findViewById(R.id.logout_button);

        //Set array adapters to our spinners
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.life_story_colors, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lifeStorySpinner.setAdapter(colorAdapter);
        familyTreeSpinner.setAdapter(colorAdapter);
        spouseSpinner.setAdapter(colorAdapter);

        ArrayAdapter<CharSequence> mapAdapter = ArrayAdapter.createFromResource(this,
                R.array.map_types, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(mapAdapter);

        lifeStorySpinner.setSelection(dataCache.getLifeStoryColor());
        familyTreeSpinner.setSelection(dataCache.getFamilyTreeColor());
        spouseSpinner.setSelection(dataCache.getSpouseColor());
        mapSpinner.setSelection(dataCache.getMapType());

        //Give functionality to the various spinners
        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case RED_POSITION:
                        DataCache.getInstance().setLifeStoryColor(RED_POSITION);
                        break;
                    case CYAN_POSITION:
                        DataCache.getInstance().setLifeStoryColor(CYAN_POSITION);
                        break;
                    case YELLOW_POSITION:
                        DataCache.getInstance().setLifeStoryColor(YELLOW_POSITION);
                        break;
                    case GREEN_POSITION:
                        DataCache.getInstance().setLifeStoryColor(GREEN_POSITION);
                        break;
                    case BLUE_POSITION:
                        DataCache.getInstance().setLifeStoryColor(BLUE_POSITION);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case RED_POSITION:
                        DataCache.getInstance().setFamilyTreeColor(RED_POSITION);
                        break;
                    case CYAN_POSITION:
                        DataCache.getInstance().setFamilyTreeColor(CYAN_POSITION);
                        break;
                    case YELLOW_POSITION:
                        DataCache.getInstance().setFamilyTreeColor(YELLOW_POSITION);
                        break;
                    case GREEN_POSITION:
                        DataCache.getInstance().setFamilyTreeColor(GREEN_POSITION);
                        break;
                    case BLUE_POSITION:
                        DataCache.getInstance().setFamilyTreeColor(BLUE_POSITION);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case RED_POSITION:
                        DataCache.getInstance().setSpouseColor(RED_POSITION);
                        break;
                    case CYAN_POSITION:
                        DataCache.getInstance().setSpouseColor(CYAN_POSITION);
                        break;
                    case YELLOW_POSITION:
                        DataCache.getInstance().setSpouseColor(YELLOW_POSITION);
                        break;
                    case GREEN_POSITION:
                        DataCache.getInstance().setSpouseColor(GREEN_POSITION);
                        break;
                    case BLUE_POSITION:
                        DataCache.getInstance().setSpouseColor(BLUE_POSITION);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case NORMAL_POSITION:
                        DataCache.getInstance().setMapType(NORMAL_POSITION);
                        break;
                    case HYBRID_POSITION:
                        DataCache.getInstance().setMapType(HYBRID_POSITION);
                        break;
                    case SATELLITE_POSITION:
                        DataCache.getInstance().setMapType(SATELLITE_POSITION);
                        break;
                    case TERRAIN_POSITION:
                        DataCache.getInstance().setMapType(TERRAIN_POSITION);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Only enable the spinners if the switches are on
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lifeStorySpinner.setEnabled(isChecked);
                DataCache.getInstance().setLifeStorySwitch(isChecked);
            }
        });
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                familyTreeSpinner.setEnabled(isChecked);
                DataCache.getInstance().setFamilyTreeSwitch(isChecked);
            }
        });
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spouseSpinner.setEnabled(isChecked);
                DataCache.getInstance().setSpouseSwitch(isChecked);
            }
        });

        final Context context = this;

        //Give functionality to the Sync and LogOut buttons
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheTask cacheTask = new CacheTask(context);
                cacheTask.execute();
            }
        });

        ///////////////////////////////////////????????????????
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(YES_LOGOUT);
                finish();
            }
        });
    }

    public class CacheTask extends AsyncTask<Void, Void, Void> {
        Context mContext;

        public CacheTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DataCache dataCache = DataCache.getInstance();
            RegisterResult registerResult = new RegisterResult(dataCache.getAuthToken(),
                                                                dataCache.getUser().getUserName(),
                                                                dataCache.getUser().getPersonID());
            Client client = new Client(dataCache.getServerHost(), dataCache.getServerPort());
            dataCache.clear();
            client.getCache(registerResult);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(mContext, "Cache successfully synchronized", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
