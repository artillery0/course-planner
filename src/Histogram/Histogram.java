package Histogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Generate a histogram from an array of integers. Supports a configurable number of bars, and can be changed after creation (mutable). When changed, notifies all register ActionListeners of the
 * change for them to update.
 */
public class Histogram {
    private final int MIN_VALUE = 0;

    private int[] data;
    private int numDivisions;
    private List<Bar> bars;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    public Histogram(int[] data, int numBars) {
        assert numBars > 0;
        this.data = Arrays.copyOf(data, data.length);
        this.numDivisions = numBars;
        updateHistogram();
    }

    public void setData(int[] data) {
        this.data = Arrays.copyOf(data, data.length);
        updateHistogram();
    }

    public void setNumberBars(int numBars) {
        assert numBars > 0;
        this.numDivisions = numBars;
        updateHistogram();
    }

    private void updateHistogram() {
        int[] counts = getCountOfValuesPerBar();
        populateBars(counts);

        notifyListeners();
    }

    private int[] getCountOfValuesPerBar() {
        int rangePerBar = calculateRangePerBar(numDivisions);

        int[] counts = new int[numDivisions];
        for (int value : data) {
            int barIndex = getBarIndexFromValue(value, rangePerBar);
            counts[barIndex]++;
        }
        return counts;
    }

    private int calculateRangePerBar(int numDivisions) {
        int max = findMax(data);
        return (int) Math.ceil((double) (max + 1) / numDivisions);
    }

    private int findMax(int[] data) {
        // If data[] is empty, return MIN_VALUE so class works with empty data set.
        int max = MIN_VALUE;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int getBarIndexFromValue(int value, int valuesPerBar) {
        int index = value / valuesPerBar;
        assert index >= 0;
        assert index < numDivisions;
        return index;
    }

    private void populateBars(int[] counts) {
        assert counts.length == numDivisions;
        int rangePerBar = calculateRangePerBar(numDivisions);

        bars = new ArrayList<Bar>(numDivisions);
        for (int barNum = 0; barNum < numDivisions; barNum++) {
            int rangeMin = barNum * rangePerBar;
            int rangeMax = rangeMin + rangePerBar - 1;
            int count = counts[barNum];

            bars.add(new Bar(rangeMin, rangeMax, count));
        }
    }

    public int getNumberBars() {
        return bars.size();
    }

    public Iterable<Bar> bars() {
        return Collections.unmodifiableList(bars);
    }

    public int getMaxBarHeight() {
        // Histogram's minimum bar height is 0, even if empty.
        int max = 0;
        for (Bar bar : bars) {
            if (bar.getCount() > max) {
                max = bar.getCount();
            }
        }
        return max;
    }

    // Observer Functions
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(event);
        }
    }

    /**
     * Store information about a single histogram bar including: - Range (minimum and maximum): The range of values mapped to this bar. - The number of elements that mapped to this bar (bar's height).
     * (Immutable)
     */
    public class Bar {
        private int rangeMin = 0;
        private int rangeMax = 0;
        private int count = 0;

        public Bar(int min, int max, int count) {
            rangeMin = min;
            rangeMax = max;
            this.count = count;
        }

        public int getRangeMin() {
            return rangeMin;
        }

        public int getRangeMax() {
            return rangeMax;
        }

        public int getCount() {
            return count;
        }

        public String toString() {
            return "[" + rangeMin + ", " + rangeMax + "] = " + count;
        }
    }
}
