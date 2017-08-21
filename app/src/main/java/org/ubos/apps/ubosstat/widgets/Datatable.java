package org.ubos.apps.ubosstat.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ubos.apps.ubosstat.R;

import java.util.ArrayList;


/**
 * Created by Andrew on 12-06-2017.
 */

public class Datatable extends LinearLayout {
    public String[] green_table = new String[]{"#1D6835", "#1D6835", "#7AC145"};
    public String[] orange_table = new String[]{"#4D372A", "#E83A33", "#ED6F35", "#F5A654"};
    public String[] white_table = new String[]{"#4D372A", "#E83A33", "#ED6F35", "#F5A654"};
    public String[] base  = null;
    public TextView header_title;
    public LinearLayout header;
    public LinearLayout base_view;
    public boolean flag;
    Integer color_count;

    public Datatable(Context context) {
        super(context);
        Initialize();

    }
    public Datatable(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initialize();
    }

    public Datatable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Initialize();
    }
    public void Initialize(){
        inflate(getContext(), R.layout.table, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //LinearLayout base_view = (LinearLayout)inflate(getContext(), R.layout.table, null);
        header = (LinearLayout)inflate(getContext(), R.layout.table_header, null);
        header_title = (TextView)header.findViewById(R.id.header);
        if(base==null){
            base = orange_table;
        }

    }
    public void setColorPallet(Boolean flag){
        this.flag = flag;
        color_count = 0;

        if(flag){
            base = green_table;
        }else{
            base = orange_table;
        }
        if(flag){
            ((LinearLayout)header.findViewById(R.id.header_p)).setBackgroundColor(Color.parseColor(base[color_count]));
            ((LinearLayout)header.findViewById(R.id.header_r)).setBackgroundColor(Color.parseColor(base[color_count]));
        }
    }
    public void setHeader(String title){
        header_title.setText(title);
        ((LinearLayout)this.findViewById(R.id.main_table)).addView(header);
    }
    public void addRow(ArrayList<String[]> values){
        LinearLayout row;
        TextView left;
        TextView value;


        for(String item[] : values){
            row = (LinearLayout)inflate(getContext(), R.layout.table_row,null);
            left = (TextView) row.findViewById(R.id.left);
            ((LinearLayout)row.findViewById(R.id.first)).setBackgroundColor(Color.GRAY);
            ((LinearLayout)row.findViewById(R.id.second)).setBackgroundColor(Color.GRAY);
            value = (TextView) row.findViewById(R.id.value);
            left.setText(item[0]);
            value.setText(item[1]);
            ((LinearLayout)this.findViewById(R.id.main_table)).addView(row);
        }



    }


}
