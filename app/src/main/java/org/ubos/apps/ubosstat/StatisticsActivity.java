package org.ubos.apps.ubosstat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ubos.apps.ubosstat.config.global;
import org.ubos.apps.ubosstat.config.menu;
import org.ubos.apps.ubosstat.json.Order;
import org.ubos.apps.ubosstat.utility.Sectioner;
import org.ubos.apps.ubosstat.widgets.HorizontalScroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static org.ubos.apps.ubosstat.R.id.textView;


/**
 * Created by Andrew on 18-01-2017.
 */

public class StatisticsActivity extends Activity {
    public Context context;
    public static Class default_class;
    public static int position;
    HorizontalScroll scrollPane;
    Typeface customFont;
    static LinearLayout layout;

    public void onCreate(Bundle os) {
        super.onCreate(null);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/avenir.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_startr);
        global.createMap();
        if(layout!=null){
            layout.removeAllViews();
        }
        currentActivity();


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public static double getRandom(double min, double max) {
        return (Math.random() * (max+1-min)) + min;
    }
    public ColumnChartData generateData() {
        List<Column> columns = new ArrayList<Column>();
        //List<ColumnValue> value_c new ArrayList<ColumnValue>()
        Column i_1 = new Column();
        i_1.setHasLabels(true);
        i_1.setHasLabelsOnlyForSelected(true);

        List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();

        List<AxisValue> axisValues= new ArrayList<AxisValue>();
        for(int i=0;i<5;i++){
            i_1 = new Column();
            values = new ArrayList<SubcolumnValue>();
            //axisValues = new ArrayList<AxisValue>();
            values.add(new SubcolumnValue((float)getRandom(5,120), ChartUtils.pickColor()));
            axisValues.add(new AxisValue(i).setLabel("bal"+i));
            i_1.setValues(values);
            columns.add(i_1);
        }


        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = new Axis(axisValues);
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("Census year");
        axisY.setName("Population");

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);


        return data;


    }

    public void currentActivity() {
        scrollPane = (HorizontalScroll) findViewById(R.id.hsv);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        View side_holder = getLayoutInflater()
                .inflate(R.layout.sub_sidebar, scrollPane, false);
        LinearLayout holder = (LinearLayout) side_holder.findViewById(R.id.sidebar_holder);
        menu menus = new menu();
        menus.buildMenu();
        System.out.println(menus.sidebar.size() + " is the size");

        for (Map.Entry<String, Integer> entry : menus.sidebar_v.entrySet()) {
            View element = getLayoutInflater()
                    .inflate(R.layout.sub_sidebar_button, holder, false);
            TextView text = (TextView) element.findViewById(textView);
            text.setText(entry.getKey());
            text.setTypeface(customFont);
            element.setTag(entry.getValue());
            element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    global.position = (int)view.getTag();
/*                    Intent intent = getIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);*/
                    recreate();
                }
            });
            holder.addView(element);
        }
        LinearLayout main = (LinearLayout) findViewById(R.id.main);
        main.addView(side_holder);

        ImageView main_center_replace = new ImageView(context);

        //main_center_replace.setImageResource(R.drawable.side_panel_data);
        Sectioner sectioner = new Sectioner();
        View main_content = getLayoutInflater()
                .inflate(R.layout.activity_startr_right, scrollPane, false);

        layout = (LinearLayout) main_content.findViewById(R.id.main);
        for(Order order: global.data.get(global.position).Order){
            System.out.println("We are working on section:"+global.position);
            View sections = sectioner.generateLayout(order,layout,context,this,customFont);

        }
        main.addView(main_content);


        ((ImageView) side_holder.findViewById(R.id.menu_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollPane.fullScroll(scrollPane.FOCUS_LEFT);
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollPane.fullScroll(scrollPane.FOCUS_RIGHT);
            }
        });
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                scrollPane.fullScroll(scrollPane.FOCUS_RIGHT);
            }
        }, 1500);
    }
}
