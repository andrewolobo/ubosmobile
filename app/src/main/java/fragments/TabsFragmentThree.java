package fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.config.global;
import org.ubos.apps.ubosstat.utility.Global;
import org.ubos.apps.ubosstat.widgets.Pie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import utility.json.CDS;
import utility.json.data;

public class TabsFragmentThree extends Fragment {

    public TabsFragmentThree() {
        Log.i("Fragment Check", "Fragment Three Created");
    }

    public Context context;
    File fi;
    public static LinearLayout layout;
    public static View view;
    Gson g = new Gson();
    Handler m = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                DecimalFormat formatter = new DecimalFormat("##,###,###");

                TextView counter = (TextView) layout.findViewById(R.id.counter);
                Integer value = Integer.parseInt(counter.getText().toString().replace(",",""));

                value += 1;
                String fString = formatter.format(value);
                counter.setText(fString);


            }
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        AsyncHttpClient client = new AsyncHttpClient();
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        View counter = inflater.inflate(R.layout.a_counter_two, container, false);
        //layout.addView(inflater.inflate(R.layout.a_title, container, false));
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                m.sendEmptyMessage(1);
            }
        }, 37000,37000);
        layout.addView(counter);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Urban", "1812696");
        map.put("Rural", "5494246");
        Pie chart = new Pie(context, (PieChart) inflater.inflate(R.layout.a_piechart, container, false), map);
        layout.addView(inflater.inflate(R.layout.a_titlesection, container, false));
        layout.addView(chart.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800));
        layout.addView(inflater.inflate(R.layout.separator, container, false));


        layout.addView(inflater.inflate(R.layout.a_mfcompare, container, false));

        view = inflater.inflate(R.layout.fragment_three, container, false);

        layout.addView(inflater.inflate(R.layout.a_quickstat, container, false));

        map = new HashMap<String, String>();
        map.put("Literate","72");
        map.put("Illiterate","28");

        chart = new Pie(context, (PieChart) inflater.inflate(R.layout.a_piechart, container, false), map);
        layout.addView(inflater.inflate(R.layout.a_literacy, container, false));
        layout.addView(chart.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900));


        layout.addView(inflater.inflate(R.layout.a_addann, container, false));
        client.addHeader("Content-Type", "application/json");
        fi = new File("/data/data/org.ubos.apps.ubosstat/census.json");
        if ((fi.exists())) {
            try {
                String file = global.read(new FileInputStream(fi));
                Type listType = new TypeToken<ArrayList<CDS>>() {
                }.getType();
                ArrayList<CDS> data = g.fromJson(file, listType);
                for (CDS i : data) {
                    int index = 0;
                    View title_banner = inflater.inflate(R.layout.a_section_title, null);
                    TextView title = (TextView) title_banner.findViewById(R.id.title);
                    title.setText(i.name);
                    layout.addView(title_banner);
                    for (data d : i.data) {
                        int l = index % 2 == 0 ? R.layout.a_section_data : R.layout.a_section_data_light;
                        View data_view = inflater.inflate(l, null);
                        TextView ti_tle = (TextView) data_view.findViewById(R.id.title);
                        TextView description = (TextView) data_view.findViewById(R.id.description);
                        ti_tle.setText(d.title);
                        description.setText(d.description);
                        layout.addView(data_view);
                        index++;

                    }

                }
                ScrollView base = (ScrollView) view.findViewById(R.id.base);
                base.removeAllViews();
                base.addView(layout);
            } catch (Exception e) {

            }
        }
        //Create a settings file
        client.get(Global.census_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Type listType = new TypeToken<ArrayList<CDS>>() {
                }.getType();
                ArrayList<CDS> data = g.fromJson(new String(responseBody), listType);
                if (!(fi.exists())) {
                    try {
                        fi.createNewFile();
                        FileOutputStream write = new FileOutputStream(fi);
                        write.write(responseBody);
                    } catch (Exception e) {
                        Toast.makeText(context, "Failed to write file, Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                for (CDS i : data) {
                    int index = 0;
                    View title_banner = inflater.inflate(R.layout.a_section_title, null);
                    TextView title = (TextView) title_banner.findViewById(R.id.title);
                    title.setText(i.name);
                    layout.addView(title_banner);
                    for (data d : i.data) {
                        int l = index % 2 == 0 ? R.layout.a_section_data : R.layout.a_section_data_light;
                        View data_view = inflater.inflate(l, null);
                        TextView ti_tle = (TextView) data_view.findViewById(R.id.title);
                        TextView description = (TextView) data_view.findViewById(R.id.description);
                        ti_tle.setText(d.title);
                        description.setText(d.description);
                        layout.addView(data_view);
                        index++;

                    }

                }
                ScrollView base = (ScrollView) view.findViewById(R.id.base);
                base.removeAllViews();
                base.addView(layout);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Preliminary no data error
                if (!(fi.exists())) {
                    TextView fail = new TextView(context);
                    fail.setText("You internet connection appears to have a problem");
                    layout.addView(fail);
                    ScrollView base = (ScrollView) view.findViewById(R.id.base);
                    base.addView(layout);
                    Toast.makeText(context, "You internet connection appears to have a problem", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String file = global.read(new FileInputStream(fi));
                        Type listType = new TypeToken<ArrayList<CDS>>() {
                        }.getType();
                        ArrayList<CDS> data = g.fromJson(file, listType);
                        for (CDS i : data) {
                            int index = 0;
                            View title_banner = inflater.inflate(R.layout.a_section_title, null);
                            TextView title = (TextView) title_banner.findViewById(R.id.title);
                            title.setText(i.name);
                            layout.addView(title_banner);
                            for (data d : i.data) {
                                int l = index % 2 == 0 ? R.layout.a_section_data : R.layout.a_section_data_light;
                                View data_view = inflater.inflate(l, null);
                                TextView ti_tle = (TextView) data_view.findViewById(R.id.title);
                                TextView description = (TextView) data_view.findViewById(R.id.description);
                                ti_tle.setText(d.title);
                                description.setText(d.description);
                                layout.addView(data_view);
                                index++;

                            }

                        }
                        ScrollView base = (ScrollView) view.findViewById(R.id.base);
                        base.addView(layout);
                    } catch (Exception e) {

                    }

                }

            }
        });
        return view;
    }
}
