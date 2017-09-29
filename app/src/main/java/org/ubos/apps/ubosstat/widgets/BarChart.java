package org.ubos.apps.ubosstat.widgets;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.json.Graphs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Andrew on 13-06-2017.
 */

public class BarChart {
    ColumnChartView chart_column ;
    static Context context;
    public BarChart(Context context, Graphs graph){
        chart_column = new ColumnChartView(context);
        chart_column.setValueTouchEnabled(true);
        this.context = context;
        chart_column.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                Toast.makeText(BarChart.context,""+subcolumnValue.getValue(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });
        chart_column.setColumnChartData(generateData(graph));
    }
    public ColumnChartView getChart(){
        return chart_column;
    }
    public ColumnChartData generateData(Graphs graph) {
        List<Column> columns = new ArrayList<Column>();
        Column i_1 = new Column();
        i_1.setHasLabels(true);
        i_1.setHasLabelsOnlyForSelected(true);

        List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();
        String parse = graph.data.replace("\\","");
        Gson g = new Gson();
        Type listType = new TypeToken<ArrayList<String[]>>() {}.getType();
        ArrayList<String[]> result = g.fromJson(parse,listType);


        List<AxisValue> axisValues= new ArrayList<AxisValue>();
        for(int i=0;i<result.size();i++){
            i_1 = new Column();
            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(Float.parseFloat(result.get(i)[1]), ChartUtils.pickColor()));
            AxisValue v = new AxisValue(Float.parseFloat(result.get(i)[1]));
            v.setLabel(result.get(i)[0]);
            axisValues.add(v);

            i_1.setValues(values);
            i_1.setHasLabels(true);
            i_1.setHasLabelsOnlyForSelected(true);
            columns.add(i_1);
        }


        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = new Axis(axisValues);
        data.setStacked(true);


        Axis axisY = new Axis().setHasLines(true);

        axisX.setName(graph.x_axis);
        axisY.setName(graph.y_axis);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);


        return data;


    }
}
