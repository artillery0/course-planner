package ca.cmpt213.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ca.cmpt213.courseplanner.model.Model;

/**
 * Create and display the main UI and load data into the model.
 */
public class CoursePlannerUI {
    // private final static String COURSE_DATA_FILE_PATH = "data/course_data.csv";
    private final static String COURSE_DATA_FILE_PATH = "data/course_data_2014.csv";

    Model model;

    /*
     * Static interface to start application
     */
    public static void main(String[] args) {
        Model model = new Model();

        if (tryLoadData(model)) {
            model.dumpModelToConsole();

            // Launch the UI
            new CoursePlannerUI(model);
        }
    }

    private static boolean tryLoadData(Model model) {
        File file = new File(COURSE_DATA_FILE_PATH);
        try {
            model.loadDataFromFile(file);
            return true;
        } catch (FileNotFoundException e) {
            String message = "Data file (" + file.getAbsolutePath() + ") not found.";
            JOptionPane.showMessageDialog(null, message);
        } catch (Exception e) {
            String message = "Error opening data file: " + e.getMessage();
            JOptionPane.showMessageDialog(null, message);
        }
        return false;
    }

    public CoursePlannerUI(Model model) {
        this.model = model;
        JFrame frame = new JFrame("FAS Course Planner");
        frame.setLayout(new BorderLayout());

        frame.add(makeLeftPanel(), BorderLayout.WEST);
        frame.add(makeCentrePanel(model), BorderLayout.CENTER);
        frame.add(makeRightPanel(), BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Component makeLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new FilterPanel(model), BorderLayout.NORTH);
        panel.add(new CourseListPanel(model), BorderLayout.CENTER);
        return panel;
    }

    private OfferingGridPanel makeCentrePanel(Model model) {
        return new OfferingGridPanel(model);
    }

    private Component makeRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(new StatisticsPanel(model));
        panel.add(new OfferingDetailsPanel(model));
        return panel;
    }

}
