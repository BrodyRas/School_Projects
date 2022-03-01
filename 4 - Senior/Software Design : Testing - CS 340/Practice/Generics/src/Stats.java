public class Stats<T extends Comparable> {
    public T min, max;

    public Stats(T min, T max) {
        this.min = min;
        this.max = max;
    }
}
