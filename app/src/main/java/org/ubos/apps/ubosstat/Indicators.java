package org.ubos.apps.ubosstat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ubos.apps.ubosstat.utility.Global;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import adapters.Rcycview_ind;
import db.IndicatorsDataSource;
import model.Indicator;
import model.SyncIndicator;

import static adapters.Rcycview.ITEM_KEY;

public class Indicators extends AppCompatActivity {

    IndicatorsDataSource datasource;
    Context mContext;
    //private static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";
    private static final String ENDPOINT = Global.ENDPOINT;
    private static final String ENDPOINT_CATEGORIES = Global.ENDPOINT + "/index_get_categories.php";

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

        datasource = new IndicatorsDataSource(this);
        datasource.open();

        requestQueue = Volley.newRequestQueue(this);

        System.out.print("Tab one...");

        // setup categories table

        // get category specific indicators

        String category_id = String.valueOf(s);

        String cat = getIntent().getStringExtra("CAT_TITLE");

        this.setTitle(" "+ cat +" ") ;

        List<Indicator> tours = datasource.getSpecificCategories(category_id);
        List<Indicator> f = new List<Indicator>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Indicator> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(Indicator indicator) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Indicator> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, @NonNull Collection<? extends Indicator> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Indicator get(int i) {
                return null;
            }

            @Override
            public Indicator set(int i, Indicator indicator) {
                return null;
            }

            @Override
            public void add(int i, Indicator indicator) {

            }

            @Override
            public Indicator remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Indicator> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Indicator> listIterator(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<Indicator> subList(int i, int i1) {
                return null;
            }
        };


        if (tours.size() > 0) {

            Log.i("recordset count", "get record count" + tours.size());
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_recycler_indicators_view);
            //RecyclerAdapter adapter = new RecyclerAdapter(getContext(), Landscape.getData());
            for (Indicator i : tours) {
                Log.d("ECHO ",i.getTitle()+"Cat:"+i.getCat_id());
                if (i.getCat_id().equals("0")) {
                    f.add(i);
                    Log.d("ECHO ","Category Added"+i.getData());
                }
            }
            Rcycview_ind adapter = new Rcycview_ind(this, tours);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

            recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we don't use it then also our items shows de
        }
        if (tours.size() == 0) {

            Log.i("recordset count", "count 0");
            fetchPosts();
        }


/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
                Log.i("Indicators", syncIndicator.indicatorId + ": " + syncIndicator.title + "cat_id" + syncIndicator.cat_id);
                System.out.println("Indicators" + syncIndicator.indicatorId + ": " + syncIndicator.title + syncIndicator.change_type);
                //  datasource.create(syncIndicator);
                datasource.insertIndicator(syncIndicator.indicatorId ,syncIndicator.title, syncIndicator.headline, syncIndicator.summary, syncIndicator.unit, syncIndicator.description, syncIndicator.data, syncIndicator.period, syncIndicator.url, syncIndicator.updated_on, syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);

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
