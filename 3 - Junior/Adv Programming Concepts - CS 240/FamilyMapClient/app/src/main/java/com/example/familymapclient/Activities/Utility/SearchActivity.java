package com.example.familymapclient.Activities.Utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymapclient.Models.DataCache;
import com.example.familymapclient.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Models.Event;
import Models.Person;

public class SearchActivity extends AppCompatActivity {
    private Set<Person> filteredPersons = new HashSet<>();
    private Set<Event> filteredEvents = new HashSet<>();
    private Set<Person> shownPersons = new HashSet<>();
    private Set<Event> shownEvents = new HashSet<>();
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        filteredEvents = DataCache.getInstance().getEventsSet();
        filteredPersons = DataCache.getInstance().getPersonSet();

        searchBar = findViewById(R.id.search_text);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().toLowerCase();
                if(input.length() > 0){
                    search(input);
                    initRecyclerView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void search(String input){
        shownEvents.clear();
        shownPersons.clear();

        for(Person p: filteredPersons){
            if(personStringFound(p, input)) shownPersons.add(p);
        }
        for(Event e: filteredEvents){
            if(eventStringFound(e, input)) shownEvents.add(e);
        }
    }
    private boolean personStringFound(Person person, CharSequence s){
        String firstName = person.getFirstName().toLowerCase();
        String lastName = person.getLastName().toLowerCase();

        return firstName.contains(s) || lastName.contains(s);
    }
    private boolean eventStringFound(Event event, CharSequence s){
        String city = event.getCity().toLowerCase();
        String country = event.getCountry().toLowerCase();
        String eventType = event.getEventType().toLowerCase();
        String year = Integer.toString(event.getYear());

        return city.contains(s) ||
                country.contains(s) ||
                eventType.contains(s) ||
                year.contains(s);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.search_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(shownPersons, shownEvents, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private ArrayList<Object> entries = new ArrayList<>();
        private Context context;

        public RecyclerViewAdapter(Set<Person> persons, Set<Event> events, Context context) {
            entries.addAll(persons);
            entries.addAll(events);
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_person_list_item, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
            Object object = entries.get(i);
            Person person = null;
            Event event = null;
            if(object.getClass() == Person.class) person = (Person) object;
            else event = (Event) object;

            //PERSON
            if(event == null){
                Drawable genderIcon = null;
                switch (person.getGender()){
                    case "m": genderIcon = new IconDrawable(
                            SearchActivity.this,
                            FontAwesomeIcons.fa_male)
                            .colorRes(R.color.male_icon)
                            .sizeDp(40); break;
                    case "f": genderIcon = new IconDrawable(
                            SearchActivity.this,
                            FontAwesomeIcons.fa_female)
                            .colorRes(R.color.female_icon)
                            .sizeDp(40); break;
                }
                viewHolder.entryIcon.setImageDrawable(genderIcon);
                String fullName = person.getFirstName() + " " + person.getLastName();
                viewHolder.entryInfo.setText(fullName);
            }

            //EVENT
            else{
                person = DataCache.getInstance().getPerson(event.getPersonID());
                viewHolder.entryIcon.setImageDrawable(new IconDrawable(
                        SearchActivity.this,
                        FontAwesomeIcons.fa_map_marker)
                        .color(R.color.gray)
                        .sizeDp(40));
                String eventInfo = person.getFirstName() + " " + person.getLastName() + "'s " +
                        event.getEventType() + '\n' + event.getCity() + ", " + event.getCountry() +
                        ": " + event.getYear();
                viewHolder.entryInfo.setText(eventInfo);
            }

            viewHolder.infoLayout.setOnClickListener(new ClickListener(person, event));
        }

        private class ClickListener implements View.OnClickListener{
            Person person;
            Event event;

            ClickListener(Person person, Event event){
                this.person = person;
                this.event = event;
            }

            @Override
            public void onClick(View v) {
                if(event == null){
                    DataCache.getInstance().setClickedPerson(person);
                    Intent intent = new Intent(getBaseContext(), PersonActivity.class);
                    startActivity(intent);
                }
                else{
                    DataCache.getInstance().setClickedEvent(event);
                    DataCache.getInstance().setClickedPerson(person);
                    Intent intent = new Intent(getBaseContext(), EventActivity.class);
                    startActivity(intent);
                }
            }
        }

        @Override
        public int getItemCount() {
            return shownEvents.size() + shownPersons.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView entryIcon;
            TextView entryInfo;
            LinearLayout infoLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                entryIcon = itemView.findViewById(R.id.list_icon);
                entryInfo = itemView.findViewById(R.id.list_text);
                infoLayout = itemView.findViewById(R.id.info_layout);

            }
        }
    }

}
