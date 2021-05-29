package ksr.pl.kw.service;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileDeserializationService {
    public Optional<Object> load(String path) {
        if (Files.exists(Path.of(path))) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
                return Optional.ofNullable(in.readObject());
            } catch (Exception e) {
                throw new IllegalStateException("Unable to load from file", e);
            }
        }
        return Optional.empty();
    }
}
