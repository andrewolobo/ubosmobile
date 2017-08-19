package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.utility.Global;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import utility.json.CDS;
import utility.json.data;

public class TabsFragmentThree extends Fragment {

    public TabsFragmentThree() {
        Log.i("Fragment Check", "Fragment Three Created");
    }
    public Context context;
    public static LinearLayout layout;
    public static View view;
    Gson g = new Gson();
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        AsyncHttpClient client = new AsyncHttpClient();
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        view = inflater.inflate(R.layout.fragment_three, container, false);
        client.addHeader("Content-Type","application/json");
        client.get(Global.census_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Type listType = new TypeToken<ArrayList<CDS>>(){}.getType();
                ArrayList<CDS> data = g.fromJson(new String(responseBody),listType);

                for(CDS i : data){
                    int index = 0;
                    View title_banner = inflater.inflate(R.layout.a_section_title,null);
                    TextView title = (TextView)title_banner.findViewById(R.id.title);
                    title.setText(i.name);
                    layout.addView(title_banner);
                    for(data d: i.data){
                        int l = index%2==0?R.layout.a_section_data:R.layout.a_section_data_light;
                        View data_view = inflater.inflate(l,null);
                        TextView ti_tle = (TextView)data_view.findViewById(R.id.title);
                        TextView description = (TextView)data_view.findViewById(R.id.description);
                        ti_tle.setText(d.title);
                        description.setText(d.description);
                        layout.addView(data_view);
                        index++;

                    }

                }
                ScrollView base = (ScrollView) view.findViewById(R.id.base);
                base.addView(layout);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Preliminary no data error
                TextView fail = new TextView(context);
                fail.setText("You internet connection appears to have a problem");
                layout.addView(fail);
                ScrollView base = (ScrollView) view.findViewById(R.id.base);
                base.addView(layout);
                Toast.makeText(context,"You internet connection appears to have a problem", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
