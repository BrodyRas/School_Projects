package com.example.familymapclient.Activities.Utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.Models.DataCache;
import com.example.familymapclient.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Event;
import Models.Person;

public class PersonActivity extends AppCompatActivity {
    TextView firstName, lastName, gender;
    ExpandableListView expandableListView;
    Person clickedPerson;
    Map<Float, Integer> colorMatcher = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        colorMatcher.put(0.0F, R.color.red);
        colorMatcher.put(30.0F, R.color.orange);
        colorMatcher.put(60.0F, R.color.yellow);
        colorMatcher.put(90.0F, R.color.green);
        colorMatcher.put(120.0F, R.color.blue);
        colorMatcher.put(150.0F, R.color.indigo);
        colorMatcher.put(180.0F, R.color.violet);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        gender = findViewById(R.id.gender);

        DataCache dataCache = DataCache.getInstance();
        clickedPerson = dataCache.getClickedPerson();

        firstName.setText(clickedPerson.getFirstName());
        lastName.setText(clickedPerson.getLastName());

        String genderString = null;
        switch (clickedPerson.getGender()){
            case "m": genderString = "Male"; break;
            case "f": genderString = "Female"; break;
        }
        gender.setText(genderString);

        Person person = DataCache.getInstance().getClickedPerson();
        String personID = person.getPersonID();
        List<Person> persons = DataCache.getInstance().getAssociatedPersons(personID);
        List<Event> events = DataCache.getInstance().getLifeStoryEvents(personID);

        expandableListView = findViewById(R.id.expandable_list);
        expandableListView.setAdapter(new ExpandableListAdapter(persons, events));
    }


    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int PERSON_GROUP_POSITION = 0;
        private static final int EVENT_GROUP_POSITION = 1;

        private final List<Person> persons;
        private final List<Event> events;

        public ExpandableListAdapter(List<Person> persons, List<Event> events) {
            this.persons = persons;
            this.events = events;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch(groupPosition){
                case PERSON_GROUP_POSITION:
                    return persons.size();
                case EVENT_GROUP_POSITION:
                    return events.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch(groupPosition){
                case PERSON_GROUP_POSITION:
                    return getString(R.string.group_title_family);
                case EVENT_GROUP_POSITION:
                    return getString(R.string.group_title_events);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch(groupPosition){
                case PERSON_GROUP_POSITION:
                    return persons.get(childPosition);
                case EVENT_GROUP_POSITION:
                    return events.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
                                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.layout_person_title,
                                                    parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.group_title);

            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.group_title_family);
                    break;
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.group_title_events);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView;

            switch(groupPosition) {
                case PERSON_GROUP_POSITION:
                    itemView = layoutInflater.inflate(R.layout.layout_person_list_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                case EVENT_GROUP_POSITION:
                    itemView = layoutInflater.inflate(R.layout.layout_person_list_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializePersonView(View listItemView, final int childPosition) {
            TextView personText = listItemView.findViewById(R.id.list_text);
            ImageView imageView = listItemView.findViewById(R.id.list_icon);

            Person person = persons.get(childPosition);
            String relation = findRelation(clickedPerson, person);
            personText.setText(relation);

            Drawable genderIcon = null;
            switch (person.getGender()){
                case "m": genderIcon = new IconDrawable(
                        PersonActivity.this,
                        FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon)
                        .sizeDp(40); break;
                case "f": genderIcon = new IconDrawable(
                        PersonActivity.this,
                        FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon)
                        .sizeDp(40); break;
            }
            imageView.setImageDrawable(genderIcon);

            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataCache.getInstance().setClickedPerson(persons.get(childPosition));
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    startActivity(intent);
                }
            });
        }

        private void initializeEventView(View listItemView, final int childPosition) {
            TextView eventText = listItemView.findViewById(R.id.list_text);
            ImageView imageView = listItemView.findViewById(R.id.list_icon);

            final Event event = events.get(childPosition);
            Person person = DataCache.getInstance().getPerson(event.getPersonID());

            String eventTitle = person.getFirstName() + " " + person.getLastName() + "'s " + event.getEventType() + '\n'
                    + event.getCity() + ", " + event.getCountry() + ": " + event.getYear();
            eventText.setText(eventTitle);

            int color;
            Map<String, Float> markerColors = DataCache.getInstance().getMarkerColors();
            float floatColor = markerColors.get(event.getEventType().toLowerCase());

            color = colorMatcher.get(floatColor);

            Drawable genderIcon = new IconDrawable(
                        PersonActivity.this,
                        FontAwesomeIcons.fa_map_marker)
                        .colorRes(color)
                        .sizeDp(40);

            imageView.setImageDrawable(genderIcon);


            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataCache.getInstance().setClickedEvent(events.get(childPosition));
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    startActivity(intent);
                }
            });
        }

        private String findRelation(Person clickedPerson, Person associatedPerson){
            String clickedID = clickedPerson.getPersonID();
            String associatedID = associatedPerson.getPersonID();
            String name = associatedPerson.getFirstName() + " " + associatedPerson.getLastName() + '\n';

            //Child found
            if((associatedPerson.getMother() != null && associatedPerson.getMother().matches(clickedID)) ||
                    (associatedPerson.getFather() != null && associatedPerson.getFather().matches(clickedID))){
                switch (associatedPerson.getGender()){
                    case "m": return name + "Son";
                    case "f": return name + "Daughter";
                }
            }

            //Parent found
            if((clickedPerson.getFather() != null &&
                    clickedPerson.getFather().matches(associatedID))) return name + "Father";
            if((clickedPerson.getMother() != null &&
                    clickedPerson.getMother().matches(associatedID))) return name + "Mother";

            //spouse found
            if((clickedPerson.getSpouse() != null &&
                    clickedPerson.getSpouse().matches(associatedID))) return name + "Spouse";

            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
