package rms.netsol.com.rmsystem.View.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import rms.netsol.com.rmsystem.R;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener{

    private static int SPLASH_TIME_OUT = 1000;
    Animation animation;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageview=(ImageView)findViewById(R.id.zoom_image);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageview.startAnimation(animation);
            }
        }, SPLASH_TIME_OUT);
        animation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login = new Intent (SplashActivity.this, MenuActivity.class);
                startActivity(login);
                finish();
            }
        }, 100);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
