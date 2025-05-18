package com.plamen.employees.dates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

/**
 * Utility class for parsing dates from strings using a set of common formats.
 * <p>
 * This class attempts to parse a date string against multiple supported formats and returns the
 * first successfully parsed {@link LocalDate}. If none match, an {@link IllegalArgumentException} is thrown.
 * </p>
 * Supported formats:
 * <ul>
 *     <li>yyyy-MM-dd</li>
 *     <li>dd-MM-yyyy</li>
 *     <li>yyyy/MM/dd</li>
 *     <li>dd/MM/yyyy</li>
 *     <li>dd MMM yyyy</li>
 *     <li>yyyy MMM dd</li>
 * </ul>
 */
public class DateFormatter {

    private static final List<DateTimeFormatter> SUPPORTED_DATE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy MMM dd", Locale.ENGLISH)
    );

    /**
     * Attempts to parse the given date string using the supported date formats.
     *
     * @param rawDate the raw date string to parse
     * @return the parsed {@link LocalDate}
     * @throws IllegalArgumentException if none of the supported formats match
     */
    public static LocalDate parseDate(final String rawDate) {
        for (final DateTimeFormatter formatter : SUPPORTED_DATE_FORMATTERS) {
            try {
                return LocalDate.parse(rawDate.trim(), formatter);
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + rawDate);
    }
}
