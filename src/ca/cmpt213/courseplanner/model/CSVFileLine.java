package ca.cmpt213.courseplanner.model;

/**
 * Contain information from a single line from the input CSV file.
 */
public class CSVFileLine {

    public static final int COLUMN_SEMESTER = 0;
    public static final int COLUMN_SUBJECT = 1;
    public static final int COLUMN_CATALOG_NUMBER = 2;
    public static final int COLUMN_LOCATION = 3;
    public static final int COLUMN_ENROLLMENT_CAP = 4;
    public static final int COLUMN_COMPONENT = 5;
    public static final int COLUMN_ENROLLMENT_TOTAL = 6;
    public static final int COLUMN_INSTRUCTOR = 7; // And all beyond

    private String[] data;

    public CSVFileLine(String line) {
        data = line.split(",");
    }

    public String get(int columnSemester) {
        if (data.length >= columnSemester + 1) {
            return getColumnData(columnSemester);
        } else {
            return "";
        }
    }

    private String getColumnData(int columnSemester) {
        if (columnSemester == COLUMN_INSTRUCTOR) {
            String instructors = "";
            for (int i = columnSemester; i < data.length; i++) {
                if (instructors.length() > 0) {
                    instructors += ", ";
                }
                instructors += data[i];
            }
            return instructors;
        } else {
            return data[columnSemester];
        }
    }

}
