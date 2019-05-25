package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * An interface for providing custom x-axis Strings.
 */
public interface XAxisValueFormatter {

    String getXValue(String original, int index, ViewPortHandler viewPortHandler);
}
