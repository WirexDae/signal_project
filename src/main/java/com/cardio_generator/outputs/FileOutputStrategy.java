package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An {@link OutputStrategy} implementation that outputs patient data to files.
 * The {@code FileOutputStrategy} class is responsible for printing patien related data
 * to a file on a given base directory.
 */
public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name to lowerCamelCase
    private String baseDirectory;

    // Changed variable name to lowerCamelCase, also made it private for encapsulation
    private final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@code FileOutputStrategy}.
     *
     * @param baseDirectory the directory where output files will be created.
     */
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes patient data to the given file.
     * Creates the directory if it isn't already initialized.
     *
     * @param patientId identifier of a patient.
     * @param timestamp the time at which the data was generated.
     * @param label a label indicating the type of data.
     * @param data the actual data value as a string.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {

        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        }

        catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // Line-wrapping for clarity
        String FilePath =
                fileMap.computeIfAbsent(label, k ->
                        Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        // Line-wrapping for clarity
        try (PrintWriter out =
                     new PrintWriter(Files.newBufferedWriter(Paths.get(FilePath),
                             StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                    patientId, timestamp, label, data);
        }
        // Leave spacing between methods
        catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}