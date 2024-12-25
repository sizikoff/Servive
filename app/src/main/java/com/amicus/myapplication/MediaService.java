package com.amicus.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MediaService extends Service {

    public static final String TAG="MySimpleService";
    private Handler handler;
    private Runnable runnable;
    MediaPlayer ambientMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Service created");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"Service is running....");
                handler.postDelayed(this,1000);
            }
        };

//        ambientMediaPlayer = MediaPlayer.create(this,R.raw.music);
//        ambientMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service started");
        handler.post(runnable);
        // ambientMediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Service destroyed");
        handler.removeCallbacks(runnable);
      //  ambientMediaPlayer.stop();
    }
}
