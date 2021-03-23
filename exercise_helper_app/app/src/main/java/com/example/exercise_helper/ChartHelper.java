package com.example.exercise_helper;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
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

import java.util.ArrayList;
import java.util.Map;

public class ChartHelper {

    // https://medium.com/@clyeung0714/using-mpandroidchart-for-android-application-piechart-123d62d4ddc0
    public void showPieChart(PieChart pieChart, Map<String, Integer> myMap, String title) {
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
        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.animateXY(5000, 5000);
    }

    // https://medium.com/@clyeung0714/using-mpandroidchart-for-android-application-barchart-540a55b4b9ef
    public void showVerticalBarChart(BarChart barChart, ArrayList<String> labels, ArrayList<Double> valueLists, String title){
        ArrayList<BarEntry> entries = new ArrayList<>();

        //input data and fit data into entry
        for(int i = 0;i<valueLists.size(); i++){
            BarEntry barEntry = new BarEntry(i, valueLists.get(i).floatValue());
            entries.add(barEntry);
        }

        // custom XAxis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(labels.size(),true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int)value);
            }
        });

        BarDataSet barDataSet = new BarDataSet(entries, title);
        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();

        //barChart.animateX(5000);
    }

    // https://under-desk.tistory.com/183
    public void showVerticalLineChart(LineChart lineChart, ArrayList<String> labels, ArrayList<Double> valueLists, String title) {
        ArrayList<Entry> entry_chart = new ArrayList<>();

        //input data and fit data into entry
        for(int i = 0;i<valueLists.size(); i++){
            BarEntry barEntry = new BarEntry(i, valueLists.get(i).floatValue());
            entry_chart.add(barEntry);
        }

        // custom XAxis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(labels.size(),true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int)value);
            }
        });

        LineData chartData = new LineData();
        LineDataSet lineDataSet = new LineDataSet(entry_chart, title);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // make smooth line chart
        chartData.addDataSet(lineDataSet);
        lineChart.setData(chartData);
        lineChart.invalidate();

        //lineChart.animateX(5000);
    }
}