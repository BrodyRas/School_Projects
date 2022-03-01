package Utility;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;

public class JsonObjectSerializer {
    public JsonObjectSerializer() {
    }

    public static <T> T decode(File file, Class<T> klass) throws Exception {
        Object result;
        try (FileReader fileReader = new FileReader(file)) {
            Gson gson = new Gson();
            result = gson.fromJson(fileReader, klass);
        }
        return (T) result;
    }
}