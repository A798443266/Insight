package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;


public interface OnChartValueSelectedListener {


    void onValueSelected(Entry e, int dataSetIndex, Highlight h);

    void onNothingSelected();
}
