package com.plamen.employees.registries;

import com.plamen.employees.enums.FileWorkDataType;
import com.plamen.employees.parsers.csv.CsvWorkDataParser;
import com.plamen.employees.parsers.FileWorkDataParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry class responsible for managing and providing the correct {@link FileWorkDataParser} implementation based on
 * the input file type.
 */
public class FileWorkDataParserRegistry {

    private final Map<FileWorkDataType, FileWorkDataParser> fileWorkDataParsers = new HashMap<>();

    public FileWorkDataParserRegistry() {
        fileWorkDataParsers.put(FileWorkDataType.CSV, new CsvWorkDataParser());
    }

    /**
     * Retrieves the appropriate {@link FileWorkDataParser} for the given file type.
     *
     * @param fileWorkDataType the type of the input file
     * @return the corresponding parser, or {@code null} if not supported
     */
    public FileWorkDataParser getFileWorkDataParser(final FileWorkDataType fileWorkDataType) {
        return fileWorkDataParsers.get(fileWorkDataType);
    }
}
