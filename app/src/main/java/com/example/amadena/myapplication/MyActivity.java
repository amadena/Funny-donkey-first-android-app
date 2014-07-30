package com.example.amadena.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MyActivity extends Activity {

    final Context context = this;
    AnimationDrawable donkeyAnimation;
    Timer timerStartAnimation = new Timer();
    Timer timerStopAnimation = new Timer();
    boolean isClicked = false;

    //for shaking detection, took it from the web + class ShakeDetector
    private com.example.amadena.myapplication.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private void playSound(Context context, int idSound)
    {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, idSound);
        mediaPlayer.start();
    }

    private void playAnimation(int duration)
    {
        timerStartAnimation = new Timer();
        timerStopAnimation = new Timer();
        timerStartAnimation.schedule(new StartTimeTask(), 0, 200);
        timerStopAnimation.schedule(new StopTimeTask(), duration);
    }

    private ImageView initializationImageView(int id, View.OnClickListener listener) {

        ImageView res = (ImageView) findViewById(id);
        res.setOnClickListener(listener);

        return res;
    }

    private void actionsOnClickDonkey(int idSound, int duration)
    {
        if(isClicked) {
            return;
        }
        else {

            isClicked = true;

            playSound(context, idSound);
            playAnimation(duration);

        }
    }

    private View.OnClickListener donkeyFaceListener = new View.OnClickListener() {
        public void onClick(View v) {

            actionsOnClickDonkey(R.raw.i_like_you, 2500);

        }
    };

    private View.OnClickListener donkeyBodyListener = new View.OnClickListener() {
        public void onClick(View v) {

            actionsOnClickDonkey(R.raw.lets_do_that_again, 5000);

        }
    };

    private View.OnClickListener donkeyBottomListener = new View.OnClickListener() {
        public void onClick(View v) {

            actionsOnClickDonkey(R.raw.unwanted_contact, 2500);

        }
    };

    class StartTimeTask extends TimerTask {
        public void run() {

            isClicked = true;
            donkeyAnimation.start();
        }
    }

    class StopTimeTask extends TimerTask {
        public void run() {

            donkeyAnimation.stop();

            timerStartAnimation.cancel();
            timerStopAnimation.cancel();

            isClicked = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        ImageView imageDonkey1 = initializationImageView(R.id.imageDonkeyFace, donkeyFaceListener);
        ImageView imageDonkey2 = initializationImageView(R.id.imageDonkeyBody, donkeyBodyListener);
        ImageView imageDonkey3 = initializationImageView(R.id.imageDonkeyBottom, donkeyBottomListener);

        ImageView imageDonkey = (ImageView)findViewById(R.id.imageDonkey);
        imageDonkey.setBackgroundResource(R.drawable.anim);
        donkeyAnimation = (AnimationDrawable) imageDonkey.getBackground();

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                actionsOnClickDonkey(R.raw.whoo_hoo, 2500);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
