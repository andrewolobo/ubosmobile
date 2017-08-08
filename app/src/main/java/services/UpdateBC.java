package services;

/**
 * Created by Dell on 8/3/2017.
 */

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import org.ubos.apps.ubosstat.MainActivity;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.location.Location;
        import android.preference.PreferenceActivity;
        import android.util.Log;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.JsonHttpResponseHandler;
        import com.loopj.android.http.RequestParams;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;

        import cz.msebera.android.httpclient.Header;
        import db.IndicatorsDBOpenHelper;
        import db.IndicatorsDataSource;
        import model.DataItem;
        import model.Indicator;
        import model.SyncIndicator;

        import static android.support.constraint.R.id.parent;


public class UpdateBC extends BroadcastReceiver {
    static int noOfTimes = 0;
    IndicatorsDataSource datasource;
    Cursor indicatorItems, nativeIndicatorItems;
    private  ArrayList nativeList = new ArrayList();
    private  ArrayList list = new ArrayList();
    List<SyncIndicator> UnsyncIndicators = new ArrayList<SyncIndicator>();
    private static final String SERVER_IP = "http://192.168.8.100/ubos_app";
    private static final String ENDPOINT = SERVER_IP;
    // Method gets called when Broad Case is issued from MainActivity for every 10 seconds
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
        noOfTimes++;
        Toast.makeText(context, "BC Service Running for " + noOfTimes + " times", Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
        client.get(SERVER_IP, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context,"Checking for Updates", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context,"failure...", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
