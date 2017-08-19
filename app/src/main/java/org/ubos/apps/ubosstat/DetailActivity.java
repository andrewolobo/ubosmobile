package org.ubos.apps.ubosstat;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import static adapters.Rcycview.ITEM_KEY;


public class DetailActivity extends AppCompatActivity {

    private  TextView tvTitle ,tvPeriod, tvIndexValue , tvChangeType , tvChangeValue , tvUnit, tvHeadline , tvDescription , tvUrl , tvUpdatedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_i);

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

        if(cat.equals("1"))
        {
            Toast.makeText(getApplicationContext(), "cat"+cat, Toast.LENGTH_LONG).show();

        }


        Toast.makeText(getApplicationContext(), "Indicators ID: " + s + "TITLE"+title + "PERIOD"+period+"VALUE"+value, Toast.LENGTH_LONG).show();

        this.setTitle(" "+ title +" ") ;
// create handlers
        tvPeriod = (TextView)  findViewById(R.id.period);
        tvIndexValue = (TextView) findViewById(R.id.indexValue);
        tvChangeType = (TextView) findViewById(R.id.changeType);
        tvChangeValue = (TextView) findViewById(R.id.changeValue);
        //tvHeadline = (TextView) findViewById(R.id.headline);
        tvUnit = (TextView) findViewById(R.id.unit);
        tvDescription = (TextView) findViewById(R.id.description);
        tvUrl = (TextView) findViewById(R.id.url);
        tvUpdatedOn = (TextView) findViewById(R.id.updatedOn);

        // populate textviews with text
        tvPeriod.setText(period);
        tvIndexValue.setText(value);
        tvChangeType.setText(change_type);
        tvChangeValue.setText(change_value);
        //tvHeadline.setText(headline);
        tvUnit.setText(unit);
        tvDescription.setText(description);
        tvUrl.setText(url);
        tvUpdatedOn.setText(updated_on);

        // disable description field

        // tvDescription.setEnabled(false);



    }
}