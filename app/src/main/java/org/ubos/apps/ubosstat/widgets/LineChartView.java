package org.ubos.apps.ubosstat.widgets;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.json.Graphs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 13-06-2017.
 */

public class LineChartView {
    LineChart chart_column ;
    static HashMap<Integer, String> numMap;
    public LineChartView(Context context, LineData data){
        chart_column = new LineChart(context);
        chart_column.setData(data);
    }
    public LineChart getChart(){
        Gson g = new Gson();
        System.out.println("MapMe:"+g.toJson(numMap));
        XAxis xAxis = chart_column.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                System.out.print("The size of this array is :"+numMap.size());
                return numMap.get((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        Description n = new Description();
        n.setText("");
        chart_column.setDescription(n);
        return chart_column;
    }
    public static LineData generateData(Graphs graph) {
        String parse = graph.data.replace("\\","");
        Gson g = new Gson();
        Type listType = new TypeToken<ArrayList<String[]>>() {}.getType();
        ArrayList<String[]> result = g.fromJson(parse,listType);
        ArrayList<Entry> vals = new ArrayList<Entry>();

        int is = 0;
        for(String[] i: result){
            is++;
            vals.add(new Entry(is,Float.parseFloat(i[1])));
        }
        is = 0;
        numMap = new HashMap<>();
        for (String[] i : result) {
            is++;
            System.out.println("Value is:"+i[0]);
            numMap.put(is, i[0]);
        }
        LineDataSet l_data = new LineDataSet(vals,graph.y_axis);
        l_data.setAxisDependency(YAxis.AxisDependency.LEFT);
        l_data.setColor(ColorTemplate.getHoloBlue());
        l_data.setValueTextColor(ColorTemplate.getHoloBlue());
        l_data.setLineWidth(1.5f);
        l_data.setDrawCircles(false);
        l_data.setDrawValues(false);
        l_data.setFillAlpha(65);
        l_data.setFillColor(ColorTemplate.getHoloBlue());
        l_data.setHighLightColor(Color.rgb(244, 117, 117));
        l_data.setDrawCircleHole(false);

        LineData data = new LineData(l_data);
        data.setValueTextColor(Color.WHITE);

        data.setValueTextSize(9f);

        return data;


    }
}
