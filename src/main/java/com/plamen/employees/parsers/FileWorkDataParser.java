package com.plamen.employees.parsers;

import com.plamen.employees.dto.WorkRecord;

import java.util.List;

/**
 * Interface defining a parser for employee work data from a specific file format.
 */
public interface FileWorkDataParser {

    /**
     * Parses the specified file and returns a list of work records.
     *
     * @param fileName the name of the file to parse (must be available in the classpath)
     * @return a list of {@link WorkRecord} parsed from the file
     */
    List<WorkRecord> getWorkRecords(String fileName);

}
