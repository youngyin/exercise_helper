package com.example.exercise_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    ChartHelper chartHelper;
    DBHelper dbHelper;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        chartHelper = new ChartHelper();
        dbHelper = new DBHelper(getApplicationContext());

        PieChart pieChart = findViewById(R.id.pieChart);
        BarChart barChart = findViewById(R.id.barChart);
        LineChart lineChart = findViewById(R.id.lineChart);

        cursor = dbHelper.select_count_id_from_diary_group_by_category();
        draw_pieChart(pieChart, cursor);

        cursor = dbHelper.select_count_id_from_diary_group_by_date();
        draw_VerticalBarChart(barChart, cursor);

        //cursor = dbHelper.select_average_from_diary_group_by_category();
        cursor = dbHelper.select_sum_delay_from_diary_group_by_category();
        draw_VerticalLineChart(lineChart, cursor);
    }

    private void draw_pieChart(PieChart pieChart, Cursor cursor) {
        String message = "";
        Map<String, Integer> myMap = new HashMap<>();
        while (cursor.moveToNext()){
            myMap.put(cursor.getString(1), cursor.getInt(0));
            message += cursor.getInt(0)+" : "+ cursor.getString(1) + ", ";
        }
        chartHelper.showPieChart(pieChart, myMap,"count of record");
    }

    private void draw_VerticalBarChart(BarChart barChart, Cursor cursor) {
        String message = "";
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Double> values = new ArrayList<Double>();
        while (cursor.moveToNext()){
            labels.add(cursor.getString(1));
            values.add(cursor.getDouble(0));
            message += cursor.getInt(0)+" : "+ cursor.getString(1) + ", ";
        }
        chartHelper.showVerticalBarChart(barChart, labels, values,"count of record");
        //Toast.makeText(getApplicationContext(), message + "---> "+cursor.getCount(), Toast.LENGTH_LONG).show();
    }

    private void draw_VerticalLineChart(LineChart lineChart, Cursor cursor) {
        String message = "";
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Double> values = new ArrayList<Double>();
        while (cursor.moveToNext()){
            labels.add(cursor.getString(1));
            values.add(cursor.getDouble(0));
            message += cursor.getInt(0)+" : "+ cursor.getString(1) + ", ";
        }
        if (cursor.getCount()<2){
            lineChart.setVisibility(View.INVISIBLE);
        } else {
            chartHelper.showVerticalLineChart(lineChart, labels, values,"근육별 근전도값의 평균");
        }
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}