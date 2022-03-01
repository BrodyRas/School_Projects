package Utility;

import java.util.Random;

public class NameList {
    public NameList() {
    }

    public String[] data;

    /**
     * Returns a random name from the list of male first names
     *
     * @return a random male name
     */
    public String random() {
        return data[new Random().nextInt(data.length)];
    }
}
