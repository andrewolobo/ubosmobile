package fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.utility.Global;

import java.util.Arrays;
import java.util.List;

import adapters.Rcycview_cat;
import db.IndicatorsDataSource;
import model.Category;
import model.SyncCategories;

public class TabsFragmentTwo extends Fragment {

    IndicatorsDataSource datasource;
    Context mContext ;
    //private static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";
    private static final String ENDPOINT = Global.ENDPOINT;
    private static final String ENDPOINT_CATEGORIES = Global.ENDPOINT+"/index_get_categories.php";

    private RequestQueue requestQueue;

    private Gson gson;

    Cursor cursor;

    private static final int URL_LOADER = 0;


    public TabsFragmentTwo() {
        Log.i("Fragment Check", "Fragment Two Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // return inflater.inflate(R.layout.fragment_two, container, false);

        View rootView = inflater.inflate(R.layout.fragment_two, container, false);


        datasource = new IndicatorsDataSource(getContext());
        datasource.open();

        requestQueue = Volley.newRequestQueue(getContext());

        System.out.print("Tab two...");

        // setup categories table

        List<Category> tours = datasource.findAllCategories();



        if(tours.size() > 0){


            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recycler_cat_view);
            //RecyclerAdapter adapter = new RecyclerAdapter(getContext(), Landscape.getData());
            Rcycview_cat  adapter = new Rcycview_cat(getContext(), tours);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext()); // (Context context, int spanCount)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

            recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we don't use it then also our items shows de
        }
        if (tours.size() == 0) {


            if (isOnline()) {
              fetchPosts();
            }
            else
            {
                Toast.makeText(getContext(),"Your Internet connection seems to be off. Please turn it on " , Toast.LENGTH_LONG).show();

            }
        }



        datasource.close();
        return rootView;


    }


    // restful calls to load categories for remote database

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT_CATEGORIES, onCategoriesLoaded, onPostsCategoryError);
        requestQueue.add(request);
    }


    private final Response.Listener<String> onCategoriesLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();

            // Log.i("PostActivity", response );
            //List<Indicators> indicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));
            List<SyncCategories> syncCategories = Arrays.asList(gson.fromJson(response, SyncCategories[].class));

            for (SyncCategories syncCategory : syncCategories) {

                //  datasource.create(syncIndicator);
                datasource.insertCategories(syncCategory.cat_id,syncCategory.cat_name);

            }

        }
    };

    private final Response.ErrorListener onPostsCategoryError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
