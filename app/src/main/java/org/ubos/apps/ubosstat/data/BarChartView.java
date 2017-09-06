package org.ubos.apps.ubosstat.data;

import android.content.Context;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.json.Graphs;
import org.ubos.apps.ubosstat.utility.ColorDiagram;
import org.ubos.apps.ubosstat.widgets.CustomMarkerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * Created by Andrew on 19-08-2017.
 */

public class BarChartView  {
    public BarChart chart;
    public BarChartView(Context context, Graphs data){
        chart = new BarChart(context);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setData(generateData(data));
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setDrawLabels(false);
        org.ubos.apps.ubosstat.widgets.CustomMarkerView mv = new CustomMarkerView(context, R.layout.custom_marker_view_layout);
        chart.setMarker(mv);


    }
    public void setVisibility(int i){


        chart.setVisibility(i);
    }
    public BarChart getChart(){
        return this.chart;
    }
    public BarData generateData(Graphs graph){
        List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();
        String parse = graph.data.replace("\\","");
        Gson g = new Gson();
        Type listType = new TypeToken<ArrayList<String[]>>() {}.getType();
        ArrayList<String[]> result = g.fromJson(parse,listType);
        float index = 0;
        ColorDiagram d = new ColorDiagram();
        BarData data = new BarData();
        for(String[] i: result){
            BarEntry n = new BarEntry(index,Float.parseFloat(i[1]));
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            yVals1.add(n);
            BarDataSet set = new BarDataSet(yVals1,graph.y_axis);
            set.setLabel(i[0]);
            set.setColor(d.getColor());
            index++;
            yVals1.add(n);
            data.addDataSet(set);
        }
        return data;
    }

}
