package fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ubos.apps.ubosstat.MainActivity;
import org.ubos.apps.ubosstat.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.Rcycview;
import db.IndicatorsDataSource;
import model.Indicator;
import model.SyncIndicator;

public class TabsFragmentOne extends Fragment {

    IndicatorsDataSource datasource;
    Context mContext;
    //private static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";
    private static final String SERVER_IP = "http://192.168.8.101/ubos_app";
    private static final String ENDPOINT = SERVER_IP;
    private static final String ENDPOINT_CATEGORIES = "http://192.168.8.101/ubos_app/index_get_categories.php";

    private RequestQueue requestQueue;

    private Gson gson;

    Cursor cursor;

    private static final int URL_LOADER = 0;

    List<SyncIndicator> indicatorsAlreadyDownloaded = new ArrayList<SyncIndicator>();

    public TabsFragmentOne() {
        Log.i("Fragment Check", "Fragment One Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);


        datasource = new IndicatorsDataSource(getContext());
        datasource.open();

        requestQueue = Volley.newRequestQueue(getContext());


        System.out.print("Tab one...");
        //     Toast.makeText(getContext(),"Hello ... " ,
        //         Toast.LENGTH_SHORT).show();

        // setup categories table

// Manually set the key indicators Category ID

        String KE_CAT_ID = "1";

        List<Indicator> tours = datasource.findAll(KE_CAT_ID);


        if (tours.size() > 0) {

            //check for any new items
            //    fetchNewPosts();

            Log.i("recordset count", "get record count" + tours.size());
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
            //RecyclerAdapter adapter = new RecyclerAdapter(getContext(), Landscape.getData());
            Rcycview adapter = new Rcycview(getContext(), tours);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext()); // (Context context, int spanCount)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

            recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we don't use it then also our items shows de
        }
        if (tours.size() == 0) {

            Log.i("recordset count", "count 0");
            fetchPosts();
        }


        datasource.close();
        return rootView;
    }


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }


    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();

            // Log.i("PostActivity", response );
            //List<Indicators> indicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));
            List<SyncIndicator> syncIndicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));

            // store list of downloaded and inserted in native DB
            indicatorsAlreadyDownloaded = syncIndicators;
            Log.i("PostActivity", syncIndicators.size() + " indicators loaded.");
            for (SyncIndicator syncIndicator : syncIndicators) {
                Log.i("Indicators", syncIndicator.indicatorId + ": " + syncIndicator.title + "cat_id" + syncIndicator.cat_id);
                System.out.println("Indicators" + syncIndicator.indicatorId + ": " + syncIndicator.title + syncIndicator.change_type);
                //  datasource.create(syncIndicator);
                datasource.insertIndicator(syncIndicator.title, syncIndicator.headline, syncIndicator.summary, syncIndicator.unit, syncIndicator.description, syncIndicator.data, syncIndicator.period, syncIndicator.url, syncIndicator.updated_on, syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);

                // Reload - update the recycler view
                reloadActivity();


            }

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

// check for new indicators

    private void fetchNewPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoadedNew, onPostsErrorNew);
        requestQueue.add(request);
    }


    private final Response.Listener<String> onPostsLoadedNew = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();

            // Log.i("PostActivity", response );
            //List<Indicators> indicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));
            List<SyncIndicator> syncNewIndicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));

            System.out.println("check for new indicators...");

            Log.i("New PostActivity", syncNewIndicators.size() + " New indicators loaded.");

            // System.out.println("Current Indicators ...:"+indicatorsAlreadyDownloaded.size());

            /**
             for (SyncIndicator syncIndicator : syncIndicators) {
             Log.i("Indicators", syncIndicator.indicatorId + ": " + syncIndicator.title + "cat_id" +syncIndicator.cat_id);
             System.out.println("Indicators"+syncIndicator.indicatorId + ": " + syncIndicator.title + syncIndicator.change_type);
             //  datasource.create(syncIndicator);
             // datasource.insertIndicator(syncIndicator.title,syncIndicator.headline,syncIndicator.summary,syncIndicator.unit,syncIndicator.description,syncIndicator.data,syncIndicator.period,syncIndicator.url,syncIndicator.updated_on,syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);

             }

             **/

        }
    };

    private final Response.ErrorListener onPostsErrorNew = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };


    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getContext(), MainActivity.class);
        startActivity(objIntent);
    }
}
