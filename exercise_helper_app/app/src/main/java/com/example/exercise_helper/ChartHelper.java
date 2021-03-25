package com.example.exercise_helper;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Map;

public class ChartHelper {

    // https://medium.com/@clyeung0714/using-mpandroidchart-for-android-application-piechart-123d62d4ddc0
    public void showPieChart(PieChart chart, Map<String, Integer> myMap, String title) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        for(int i=0;i<myMap.size();i++){
            int red = (int)(Math.random()*255); //todo : 색깔 다시 지정
            int blue = (int)(Math.random()*255);
            int green = (int)(Math.random()*255);
            colors.add(Color.rgb(red, blue, green));
        }

        //input data and fit data into pie chart entry
        for(String type: myMap.keySet()){
            pieEntries.add(new PieEntry(myMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,title);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        chart.setData(pieData);
        chart.invalidate();
        chart.animateXY(5000, 5000);
    }

    // https://medium.com/@clyeung0714/using-mpandroidchart-for-android-application-barchart-540a55b4b9ef
    public void showVerticalBarChart(BarChart chart, ArrayList<String> labels, ArrayList<Double> valueLists, String title){
        ArrayList<BarEntry> entries = new ArrayList<>();

        //input data and fit data into entry
        for(int i = 0;i<valueLists.size(); i++){
            BarEntry barEntry = new BarEntry(i, valueLists.get(i).floatValue());
            entries.add(barEntry);
        }

        // custom XAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(labels.size(),true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int)value);
            }
        });

        // custom YAxis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        BarDataSet barDataSet = new BarDataSet(entries, title);
        BarData data = new BarData(barDataSet);

        chart.setVisibleXRangeMaximum(5); // 최대 데이터 개수
        chart.setData(data);
        chart.invalidate();

        //chart.animateX(5000);
    }

    // https://under-desk.tistory.com/183
    public void showVerticalLineChart(LineChart chart, ArrayList<String> labels, ArrayList<Double> valueLists, String title) {
        ArrayList<Entry> entry_chart = new ArrayList<>();

        // input data and fit data into entry
        for(int i = 0;i<valueLists.size(); i++){
            BarEntry barEntry = new BarEntry(i, valueLists.get(i).floatValue());
            entry_chart.add(barEntry);
        }

        // custom XAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(labels.size(),true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int)value);
            }
        });

        // custom YAxis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        LineData chartData = new LineData();
        LineDataSet lineDataSet = new LineDataSet(entry_chart, title);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // make smooth line chart
        chartData.addDataSet(lineDataSet);

        chart.setVisibleXRangeMaximum(5); // 최대 데이터 개수
        chart.setData(chartData);
        chart.invalidate();

        //chart.animateX(5000);
    }

    // https://ddangeun.tistory.com/52
    public void showRealTimeLineChart(LineChart chart, String title, double num) {
        LineData chartData = chart.getData();

        if (chartData == null){
            chartData = new LineData();
            chart.setData(chartData);
        }
        LineDataSet lineDataSet = (LineDataSet) chartData.getDataSetByIndex(0);

        // create set
        if (lineDataSet == null){
            // custom XAxis
            XAxis xAxis = chart.getXAxis();
            xAxis.setEnabled(false);

            // custom YAxis
            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setEnabled(true);
            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);

            lineDataSet = new LineDataSet(null, title);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // make smooth line chart
            chartData.addDataSet(lineDataSet);
        }

        // input data and fit data into entry
        Entry entry = new Entry((float)lineDataSet.getEntryCount(), (float)num);
        chartData.addEntry(entry, 0);
        chartData.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();
        chart.setVisibleXRangeMaximum(5); // 최대 데이터 개수
        chart.moveViewToX(chartData.getEntryCount()); // 치근 추가한 데이터로 이동

        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(chartData.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);
    }
}