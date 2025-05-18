package com.plamen.employees;

import com.plamen.employees.dto.EmployeePair;
import com.plamen.employees.dto.WorkRecord;
import com.plamen.employees.registries.FileWorkDataParserRegistry;

import java.util.List;
import java.util.Map;

import static com.plamen.employees.calculators.EmployeeOverlapCalculator.computeOverlaps;
import static com.plamen.employees.calculators.EmployeeOverlapCalculator.findMaxPair;
import static com.plamen.employees.enums.FileWorkDataType.CSV;

public class Application {

    public static void main(final String[] args) {

        final FileWorkDataParserRegistry fileWorkDataParserRegistry = new FileWorkDataParserRegistry();

        final List<WorkRecord> records =
                fileWorkDataParserRegistry.getFileWorkDataParser(CSV).getWorkRecords("work-data.csv");

        final Map<EmployeePair, Long> overlaps = computeOverlaps(records);
        final var optionalMaxPair = findMaxPair(overlaps);

        if (optionalMaxPair.isPresent()) {
            final var maxPair = optionalMaxPair.get();
            System.out.printf("%d, %d, %d%n",
                    maxPair.getKey().emp1(),
                    maxPair.getKey().emp2(),
                    maxPair.getValue());
        } else {
            System.out.println("No overlapping work periods found.");
        }
    }
}
