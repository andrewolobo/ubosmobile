package org.ubos.apps.ubosstat.widgets;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.json.Graphs;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Andrew on 13-06-2017.
 */

public class LineChartView {
    LineChart chart_column ;
    public LineChartView(Context context, LineData data){
        chart_column = new LineChart(context);
        chart_column.setData(data);
    }
    public LineChart getChart(){
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
            vals.add(new Entry(Float.parseFloat(i[0]),Float.parseFloat(i[1])));
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
