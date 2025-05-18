package com.plamen.employees.parsers.csv;

import com.plamen.employees.parsers.FileWorkDataParser;
import com.plamen.employees.readers.CsvReader;
import com.plamen.employees.dto.WorkRecord;

import java.util.List;

/**
 * CSV-specific implementation of {@link FileWorkDataParser}.
 */
public class CsvWorkDataParser implements FileWorkDataParser {

    // These can be configured in application.properties file.
    private static final char SEPARATOR = ',';
    private static final boolean HAS_HEADER = false;


    @Override
    public List<WorkRecord> getWorkRecords(final String fileName) {
        final var rows = CsvReader.readCSV(fileName, SEPARATOR, HAS_HEADER);
        return CsvWorkRecordsParser.createWorkRecords(rows);
    }

}
