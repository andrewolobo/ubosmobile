package org.ubos.apps.ubosstat.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.json.SpecialStats;

import static org.ubos.apps.ubosstat.R.id.textView;


/**
 * Created by Andrew on 13-06-2017.
 */

public class SpecialStatistic {
    Activity activity;
    ViewGroup layout;
    Typeface customFont;
    View base;
    public SpecialStatistic(Context context, Activity activity, ViewGroup layout, Typeface customFont){
/*        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.weight = 1.0f;
        //params.setMargins(5,3,3,0);
        //params.gravity = Gravity.CENTER_HORIZONTAL;
        base = new LinearLayout(context);
        base.setLayoutParams(params);
        base.setOrientation(LinearLayout.HORIZONTAL);
        this.activity = activity;
        this.layout = layout;
        this.customFont = customFont;*/
        base = activity.getLayoutInflater()
                .inflate(R.layout.sub_statistic, layout, false);
    }
    public View getSpecialStatistic(){
        return base;
    }

    public void buildStatistic(SpecialStats statistic){
        String[] parameters = statistic.parameters.split("-");
        TextView information = (TextView)base.findViewById(R.id.information);
        TextView description = (TextView) base.findViewById(textView);
        TextView title = (TextView) base.findViewById(R.id.textView_r);
        description.setText(parameters[0]);
        title.setText(parameters[1]);
    }
    public void buildStatistics(SpecialStats statistic){
        String[] arguments = statistic.arguments.split("-");
        String[] parameters = statistic.parameters.split("-");
        for(int i= 0; i<arguments.length;i++){
            switch (arguments[i]) {
                case "[B]":
                    LinearLayout bold = (LinearLayout) activity.getLayoutInflater()
                            .inflate(R.layout.bold_text, layout, false);
                    TextView bold_text = (TextView) bold.findViewById(textView);
                    bold_text.setText(parameters[i]);
                    System.out.println(parameters[i]);
                    bold_text.setTypeface(customFont, Typeface.BOLD);
                   // base.addView(bold);
                    break;
                case "[N]":
                    LinearLayout normal = (LinearLayout) activity.getLayoutInflater()
                            .inflate(R.layout.normal_text, layout, false);
                    TextView normal_text = (TextView) normal.findViewById(textView);
                    normal_text.setText(parameters[i]);
                    System.out.println(parameters[i]);
                    //base.addView(normal);
                    break;
                case "[G]":
                    LinearLayout green = (LinearLayout) activity.getLayoutInflater()
                            .inflate(R.layout.green_text, layout, false);
                    TextView green_text = (TextView) green.findViewById(textView);
                    green_text.setText(parameters[i]);
                    System.out.println(parameters[i]);
                    green_text.setTypeface(customFont, Typeface.BOLD);
                    //base.addView(green);
                    break;

            }


            /*if(arguments[i].equals("[B]")){
                LinearLayout bold = (LinearLayout)activity.getLayoutInflater()
                        .inflate(bold_text, layout, false);
                TextView bold_text = (TextView) bold.findViewById(textView);
                bold_text.setText(parameters[i]);
                System.out.println(parameters[i]);
                bold_text.setTypeface(customFont, Typeface.BOLD);
                base.addView(bold);
            }
            if(arguments[i].equals("[N]")){
                LinearLayout normal = (LinearLayout)activity.getLayoutInflater()
                        .inflate(normal_text, layout, false);
                TextView normal_text = (TextView) normal.findViewById(textView);
                normal_text.setText(parameters[i]);
                System.out.println(parameters[i]);
                base.addView(normal);
            }
            if(arguments[i].equals("[G]")){
                LinearLayout green = (LinearLayout)activity.getLayoutInflater()
                        .inflate(green_text, layout, false);
                TextView green_text = (TextView) green.findViewById(textView);
                green_text.setText(parameters[i]);
                System.out.println(parameters[i]);
                green_text.setTypeface(customFont, Typeface.BOLD);
                base.addView(green);
            }*/

        }

    }
}
