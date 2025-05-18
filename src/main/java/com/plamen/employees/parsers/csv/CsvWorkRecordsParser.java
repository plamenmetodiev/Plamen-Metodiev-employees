package com.plamen.employees.parsers.csv;

import com.plamen.employees.dto.WorkRecord;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.plamen.employees.dates.DateFormatter.parseDate;

/**
 * Parses raw CSV row data into structured {@link WorkRecord} instances.
 */
public class CsvWorkRecordsParser {

    /**
     * Converts a list of CSV row arrays into a list of {@link WorkRecord} objects.
     * <p>
     * Empty rows are skipped. The "DateTo" field can be "NULL", which is treated as today's date.
     *
     * @param rows list of string arrays from CSV rows
     * @return list of valid {@link WorkRecord}
     */
    public static List<WorkRecord> createWorkRecords(final List<String[]> rows) {
        final List<WorkRecord> records = new ArrayList<>();
        final LocalDate today = LocalDate.now();

        for (final String[] row : rows) {
            if(StringUtils.isAnyEmpty(row)){
                continue;
            }

            final WorkRecord workRecord = getWorkRecord(row, today);
            records.add(workRecord);
        }

        return records;
    }

    private static WorkRecord getWorkRecord(String[] row, LocalDate today) {
        final int empId = Integer.parseInt(row[0].trim());
        final int projectId = Integer.parseInt(row[1].trim());
        final LocalDate from = parseDate(row[2].trim());
        final LocalDate to = getToDate(row[3], today);

        return new WorkRecord(empId, projectId, from, to);
    }

    private static LocalDate getToDate(final String rawDate, final LocalDate today) {
        return rawDate.trim().equalsIgnoreCase("null")
                ? today
                : parseDate(rawDate);
    }
}
