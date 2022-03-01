package Utility;

import java.util.Random;

public class LocationList {
    public LocationList() {
    }

    public Location[] data;

    /**
     * Returns a random Location
     *
     * @return a random location
     */
    public Location random() {
        return data[new Random().nextInt(data.length)];
    }
}
