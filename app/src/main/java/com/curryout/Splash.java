package com.curryout;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.curryout.appsharedpreference.AppSharedPreference;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {

    /** Duration of wait **/
    public final int SPLASH_DISPLAY_LENGTH = 5000; //splash screen will be shown for 2 seconds
    GifImageView gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        gif = findViewById(R.id.spgif);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                actionNext();
                //Intent mainIntent = new Intent(Splash.this, WelcomePhoneActivity.class);
                //startActivity(mainIntent);
                //finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void actionNext(){

        String mob = AppSharedPreference.loadUserPhoneFromPreference(Splash.this);
        Log.e("Shared Pref",mob);
        if(!mob.equalsIgnoreCase("")){
            Intent intent = new Intent(Splash.this,MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }else{
            Intent i = new Intent(Splash.this, WelcomePhoneActivity.class);
            startActivity(i);
            finish();
        }
    }


}
