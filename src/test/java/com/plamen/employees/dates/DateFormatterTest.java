package com.plamen.employees.dates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.plamen.employees.dates.DateFormatter.parseDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class DateFormatterTest {

    @Test
    void parseDate_whenEmptyOrInvalid_shouldThrowException() {
        final String date = "invalid-date";

        assertThatCode(() -> parseDate(date))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported date format: " + date);
    }

    @ParameterizedTest
    @MethodSource("dateStrings")
    void parseDate_whenValidFormat_shouldReturnLocalDate(final String rawDate) {
        final LocalDate EXPECTED_DATE = LocalDate.of(2024, 5, 18);

        final LocalDate result = parseDate(rawDate);

        assertThat(result).isEqualTo(EXPECTED_DATE);
    }

    private static Stream<String> dateStrings() {
        return Stream.of(
                "2024-05-18",     // yyyy-MM-dd
                "18-05-2024",     // dd-MM-yyyy
                "2024/05/18",     // yyyy/MM/dd
                "18/05/2024",     // dd/MM/yyyy
                "18 May 2024",    // dd MMM yyyy
                "18 May 2024"    // yyyy MMM dd
        );
    }
}