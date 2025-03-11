package org.kla.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kla.dto.ComputationJob;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class FileService {

    public static final String JSON_EXTENSION = ".json";
    @Inject
    ObjectMapper objectMapper;

    public void saveToFile(ComputationJob computationJob) {
        LocalDateTime now = LocalDateTime.now();
        String dateNow = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        File file = new File("src/main/resources/jobs/" + dateNow + ".json");
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, computationJob);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ComputationJob readFromFile(String filename) {
        if (!filename.endsWith(JSON_EXTENSION)) {
            filename = filename + JSON_EXTENSION;
        }

        File file = new File("src/main/resources/jobs/" + filename);
        try {
            return objectMapper.readValue(file, ComputationJob.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> listFiles() {
        return Stream.of(new File("src/main/resources/jobs/").listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .map(this::removeExtension)
                .collect(Collectors.toSet());
    }

    private String removeExtension(final String s) {
        return s != null && s.lastIndexOf(".") > 0 ? s.substring(0, s.lastIndexOf(".")) : s;
    }
}
