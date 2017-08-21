package org.ubos.apps.ubosstat;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.config.utility;
import org.ubos.apps.ubosstat.data.BarChartView;
import org.ubos.apps.ubosstat.json.Graphs;
import org.ubos.apps.ubosstat.widgets.Datatable;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static adapters.Rcycview.ITEM_KEY;


public class DetailVActivity extends AppCompatActivity {

    private  TextView tvTitle ,tvPeriod, tvIndexValue , tvChangeType , tvChangeValue , tvUnit, tvHeadline , tvDescription , tvUrl , tvUpdatedOn,titleH;
    public Context context;
    public boolean image_toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image_toggle = true;
        context = this;
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        Long s = getIntent().getLongExtra(ITEM_KEY, 1);
        String title = getIntent().getStringExtra("ITEM_TITLE");
        String period = getIntent().getStringExtra("ITEM_PERIOD");
        String value = getIntent().getStringExtra("ITEM_VALUE");
        String change_type = getIntent().getStringExtra("ITEM_CHANGE_TYPE");
        String change_value = getIntent().getStringExtra("ITEM_CHANGE_VALUE");
        String headline = getIntent().getStringExtra("ITEM_HEADLINE");
        String description = getIntent().getStringExtra("ITEM_DESCRIPTION");
        String url = getIntent().getStringExtra("ITEM_URL");
        String updated_on = getIntent().getStringExtra("ITEM_UPDATED_ON");
        String unit  = getIntent().getStringExtra("ITEM_UNIT");
        String cat = getIntent().getStringExtra("ITEM_CAT");
        String data = getIntent().getStringExtra("ITEM_DATA");


        if(true)
        {
            //setContentView(R.layout.activity_detail_i);
            setContentView(R.layout.activity_detail_i_r);
            final LinearLayout layout = (LinearLayout)findViewById(R.id.main_title);
            ImageView toggle = (ImageView)findViewById(R.id.toggle);

            utility util = new utility();;
            String file_data = util.readFile(context,"table.json");
            Gson g = new Gson();
            Type listType = new TypeToken<ArrayList<Graphs>>() {}.getType();
            ArrayList<Graphs> result = g.fromJson(data,listType);
            Log.d("ECHO","Drew the Graph");
            Log.d("DATA",data);
            final BarChartView chart1 = new BarChartView(context,result.get(0));
            layout.addView(chart1.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800));


            final Datatable datatable = new Datatable(context);
            boolean color_t = false;
            datatable.setHeader(result.get(0).title);
            listType = new TypeToken<ArrayList<String[]>>() {}.getType();
            ArrayList<String[]> rows = g.fromJson(result.get(0).data,listType);
            datatable.addRow(rows);

            //layout.addView(datatable);

            toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(image_toggle){
                        ((ImageView)view).setImageResource(R.drawable.graph_ico);
                        layout.removeAllViews();
                        layout.addView(datatable);
                    }else{
                        ((ImageView)view).setImageResource(R.drawable.table_icon);
                        layout.removeAllViews();
                        layout.addView(chart1.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800));
                    }
                    image_toggle = !image_toggle;

                }
            });

            //LineChartView chart2 = new LineChartView(context,LineChartView.generateData(result.get(0)));
            //layout.addView(chart2.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900));
        }else{
            setContentView(R.layout.activity_detail_i_r);

        }


        //Toast.makeText(getApplicationContext(), "Indicators ID: " + s + "TITLE"+title + "PERIOD"+period+"VALUE"+value, Toast.LENGTH_LONG).show();

        this.setTitle(" "+ title +" ") ;
// create handlers
        tvPeriod = (TextView)  findViewById(R.id.period);
        titleH = (TextView)  findViewById(R.id.title);
        tvIndexValue = (TextView) findViewById(R.id.indexValue);
        tvChangeType = (TextView) findViewById(R.id.changeType);
        tvChangeValue = (TextView) findViewById(R.id.changeValue);
        //tvHeadline = (TextView) findViewById(R.id.headline);
        tvUnit = (TextView) findViewById(R.id.unit);
        tvDescription = (TextView) findViewById(R.id.description);
        tvUrl = (TextView) findViewById(R.id.url);
        tvUpdatedOn = (TextView) findViewById(R.id.updatedOn);

        try{
            titleH.setText(title);
            tvPeriod.setText(period);
            tvIndexValue.setText(value);
            tvChangeType.setText(change_type);
            tvChangeValue.setText(change_value);
            //tvHeadline.setText(headline);
            tvUnit.setText(unit);
            tvDescription.setText(description);
            tvUrl.setText(url);
            tvUpdatedOn.setText(updated_on);
        }catch(Exception e){

        }
        // populate textviews with text


        // disable description field

        // tvDescription.setEnabled(false);



    }
}