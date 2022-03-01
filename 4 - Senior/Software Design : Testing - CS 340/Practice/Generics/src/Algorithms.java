import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Algorithms {

    public static <T extends Comparable> Stats<T> calcStats(T[] array){
        ArrayList<T> arrayList = new ArrayList(Arrays.asList(array));
        Collections.sort(arrayList);

        return new Stats<T>(arrayList.get(0), arrayList.get(arrayList.size()-1));
    }

}
