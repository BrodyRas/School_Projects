package com.example.familymapclient.Activities.Main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymapclient.Activities.Utility.FilterActivity;
import com.example.familymapclient.Activities.Utility.PersonActivity;
import com.example.familymapclient.Activities.Utility.SearchActivity;
import com.example.familymapclient.Activities.Utility.SettingsActivity;
import com.example.familymapclient.Models.DataCache;
import com.example.familymapclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Models.Event;
import Models.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MAPFRAG";
    private GoogleMap map;
    private ImageView genderImageView;
    private TextView eventInfoText;
    private MenuItem searchItem, filterItem, settingsItem;
    private int TEXT_REQUEST = 1;
    private int NO_LOGOUT = 6;
    private int YES_LOGOUT = 7;
    private List<Polyline> myLines = new ArrayList<>();
    private int familyTreeColor;

    private final int RED_POSITION = 0;
    private final int CYAN_POSITION = 1;
    private final int YELLOW_POSITION = 2;
    private final int GREEN_POSITION = 3;
    private final int BLUE_POSITION = 4;
    private final int NORMAL_POSITION = 0;
    private final int HYBRID_POSITION = 1;
    private final int SATELLITE_POSITION = 2;
    private final int TERRAIN_POSITION = 3;
    private boolean inEventActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inEventActivity = DataCache.getInstance().isInEventActivity();
        setHasOptionsMenu(!inEventActivity);
    }

    //Set up menu items, listen for their clicks
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        searchItem = menu.findItem(R.id.menu_search);
        filterItem = menu.findItem(R.id.menu_filter);
        settingsItem = menu.findItem(R.id.menu_settings);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                searchTool();
                return false;
            }
        });

        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                filterTool();
                return false;
            }
        });

        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                settingsTool();
                return false;
            }
        });

        searchItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white).actionBarSize());
        filterItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_filter)
                .colorRes(R.color.white).actionBarSize());
        settingsItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white).actionBarSize());
    }

    private void searchTool() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    private void filterTool() {
        Intent intent = new Intent(getContext(), FilterActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    private void settingsTool() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivityForResult(intent, NO_LOGOUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == YES_LOGOUT){
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.launchLoginFragment();
        }
    }

    //Get references to the necessary views
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        genderImageView = view.findViewById(R.id.gender_icon);
        eventInfoText = view.findViewById(R.id.event_text);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LinearLayout eventLayout = view.findViewById(R.id.eventInfoLayout);
        eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personTool();
            }
        });

        return view;
    }

    private void personTool() {
        Marker marker;
        if(inEventActivity) marker = DataCache.getInstance().getClickedMarker();
        else marker = DataCache.getInstance().getMasterClickedMarker();

        if(marker != null) {
            Intent intent = new Intent(getContext(), PersonActivity.class);
            startActivityForResult(intent, TEXT_REQUEST);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //used to continually update event info when markers are clicked
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.hideInfoWindow();

                DataCache dataCache = DataCache.getInstance();

                if(inEventActivity) {
                    dataCache.setClickedMarker(marker);
                }
                else dataCache.setMasterClickedMarker(marker);

                Event clickedEvent = dataCache.getEvent(marker.getTitle());
                Person clickedPerson = dataCache.getPerson(clickedEvent.getPersonID());
                dataCache.setClickedEvent(clickedEvent);
                dataCache.setClickedPerson(clickedPerson);

                updateEventInfo(marker);
                drawPolyLines(marker);
                return false;
            }
        });

        //Disables external link to full Google Maps application
        map.getUiSettings().setMapToolbarEnabled(false);
        //used to generate markers upon creating map
        updateMarkers();
        DataCache dataCache = DataCache.getInstance();
        if(dataCache.getClickedMarker() != null){
            map.animateCamera(CameraUpdateFactory.newLatLng(dataCache.getClickedMarker().getPosition()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //used to generate marker upon returning to map
        if(map != null) updateMarkers();

        Marker marker;
        if(inEventActivity) marker = DataCache.getInstance().getClickedMarker();
        else marker = DataCache.getInstance().getMasterClickedMarker();

        if(marker == null){
            eventInfoText.setText(R.string.initial_event_info_text);
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.gray).sizeDp(40);
            genderImageView.setImageDrawable(genderIcon);
        }
        else{
            updateEventInfo(marker);
        }
    }

    private void drawPolyLines(Marker marker){
        //remove lines from previous clicks
        for(Polyline pl: myLines) pl.remove();
        myLines.clear();

        //get universally needed info
        DataCache dataCache = DataCache.getInstance();
        Event event = dataCache.getEvent(marker.getTitle());
        Person person = dataCache.getPerson(event.getPersonID());

        //LIFE STORY LINES
        if(dataCache.getLifeStorySwitch()){
            int colorPosition = dataCache.getLifeStoryColor();
            int color = 0;

            switch (colorPosition){
                case RED_POSITION: color = Color.RED; break;
                case CYAN_POSITION: color = Color.CYAN; break;
                case YELLOW_POSITION: color = Color.YELLOW; break;
                case GREEN_POSITION: color = Color.GREEN; break;
                case BLUE_POSITION: color = Color.BLUE; break;
            }

            List<Event> lifeStory = dataCache.getLifeStoryEvents(person.getPersonID());
            List<Marker> lifeStoryMarkers = new ArrayList<>();

            for(Event e: lifeStory) {
                Marker thisMarker = DataCache.getInstance().getMyMarker(e.getEventID());
                lifeStoryMarkers.add(thisMarker);
            }

            PolylineOptions options = new PolylineOptions()
                                            .width(8f).color(color);

            for(Marker m: lifeStoryMarkers){
                options.add(m.getPosition());
            }

            myLines.add(map.addPolyline(options));
        }

        //SPOUSE LINE
        if(dataCache.getSpouseSwitch()){
            if(person.getSpouse() != null){
                Person spouse = dataCache.getPerson(person.getSpouse());

                if(spouse != null){
                    Event spouseBirthEvent = dataCache.getEarliestEvent(spouse.getPersonID());

                    Marker spouseBirth = DataCache.getInstance().getMyMarker(spouseBirthEvent.getEventID());

                    int colorPosition = dataCache.getSpouseColor();
                    int color = 0;

                    switch (colorPosition){
                        case RED_POSITION: color = Color.RED; break;
                        case CYAN_POSITION: color = Color.CYAN; break;
                        case YELLOW_POSITION: color = Color.YELLOW; break;
                        case GREEN_POSITION: color = Color.GREEN; break;
                        case BLUE_POSITION: color = Color.BLUE; break;
                    }

                    myLines.add(map.addPolyline(
                            new PolylineOptions()
                                    .add(marker.getPosition())
                                    .add(spouseBirth.getPosition())
                                    .width(10f)
                                    .color(color)
                    ));
                }
            }
        }

        //FAMILY TREE LINES
        if(dataCache.getFamilyTreeSwitch()){
            int colorPosition = dataCache.getFamilyTreeColor();

            switch (colorPosition){
                case RED_POSITION: familyTreeColor = Color.RED; break;
                case CYAN_POSITION: familyTreeColor = Color.CYAN; break;
                case YELLOW_POSITION: familyTreeColor = Color.YELLOW; break;
                case GREEN_POSITION: familyTreeColor = Color.GREEN; break;
                case BLUE_POSITION: familyTreeColor = Color.BLUE; break;
            }

            familyTreeRecursion(person, 20f, marker);
        }

    }

    public void familyTreeRecursion(Person person, Float width, Marker marker){
        if(person.getFather() != null) {
            Person father = DataCache.getInstance().getPerson(person.getFather());

            if(father != null){
                Event fatherEarlyEvent = DataCache.getInstance().getEarliestEvent(father.getPersonID());

                Marker fathersEarlyMarker = DataCache.getInstance().getMyMarker(fatherEarlyEvent.getEventID());
                myLines.add(map.addPolyline(
                        new PolylineOptions()
                                .add(marker.getPosition(), fathersEarlyMarker.getPosition())
                                .width(width)
                                .color(familyTreeColor)
                ));
                familyTreeRecursion(father, (float) (width * .65), fathersEarlyMarker);
            }
        }

        if(person.getMother() != null){
            Person mother = DataCache.getInstance().getPerson(person.getMother());

            if(mother != null){
                Event motherEarlyEvent = DataCache.getInstance().getEarliestEvent(mother.getPersonID());
                Marker mothersEarlyMarker = DataCache.getInstance().getMyMarker(motherEarlyEvent.getEventID());
                myLines.add(map.addPolyline(
                        new PolylineOptions()
                                .add(marker.getPosition(), mothersEarlyMarker.getPosition())
                                .width(width)
                                .color(familyTreeColor)
                ));
                familyTreeRecursion(mother, (float) (width*.65), mothersEarlyMarker);
            }
        }
    }

    //Fill in the event information in the bottom of the screen
    private void updateEventInfo(Marker marker) {
        Drawable genderIcon;
        String eventID = marker.getTitle();
        Event event = DataCache.getInstance().getEvent(eventID);
        String personID = event.getPersonID();
        Person person = DataCache.getInstance().getPerson(personID);

        String newText = person.getFirstName() + " " + person.getLastName() + "'s " + event.getEventType() + '\n'
                + event.getCity() + ", " + event.getCountry() + ": " + event.getYear();
        eventInfoText.setText(newText);

        //Pick icon by gender
        if (person.getGender().matches("m")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(40);
        }
        genderImageView.setImageDrawable(genderIcon);

        //marker.showInfoWindow();
    }

    //Parsing the event list of the DataCache, add color coded markers for Birth, Marriage, and Death
    public void updateMarkers() {
        map.clear();

        //Update map type
        int mapType = DataCache.getInstance().getMapType();
        switch(mapType){
            case NORMAL_POSITION:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case HYBRID_POSITION:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case SATELLITE_POSITION:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case TERRAIN_POSITION:
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }

        //Update Markers
        Set<Event> events = DataCache.getInstance().getEventsSet();
        Map<String, Float> markerColors = DataCache.getInstance().getMarkerColors();
        float rootColor = 0.0F;

        for (Event e : events) {
            LatLng marker = new LatLng(e.getLatitude(), e.getLongitude());
            String title = e.getEventID();
            String eventType = e.getEventType().toLowerCase();
            float currentColor;

            if(markerColors.get(eventType) == null){
                markerColors.put(eventType, rootColor);
                currentColor = rootColor;
                rootColor += 30.0F;
            }
            else{
                currentColor = markerColors.get(eventType);
            }

            Marker thisMarker = map.addMarker(new MarkerOptions()
                    .position(marker)
                    .title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(currentColor)));

            DataCache.getInstance().putMyMarker(title, thisMarker);
        }

        Marker clickedMarker = DataCache.getInstance().getClickedMarker();
        if(clickedMarker != null)drawPolyLines(clickedMarker);
    }
}
