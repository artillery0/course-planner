package ca.cmpt213.courseplanner.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Histogram.Histogram;
import Histogram.HistogramIcon;
import ca.cmpt213.courseplanner.model.Course;
import ca.cmpt213.courseplanner.model.CourseOffering;
import ca.cmpt213.courseplanner.model.Model;
import ca.cmpt213.courseplanner.model.Semester;

/**
 * UI to display statistics for all the offerings of the currently selected course.
 */
@SuppressWarnings("serial")
public class StatisticsPanel extends TitledPanel {
    private static final int ICON_HEIGHT = 150;
    private static final int ICON_WIDTH = 250;

    private static final int NUM_CAMPUSES = 4;
    private static final int NUM_SEMESTERS = 3;
    private static final int HISTOGRAM_SPACING = 20;

    private static final int LOCATION_INDEX_BBY = 0;
    private static final int LOCATION_INDEX_SRY = 1;
    private static final int LOCATION_INDEX_VAN = 2;
    private static final int LOCATION_INDEX_OTHER = 3;

    private static final int SEMESTER_INDEX_SPRING = 0;
    private static final int SEMESTER_INDEX_SUMMER = 1;
    private static final int SEMESTER_INDEX_FALL = 2;

    private Histogram locationHistogram;
    private Histogram semesterHistogram;
    private JLabel courseNameLabel;

    public StatisticsPanel(Model model) {
        super("Statistics", model);
        setMainContents(makeMainPanel());
    }

    private JComponent makeMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(makeCourseNameLabel());
        panel.add(makeSemesterHistogramPane());
        panel.add(makeLocationHistogramPanel());

        allowOnlyHorizontalStretching(this);
        registerForUpdates();

        return panel;
    }

    private Component makeCourseNameLabel() {
        courseNameLabel = new JLabel("");
        return courseNameLabel;
    }

    private Component makeSemesterHistogramPane() {
        int[] data = new int[0];
        semesterHistogram = new Histogram(data, NUM_SEMESTERS);
        HistogramIcon icon = new HistogramIcon(semesterHistogram, ICON_WIDTH, ICON_HEIGHT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalStrut(HISTOGRAM_SPACING));
        panel.add(new JLabel("Semester Offerings:"));
        panel.add(new JLabel(icon));
        panel.add(new JLabel("(0=Spring, 1=Summer, 2=Fall)"));

        return panel;
    }

    private Component makeLocationHistogramPanel() {
        int[] data = new int[0];
        locationHistogram = new Histogram(data, NUM_CAMPUSES);
        HistogramIcon icon = new HistogramIcon(locationHistogram, ICON_WIDTH, ICON_HEIGHT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalStrut(HISTOGRAM_SPACING));
        panel.add(new JLabel("Campus Offerings:"));
        panel.add(new JLabel(icon));
        panel.add(new JLabel("(0=Bby, 1=Sry, 2=Van, 3=Other)"));
        return panel;
    }

    private void registerForUpdates() {
        getModel().addSelectedCourseListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                update();
            }
        });
    }

    private void update() {
        Course selectedCourse = getModel().getSelectedCourse();

        if (selectedCourse == null) {
            clearData();
        } else {
            courseNameLabel.setText("Course: " + selectedCourse.toString());
            semesterHistogram.setData(countOfferingsPerSemester(selectedCourse));
            locationHistogram.setData(countOfferingsPerLocation(selectedCourse));
        }

        // Have Java redraw the area on the UI.
        updateUI();
    }

    private void clearData() {
        int[] data = new int[0];
        courseNameLabel.setText("Course: " + "");
        semesterHistogram.setData(data);
        locationHistogram.setData(data);
    }

    private int[] countOfferingsPerLocation(Course course) {
        List<Integer> data = new ArrayList<Integer>();
        for (CourseOffering offering : course.offerings()) {
            String campus = offering.getLocation();
            if (campus.equals("BURNABY")) {
                data.add(LOCATION_INDEX_BBY);
            } else if (campus.equals("SURREY")) {
                data.add(LOCATION_INDEX_SRY);
            } else if (campus.equals("HRBRCNTR")) {
                data.add(LOCATION_INDEX_VAN);
            } else {
                data.add(LOCATION_INDEX_OTHER);
            }
        }

        return convertIntegerListToArray(data);
    }

    private int[] countOfferingsPerSemester(Course course) {
        List<Integer> data = new ArrayList<Integer>();
        for (CourseOffering offering : course.offerings()) {
            Semester semester = offering.getSemester();

            if (semester.isSpring()) {
                data.add(SEMESTER_INDEX_SPRING);
            } else if (semester.isSummer()) {
                data.add(SEMESTER_INDEX_SUMMER);
            } else if (semester.isFall()) {
                data.add(SEMESTER_INDEX_FALL);
            } else {
                assert false;
            }
        }

        return convertIntegerListToArray(data);
    }

    private int[] convertIntegerListToArray(List<Integer> data) {
        int[] dataReturn = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dataReturn[i] = data.get(i);
        }
        return dataReturn;
    }
}
