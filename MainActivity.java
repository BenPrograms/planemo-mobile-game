package com.davey.spaceexplorer.spaceexplorer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    public static final String SAVE = "SavedGame";
    public static AdView mAdView;

    private SharedPreferences datasave;
    private SharedPreferences.Editor dataeditor;

    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PreferenceManager.setDefaultValues(this, PREFERENCE_MODE_PRIVATE, false);

        datasave = getPreferences(PREFERENCE_MODE_PRIVATE);
        dataeditor = datasave.edit();

        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY )!=0){
            Log.d(TAG, "Called from history");
            //clear flag from history
            Intent intent = getIntent().setFlags( getIntent().getFlags() & (~ Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY));
            setIntent(intent);
        }
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Constants.height = displaymetrics.heightPixels;
        Constants.width = displaymetrics.widthPixels;

        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.vMain);
        layout.addView(new GamePanel(this));

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        MainActivity.mAdView.loadAd(adRequest);
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        GamePanel.state = savedInstanceState.getInt(SAVE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // insert here your instructions
        dataeditor.putInt("level", Level.progression);
        dataeditor.putInt("growth", Level.growth);
        dataeditor.putInt("record", GamePanel.record);
        dataeditor.commit();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Constants.paused = true;
        if(GamePanel.rain.isPlaying()) {
            GamePanel.rain.pause();
        }
        if(GamePanel.backgroundMusic.isPlaying()){
            GamePanel.backgroundMusic.pause();
        }
        System.out.println("paused");
    }
    @Override
    protected void onRestart() {
        super.onStart();
        System.out.println("restart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(GamePanel.rain.isPlaying() == false) {
            GamePanel.rain.start();
        }
        if(GamePanel.backgroundMusic.isPlaying() == false){
            GamePanel.backgroundMusic.start();
        }
        System.out.println("resume");
    }

    @Override
    protected void onStart() {
        System.out.println("start");
        super.onStart();

        if(GamePanel.rain.isPlaying() == false) {
            GamePanel.rain.start();
        }

        if(GamePanel.backgroundMusic.isPlaying() == false){
            GamePanel.backgroundMusic.start();
        }

        Level.progression = datasave.getInt("level", 1);
        Level.growth = datasave.getInt("growth", 0);
        GamePanel.record = datasave.getInt("record", 0);
        //Level.progression = 1;
        //Level.growth = 0;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
