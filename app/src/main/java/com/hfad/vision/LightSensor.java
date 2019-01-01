package com.hfad.vision;

import android.app.Service;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class LightSensor extends AppCompatActivity implements SensorEventListener,CompoundButton.OnCheckedChangeListener {
    Switch sw_s,sw_v;
    TextView tv;
    SensorManager sensorManager;
    Sensor sensor;
    private float maxValue;
    MediaPlayer mediap;
    private View root;
    private SoundPool mSoundPool,mSoundPool1;
    private java.util.HashMap<Integer, Integer> soundsMap;
    private int soundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager mAudioManager;
    int counter;
    private boolean isLoaded = false;

    int streamId = 0;
    int soundId = -1;
    float pitch = 01f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 mediap = MediaPlayer.create(this,R.raw.rec);

        setContentView(R.layout.activity_light_sensor);


       // mediap.start();
      //  mediap.setLooping(true);
       //mediap.seekTo(0);
        sw_s=(Switch)findViewById(R.id.sw_s);
        sw_v=(Switch)findViewById(R.id.sw_v);
        sw_s.setChecked(true);
      /*  sw_v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean switchState = sw_v.isChecked();
             //    Log.e("check","++++++++++++clicked"+switchState);
                if(!switchState)
                    {

                     }
                else
                    {}

            }});
      */  root = findViewById(R.id.root);
        tv=(TextView)findViewById(R.id.tv);
        sensorManager=(SensorManager)getSystemService(Service.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        maxValue = sensor.getMaximumRange();
        Log.e("++++++++++++++++",Float.toString(maxValue));
        if (sensor == null) {
            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
            finish();
        }

        mSoundPool = new SoundPool(0, AudioManager.STREAM_MUSIC, 0);


sw_v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Boolean switchState = sw_v.isChecked();
        Log.e("check","++++++++++++vibrate");

        android.os.Vibrator v = (android.os.Vibrator) getSystemService(android.content.Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000};
        if(!switchState)
            {
                v.cancel();
            }
        else
            {
                v.vibrate(pattern, 0);
           }

    }
    });


}



    @Override
    public void onBackPressed ()
    {
        mSoundPool.release();

        /*if (mediap != null)
            mediap.stop();
        */super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        final float value = event.values[0];
        tv.setText("Luminosity : " + value + " lx");

        // between 0 and 255
        int newValue = (int) (255f * value / maxValue);
        root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
          soundId = mSoundPool.load(this, R.raw.rec, 1);
         mSoundPool.setRate(streamId, value);
         pitch=value/100;
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                int streamId = mSoundPool.play(soundId , 1, 1, 1, 3, pitch);
                mSoundPool.setLoop(streamId, -1);
                Log.e("TAG", String.valueOf(streamId));
            }
        });

                sw_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("check","++++++++++++clicked");
                Boolean switchState = sw_s.isChecked();
                 Log.e("check","++++++++++++clicked"+switchState);
                if(!switchState)
                    {
                Log.e("check","++++++++++++stop");
                    mSoundPool.release();
                    }
                else
                    {

                          setVolumeControlStream(AudioManager.STREAM_MUSIC);
          soundId = mSoundPool.load(getApplicationContext(), R.raw.rec, 1);
         mSoundPool.setRate(streamId, value);
        // pitch=value/100;
 mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                int streamId = mSoundPool.play(soundId , 1, 1, 1, -1, pitch);
                mSoundPool.setLoop(streamId, -1);
                Log.e("TAG", String.valueOf(streamId));
            }
        });

//                        mSoundPool.play(soundId,1, 1 ,1,3,pitch);
                    }
                    }
        });



    /*    mAudioManager = (AudioManager)this.getSystemService(android.content.Context.AUDIO_SERVICE);
float actualVolume = (float) mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

    soundsMap = new java.util.HashMap<Integer, Integer>();
    soundsMap.put(0, mSoundPool.load(this, R.raw.rec, 1));
    mSoundPool.play(0, volume, volume, 1, 0, newValue);
    */}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
