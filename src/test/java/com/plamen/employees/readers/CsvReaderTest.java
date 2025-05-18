package com.plamen.employees.readers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.plamen.employees.readers.CsvReader.readCSV;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Unit tests for {@link CsvReader}
 */
public class CsvReaderTest {

    private static final String WORK_DATA = "work-data/";

    @Test
    void readCSV_whenHeaderTrue_shouldSkipFirstRow() {
        final List<String[]> rows = readCSV(WORK_DATA + "work-data-header.csv", ',', true);

        assertThat(rows.size()).isEqualTo(2);
        assertThat(rows.get(0)).isEqualTo(new String[]{"143", "12", "2013-11-01", "2014-01-05"});
        assertThat(rows.get(1)).isEqualTo(new String[]{"218", "10", "2012-05-16", "NULL"});
    }

    @Test
    void readCSV_whenHeaderFalse_shouldReadAllRows() {
        final List<String[]> rows = readCSV(WORK_DATA + "work-data-without-header.csv", ',', false);

        assertThat(rows.size()).isEqualTo(2);
        assertThat(rows.get(0)).isEqualTo(new String[]{"143", "12", "2013-11-01", "2014-01-05"});
        assertThat(rows.get(1)).isEqualTo(new String[]{"218", "10", "2012-05-16", "NULL"});
    }

    @Test
    void readCSV_shouldReadRowsWithDifferentSeparator() {
        final List<String[]> rows = readCSV(WORK_DATA + "work-data-semicolon.csv", ';', true);

        assertThat(rows.size()).isEqualTo(1);
        assertThat(rows.getFirst()).isEqualTo(new String[]{"143", "12", "2013-11-01", "2014-01-05"});
    }

    @Test
    void readCSV_whenCsvMalformed_shouldThrowException() {
        assertThatCode(() -> readCSV(WORK_DATA + "malformed.csv", ',', false))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Could not process CSV file.");
    }
}