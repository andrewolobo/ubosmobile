package fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
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

import java.io.File;
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

    IndicatorsDataSource datasource, datasource2 , datasource_new_cat , datasource_new_ind;
    Context mContext;
    //private static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";
    private static final String SERVER_IP = Global.ENDPOINT;
    private static final String ENDPOINT = SERVER_IP;
    private static final String ENDPOINT_CATEGORIES = Global.ENDPOINT+"/index_get_categories.php";
    public  RecyclerView recyclerView;
    Cursor indicatorItems , missing_cat;
    private RequestQueue requestQueue;
    private static final String json_updates_for_indicators = Global.json_updates_for_indicators;
    private Gson gson;
    HashMap<String, String> queryValues;
    Cursor cursor;
    private Boolean exit = false;
    private static final int URL_LOADER = 0;
    private String not_title ;
    private String[] note_info ;
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings ;
    private boolean firstStart;
    private String highScore;
    private  String testString;
    private    ProgressDialog pd2 , progressDialog;
    List<SyncIndicator> indicatorsAlreadyDownloaded = new ArrayList<SyncIndicator>();

    public TabsFragmentOne() {
        Log.i("Fragment Check", "Fragment One Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);


        // check for updates

        if (!hasRunSinceBoot(getContext())) {
            //do whatever you need to do
            System.out.println("App status - Not booted yet");
            //  check for updates
            if (isOnline()) {
                new UpdateTask().execute();
            }
            else
            {
                Toast.makeText(getContext(),"Your Internet connection seems to be off. Please close the UGSTATS App, turn on the internet connection and open the UGSTATS App again" , Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            // System.out.println("App status - Already booted");
        }




        datasource = new IndicatorsDataSource(getContext());
        datasource.open();

        requestQueue = Volley.newRequestQueue(getContext());


        System.out.print("Tab one...");


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

            recyclerView.swapAdapter(adapter, false);
            recyclerView.invalidate();

            // recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we don't use it then also our items shows de
        }
        if (tours.size() == 0) {

         //   Log.i("recordset count", "count 0");
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


    private void fetchPosts() {
        ///ENABLE PROGRESS BAR HERE


        showProgress("....");
        StringRequest request = new StringRequest(Request.Method.GET, SERVER_IP, onPostsLoaded, onPostsError);
        request.setShouldCache(false);
        requestQueue.getCache().clear();

        requestQueue.add(request);

    }


    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();

            Log.d("PostActivity", response );
            //List<Indicators> indicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));
            List<SyncIndicator> syncIndicators = Arrays.asList(gson.fromJson(response, SyncIndicator[].class));

            // store list of downloaded and inserted in native DB
            indicatorsAlreadyDownloaded = syncIndicators;
            Log.i("PostActivity", syncIndicators.size() + " indicators loaded.");
            for (SyncIndicator syncIndicator : syncIndicators) {
                Log.i("Indicators", syncIndicator.indicatorId + ": " + syncIndicator.title + "cat_id" + syncIndicator.cat_id);
                System.out.println("Indicators" +"   update"+syncIndicator.updated_on  + syncIndicator.indicatorId + ": " + syncIndicator.title + syncIndicator.change_type);
                //  datasource.create(syncIndicator);


                datasource.insertIndicator(syncIndicator.indicatorId, syncIndicator.title, syncIndicator.headline, syncIndicator.summary, syncIndicator.unit, syncIndicator.description, syncIndicator.data, syncIndicator.period, syncIndicator.url, syncIndicator.updated_on, syncIndicator.change_type, syncIndicator.change_value, syncIndicator.change_desc, syncIndicator.index_value, syncIndicator.cat_id);

             //   pd2.dismiss();


                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                // Reload - update the recycler view
                reloadActivity();


            }

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

          /**  pd2.dismiss(); **/

            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }


            Log.e("PostActivity", error.toString());
        }
    };

    /// progress dialog

    private void showProgress(String message) {
       ProgressDialog progressDialog=null;// Initialize to null
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading Data " + message);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
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



            //  deleteCache(getContext());
            if (isOnline()) {
                //check if indicators have been loaded
                datasource.open();



                String KE_CAT_ID = "1";

                List<Indicator> tours = datasource.findAll(KE_CAT_ID);

                if (tours.size() == 0) {

                    Log.i("recordset count", "count 0");
                    if (isOnline()) {
                        fetchPosts();
                    }
                    datasource.close();
                    reloadActivity();
                }



                new UpdateTask().execute();
            }
            else
            {
                Toast.makeText(getContext(),"Your Internet connection seems to be off. Please turn it on " , Toast.LENGTH_LONG).show();

            }


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

            // recyclerView.invalidate();

            if (pd != null)
            {
                pd.dismiss();

            }
            //  reloadActivity();
        }
    }


    private void checkUpdates() {
        syncSQLiteMySQLDB();
        System.out.println("checking for Indicator Updates");
    }

    // check for updates for each indicator

    public void syncSQLiteMySQLDB() {

        System.out.println("checking for Indicator Updates");

        // prgDialog.show();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, json_updates_for_indicators,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //    Gson gson = new GsonBuilder().create()

                        System.out.println("Indicator Sync Status" + response);
                        // Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();

                        try {
                            // Extract JSON array from the response
                            JSONArray arr = new JSONArray(response);
                            String[] notes ;
                            // set array size

                            notes = new String[arr.length()];

                            // If no of array elements is not zero
                            if (arr.length() != 0) {
                                // Loop through each array element, get JSON object which has userid and username

                                // set array index counter
                                int j = 0 ;
                                int i;
                                for (i = 0; i < arr.length(); i++) {

                                    j = i;

                                    JSONObject obj = (JSONObject) arr.get(i);

                                    String update_status = obj.getString("update");


                                    if (update_status.equals("false")) {
                                        System.out.println("Update Available");

                                        //     System.out.println("up response"+response);
                                        System.out.println("Unsync Returns" + obj.get("id") +" " + obj.get("title") +" "+ obj.get("timestamp") + "Status" + obj.get("update"));

                                        queryValues = new HashMap<String, String>();

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

                                        String app_title = obj.getString("title");
                                        String remote_cat_name = obj.getString("cat_name");
                                        String remote_item_id = obj.getString("id");

                                        // check whether the native indicator is updated

                                        String remote_id = obj.getString("id");
                                        String remote_up = obj.getString("updated_on");
                                        String missing_cat_id =  obj.getString("cat_id") ;

                                        datasource.open();

                                        Cursor nativeindicatorupdate = datasource.checkForUpdate(remote_id, remote_up);

                                        System.out.println("native item updated: "+nativeindicatorupdate.getCount());

                                        int up_status = nativeindicatorupdate.getCount();

                                        System.out.println("update status flag"+up_status);
                                        if(up_status > 0) {
                                            // native items are updated

                                        }
                                        else
                                        {
                                            // check if id exists in native db, if not add


                                            // check for new indicators
                                            datasource.close();
                                            datasource_new_ind= new IndicatorsDataSource(getContext());
                                            datasource_new_ind.open();
                                            indicatorItems = datasource_new_ind.getMissingIndicators(remote_id);

                                            if(indicatorItems.getCount() == 0)
                                            {
                                                // add missing categories and indicators

                                                // check for any new categories  first

                                                //new connection handler for missing categories
                                                datasource_new_cat = new IndicatorsDataSource(getContext());
                                                datasource_new_cat.open();

                                                missing_cat = datasource_new_cat.getMissingCategories(remote_cat_name);

                                                // missing_cat = datasource.getMissingCategories(remote_cat_name);
                                                if(missing_cat.getCount() == 0)
                                                {

                                                    // add missing categories and indicators
                                                    datasource_new_cat.insertCategory(missing_cat_id,remote_cat_name);
                                                    // datasource_new_cat.insertIndicator(queryValues);
                                                    datasource_new_cat.close();
                                                }
                                                else
                                                {
                                                    // System.out.println("no new categories");
                                                }

                                                // insert new indicators

                                                long insert_flag  =   datasource_new_ind.insertIndicator(queryValues);
                                                datasource_new_ind.close();
                                                if(insert_flag != 0)
                                                {
                                                    notes[i] = obj.getString("title");
                                                   // System.out.println("Inserted new indicators");
                                                }
                                            }
                                            else
                                            {
                                               // System.out.println("Update the indicators");



                                                // native items are not updated

                                                // show_up_status(up_status, app_title, i);
                                           String show_title = obj.getString("title");

                                                //if(!title.equals("")) {
                                                //   note_info[i] = obj.getString("title");
                                                datasource.open();
                                                int update_flag = datasource.updateIndicator(queryValues);
                                                System.out.println("update flag :" + update_flag);
                                                if(update_flag == 1)
                                                {
                                                    // show_notice(show_title);
                                                    notes[i] = obj.getString("title");
                                                    // adapter.notifyDataSetChanged();
                                                }
                                                datasource.close();
                                            }
                                            //end
                                          //  System.out.println("Item  is up to date");
                                        }

                                    } else {
                                     //   System.out.println("Item  is up to date");
                                    }


                                }
                                // show notification

                              //  System.out.println("notification String..."+notes);



                                // remove null values

                                List<String> noteslist = new ArrayList<String>();

                                for(String s : notes) {
                                    if(s != null && s.length() > 0) {
                                        noteslist.add(s);
                                    }
                                }

                                notes = noteslist.toArray(new String[noteslist.size()]);

                                for( int u = 0; u < notes.length; u++)
                                {
                                    int not_id = 900 + u ;
                                    String element = notes[u];
                                    System.out.println("String array"+ element );

                                    show_notice(element, not_id);
                                }

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //    recyclerView.invalidate();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {



        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    // notify
    public void show_notice(String title , int id)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.ic_launcher_r)
                        .setContentTitle("UGSTATS Notifications")
                        .setContentText(title);
        // int notifyID = 9002;
        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        // Add as notification
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
    }

    // check if app has already booted
    public static boolean hasRunSinceBoot(Context context) {
        long bootTime = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        SharedPreferences prefs = context.getSharedPreferences("my_prefs_file", Context.MODE_PRIVATE);
        if (prefs.getLong("last_boot_time", 0) == bootTime) {
            return true;
        }
        prefs.edit().putLong("last_boot_time", bootTime).apply();
        return false;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}


