package org.ubos.apps.ubosstat;

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
import java.util.Arrays;
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

public class InsertNewIndicators extends AppCompatActivity {


    IndicatorsDataSource datasource , dataSource_missing_cat;
    Cursor indicatorItems, nativeIndicatorItems , missing_cat , nativeCategoryItems;
    private  ArrayList<Indicator> nativeList = new ArrayList();
    private  ArrayList nativeListOrg = new ArrayList();
    private  ArrayList list = new ArrayList();
    private String[] IntegerArrayCategories =  new String[] { };
    ArrayList<SyncIndicator> UnsyncIndicators = new ArrayList<SyncIndicator>();
    private static final String SERVER_IP = "http://192.168.8.101/ubos_app";
    private int nativeCatCount ;

    private static final String ENDPOINT = SERVER_IP;
    public ArrayList<Indicator> nativeItems=new ArrayList<Indicator>();

    public    ArrayList<Indicator> al=new ArrayList<Indicator>();
    private static final String json_updates_for_indicators = "http://192.168.8.101/ubos_app/check_updates.php";
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

                datasource = new IndicatorsDataSource(InsertNewIndicators.this);
                datasource.open();


                String res = new String(responseBody);

                  System.out.print("Hello ....."+ res);

                //  Toast.makeText(context,"hello..."+res, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));

                    nativeIndicatorItems = datasource.findAllIndicators();

                    // create array list of native categories


                    // System.out.println("Native Items receord count"+ nativeIndicatorItems.getCount());

                    //create arraylist of items in native database


                    for (nativeIndicatorItems.moveToFirst(); !nativeIndicatorItems.isAfterLast(); nativeIndicatorItems.moveToNext()) {

                        // nativeIndicators = new(DataItem());

                        nativeListOrg.add(nativeIndicatorItems.getLong((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID))));

                        Indicator indicator = new Indicator(
                                nativeIndicatorItems.getLong((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_TITLE))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_HEADLINE))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_SUMMARY))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DESCRIPTION))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DATA))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_PERIOD))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UNIT))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_URL))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_VALUE))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_DESC))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_VALUE))),
                                nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID))));



                      //  syncSQLiteMySQLDB(String.valueOf(nativeIndicatorItems.getLong((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID)))),nativeIndicatorItems.getString((nativeIndicatorItems.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON))));


                        nativeItems.add(indicator);

                    }


                    // System.out.println("Show native  indicators  now..:"+nativeList.size());
                    // Toast.makeText(context,"Show native  indicators  now..:"+nativeList.size(), Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(context,"hello..."+res, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(context,"hello..."+jsonArray.length(), Toast.LENGTH_SHORT).show();

                    //creating arraylist


                    if(jsonArray.length() != 0) {
                        // REMOTE LIST
                        ArrayList list = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);

                            //                            Toast.makeText(context,"title..."+obj.get("indicatorId"), Toast.LENGTH_SHORT).show();
                            list.add(obj.get("indicatorId"));
                            long ind_id = Long.valueOf(obj.get("indicatorId").toString());
                            String cat_name = obj.getString("cat_name");
                            String missing_cat_id =  obj.getString("cat_id") ;

                            //   UnsyncIndicators.add(new SyncIndicator(ind_id,obj.get("title").toString()));
                            Indicator indicator = new Indicator(obj.getLong("indicatorId"),
                                    obj.getString("title"),
                                    obj.getString("headline"),
                                    obj.getString("summary"),
                                    obj.getString("description"),
                                    obj.getString("data"),
                                    obj.getString("period"),
                                    obj.getString("unit"),
                                    obj.getString("url"),
                                    obj.getString("updated_on"),
                                    obj.getString("change_type"),
                                    obj.getString("change_value"),
                                    obj.getString("change_desc"),
                                    obj.getString("index_value"),
                                    obj.getString("cat_id"));





                            // check if a category is missing
                            dataSource_missing_cat = new IndicatorsDataSource(InsertNewIndicators.this);
                            dataSource_missing_cat.open();

                            missing_cat = dataSource_missing_cat.getMissingCategories(cat_name);

                            if(missing_cat.getCount() == 0)
                            {

                                // store what has already been inserted ....
                                // remember to kill back goround services ....
                                System.out.println("Missing Cat...");

                                dataSource_missing_cat.insertCategory(missing_cat_id , cat_name);

                              //  int cat_array_count = IntegerArrayCategories.length;

                             //   IntegerArrayCategories[cat_array_count + 1] = cat_name;
                                // check if the category kas alreaady been inserted
                              //  int cat_array_count2 = IntegerArrayCategories.length;


                           /**     if(IntegerArrayCategories.length == 0)
                                {
                                    System.out.println("Array is empty");
                            }


                                boolean contains = Arrays.asList(IntegerArrayCategories).contains(cat_name) ;

                                System.out.println("missing cat check"+contains);

                                if(contains == false) {

                                    dataSource_missing_cat.insertCategory(missing_cat_id , cat_name);

                                    int cat_array_count = IntegerArrayCategories.length;

                                    IntegerArrayCategories[cat_array_count + 1] = cat_name;
                                }
                                else
                                {
                                    System.out.println("Category namae already exists");
                                }
                            **/
                            }





                            al.add(indicator);
                        }



                        //test

                        //Creating user-defined class objects
                        /**
                         Student s1=new Student(101,"Sonoo",23);
                         Student s2=new Student(102,"Ravi",21);
                         Student s3=new Student(103,"Hanumat",25);
                         **/



                        // al.add(s1);//adding Student class object
                        //al.add(s2);
                        // al.add(s3);
                        //Getting Iterator
                        Iterator itr=al.iterator();
                        //traversing elements of ArrayList object
                        while(itr.hasNext()){
                            Indicator st=(Indicator)itr.next();
                            System.out.println("Model items"+st.getTitle()+" "+st.getHeadline()+" "+st.getDescription());
                        }

                        //    System.out.println("test arraylist"+list2.get(0).getName()); //Prints "Coca-Cola"
                        //Toast.makeText(InsertNewIndicators.this,"Remote Item Count..."+list.size()+"native item count"+nativeList.size()+"model ind"+UnsyncIndicators.size(), Toast.LENGTH_SHORT).show();

                        // retrieve vales of UnsyncIndicators

                        Log.d("Unsyc items count","HELLO...");

                        //   System.out.println("Unsyns items"+list2.); //Prints "Coca-Cola"

                        Iterator<SyncIndicator> i = UnsyncIndicators.iterator();
                        if(i.hasNext()){
                            // retrievedThing = i.next();
                            System.out.println("Unsync Items..."+i.next());
                        }
                        /**
                         for (int t = 0; t < UnsyncIndicators.size(); t++) {

                         System.out.println("Unsync Items..."+UnsyncIndicators.get(t));

                         }
                         */


                        // get unique item
                        list.removeAll(nativeListOrg);

                        //get unique array list objects


                        //                    al.removeAll(nativeItems);


                        for (int j = 0; j < nativeListOrg.size(); j++){

                            if (!list.contains(nativeListOrg.get(j))){

                                list.remove(nativeListOrg.get(j).toString());
                                // finalResult.add(nativeList.get(j).toString());

                            }


                        }

                        // remove arraylist objects

                        for (int j = 0; j < nativeItems.size(); j++){

                            if (!al.contains(nativeItems.get(j))){

                                al.remove(nativeItems.get(j).toString());

                                System.out.println("Item removed"+nativeItems.get(j).toString());
                                // finalResult.add(nativeList.get(j).toString());

                            }


                        }
                        /**
                         Iterator itr2 = nativeItems.iterator();
                         //traversing elements of ArrayList object
                         while(itr2.hasNext()){
                         Indicator st=(Indicator)itr2.next();

                         if (!al.contains(st.getTitle())) {

                         al.remove(st.getTitle());
                         }
                         //   System.out.println("Model items"+st.getTitle()+" "+st.getHeadline()+" "+st.getDescription());
                         }
                         **/

                        for (Indicator element : al) {

                            // al.remove(element);
                            System.out.println(" Remote output element"+element);

                        }

                        for (Indicator element : nativeItems) {

                            // al.remove(element);
                            System.out.println(" Native output element"+element);

                        }


                        /// testing ...

                        ArrayList<Indicator> results = new ArrayList<Indicator>();

                        // Loop arrayList2 items
                        for (Indicator person2 : al) {
                            // Loop arrayList1 items
                            boolean found = false;
                            for (Indicator person1 : nativeItems) {
                                if (person2.getId() == person1.getId()) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                Indicator indicator = new Indicator(person2.getId(),person2.getTitle(),person2.getHeadline(),person2.getSummary(),person2.getDescription(),person2.getData(),person2.getPeriod(),person2.getUnit(),person2.getUrl(),person2.getUpdated_on(),person2.getChangeType(),person2.getChange_value(),person2.getChange_desc(),person2.getIndex_value(),person2.getCat_id());

                                results.add(indicator);
                            }
                        }

                        //Iterate through unique arraylist

                        Iterator itr2 = results.iterator();
                        //traversing elements of ArrayList object
                        while(itr2.hasNext()){
                            Indicator st=(Indicator)itr2.next();
                            System.out.println("Unsync Items"+st.getTitle()+" "+st.getHeadline()+" "+st.getDescription());

                            // insert new remote item
                            datasource.insertIndicator(st.getTitle(),
                                    st.getHeadline(),
                                    st.getSummary(),
                                    st.getUnit(),
                                    st.getDescription(),
                                    st.getData(),
                                    st.getPeriod(),
                                    st.getUrl(),
                                    st.getUpdated_on(),
                                    st.getChangeType(),
                                    st.getChange_value(),
                                    st.getChange_desc(),
                                    st.getIndex_value(),
                                    st.getCat_id());

                        }

                        // end testing
                        //  Toast.makeText(getApplicationContext(),"UNIQUE ITEMS"+list.size()+"int"+al.size(), Toast.LENGTH_SHORT).show();

                        //    Toast.makeText(getApplicationContext(),"UNIQUE ITEMS"+list.size()+"int"+results.size(), Toast.LENGTH_SHORT).show();


                        //  System.out.println("union: " + union);

                        // retrieve list items

                        if(list.size() > 0) {

                            for (int u = 0; u < list.size(); u++) {

                                if (!list.contains(nativeList.get(u))) {

                                    // list.get(u).toString();

                                    // retrieve new items

                                    System.out.println("Unsync Items..."+  list.get(u).toString());
                                    // finalResult.add(nativeList.get(j).toString());
                                    //          Toast.makeText(context, "UNIQUE ITEMS details " + list.get(u).toString(), Toast.LENGTH_SHORT).show();

                                    //     final Intent intnt = new Intent(InsertNewIndicators.this, MyService.class);
                                    // Set unsynced count in intent data
                                    //   intnt.putExtra("intntdata", "Unsyc rows count"+list.size());
                                    // intnt.putExtra("res", res);
                                    // Call MyService
                                    // InsertNewIndicators.this.startService(intnt);

                                }


                            }
                        }
                        else
                        {
                            Toast.makeText(InsertNewIndicators.this, "All items are up to date", Toast.LENGTH_SHORT).show();

                            Log.d("Item Status Update","All items are upto date");
                        }

                    }


                    // fetch updated ids from remote database and update the native database
                    //  for (int i = 0; i < list.size(); i++) {
                    //    System.out.println("rEMOTE  ITEMS unique"+list.get(i));
                    //  Toast.makeText(context,"Unique Item..."+list.get(i), Toast.LENGTH_SHORT).show();
                    //}

                    // System.out.println("UNIQUE ITEMS"+list.size());
                    //     Toast.makeText(getApplicationContext(),"UNIQUE ITEMS"+list.size(), Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(InsertNewIndicators.this,"failure...", Toast.LENGTH_SHORT).show();
                Log.d("Update Status","Failed");
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
                    System.out.println("BC response Length"+arr.length());
                    // If no of array elements is not zero
                    if(arr.length() != 0){
                        // Loop through each array element, get JSON object which has userid and username
                        for (int i = 0; i < arr.length(); i++) {
                            // Get JSON object
                            JSONObject obj = (JSONObject) arr.get(i);
                            System.out.println("BC Unsync Returns" + obj.get("id")+ obj.get("timestamp")+ "Status" + obj.get("update"));




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
                    //   mDataSource.insertRemote
                    // Outlet(queryValues);


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
