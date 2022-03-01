public class Maximizer<T extends Comparable>{
    private T value;

    public void updateValue(T val){
        if(value == null || val.compareTo(value) > 0){value = val;}
    }

    public T getValue(){
        return value;
    }
}
