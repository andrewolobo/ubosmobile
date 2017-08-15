import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.R;
import org.ubos.apps.ubosstat.config.global;
import org.ubos.apps.ubosstat.config.utility;
import org.ubos.apps.ubosstat.json.JsonPacket;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {
    public Context context;
    public void onCreate(Bundle os){
        super.onCreate(os);
        System.out.print("hello");
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_splash);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Toast.makeText(context, "retrieving stats",Toast.LENGTH_SHORT).show();
            }
        },5000);
        utility util = new utility();
        global.createMap();
        String file_data = util.readFile(context,"simple_sample.json");
        ParseJson(context,file_data);
/*        ((ImageView)findViewById(R.id.sign_up_temp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(context, StartRActivity.class);
                startActivity(n);
                finish();
            }
        });*/

    }
    public static void ParseJson(Context context, String parse){
        Gson g = new Gson();
        //Toast.makeText(context, parse, Toast.LENGTH_LONG).show();
        Type listType = new TypeToken<ArrayList<JsonPacket>>() {}.getType();
        ArrayList<JsonPacket> result = g.fromJson(parse,listType);
        global.data = result;

    }
}
