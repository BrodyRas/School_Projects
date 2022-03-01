package Utility;

import java.util.Objects;

public class Location {
    public Location(String country,
                    String city,
                    float latitude,
                    float longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String country, city;
    private float latitude, longitude;

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void print() {
        System.out.println(country + ", " + city + ":\nLAT=" + latitude + ", LONG=" + longitude + "\n");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Float.compare(location.getLatitude(), getLatitude()) == 0 &&
                Float.compare(location.getLongitude(), getLongitude()) == 0 &&
                Objects.equals(getCountry(), location.getCountry()) &&
                Objects.equals(getCity(), location.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getCity(), getLatitude(), getLongitude());
    }
}
