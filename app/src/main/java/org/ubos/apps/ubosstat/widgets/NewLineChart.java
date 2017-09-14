package org.ubos.apps.ubosstat.widgets;

import android.content.Context;
import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.json.Graphs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * Created by Andrew on 14-09-2017.
 */

public class NewLineChart {
    static lecho.lib.hellocharts.view.LineChartView chart;

    public LineChartView getLineChart(Context context, Graphs graph) {
        chart = new LineChartView(context);
        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        generateData(graph);
        return chart;
    }
    public lecho.lib.hellocharts.view.LineChartView getChart(){
        return chart;
    }

    public static void generateData(Graphs graph) {
        String parse = graph.data.replace("\\", "");
        Gson g = new Gson();
        Type listType = new TypeToken<ArrayList<String[]>>() {
        }.getType();
        ArrayList<String[]> result = g.fromJson(parse, listType);

        List<PointValue> values = new ArrayList<PointValue>();
        List<String> labels = new ArrayList<String>();
        List<Float> vals = new ArrayList<Float>();
        int is = 0;
        for (String[] i : result) {
            values.add(new PointValue(is, Float.parseFloat(i[1])));
            labels.add(i[0]);
            vals.add(Float.parseFloat(i[1]));
            is++;
        }
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        line.setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(Axis.generateAxisFromCollection(vals,labels));
        chart.setLineChartData(data);


    }
}
