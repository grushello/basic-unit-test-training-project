package lv.bootcamp.shelter.stretch;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Stretch goal: Testing file output
 *
 * Practice:
 * - Writing to temp files and reading them back
 * - String content assertions
 * - Cleanup with Files.deleteIfExists
 *
 * Instructions:
 * These tests verify that AnimalReportWriter produces correct output.
 * This task is optional — attempt it after completing tasks 1–6.
 */
@DisplayName("AnimalReportWriter (stretch)")
class AnimalReportWriterTest {

    private final AnimalReportWriter writer = new AnimalReportWriter();

    @Test
    @DisplayName("writes report file that contains total count")
    void shouldWriteTotalCount() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 10)),
                new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 11)),
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 12))
        );
        Path output = Files.createTempFile("report-test", ".txt");

        try {
            writer.writeReport(animals, output);

            String content = Files.readString(output, StandardCharsets.UTF_8);

            assertTrue(content.contains("Total animals: 3"));
        } finally {
            Files.deleteIfExists(output);
        }
    }

    @Test
    @DisplayName("writes per-species breakdown in alphabetical order")
    void shouldWriteSpeciesBreakdown() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 10)),
                new Animal("Max", "Dog", 5, true, LocalDate.of(2026, 1, 11)),
                new Animal("Luna", "Cat", 2, false, LocalDate.of(2026, 1, 12)),
                new Animal("Bella", "Cat", 1, false, LocalDate.of(2026, 1, 5))
        );

        Path output = Files.createTempFile("report-test", ".txt");

        try {
            writer.writeReport(animals, output);

            String content = Files.readString(output, StandardCharsets.UTF_8);

            assertTrue(content.indexOf("Cat:") < content.indexOf("Dog:"));

            assertTrue(content.contains("2 vaccinated"));
        } finally {
            Files.deleteIfExists(output);
        }
    }

    @Test
    @DisplayName("writes oldest animal per species")
    void shouldWriteOldestPerSpecies() throws IOException {
            List<Animal> animals = List.of(
                    new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 10)),
                    new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 11)),
                    new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 12)),
                    new Animal("Bella", "Cat", 1, true, LocalDate.of(2026, 1, 5))
            );

            Path output = Files.createTempFile("report-test", ".txt");

            try {
                writer.writeReport(animals, output);

                String content = Files.readString(output, StandardCharsets.UTF_8);

                assertTrue(content.contains("Dog: Max (age 5)"));
            } finally {
                Files.deleteIfExists(output);
            }
        }
}
