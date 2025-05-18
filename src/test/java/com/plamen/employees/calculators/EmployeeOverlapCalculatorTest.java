package com.plamen.employees.calculators;

import com.plamen.employees.dto.EmployeePair;
import com.plamen.employees.dto.WorkRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.plamen.employees.calculators.EmployeeOverlapCalculator.computeOverlaps;
import static com.plamen.employees.calculators.EmployeeOverlapCalculator.findMaxPair;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link EmployeeOverlapCalculator}
 */
public class EmployeeOverlapCalculatorTest {

    @Test
    void computeOverlaps_whenNoOverlap_shouldReturnEmptyMap() {
        final List<WorkRecord> records = List.of(
                new WorkRecord(1, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1)),
                new WorkRecord(2, 100, LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 1))
        );

        final Map<EmployeePair, Long> result = computeOverlaps(records);

        assertThat(result).isEmpty();
    }

    @Test
    void computeOverlaps_whenOneOverlap_shouldReturnCorrectDays() {
        final List<WorkRecord> records = List.of(
                new WorkRecord(1, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 3, 1)),
                new WorkRecord(2, 100, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 4, 1))
        );

        final Map<EmployeePair, Long> result = computeOverlaps(records);

        final EmployeePair expectedEmployeePair = new EmployeePair(1, 2);
        assertThat(result).containsOnlyKeys(expectedEmployeePair);
        assertThat(result.get(expectedEmployeePair)).isEqualTo(30L); // Feb 1 to Mar 1 inclusive
    }

    @Test
    void computeOverlaps_whenMultipleEmployees_shouldAccumulateOverlap() {
        final List<WorkRecord> records = List.of(
                new WorkRecord(1, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 3, 1)),
                new WorkRecord(2, 100, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 4, 1)),
                new WorkRecord(3, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 15)),
                new WorkRecord(1, 200, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new WorkRecord(2, 200, LocalDate.of(2021, 1, 15), LocalDate.of(2021, 2, 15))
        );

        final Map<EmployeePair, Long> result = computeOverlaps(records);

        final EmployeePair pair1 = new EmployeePair(1, 2);
        final EmployeePair pair2 = new EmployeePair(1, 3);

        assertThat(result).containsOnlyKeys(pair1, pair2);
        assertThat(result.get(pair1)).isEqualTo(30L + 18L); // Two overlaps summed
        assertThat(result.get(pair2)).isEqualTo(15L);
    }

    @Test
    void computeOverlaps_whenEmployeeHasWorkedOnSameProjectMultipleTimes_shouldAccumulateOverlap() {
        final List<WorkRecord> records = List.of(
                new WorkRecord(1, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 3, 1)),
                new WorkRecord(2, 100, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 14)),

                // Pause on project 2 for 10 days. No overlap with Employee 1
                new WorkRecord(2, 200, LocalDate.of(2020, 2, 15), LocalDate.of(2020, 2, 24)),
                new WorkRecord(2, 100, LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 1)),

                new WorkRecord(1, 200, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new WorkRecord(2, 200, LocalDate.of(2021, 1, 15), LocalDate.of(2021, 2, 15))
        );

        final Map<EmployeePair, Long> result = computeOverlaps(records);

        final EmployeePair pair = new EmployeePair(1, 2);
        assertThat(result).containsOnlyKeys(pair);
        assertThat(result.get(pair)).isEqualTo(20L + 18L); // Two overlaps summed (2020-02-01 to 2020-02-14) + (2020-2-25 to 2020-03-01) + (2021-01-15 to 2021-02-01)
    }

    @Test
    void computeOverlaps_whenSameEmployee_shouldIgnore() {
        final List<WorkRecord> records = List.of(
                new WorkRecord(1, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1)),
                new WorkRecord(1, 100, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 2, 15))
        );

        final Map<EmployeePair, Long> result = computeOverlaps(records);

        assertThat(result).isEmpty();
    }

    @Test
    void computeOverlaps_whenExactSamePeriod_shouldReturnFullDuration() {
        final List<WorkRecord> records = List.of(
                new WorkRecord(1, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31)),
                new WorkRecord(2, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31))
        );

        final Map<EmployeePair, Long> result = computeOverlaps(records);

        final EmployeePair pair = new EmployeePair(1, 2);
        assertThat(result.get(pair)).isEqualTo(31L);
    }

    @Test
    void findMaxPair_whenMapIsEmpty_shouldReturnEmptyOptional() {
        final Map<EmployeePair, Long> input = Map.of();

        final Optional<Map.Entry<EmployeePair, Long>> result = findMaxPair(input);

        assertThat(result).isEmpty();
    }

    @Test
    void findMaxPair_whenSingleEntry_shouldReturnThatEntry() {
        final EmployeePair pair = new EmployeePair(1, 2);
        final Map<EmployeePair, Long> input = Map.of(pair, 15L);

        final Optional<Map.Entry<EmployeePair, Long>> result = findMaxPair(input);

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo(pair);
        assertThat(result.get().getValue()).isEqualTo(15L);
    }

    @Test
    void findMaxPair_whenMultipleEntries_shouldReturnEntryWithMaxValue() {
        final EmployeePair pair1 = new EmployeePair(1, 2);
        final EmployeePair pair2 = new EmployeePair(3, 4);
        final EmployeePair pair3 = new EmployeePair(5, 6);

        final Map<EmployeePair, Long> input = Map.of(
                pair1, 10L,
                pair2, 25L,
                pair3, 5L
        );

        final Optional<Map.Entry<EmployeePair, Long>> result = findMaxPair(input);

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo(pair2);
        assertThat(result.get().getValue()).isEqualTo(25L);
    }

    @Test
    void findMaxPair_whenMultipleMaxValues_shouldReturnOneOfThem() {
        final EmployeePair pair1 = new EmployeePair(1, 2);
        final EmployeePair pair2 = new EmployeePair(2, 3);
        final EmployeePair pair3 = new EmployeePair(3, 4);

        final Map<EmployeePair, Long> input = Map.of(
                pair1, 30L,
                pair2, 20L,
                pair3, 30L
        );

        final Optional<Map.Entry<EmployeePair, Long>> result = findMaxPair(input);

        assertThat(result).isPresent();
        assertThat(result.get().getValue()).isEqualTo(30L);
        assertThat(result.get().getKey()).isIn(pair1, pair3);
    }

}