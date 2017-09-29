package org.ubos.apps.ubosstat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ubos.apps.ubosstat.json.Header;
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
import cz.msebera.android.httpclient.entity.StringEntity;
import database.DataSource;
import db.IndicatorsDBOpenHelper;
import db.IndicatorsDataSource;
import fragments.TabsFragmentOne;
import fragments.TabsFragmentThree;
import fragments.TabsFragmentTwo;
import model.DataItem;
import model.Indicator;
import model.SyncIndicator;


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
    private String not_title;
    private Activity activity;

    ArrayList<DataItem> nativeIndicators = new ArrayList<DataItem>();

    IndicatorsDataSource datasource, database2;
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

        activity = MainActivity.this;
        Constants.activities.add(activity);
        context = this;

        initialise();

        prepareDataResource();

        adapter = new TextTabsAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.RED);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }



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

        addData(new TabsFragmentOne(), "Regular Indicators");
        addData(new TabsFragmentTwo(), "Categories");
        addData(new TabsFragmentThree(), "Population");
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
                            Uri.parse("http://www.ubos.org/pnsd/"));
            startActivity(viewIntent);


        } else if (position == 2) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("http://www.ubos.org/about-the-data/"));
            startActivity(viewIntent);


        } else if (position == 3) {

            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://twitter.com/StatisticsUg"));
            startActivity(viewIntent);

        } else if (position == 4) {
            /**  Intent n = new Intent(context,AboutActivity.class);
             startActivity(n); **/

            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://www.facebook.com/UgandaBureauOfStatistics/"));
            startActivity(viewIntent);

        } else if (position == 5) {


            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("http://www.ubos.org/"));
            startActivity(viewIntent);

        } else if (position == 6) {

            // launch UBOS Website

            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("http://www.ubos.org/copyright/"));
            startActivity(viewIntent);

        } else if (position == 7) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("http://www.ubos.org/contact-us/"));
            startActivity(viewIntent);

        }


        else {

        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
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
            case 8:
                mTitle = getString(R.string.title_section8);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    /**
     * A placeholder fragment containing a simple view.
     */

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
        Log.e("onDestroy","onDestroy");
    }


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


    public void exit(View v) {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
