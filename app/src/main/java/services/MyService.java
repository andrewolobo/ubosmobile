package services;

/**
 * Created by Dell on 7/28/2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ubos.apps.ubosstat.InsertNewIndicators;
import org.ubos.apps.ubosstat.MainActivity;
import org.ubos.apps.ubosstat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import db.IndicatorsDBOpenHelper;
import db.IndicatorsDataSource;
import model.Indicator;
import model.SyncIndicator;

public class MyService extends Service {
    int numMessages = 0;
    IndicatorsDataSource datasource , dataSource_missing_cat;
    Cursor indicatorItems, nativeIndicatorItems , missing_cat , nativeCategoryItems;
    private ArrayList<Indicator> nativeList = new ArrayList();
    private  ArrayList nativeListOrg = new ArrayList();
    private  ArrayList list = new ArrayList();
    private String[] IntegerArrayCategories =  new String[] { };
    ArrayList<SyncIndicator> UnsyncIndicators = new ArrayList<SyncIndicator>();
    private static final String SERVER_IP = "http://192.168.8.104/ubos_app";
    private int nativeCatCount ;

    private static final String ENDPOINT = SERVER_IP;
    public ArrayList<Indicator> nativeItems=new ArrayList<Indicator>();

    public    ArrayList<Indicator> al=new ArrayList<Indicator>();
    private static final String json_updates_for_indicators = "http://192.168.8.104/ubos_app/check_updates.php";
    // private static final String json_updates_for_indicators = "http://192.168.8.101/ubos_app/test_post.php";

    // List<Indicator> storeUpdateIndicators = new ArrayList<Indicator>();

    ArrayList<String> storeUpdateIndicators =new ArrayList<String>() ;

    private RequestQueue requestQueue;

    HashMap<String, String> queryValues;

    private Gson gson;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
        Log.d("Service","Created");


    }

    @Override
    public void onStart(Intent intent, int startId) {
        //   Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Log.d("Service","Created");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
        client.get(ENDPOINT, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //  System.out.print("Hello ..."+ "server response");

                // retrieve native indicator items

                datasource = new IndicatorsDataSource(getApplication());
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
                            dataSource_missing_cat = new IndicatorsDataSource(getApplication());
                            dataSource_missing_cat.open();

                            missing_cat = dataSource_missing_cat.getMissingCategories(cat_name);

                            if(missing_cat.getCount() == 0)
                            {

                                // store what has already been inserted ....
                                // remember to kill back goround services ....
                                System.out.println("Missing Cat...");

                           //     dataSource_missing_cat.insertCategory(missing_cat_id , cat_name);

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

                            // show notification

                          //  show_notification(st.getTitle());

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
                            Toast.makeText(getApplication(), "All items are up to date", Toast.LENGTH_SHORT).show();

                            Log.d("Item Status Update","All items are upto date");
                        }

                    }





                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
              //  Toast.makeText(getApplication(),"failure...", Toast.LENGTH_SHORT).show();
                Log.d("Update Status","Failed");
            }

            public void show_notification(String Title)
            {

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplication(), 0,
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mNotifyBuilder;
                NotificationManager mNotificationManager;
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // Sets an ID for the notification, so it can be updated
                int notifyID = 9001;
                mNotifyBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle("UGSTAT")
                        .setContentText(Title+" Has been added, Please update.")
                        .setSmallIcon(R.drawable.ic_launcher_r);
                // Set pending intent
                mNotifyBuilder.setContentIntent(resultPendingIntent);
                // Set Vibrate, Sound and Light
                int defaults = 0;
                defaults = defaults | Notification.DEFAULT_LIGHTS;
                defaults = defaults | Notification.DEFAULT_VIBRATE;
                defaults = defaults | Notification.DEFAULT_SOUND;
                mNotifyBuilder.setDefaults(defaults);
                // Set the content for Notification
                //  mNotifyBuilder.setContentText(intent.getStringExtra("intntdata"));
                // Set autocancel
                mNotifyBuilder.setAutoCancel(true);
                // Post a notification
                mNotificationManager.notify(notifyID, mNotifyBuilder.build());

            }


        });


        /**
        Intent resultIntent = new Intent(this, InsertNewIndicators.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Sets an ID for the notification, so it can be updated
        int notifyID = 9001;
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Alert")
                .setContentText("New Indicators, Please update.")
                .setSmallIcon(R.drawable.ic_launcher_r);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(intent.getStringExtra("intntdata"));
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
        **/

        /**
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_r)
                        .setContentTitle("UGSTATS Notifications")
                        .setContentText("New Indicators have been added");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
         **/

       /** Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Sets an ID for the notification, so it can be updated
        int notifyID = 9001;
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Alert")
                .setContentText("New Indicators, Please update.")
                .setSmallIcon(R.drawable.ic_launcher_r);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
      //  mNotifyBuilder.setContentText(intent.getStringExtra("intntdata"));
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());

        */
    }

    @Override
    public void onDestroy() {
        //  Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.d("Service","Destroyed");

    }
}
