package org.ubos.apps.ubosstat.widgets;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.config.global;
import org.ubos.apps.ubosstat.json.Statistics;


/**
 * Created by Andrew on 13-06-2017.
 */

public class Statistic {
    View info_layout;
    Typeface fonts_;
    public Statistic(Activity activity, ViewGroup layout, Typeface font){
        fonts_ = font;
        info_layout = global.switch_flag ? activity.getLayoutInflater()
                .inflate(R.layout.stat_content_info_black, layout, false) : activity.getLayoutInflater()
                .inflate(R.layout.stat_content_info_white, layout, false);

    }
    public View getInfo_layout(){
        global.switch_flag = !global.switch_flag;
        return info_layout;
    }
    public void buildStatistic(Statistics stat){
        TextView text = (TextView) info_layout.findViewById(R.id.textView);
        text.setText(stat.text);
        text.setTypeface(fonts_);
    }
}
