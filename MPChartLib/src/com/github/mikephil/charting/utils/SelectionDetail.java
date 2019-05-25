package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.data.DataSet;
public class SelectionDetail {

    public float val;
    public int dataSetIndex;
    public DataSet<?> dataSet;

    public SelectionDetail(float val, int dataSetIndex, DataSet<?> set) {
        this.val = val;
        this.dataSetIndex = dataSetIndex;
        this.dataSet = set;
    }
}