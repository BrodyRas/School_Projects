package com.example.familymapclient.Models;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Models.Event;
import Models.Person;
import Models.User;


public class DataCache {
    private static DataCache instance;
    private String serverPort, serverHost, authToken;
    private User user;

    private Set<Person> rawPersons = new HashSet<>();
    private Set<Event> rawEvents = new HashSet<>();
    private Set<Person> fatherSideMales = new HashSet<>();
    private Set<Person> fatherSideFemales = new HashSet<>();
    private Set<Person> motherSideMales = new HashSet<>();
    private Set<Person> motherSideFemales = new HashSet<>();
    private Set<Person> filteredPersons = new HashSet<>();
    private Set<Event> filteredEvents = new HashSet<>();
    private Map<String, Float> markerColors = new HashMap<>();

    private Map<String, Boolean> filters = new HashMap<>();
    private int lifeStoryColor, familyTreeColor, spouseColor, mapType;
    private boolean lifeStorySwitch, familyTreeSwitch, spouseSwitch, inEventActivity;
    private Marker masterClickedMarker = null;
    private Marker clickedMarker = null;
    private Person clickedPerson = null;
    private Event clickedEvent = null;
    private Map<String, Marker> myMarkers = new HashMap<>();

    private DataCache() {
        //Set filters that won't be given in event types
        filters.put("father", true);
        filters.put("mother", true);
        filters.put("male", true);
        filters.put("female", true);

        //Set initial values of settings page
        lifeStorySwitch = true;
        familyTreeSwitch = true;
        spouseSwitch = true;
        lifeStoryColor = 0;
        familyTreeColor = 1;
        spouseColor = 2;
        mapType = 0;

        //Not in Event Activity
        inEventActivity = false;
    }

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    public int getLifeStoryColor() {
        return lifeStoryColor;
    }
    public void setLifeStoryColor(int lifeStoryColor) {
        this.lifeStoryColor = lifeStoryColor;
    }

    public int getFamilyTreeColor() {
        return familyTreeColor;
    }
    public void setFamilyTreeColor(int familyTreeColor) {
        this.familyTreeColor = familyTreeColor;
    }

    public int getSpouseColor() {
        return spouseColor;
    }
    public void setSpouseColor(int spouseColor) {
        this.spouseColor = spouseColor;
    }

    public int getMapType() {
        return mapType;
    }
    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public boolean getLifeStorySwitch() {
        return lifeStorySwitch;
    }
    public void setLifeStorySwitch(boolean lifeStorySwitch) {
        this.lifeStorySwitch = lifeStorySwitch;
    }

    public boolean getFamilyTreeSwitch() {
        return familyTreeSwitch;
    }
    public void setFamilyTreeSwitch(boolean familyTreeSwitch) {
        this.familyTreeSwitch = familyTreeSwitch;
    }

    public boolean getSpouseSwitch() {
        return spouseSwitch;
    }
    public void setSpouseSwitch(boolean spouseSwitch) {
        this.spouseSwitch = spouseSwitch;
    }

    public String getServerPort() {
        return serverPort;
    }
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Marker getClickedMarker() {
        return clickedMarker;
    }
    public void setClickedMarker(Marker clickedMarker) {
        this.clickedMarker = clickedMarker;
    }

    public Marker getMasterClickedMarker() {
        return masterClickedMarker;
    }
    public void setMasterClickedMarker(Marker masterClickedMarker) {
        this.masterClickedMarker = masterClickedMarker;
    }

    public Person getClickedPerson() {
        return clickedPerson;
    }
    public void setClickedPerson(Person clickedPerson) {
        this.clickedPerson = clickedPerson;
    }

    public Event getClickedEvent() {
        return clickedEvent;
    }
    public void setClickedEvent(Event clickedEvent) {
        this.clickedEvent = clickedEvent;
    }

    public boolean isInEventActivity() {
        return inEventActivity;
    }
    public void setInEventActivity(boolean inEventActivity) {
        this.inEventActivity = inEventActivity;
    }

    public void setPersons(Person[] people) {
        rawPersons.addAll(Arrays.asList(people));
        filteredPersons = rawPersons;   //TODO: figure out what's going on here...
    }
    public void setEvents(Event[] events) {
        for (Event e : events) {
            rawEvents.add(e);
            String eventType = e.getEventType().toLowerCase();
            filters.put(eventType, true);
        }
        initializeParentalSideSets();
        updateFilters();
    }

    public Person getPerson(String personID) {
        for (Person p : filteredPersons) {
            if (p.getPersonID().matches(personID)) {
                return p;
            }
        }
        return null;
    }
    public Event getEvent(String eventID) {
        for (Event e : filteredEvents) {
            if (e.getEventID().matches(eventID)) {
                return e;
            }
        }
        return null;
    }

    public Set<Person> getPersonSet(){
        return filteredPersons;
    }
    public Set<Event> getEventsSet() {
        return filteredEvents;
    }

    public boolean getFilter(String key) {
        return filters.get(key);
    }
    public void setFilter(String key, boolean value) {
        filters.put(key, value);
        updateFilters();
    }
    public ArrayList<String> getFilterTitles() {
        ArrayList<String> result = new ArrayList<>();
        result.add("male");
        result.add("female");
        result.add("father");
        result.add("mother");

        for (String s : filters.keySet()) {
            if (!s.matches("male") &&
                    !s.matches("female") &&
                    !s.matches("father") &&
                    !s.matches("mother")) {
                result.add(s);
            }
        }

        return result;
    }
    public void updateFilters() {
        filteredPersons.clear();
        filteredEvents.clear();

        if (filters.get("male")) {
            addFilteredElements("father", fatherSideMales);
            addFilteredElements("mother", motherSideMales);
        }
        if (filters.get("female")) {
            addFilteredElements("father", fatherSideFemales);
            addFilteredElements("mother", motherSideFemales);
        }
    }
    private void addFilteredElements(String filterMatch, Set<Person> personSet) {
        if (filters.get(filterMatch)) {
            filteredPersons.addAll(personSet);
            for (Person p : personSet) {
                for (Event e : rawEvents) {
                    if (e.getPersonID().matches(p.getPersonID())) {
                        String eventType = e.getEventType().toLowerCase();
                        if (filters.get(eventType)) {
                            filteredEvents.add(e);
                        }
                    }
                }
            }
        }
    }

    private void initializeParentalSideSets() {
        Person userPerson = getPerson(user.getPersonID());
        Person userFather = getPerson(userPerson.getFather());
        Person userMother = getPerson(userPerson.getMother());

        motherSideMales.add(userPerson);
        fatherSideMales.add(userPerson);

        findAncestors(userFather, fatherSideMales, fatherSideFemales);
        findAncestors(userMother, motherSideMales, motherSideFemales);
    }
    private void findAncestors(Person person, Set<Person> maleSet, Set<Person> femaleSet) {
        if (person.getGender().matches("m")) maleSet.add(person);
        else femaleSet.add(person);

        if (person.getFather() != null || person.getMother() != null) {
            Person father = getPerson(person.getFather());
            Person mother = getPerson(person.getMother());

            if (father != null) {
                findAncestors(father, maleSet, femaleSet);
            }
            if (mother != null) {
                findAncestors(mother, maleSet, femaleSet);
            }
        }
    }

    public Event getEarliestEvent(String personID) {
        //Get all events associated with the person
        Set<Event> events = new HashSet<>();
        for (Event e : filteredEvents) {
            if (e.getPersonID().matches(personID)) {
                events.add(e);
            }
        }
        //Find the event with the earliest year, return
        Event min = null;
        for (Event e : events) {
            if (min == null || e.getYear() < min.getYear()) {
                min = e;
            }
        }
        return min;
    }
    public List<Event> getLifeStoryEvents(String personID) {
        List<Event> lifeStory = new ArrayList<>();

        for (Event e : filteredEvents) {
            if (e.getPersonID().matches(personID)) {
                lifeStory.add(e);
            }
        }

        Collections.sort(lifeStory);

        return lifeStory;
    }
    public List<Person> getAssociatedPersons(String personID){
        List<Person> persons = new ArrayList<>();
        Person person = getPerson(personID);

        for(Person p: filteredPersons){
            //Child found
            if(p.getFather() != null && p.getFather().matches(personID)) persons.add(p);
            else if(p.getMother() != null && p.getMother().matches(personID)) persons.add(p);

            //Parent found
            else if(person.getFather() != null && person.getFather().matches(p.getPersonID())) persons.add(p);
            else if(person.getMother() != null && person.getMother().matches(p.getPersonID())) persons.add(p);

            //Spouse found
            else if(p.getSpouse() != null && p.getSpouse().matches(personID)) persons.add(p);
        }

        return persons;
    }

    public Marker getMyMarker(String eventID){return myMarkers.get(eventID);}
    public void putMyMarker(String eventID, Marker marker){myMarkers.put(eventID, marker);}

    public Map<String, Float> getMarkerColors() {
        return markerColors;
    }

    public void clear() {
        instance = null;
    }
}
