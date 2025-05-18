package com.plamen.employees.readers;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.sun.tools.javac.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * A class responsible for reading and parsing CSV files into raw data rows.
 */
public class CsvReader {

    /**
     * Reads the contents of a CSV file from the classpath and returns a list of string arrays.
     *
     * @param filePath   the name of the file to read (must exist in the classpath)
     * @param separator  the delimiter used in the CSV file
     * @param hasHeader  whether the first line of the file should be skipped as a header
     * @return list of rows, where each row is a string array of values
     */
    public static List<String[]> readCSV(final String filePath, final char separator, final boolean hasHeader) {
        final InputStreamReader streamReader = createInputStreamReader(filePath);
        final CSVReader csvReader = createCsvReader(streamReader, separator, hasHeader);

        return readCsvRows(csvReader);
    }

    private static InputStreamReader createInputStreamReader(final String filePath) {
        final ClassLoader classLoader = Main.class.getClassLoader();
        final InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(filePath));
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

    private static CSVReader createCsvReader(final InputStreamReader streamReader, final char separator, final boolean hasHeader) {
        final CSVParser parser = new CSVParserBuilder()
                .withSeparator(separator)
                .build();

        final int skipLines = hasHeader ? 1 : 0;

        return new CSVReaderBuilder(streamReader)
                .withSkipLines(skipLines)
                .withCSVParser(parser)
                .build();
    }

    private static List<String[]> readCsvRows(CSVReader csvReader) {
        final List<String[]> rows;
        try {
            rows = csvReader.readAll();
            csvReader.close();
        }
        catch (final IOException | CsvException e) {
            throw new RuntimeException("Could not process CSV file.");
        }
        return rows;
    }
}
