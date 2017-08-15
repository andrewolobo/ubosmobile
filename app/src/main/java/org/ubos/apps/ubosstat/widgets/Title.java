package org.ubos.apps.ubosstat.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.json.Titles;


/**
 * Created by Andrew on 13-06-2017.
 */

public class Title {
    View pane_title;
    public Title(Context context, Activity activity, ViewGroup layout){
        pane_title = activity.getLayoutInflater()
                .inflate(R.layout.pane_title, layout, false);

    }
    public View getTitle(){
        return pane_title;
    }
    public void buildTitle(Titles title_h){
        TextView information = (TextView)pane_title.findViewById(R.id.information);
        TextView title = (TextView) pane_title.findViewById(R.id.title);
        title.setText(title_h.title);
        information.setText(title_h.information);
    }
}
