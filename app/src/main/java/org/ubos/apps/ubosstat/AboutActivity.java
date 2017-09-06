package org.ubos.apps.ubosstat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Andrew on 14-08-2017.
 */

public class AboutActivity extends Activity {

    @Override
    public void onCreate(Bundle os){
        super.onCreate(os);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_page);

    }
}
