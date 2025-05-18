# Plamen Metodiev - Employee Project Overlap Calculator

## 📝 Description

This application identifies the **pair of employees who have worked together on common projects for the longest total period of time**.

Given a CSV file containing project assignment data, the app:

1. Parses the file into structured records.
2. Computes overlaps between all employee pairs working on the same projects.
3. Aggregates the total overlap in days for each pair.
4. Outputs the pair with the maximum total overlap.

---

## 📄 Input Format
The input must be a `.csv` file, where each row contains:

EmpID,ProjectID,DateFrom,DateTo

Example:

1,100,2020-01-01,2020-02-01

1,100,2020-03-01,2020-03-31

2,100,2020-02-01,2020-04-01


## ✅ Example Output
1, 2, 31

This means **employee 1 and employee 2** have worked together on common projects for **31 days in total**.

---

## ⚙️ How to Run

### 1. Prepare the CSV File

Place your input file in the `resources/work-data.csv` folder (or ensure it is accessible via the classpath).

For example:

## 🧾 Assumptions & Rules

- **Date format**:  
  Dates must follow these formats:
  - `yyyy-MM-dd` (e.g., `2020-01-31`)
  - `dd-MM-yyyy`
  - `yyyy/MM/dd`
  - `dd/MM/yyyy`
  - `dd MMM yyyy`
  - `yyyy MMM dd`

- **Field separator**:  
  The CSV file must use a **comma (`,`)** as the field delimiter.

- **Header row**:  
  The CSV is **not** expected to have a **header** (i.e. the first row won't be skipped).

- **DateTo**:  
  If the `DateTo` field contains the string `"NULL"` case-insensitive, it is interpreted as the current date (`LocalDate.now()`).

- **Overlaps**:  
  Time periods where two employees worked **together on the same project at the same time** are counted inclusively from start to end.

- **Multiple timeframes**:  
  An employee may work on the **same project in different time periods**. The system correctly computes overlaps across those periods.
