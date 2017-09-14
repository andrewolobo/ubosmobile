package org.ubos.apps.ubosstat;


import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.ubos.apps.ubosstat.config.utility;
import org.ubos.apps.ubosstat.data.BarChartView;
import org.ubos.apps.ubosstat.json.Graphs;
import org.ubos.apps.ubosstat.utility.Global;
import org.ubos.apps.ubosstat.widgets.Datatable;
import org.ubos.apps.ubosstat.widgets.LineChartView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static adapters.Rcycview.ITEM_KEY;


public class DetailVActivity extends AppCompatActivity {

    private TextView tvTitle, tvPeriod, tvIndexValue, tvChangeType, tvChangeValue, tvUnit, tvHeadline, tvDescription, tvUrl, tvUpdatedOn, titleH;
    public Context context;
    public boolean image_toggle;
    static String download_id;
    public boolean distinct;
    public static String download_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        distinct = false;
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        download_url = url;
        String updated_on = getIntent().getStringExtra("ITEM_UPDATED_ON");
        String unit = getIntent().getStringExtra("ITEM_UNIT");
        String cat = getIntent().getStringExtra("ITEM_CAT");
        String data = getIntent().getStringExtra("ITEM_DATA");
        String change = getIntent().getStringExtra("ITEM_CHANGE");

        System.out.println("unit show "+ change);


        //setContentView(R.layout.activity_detail_i);
        setContentView(R.layout.activity_detail_i_r);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.main_title);
        LinearLayout toggle = (LinearLayout) findViewById(R.id.toggle);
     //   final LinearLayout download = (LinearLayout) findViewById(R.id.download);

        utility util = new utility();
        ;
        String file_data = util.readFile(context, "table.json");
        Gson g = new Gson();
        Type listType = new TypeToken<ArrayList<Graphs>>() {
        }.getType();

        ArrayList<Graphs> result = g.fromJson(data, listType);
        download_id = result.get(0).id;
        Log.d("ECHO", "Drew the Graph");
        Log.d("DATA", data);
        final BarChartView chart1 = new BarChartView(context, result.get(0));
        final LineChartView chart2 = new LineChartView(context, LineChartView.generateData(result.get(0)));
        if (unit.equals("Index")) {
            distinct = false;
            layout.addView(chart2.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900));

        } else {
            distinct = true;
            layout.addView(chart1.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800));

        }


        final Datatable datatable = new Datatable(context);
        boolean color_t = false;
        datatable.setHeader(result.get(0).title);
        listType = new TypeToken<ArrayList<String[]>>() {
        }.getType();
        ArrayList<String[]> rows = g.fromJson(result.get(0).data, listType);
        datatable.addRow(rows);

        //layout.addView(datatable);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image_toggle) {
                    ((ImageView) findViewById(R.id.toggle_i)).setImageResource(R.drawable.graph_ico_i);
                    ((TextView) findViewById(R.id.text_table)).setText("Show Graph");
                    layout.removeAllViews();
                    layout.addView(datatable);
                } else {
                    ((ImageView) findViewById(R.id.toggle_i)).setImageResource(R.drawable.table_ico_i);
                    ((TextView) findViewById(R.id.text_table)).setText("Show Table");
                    layout.removeAllViews();
                    if (distinct) {
                        layout.addView(chart1.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800));
                    } else {
                        layout.addView(chart2.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800));
                    }
                }
                image_toggle = !image_toggle;

            }
        });
        /**
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Downloading you're file",Toast.LENGTH_LONG).show();
                download_r(download_url);
            }
        });
         **/

        //LineChartView chart2 = new LineChartView(context,LineChartView.generateData(result.get(0)));
        //layout.addView(chart2.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900));


        //Toast.makeText(getApplicationContext(), "Indicators ID: " + s + "TITLE"+title + "PERIOD"+period+"VALUE"+value, Toast.LENGTH_LONG).show();

        this.setTitle(" " + title + " ");
        tvPeriod = (TextView) findViewById(R.id.period);
        titleH = (TextView) findViewById(R.id.title);
        tvIndexValue = (TextView) findViewById(R.id.indexValue);
        tvChangeType = (TextView) findViewById(R.id.changeType);
        tvChangeValue = (TextView) findViewById(R.id.changeValue);
        //tvHeadline = (TextView) findViewById(R.id.headline);
        tvUnit = (TextView) findViewById(R.id.unit);
        tvDescription = (TextView) findViewById(R.id.description);
        tvUrl = (TextView) findViewById(R.id.url);
        tvUpdatedOn = (TextView) findViewById(R.id.updatedOn);

        if (unit.equals("Index")) {
            tvUnit.setVisibility(View.INVISIBLE);
        }


        titleH.setText(title);
        //tvPeriod.setText(period);
        //tvIndexValue.setText(value);
        //tvChangeType.setText(change_type);
         tvChangeValue.setText(change);
        //tvHeadline.setText(headline);
        tvUnit.setText(unit);
        tvDescription.setText(description);
        tvUrl.setText(url);
        tvUpdatedOn.setText(updated_on);


        // populate textviews with text
        // disable description field
        // tvDescription.setEnabled(false);
    }

    public void show_notification(String Title) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        intent.setDataAndType(uri, "text/csv");

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = 9001;
        PendingIntent touchIntent = PendingIntent.getActivity(getApplication(), 0,
                intent, 0);


        mNotifyBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("UGSTAT")
                .setContentText("Click to open file")
                .setSmallIcon(R.drawable.ic_download_black_48dp)
                .setContentIntent(touchIntent);

        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        mNotifyBuilder.setDefaults(defaults);

        mNotifyBuilder.setAutoCancel(true);
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());

    }

    private void download_r(String r) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.get(r, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                InputStream dis = new ByteArrayInputStream(responseBody);

                byte[] buffer = new byte[1024];
                int length;
                try {
                    FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + "Download/excel.csv"));
                    while ((length = dis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    final Dialog d = new Dialog(context);
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.download_layout);
                    d.show();
                    View.OnClickListener Ons = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            show_notification("");
                            d.cancel();
                        }
                    };
                    Button b = (Button) d.findViewById(R.id.button);
                    Button c = (Button) d.findViewById(R.id.button2);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            Uri uri = Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                            intent.setDataAndType(uri, "text/csv");
                            startActivity(Intent.createChooser(intent,"Open folder"));
                            d.cancel();
                        }
                    });
                    c.setOnClickListener(Ons);


                }  catch (IOException ioe) {
                    Log.e("SYNC getUpdate", "io error", ioe);
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(context, "You internet connection appears to have a problem", Toast.LENGTH_SHORT).show();
            }
        });
    }

}