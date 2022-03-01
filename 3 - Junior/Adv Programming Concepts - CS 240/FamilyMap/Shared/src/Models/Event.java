package Models;

import java.util.Objects;

public class Event implements Comparable<Event>{
    private String eventType, personID, city, country, eventID, descendant;
    private float latitude, longitude;
    private int year;

    public Event(String eventID, String descendant, String personID, String country, String city, float latitude, float longitude, String eventType, int year) {
        this.eventType = eventType;
        this.personID = personID;
        this.city = city;
        this.country = country;
        this.eventID = eventID;
        this.descendant = descendant;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPersonID() {
        return personID;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEventID() {
        return eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Float.compare(event.getLatitude(), getLatitude()) == 0 &&
                Float.compare(event.getLongitude(), getLongitude()) == 0 &&
                getYear() == event.getYear() &&
                Objects.equals(getEventType(), event.getEventType()) &&
                Objects.equals(getPersonID(), event.getPersonID()) &&
                Objects.equals(getCity(), event.getCity()) &&
                Objects.equals(getCountry(), event.getCountry()) &&
                Objects.equals(getEventID(), event.getEventID()) &&
                Objects.equals(getDescendant(), event.getDescendant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventType(), getPersonID(), getCity(), getCountry(), getEventID(), getDescendant(), getLatitude(), getLongitude(), getYear());
    }

    @Override
    public int compareTo(Event e) {
        int year = e.getYear();
        return this.year-year;
    }
}
