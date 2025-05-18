package com.plamen.employees.dto;

/**
 * Represents a pair of employee IDs who have worked together on a project.
 * <p>
 * The pair is always stored in ascending order (i.e., emp1 &lt;= emp2)
 * to ensure consistent keying and equality checks in collections.
 *
 * @param emp1 the first employee ID
 * @param emp2 the second employee ID
 */
public record EmployeePair(long emp1, long emp2) {
    public EmployeePair {
        if (emp1 > emp2) {
            long tmp = emp1;
            emp1 = emp2;
            emp2 = tmp;
        }
    }
}
