package org.ubos.apps.ubosstat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import org.ubos.apps.ubosstat.utility.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.TextTabsAdapter;
import database.DataSource;
import db.IndicatorsDBOpenHelper;
import db.IndicatorsDataSource;
import fragments.TabsFragmentOne;
import fragments.TabsFragmentThree;
import model.DataItem;
import model.Indicator;
import model.SyncIndicator;
import services.SampleBC;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    public Context context;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private static final String TAG_UPDATE = "MainActivity_Updates";
    private ViewPager viewPager;
    private TextTabsAdapter adapter;
    private TabLayout tabLayout;
    private ArrayList nativeList = new ArrayList();

    ArrayList<DataItem> nativeIndicators = new ArrayList<DataItem>();

    IndicatorsDataSource datasource;
    Cursor indicatorItems, nativeIndicatorItems, updateNativeIndicators;
    long nativeDBTimeStamp;

    private static final String ENDPOINT = Global.ENDPOINT;
    private static final String json_updates_for_indicators = Global.json_updates_for_indicators;

    ArrayList<String> storeUpdateIndicators = new ArrayList<String>();
    private RequestQueue requestQueue;
    HashMap<String, String> queryValues;
    private Gson gson;

    private ArrayList<Indicator> columnArray3 = new ArrayList<Indicator>();

    DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        datasource = new IndicatorsDataSource(this);
        datasource.open();

        AlarmManager mgrAlarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

        for (int i = 1; i < 2; ++i) {

            // check for updates



            if (i == 1) {
              //  checkUpdates();
                int update_flag = 1 ;

                new MainActivity.UpdateTask().execute();
               // reloadActivity();
            }

            System.out.println("alarm counter" + i);

            Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);

            intentArray.add(pendingIntent);
        }






        if (isOnline()) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }


        initialise();

        prepareDataResource();

        adapter = new TextTabsAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        nativeIndicatorItems = datasource.findAllIndicators();

        System.out.println("Native Items receord count" + nativeIndicatorItems.getCount());
        for (nativeIndicatorItems.moveToFirst(); !nativeIndicatorItems.isAfterLast(); nativeIndicatorItems.moveToNext()) {
            nativeList.add(nativeIndicatorItems.getLong((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID))));

        }


        System.out.println("Show native  indicators  now..:" + nativeList.size());
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    // check for new indicators
    // backgound task to check for new updates
    class UpdateTask extends AsyncTask<Void, Void, Void> {

        //  ProgressDialog dialog;
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
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

            if (pd != null)
            {
                pd.dismiss();

            }
          //  reloadActivity();
        }
    }

    private void checkUpdates() {

        Log.d(TAG_UPDATE, "Updating....");

        datasource = new IndicatorsDataSource(this);
        datasource.open();

        indicatorItems = datasource.findAllIndicators();

        System.out.println("No. of Indicators " + indicatorItems.getCount());

        if (indicatorItems != null) {


            if (indicatorItems.moveToFirst()) {
                do {
                    //    indicatorItems.getString(indicatorItems.getColumnIndex("title")); // "Title" is the field name(column) of the Table
                    //   System.out.println("Update Title.. " + indicatorItems.getString(indicatorItems.getColumnIndex("updated_on")));
                    timeStampToMilliSeconds(indicatorItems.getString(indicatorItems.getColumnIndex("updated_on")));
                    nativeDBTimeStamp = timeStampToMilliSeconds(indicatorItems.getString(indicatorItems.getColumnIndex("updated_on")));
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
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
                    //   System.out.println(obj.get("timestamp"));
                    // DB QueryValues Object to insert into SQLite
                    // queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    // queryValues.put("outletId", obj.get("id").toString());
                    // Add userName extracted from Object
                    // queryValues.put("outletName", obj.get("outlet_name").toString());
                    // Add userID extracted from Object
                    // queryValues.put("sub_division", obj.get("sub_division").toString());
                    // Add userName extracted from Object
                    // queryValues.put("qty_type", obj.get("qty_type").toString());
                    // Insert User into SQLite DB
                    //   mDataSource.insertRemoteOutlet(queryValues);


                    // controller.insertUser(queryValues);
//                    // Add status for each User in Hashmap
//                  //  map.put("status", "1");
                    //  outletsynclist.add(map);
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                // updateMySQLSyncSts(gson.toJson(outletsynclist));
                // Reload the Main Activity
                // reloadActivity();
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

    // reload activity

    public void reloadActivity() {
        Intent objIntent = new Intent(this, MainActivity.class);
        startActivity(objIntent);
    }




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

            List<SyncIndicator> syncIndicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));

            //  columnArray3.add();

            ArrayList<DataItem> imageArray = new ArrayList<DataItem>();

            // REMOTE LIST
            ArrayList list = new ArrayList();

            for (SyncIndicator syncIndicator : syncNewIndicators) {
                Log.i("Indicators", syncIndicator.indicatorId + ": " + syncIndicator.title + "cat_id" + syncIndicator.cat_id);
                list.add(syncIndicator.indicatorId);
                // System.out.println("Indicators"+syncIndicator.indicatorId + ": " + syncIndicator.title + syncIndicator.change_type);

                Long obj = new Long(syncIndicator.indicatorId);
                int i = obj.intValue();
                //  Integer remoteItemId = Integer.valueOf(syncIndicator.indicatorId.intValue());
                Indicator indicator = new Indicator(syncIndicator.indicatorId, syncIndicator.title, syncIndicator.headline, syncIndicator.summary, syncIndicator.unit, syncIndicator.description, syncIndicator.data, syncIndicator.period, syncIndicator.url, syncIndicator.updated_on, syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);

                columnArray3.add(indicator);
                // String a[] = new String[]{"abc","klm","xyz","pqr"};

                //  List list1 = Arrays.asList(a);
                // Collection<String> listOne = new ArrayList(Arrays.asList("a","b", "c", "d", "e", "f", "g"));


                //  columnArray3.add();


                //  datasource.create(syncIndicator);
                // datasource.insertIndicator(syncIndicator.title,syncIndicator.headline,syncIndicator.summary,syncIndicator.unit,syncIndicator.description,syncIndicator.data,syncIndicator.period,syncIndicator.url,syncIndicator.updated_on,syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);
                //  DataItem remoteItems = new DataItem(syncIndicator.indicatorId, syncIndicator.title,syncIndicator.headline,syncIndicator.summary,syncIndicator.unit,syncIndicator.description,syncIndicator.data,syncIndicator.period,syncIndicator.url,syncIndicator.updated_on,syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);

                //   String item_id = String.valueOf(syncIndicator.indicatorId);
                //  columnArray3.add(new DataItem(syncIndicator.indicatorId, syncIndicator.title,syncIndicator.headline,syncIndicator.summary,syncIndicator.unit,syncIndicator.description,syncIndicator.data,syncIndicator.period,syncIndicator.url,syncIndicator.updated_on,syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id));

            }

            if (columnArray3.size() > 0) {
                System.out.println("Remote Item count" + list.size() + " ...  Native  table Items" + nativeList.size());
                //nativeIndicators

                // remove duplicate items from remote item arraylist

                nativeIndicators.removeAll(columnArray3);

                // unique items

                // System.out.println("unique items   :"+nativeIndicators + columnArray3);

                //iterate through indicators that appear in the remote database

                for (int i = 0; i < list.size(); i++) {
                    System.out.println("rEMOTE  ITEMS" + list.get(i));
                }

                //iterate through indicators that appear in the native database

                for (int i = 0; i < nativeList.size(); i++) {
                    System.out.println("NATIVE  ITEMS" + nativeList.get(i));
                }

                // retreive the updated ids that appear in the remote database

                list.removeAll(nativeList);

                // fetch updated ids from remote database and update the native database
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("rEMOTE  ITEMS unique" + list.get(i));
                }

                System.out.println("UNIQUE ITEMS" + list.size());

                if (columnArray3.size() > nativeIndicatorItems.getCount()) {
                    System.out.println("New item available in remote database");
                } else {
                    System.out.println("Native and Remote Database are upto Date");
                }
            }


        }
    };

    private final Response.ErrorListener onPostsErrorNew = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };


    // convert timestamp to milli Seconds

    private long timeStampToMilliSeconds(String timeStamp) {
        // convert string to  long
        // long timeStampMilliSec = Long.valueOf(timeStamp);

        //Log.i(TAG_UPDATE, "--"+timeStampMilliSec+"--");
        Date stringMilliSec = null;
        if (timeStamp != null) {
            SimpleDateFormat date_format_string = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

            try {
                stringMilliSec = date_format_string.parse(timeStamp);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println("Milliseconds==" + stringMilliSec.getTime());

        }
        return stringMilliSec.getTime();
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
                Log.i("Indicators", syncIndicator.indicatorId + ": " + syncIndicator.title);
                //  datasource.create(syncIndicator);
                //   datasource.insertIndicator(syncIndicator.title,syncIndicator.headline,syncIndicator.summary,syncIndicator.unit,syncIndicator.description,syncIndicator.data,syncIndicator.period,syncIndicator.url,syncIndicator.updated_on);
            }

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    // function to check if the mobile device can access internet


    // Initialise Activity Data.
    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setTitle("Simple Tabs Example");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // restful api call to connect to indicator database
    }


    // Let's prepare Data for our Tabs - Fragments and Title List
    private void prepareDataResource() {

        addData(new TabsFragmentOne(), "Key Enonomic Indicators");
        //   addData(new TabsFragmentTwo(), "Categories");
       addData(new TabsFragmentThree(), "Census 2014");
    }

    private void addData(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        String ARG_SECTION_NUMBER = "section_number";

        Integer SECTION_NUMBER = position;


        if (position == 1) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("http://www.ubos.org/"));
            startActivity(viewIntent);
/*            FragmentOne fragment = new FragmentOne();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            Toast.makeText(context, "position:"+position,Toast.LENGTH_SHORT).show();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment.newInstance(position + 1));
            fragmentTransaction.commit();*/

        } else if (position == 2) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://twitter.com/StatisticsUg"));
            startActivity(viewIntent);
/*            FragmentTwo fragment = new FragmentTwo();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.container, fragment.newInstance(position + 1));
            fragmentTransaction.commit();*/

        } else if (position == 3) {

            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://www.facebook.com/UgandaBureauOfStatistics/"));
            startActivity(viewIntent);
/*
            FragmentTwo fragment = new FragmentTwo();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment.newInstance(position + 1));
            fragmentTransaction.commit();
            */
        }
        else if (position == 4) {
            Intent n = new Intent(context,AboutActivity.class);
            startActivity(n);
/*
            FragmentTwo fragment = new FragmentTwo();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment.newInstance(position + 1));
            fragmentTransaction.commit();
            */
        }
        else if (position == 5) {


/*
            FragmentTwo fragment = new FragmentTwo();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment.newInstance(position + 1));
            fragmentTransaction.commit();
            */
        }
        else if (position == 6) {
            Toast.makeText(getApplicationContext(), "" + position + "ID",
                    Toast.LENGTH_SHORT).show();
/*
            FragmentTwo fragment = new FragmentTwo();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment.newInstance(position + 1));
            fragmentTransaction.commit();
            */
        }
        else if (position == 7) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://www.facebook.com/UgandaBureauOfStatistics/"));
            startActivity(viewIntent);
            Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
/*
            FragmentTwo fragment = new FragmentTwo();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment.newInstance(position + 1));
            fragmentTransaction.commit();
            */
        }else {
            //  Toast.makeText(getApplicationContext(), "" + position + "ID",
            //        Toast.LENGTH_SHORT).show();
/*            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();*/
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

            new MainActivity.UpdateTask().execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //  View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
