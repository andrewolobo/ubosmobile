package org.ubos.apps.ubosstat;

/**
 * Created by Dell on 8/3/2017.
 */


        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.RequestParams;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Map;

        import cz.msebera.android.httpclient.Header;
        import db.IndicatorsDBOpenHelper;
        import db.IndicatorsDataSource;
        import model.Indicator;
        import model.Product;
        import model.Student;
        import model.SyncIndicator;
        import services.MyService;

public class UpdateIndicators extends AppCompatActivity {


    IndicatorsDataSource datasource;
    Cursor indicatorItems, nativeIndicatorItems;
    private  ArrayList<Indicator> nativeList = new ArrayList();
    private  ArrayList nativeListOrg = new ArrayList();
    private  ArrayList list = new ArrayList();
    ArrayList<SyncIndicator> UnsyncIndicators = new ArrayList<SyncIndicator>();
    private static final String SERVER_IP = "http://192.168.8.100/ubos_app";
    private static final String ENDPOINT = SERVER_IP;
    public ArrayList<Indicator> nativeItems=new ArrayList<Indicator>();
    public    ArrayList<Indicator> al=new ArrayList<Indicator>();
    private static final String json_updates_for_indicators = "http://192.168.8.100/ubos_app/check_updates.php";
    // private static final String json_updates_for_indicators = "http://192.168.8.101/ubos_app/test_post.php";

    // List<Indicator> storeUpdateIndicators = new ArrayList<Indicator>();

    ArrayList<String> storeUpdateIndicators =new ArrayList<String>() ;

    private RequestQueue requestQueue;

    HashMap<String, String> queryValues;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_indicators);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get unsync remote items

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
        client.get(ENDPOINT, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //  System.out.print("Hello ..."+ "server response");

                // retrieve native indicator items

                datasource = new IndicatorsDataSource(UpdateIndicators.this);
                datasource.open();


                String res = new String(responseBody);

                //  Toast.makeText(context,"hello..."+res, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));

                    nativeIndicatorItems = datasource.findAllIndicators();


                    // System.out.println("Native Items receord count"+ nativeIndicatorItems.getCount());

                    //create arraylist of items in native database


                    for (nativeIndicatorItems.moveToFirst(); !nativeIndicatorItems.isAfterLast(); nativeIndicatorItems.moveToNext()) {

                        // nativeIndicators = new(DataItem());



                        syncSQLiteMySQLDB(String.valueOf(nativeIndicatorItems.getLong((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID)))),nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON))));



                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(UpdateIndicators.this,"failure...", Toast.LENGTH_SHORT).show();
            }



        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // Update indicator Items
    public void syncSQLiteMySQLDB(final String id , final String timestamp) {

        System.out.println("checking for Indicator Updates");

        // prgDialog.show();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, json_updates_for_indicators,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //    Gson gson = new GsonBuilder().create();
                        // prgDialog.hide();
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

                        //  System.out.println("Indicator Sync Status"+response);
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
                        //prgDialog.hide();
                        // Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                System.out.println("query values"+id+timestamp);

                params.put("id", id);
                params.put("timestamp", timestamp);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }



    // handle sync json for outlets
    public void showIndicatorJson(String response)
    {
        Log.d("TAG",response);

        ArrayList<HashMap<String, String>> indicatorsynclist;
        indicatorsynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println("response Length"+arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println("Unsync Returns" + obj.get("id")+ obj.get("timestamp")+ "Status" + obj.get("update"));

                    String update_status = obj.getString("update");

                    System.out.println("Remote Up :"+update_status);

                    if(update_status.equals("true"))
                    {
                        System.out.println("Update Available");

                        // update indicator item
                        updateIndicator(response);
                        // update items  from here
                    }
                    else
                    {
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

    public void updateIndicator(String response){
        ArrayList<HashMap<String, String>> itemsynclist;
        itemsynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println("Item update json :"+response);
            System.out.println(arr.length());
            // If no of array elements is not zero

            ArrayList<String> UpdateIndicators = new ArrayList<String>() ;

            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username


                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);

                    // populate arraylist

//                    UpdateIndicators.add(obj.optJSONObject(i));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    System.out.println("update item ID"+obj.getString("id"));

                    //obj.get("id");

                    queryValues.put("indicatorId", obj.getString("id"));

                    queryValues.put("title", obj.getString("title"));

                    queryValues.put("headline",obj.getString("headline"));
                    queryValues.put("summary", obj.getString("summary"));
                    queryValues.put("unit", obj.getString("unit"));
                    queryValues.put("description", obj.getString("description"));
                    queryValues.put("data", obj.getString("data"));
                    queryValues.put("period", obj.getString("period"));
                    queryValues.put("url", obj.getString("url"));

                    queryValues.put("updated_on", obj.getString("updated_on"));
                    queryValues.put("change_type",obj.getString("change_type"));
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

                    int update_flag =   datasource.updateIndicator(queryValues);

                    System.out.println("update flag :"+update_flag);

                    if(update_flag == 1)
                    {
                        System.out.println("yeah....");
                        Log.d("update status", "update successful");

                    }
                    else
                    {
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
                    System.out.println("Show updates...:"+UpdateIndicators.get(count));
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

