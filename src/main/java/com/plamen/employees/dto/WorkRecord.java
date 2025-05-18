package com.plamen.employees.dto;

import java.time.LocalDate;

/**
 * A data transfer object (DTO) representing an employee's participation in a project over a specific time period.
 *
 * @param empId     the unique identifier of the employee
 * @param projectId the unique identifier of the project
 * @param dateFrom  the start date when the employee began working on the project
 * @param dateTo    the end date when the employee stopped working on the project or represent today's date if the
 *                  employee is still active on the project
 */
public record WorkRecord(long empId, long projectId, LocalDate dateFrom, LocalDate dateTo) {
}
