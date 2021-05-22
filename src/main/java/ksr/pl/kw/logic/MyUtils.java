package ksr.pl.kw.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MyUtils {

    public static String[][] readFromFile(String dirPath, String fileName, String separator){
        String[] rows;
        String[][] values = null;
        try {
            rows = Files.readString(Paths.get(dirPath + fileName)).split("[" + "\n" + "]");
            values = Arrays.stream(rows).skip(1).map(row -> row.split("[" + separator + "]")).toArray(String[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }
}
