package fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ubos.apps.ubosstat.MainActivity;
import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.utility.Global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.Rcycview;
import db.IndicatorsDataSource;
import model.Indicator;
import model.SyncIndicator;
import services.SampleBC;

import static android.R.attr.fragment;
import static android.content.Context.ALARM_SERVICE;

public class TabsFragmentOne extends Fragment {

    IndicatorsDataSource datasource, datasource2;
    Context mContext;
    //private static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";
    private static final String SERVER_IP = "http://192.168.8.101/ubos_app";
    private static final String ENDPOINT = SERVER_IP;
    private static final String ENDPOINT_CATEGORIES = "http://192.168.8.101/ubos_app/index_get_categories.php";
    public  RecyclerView recyclerView;
    Cursor indicatorItems;
    private RequestQueue requestQueue;
    private static final String json_updates_for_indicators = Global.json_updates_for_indicators;
    private Gson gson;
    HashMap<String, String> queryValues;
    Cursor cursor;
    private Boolean exit = false;
    private static final int URL_LOADER = 0;

    List<SyncIndicator> indicatorsAlreadyDownloaded = new ArrayList<SyncIndicator>();

    public TabsFragmentOne() {
        Log.i("Fragment Check", "Fragment One Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
           recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
            //RecyclerAdapter adapter = new RecyclerAdapter(getContext(), Landscape.getData());
            Rcycview adapter = new Rcycview(getContext(), tours);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext()); // (Context context, int spanCount)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

          //  recyclerView.swapAdapter(adapter, false);
// recyclerView.invalidate();

           // recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we don't use it then also our items shows de
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return true;
        }
        if (id == R.id.action_update) {




            new UpdateTask().execute();

            // restart activity

            // Reload current fragment
        //      Fragment frg = null;
          //   frg = getFragmentManager().findFragmentById(R.id.fragment_my_frame_layout);
            // String tag = (String) frg.getTag();
            //FragmentTransaction ft = getFragmentManager().beginTransaction();
            //ft.detach(frg);
            // ft.attach(frg);
            // ft.commit();

    //   Toast.makeText(getContext(),"fragment name"+tag, Toast.LENGTH_LONG).show();


                //   adapter.notifyData(indicators);
           // Toast.makeText(getContext(),"Hello ... " , Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // backgound task to check for new updates
    class UpdateTask extends AsyncTask<Void, Void, Void> {

        //  ProgressDialog dialog;
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getContext());
            pd.setMessage("Checking for Updates");
            pd.show();

            System.out.println("onPreExecute: ... Has been done");


        }

        //  @Nullable
        // @Override
        protected Void doInBackground(Void... arg0) {

            checkUpdates();

            return null;
        }


        // @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            System.out.println("onPostExecute:..."+result);

            datasource2 = new IndicatorsDataSource(getContext());
            datasource2.open();
            List<Indicator> indicators = datasource2.findAll("1");
            //RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
            Rcycview adapter = new Rcycview(getContext(), indicators);




            recyclerView.swapAdapter(adapter, false);
            // Reload current fragment
         //   Fragment frg = null;
           // frg = getFragmentManager().findFragmentById(R.id.fragment_my_frame_layout);
            //String tag = (String) frg.getTag();
            //FragmentTransaction ft = getFragmentManager().beginTransaction();
            //ft.detach(frg);
           // ft.attach(frg);
           // ft.commit();

//             Toast.makeText(getContext(),"fragment name"+tag, Toast.LENGTH_LONG).show();

           // recyclerView.invalidate();

            if (pd != null)
            {
                pd.dismiss();

            }
            //  reloadActivity();
        }
    }

    private void checkUpdates() {



        datasource = new IndicatorsDataSource(getContext());
        datasource.open();

        indicatorItems = datasource.findAllIndicators();

        System.out.println("No. of Indicators " + indicatorItems.getCount());

        if (indicatorItems != null) {


            if (indicatorItems.moveToFirst()) {
                do {
                    //    indicatorItems.getString(indicatorItems.getColumnIndex("title")); // "Title" is the field name(column) of the Table
                    //   System.out.println("Update Title.. " + indicatorItems.getString(indicatorItems.getColumnIndex("updated_on")));
                 //   timeStampToMilliSeconds(indicatorItems.getString(indicatorItems.getColumnIndex("updated_on")));
                 //   nativeDBTimeStamp = timeStampToMilliSeconds(indicatorItems.getString(indicatorItems.getColumnIndex("updated_on")));
                    //   System.out.println("Native MilliSeconds.. " + nativeDBTimeStamp);
                    syncSQLiteMySQLDB(indicatorItems.getString(indicatorItems.getColumnIndex("indicatorId")), indicatorItems.getString(indicatorItems.getColumnIndex("updated_on")));
                } while (indicatorItems.moveToNext());
            }


        }


    }

    // check for updates for each indicator

    public void syncSQLiteMySQLDB(final String id, final String timestamp) {

        System.out.println("checking for Indicator Updates");

        // prgDialog.show();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, json_updates_for_indicators,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //    Gson gson = new GsonBuilder().create();

                        /**
                         try{

                         JSONArray arr = new JSONArray(response);
                         System.out.println("Outlet Sync status" + arr.length() + " .." + response);

                         updateOutlets(response);


                         }
                         catch (JSONException e)
                         {
                         e.printStackTrace();
                         }\
                         **/

                        System.out.println("Indicator Sync Status" + response);
                        // Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();

                        showIndicatorJson(response);
                        // mItemAdapter.notifyDataSetChanged();

                        /**
                         Intent mainIntent = new Intent(OutletsActivity2.this, MainActivity.class);
                         startActivity(mainIntent);
                         **/

                        //reload recyclerview on item update

/**
                        datasource2 = new IndicatorsDataSource(getContext());
                        datasource2.open();
                        // notify adapter to change
                        List<Indicator> indicators = datasource2.findAll("1");
                        //RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
                        Rcycview adapter2 = new Rcycview(getContext(), indicators);


                        //LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext()); // (Context context, int spanCount)
                        //  mLinearLayoutManagerVertical.scrollToPositionWithOffset(count - 1, viewHeight)
                        //mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                        // recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                        recyclerView.swapAdapter(adapter2, false);
 **/
                        //  recyclerView.setAdapter(adapter);
                        // System.out.print("notify adapter");
                        //   adapter.notifyDataSetChanged();


                        //RecyclerAdapter adapter = new RecyclerAdapter(getContext(), Landscape.getData());
                        // Rcycview adapter = new Rcycview(getContext(), tours);


                        //   recyclerView.invalidate();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                System.out.println("query values" + id + timestamp);

                params.put("id", id);
                params.put("timestamp", timestamp);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    // handle sync json for outlets
    public void showIndicatorJson(String response) {
        Log.d("TAG", response);

        ArrayList<HashMap<String, String>> indicatorsynclist;
        indicatorsynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println("response Length" + arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println("Unsync Returns" + obj.get("id") + obj.get("timestamp") + "Status" + obj.get("update"));

                    String update_status = obj.getString("update");

                    System.out.println("Remote Up :" + update_status);

                    if (update_status.equals("true")) {
                        System.out.println("Update Available");

                        // update indicator item
                        updateIndicator(response);
                        // update items  from here
                    } else {
                        System.out.println("Item  is up to date");
                    }

                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

// update native indicator items

    public void updateIndicator(String response) {
        ArrayList<HashMap<String, String>> itemsynclist;
        itemsynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println("Item update json :" + response);
            System.out.println(arr.length());
            // If no of array elements is not zero

            ArrayList<String> UpdateIndicators = new ArrayList<String>();

            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username


                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);

                    // populate arraylist

//                    UpdateIndicators.add(obj.optJSONObject(i));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    System.out.println("update item ID" + obj.getString("id"));

                    //obj.get("id");

                    queryValues.put("indicatorId", obj.getString("id"));

                    queryValues.put("title", obj.getString("title"));

                    queryValues.put("headline", obj.getString("headline"));
                    queryValues.put("summary", obj.getString("summary"));
                    queryValues.put("unit", obj.getString("unit"));
                    queryValues.put("description", obj.getString("description"));
                    queryValues.put("data", obj.getString("data"));
                    queryValues.put("period", obj.getString("period"));
                    queryValues.put("url", obj.getString("url"));

                    queryValues.put("updated_on", obj.getString("updated_on"));
                    queryValues.put("change_type", obj.getString("change_type"));
                    queryValues.put("change_value", obj.getString("change_value"));
                    queryValues.put("change_desc", obj.getString("change_desc"));

                    queryValues.put("index_value", obj.getString("index_value"));
                    queryValues.put("cat_id", obj.getString("cat_id"));

                    // Insert User into SQLite DB

                    // Store the indicator to update

                    /**
                     *
                     *
                     * **
                     *
                     *  // UpdateIndicators.add(obj.getString("id"),
                     // obj.getString("title"),
                     // obj.getString("headline"),
                     // obj.getString("summary"),
                     // obj.getString("unit"),
                     // obj.getString("description"),
                     // obj.getString("data"),
                     // obj.getString("period"),
                     // obj.getString("url"),
                     // obj.getString("updated_on"),
                     // obj.getString("change_type"),
                     // obj.getString("change_value"),
                     // obj.getString("change_desc"),
                     // obj.getString("index_value"),
                     // obj.getString("cat_id"));
                     */

                    UpdateIndicators.add(obj.getString("id"));
                    UpdateIndicators.add(obj.getString("title"));
                    UpdateIndicators.add(obj.getString("headline"));
                    UpdateIndicators.add(obj.getString("summary"));
                    UpdateIndicators.add(obj.getString("unit"));
                    UpdateIndicators.add(obj.getString("description"));
                    UpdateIndicators.add(obj.getString("data"));
                    UpdateIndicators.add(obj.getString("period"));
                    UpdateIndicators.add(obj.getString("url"));
                    UpdateIndicators.add(obj.getString("updated_on"));
                    UpdateIndicators.add(obj.getString("change_type"));
                    UpdateIndicators.add(obj.getString("change_value"));
                    UpdateIndicators.add(obj.getString("change_desc"));
                    UpdateIndicators.add(obj.getString("index_value"));
                    UpdateIndicators.add(obj.getString("cat_id"));

                    //  UpdateIndicators.add(obj.getString("id"));
                    // UpdateIndicators.add(obj.getString("title"));

                    int update_flag = datasource.updateIndicator(queryValues);

                    System.out.println("update flag :" + update_flag);

                    if (update_flag == 1) {
                        System.out.println("yeah....");
                        Log.d("update status", "update successful");

                    } else {
                        System.out.println("no yeah....");
                        Log.d("update status", "not unsuccessful");
                    }
                    // controller.insertUser(queryValues);
                    //HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    //map.put("outlet_id", obj.get("outlet_id").toString());
                    //map.put("itemId", obj.get("id").toString());
                    //map.put("status", "1");
                    //  itemsynclist.add(map);
                }

                // iterate though arraylist

                int count = 0;
                while (UpdateIndicators.size() > count) {
                    System.out.println("Show updates...:" + UpdateIndicators.get(count));
                    count++;
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                //updateMySQLItemSyncSts(gson.toJson(itemsynclist));
                // Reload the Main Activity
                // reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


