package com.plamen.employees.calculators;

import com.plamen.employees.dto.EmployeePair;
import com.plamen.employees.dto.WorkRecord;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class that calculates the total overlapping working days between pairs of employees who have worked on the same
 * project during overlapping periods.
 */
public class EmployeeOverlapCalculator {

    /**
     * Computes the total number of overlapping days for each unique pair of employees who have worked together on the
     * same project.
     * <p>
     * Overlaps are calculated per project and summed across all projects.
     *
     * @param records list of {@link WorkRecord} entries representing employee project assignments
     * @return a map where each key is a unique {@link EmployeePair} and the value is the total overlap duration in days
     */
    public static Map<EmployeePair, Long> computeOverlaps(List<WorkRecord> records) {
        final Map<EmployeePair, Long> overlapMap = new HashMap<>();

        final Map<Long, List<WorkRecord>> projects = records.stream()
                .collect(Collectors.groupingBy(WorkRecord::projectId));

        for (final var entry : projects.entrySet()) {
            final List<WorkRecord> projectRecords = entry.getValue();
            for (int i = 0; i < projectRecords.size(); i++) {
                for (int j = i + 1; j < projectRecords.size(); j++) {
                    final WorkRecord r1 = projectRecords.get(i);
                    final WorkRecord r2 = projectRecords.get(j);

                    if (r1.empId() != r2.empId()) {
                        final LocalDate overlapStart = Collections.max(List.of(r1.dateFrom(), r2.dateFrom()));
                        final LocalDate overlapEnd = Collections.min(List.of(r1.dateTo(), r2.dateTo()));
                        if (!overlapEnd.isBefore(overlapStart)) {
                            final long days = ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;
                            final EmployeePair pair = new EmployeePair(r1.empId(), r2.empId());
                            overlapMap.merge(pair, days, Long::sum);
                        }
                    }
                }
            }
        }
        return overlapMap;
    }

    /**
     * Finds the employee pair with the maximum total overlap duration.
     *
     * @param overlapMap a map of employee pairs to their total overlapping days
     * @return an {@link Optional} containing the pair with the longest overlap, or empty if the map is empty
     */
    public static Optional<Map.Entry<EmployeePair, Long>> findMaxPair(final Map<EmployeePair, Long> overlapMap) {
        return overlapMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }
}
