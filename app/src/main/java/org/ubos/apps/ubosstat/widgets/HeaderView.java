package org.ubos.apps.ubosstat.widgets;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ubos.apps.ubosstat.R;


/**
 * Created by Andrew on 13-06-2017.
 */

public class HeaderView {
    View text_header;
    public HeaderView(Activity context, ViewGroup layout, String text, Typeface customFont){
        text_header = context.getLayoutInflater()
                .inflate(R.layout.sub_header_new, layout, false);
        TextView header = (TextView)text_header.findViewById(R.id.header);
        header.setText(text);
        header.setTypeface(customFont);
    }
    public View getText_header(){
        return text_header;
    }
}
