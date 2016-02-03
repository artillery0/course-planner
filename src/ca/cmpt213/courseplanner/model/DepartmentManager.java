package ca.cmpt213.courseplanner.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Manage the collection of departments and reads data from CSV file.
 */
public class DepartmentManager implements Iterable<Department> {
    private List<Department> departments = new ArrayList<Department>();

    public Department findOrMakeDepartment(String subjectName) {
        for (Department department : departments) {
            if (department.getName().equals(subjectName)) {
                return department;
            }
        }
        Department newDepartment = new Department(subjectName);
        departments.add(newDepartment);
        Collections.sort(departments);
        return newDepartment;
    }

    public Iterator<Department> iterator() {
        return Collections.unmodifiableList(departments).iterator();
    }

    public void loadDataFromFile(File file) throws FileNotFoundException {
        CSVFileReader reader = new CSVFileReader(file);

        for (CSVFileLine fileLine : reader) {
            addDataFromCSVLine(fileLine);
        }
    }

    private void addDataFromCSVLine(CSVFileLine fileLine) {
        String semesterStr = fileLine.get(CSVFileLine.COLUMN_SEMESTER);
        String subjectName = fileLine.get(CSVFileLine.COLUMN_SUBJECT);
        String catalogNumber = fileLine.get(CSVFileLine.COLUMN_CATALOG_NUMBER);
        String location = fileLine.get(CSVFileLine.COLUMN_LOCATION);
        String enrollmentCapStr = fileLine.get(CSVFileLine.COLUMN_ENROLLMENT_CAP);
        String component = fileLine.get(CSVFileLine.COLUMN_COMPONENT);
        String enrollmentTotalStr = fileLine.get(CSVFileLine.COLUMN_ENROLLMENT_TOTAL);
        String instructor = fileLine.get(CSVFileLine.COLUMN_INSTRUCTOR);

        Department department = findOrMakeDepartment(subjectName);

        Course course = department.findOrMakeCourse(catalogNumber);

        Semester semester = new Semester(semesterStr);
        int enrollmentCap = asInt(enrollmentCapStr);
        int enrollmentTotal = asInt(enrollmentTotalStr);
        course.addSection(semester, location, component, enrollmentCap, enrollmentTotal, instructor);
    }

    private int asInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Number conversion problem for string '" + str + "'");
            return 0;
        }
    }

}
