package ksr.pl.kw.service;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FileSerializationService {
    public void save(String filePath, Object serializable) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(serializable);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save current state", e);
        }
    }
}
