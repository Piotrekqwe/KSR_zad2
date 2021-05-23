package ksr.pl.kw.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class UiUtils {

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
