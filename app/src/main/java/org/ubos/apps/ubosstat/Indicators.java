package org.ubos.apps.ubosstat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import adapters.Rcycview;
import adapters.Rcycview_ind;
import db.IndicatorsDataSource;
import model.Indicator;
import model.SyncIndicator;

import static adapters.Rcycview.ITEM_KEY;
import static java.security.AccessController.getContext;

public class Indicators extends AppCompatActivity {

    IndicatorsDataSource datasource;
    Context mContext ;
    //private static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";
    private static final String ENDPOINT = "http://192.168.43.53/ubos_app";
    private static final String ENDPOINT_CATEGORIES = "http://192.168.43.53/ubos_app/index_get_categories.php";

    private RequestQueue requestQueue;

    private Gson gson;

    Cursor cursor;

    private static final int URL_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Long s = getIntent().getLongExtra(ITEM_KEY, 1);
        Toast.makeText(getApplicationContext(), "Indicators ID: " + s, Toast.LENGTH_LONG).show();

        datasource = new IndicatorsDataSource(this);
        datasource.open();

        requestQueue = Volley.newRequestQueue(this);

        System.out.print("Tab one...");
        Toast.makeText(this,"Hello ... " ,
                Toast.LENGTH_SHORT).show();

        // setup categories table

          // get category specific indicators

        String category_id = String.valueOf(s);

        List<Indicator> tours = datasource.getSpecificCategories(category_id);



        if(tours.size() > 0){

            Log.i("recordset count" , "get record count" + tours.size()  );
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_recycler_indicators_view);
            //RecyclerAdapter adapter = new RecyclerAdapter(getContext(), Landscape.getData());
            Rcycview_ind adapter = new Rcycview_ind(this, tours);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

            recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we don't use it then also our items shows de
        }
        if (tours.size() == 0) {

            Log.i("recordset count" , "count 0");
            fetchPosts();
        }





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        datasource.close();
    }

    // query database to get indicators that are category specific

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

            Log.i("PostActivity", syncIndicators.size() + " indicators loaded.");
            for (SyncIndicator syncIndicator : syncIndicators) {
                Log.i("Indicators", syncIndicator.indicatorId + ": " + syncIndicator.title + "cat_id" +syncIndicator.cat_id);
                System.out.println("Indicators"+syncIndicator.indicatorId + ": " + syncIndicator.title + syncIndicator.change_type);
                //  datasource.create(syncIndicator);
                datasource.insertIndicator(syncIndicator.title,syncIndicator.headline,syncIndicator.summary,syncIndicator.unit,syncIndicator.description,syncIndicator.data,syncIndicator.period,syncIndicator.url,syncIndicator.updated_on,syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);

            }

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };


}
