package com.plamen.employees.parsers.csv;

import com.plamen.employees.dto.WorkRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.plamen.employees.parsers.csv.CsvWorkRecordsParser.createWorkRecords;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Unit tests for {@link CsvWorkRecordsParser}
 */
public class CsvWorkRecordsParserTest {

    @Test
    void createWorkRecords_shouldCreateWorkRecords() {
        final List<String[]> input = List.of(
                new String[]{"143", "12", "2013-11-01", "2014-01-05"},
                new String[]{"218", "10", "2012-05-16", "2014-03-03"}
        );

        final List<WorkRecord> workRecords = createWorkRecords(input);

        assertThat(workRecords).hasSize(2);

        final WorkRecord first = workRecords.getFirst();
        assertThat(first.empId()).isEqualTo(143);
        assertThat(first.projectId()).isEqualTo(12);
        assertThat(first.dateFrom()).isEqualTo(LocalDate.of(2013, 11, 1));
        assertThat(first.dateTo()).isEqualTo(LocalDate.of(2014, 1, 5));
    }

    @Test
    void createWorkRecords_whenInvalidData_shouldThrowException() {
        final List<String[]> input = List.of(
                new String[]{"143", "12", "15-022-2022-2", "2014-01-05"},
                new String[]{"218", "10", "2012-13-02", "2014-03-03"}
        );

        assertThatCode(() -> createWorkRecords(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported date format: 15-022-2022-2");
    }

    @Test
    void createWorkRecords_whenDateToNull_shouldParseAsToday() {
        final List<String[]> input = List.of(
                new String[] {"143", "12", "2013-11-01", "NULL"},
                new String[] {"120", "12", "2015-11-01", "NULL"}
        );

        final List<WorkRecord> workRecords = createWorkRecords(input);

        assertThat(workRecords).hasSize(2);

        final WorkRecord workRecord = workRecords.getFirst();
        assertThat(workRecord.dateTo()).isEqualTo(LocalDate.now());
    }

    @Test
    void createWorkRecords_whenEmptyElementsInRow_shouldSkip() {
        final List<String[]> input = List.of(
                new String[]{"143", "12", "2013-11-01", "2014-01-05"},
                new String[]{"2", "12", "2013-11-01", ""},
                new String[]{"218", "10", "2012-05-16", "NULL"}
        );

        final List<WorkRecord> workRecords = createWorkRecords(input);

        assertThat(workRecords).hasSize(2);
    }

    @Test
    void createWorkRecords_whenInputEmpty_shouldReturnEmptyList() {
        final List<String[]> input = List.of();

        final List<WorkRecord> workRecords = createWorkRecords(input);

        assertThat(workRecords).isEmpty();
    }
}