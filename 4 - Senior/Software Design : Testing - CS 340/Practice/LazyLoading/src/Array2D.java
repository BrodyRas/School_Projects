import java.io.*;

public class Array2D implements Array2DInterface, Serializable {
    private int mat[][];

    public Array2D(int row, int col){
        mat = new int[row][col];
    }

    @Override
    public void set(int row, int col, int val) {
        mat[row][col] = val;
    }

    @Override
    public int get(int row, int col) {
        return mat[row][col];
    }

    public void save(String fileName) throws IOException {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            out.writeObject(this);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void load(String fileName){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(fileName)));
            Array2D newArray = (Array2D) in.readObject();
            this.mat = newArray.mat;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
