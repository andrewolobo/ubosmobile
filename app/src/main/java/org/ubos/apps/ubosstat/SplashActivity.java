package org.ubos.apps.ubosstat;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andrew on 10-08-2017.
 */

public class SplashActivity extends Activity {

    public Context context;
    public Animator out_animator;
    @Override
    public void onCreate(Bundle os){
        super.onCreate(os);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_alt);
        context = this;
        Timer t = new Timer();


        out_animator = AnimatorInflater.loadAnimator(context, R.animator.fadein);
        out_animator.setTarget(this.findViewById(R.id.main_banner));
        out_animator.setDuration(2000);
        out_animator.start();

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(context, MainActivity.class));
                finish();

            }
        },6000);

    }
}
