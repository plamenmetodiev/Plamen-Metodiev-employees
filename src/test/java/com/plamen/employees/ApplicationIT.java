package com.plamen.employees;

import com.plamen.employees.dto.EmployeePair;
import com.plamen.employees.dto.WorkRecord;
import com.plamen.employees.registries.FileWorkDataParserRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.plamen.employees.calculators.EmployeeOverlapCalculator.computeOverlaps;
import static com.plamen.employees.calculators.EmployeeOverlapCalculator.findMaxPair;
import static com.plamen.employees.enums.FileWorkDataType.CSV;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationIT {

    @Test
    void mainFlow_whenValidCsv_shouldReturnMaxOverlappingPair() {
        final FileWorkDataParserRegistry registry = new FileWorkDataParserRegistry();

        final List<WorkRecord> records =
                registry.getFileWorkDataParser(CSV).getWorkRecords("work-data/work-data-it.csv");

        final Map<EmployeePair, Long> overlaps = computeOverlaps(records);

        assertThat(overlaps).containsExactlyInAnyOrderEntriesOf(Map.of(
                new EmployeePair(1, 2), 32L,
                new EmployeePair(1, 3), 15L,
                new EmployeePair(1, 4), 18L,
                new EmployeePair(2, 3), 30L,
                new EmployeePair(2, 4), 20L,
                new EmployeePair(3, 4), 6L
        ));

        final Optional<Map.Entry<EmployeePair, Long>> optionalMax = findMaxPair(overlaps);

        assertThat(optionalMax).isPresent();

        final Map.Entry<EmployeePair, Long> result = optionalMax.get();
        final EmployeePair pair = result.getKey();
        final Long totalDays = result.getValue();

        assertThat(pair).isEqualTo(new EmployeePair(1, 2));
        assertThat(totalDays).isEqualTo(32L);
    }
}