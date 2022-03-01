import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Array2D array1 = new Array2D(2,2);
        array1.set(0,0,10);
        array1.set(1,1,10);
        array1.save("/Users/brody/Documents/IntelliJ/LazyLoading/holder/test.txt");

        Array2D array2 = new Array2D(2,2);
        array2.load("/Users/brody/Documents/IntelliJ/LazyLoading/holder/test.txt");
        array2.get(1,1);
    }
}
