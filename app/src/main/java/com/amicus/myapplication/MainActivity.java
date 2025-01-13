package com.amicus.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    Button bt1,bt2,bt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoPlayer);

//        Uri myVideiUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
//        videoView.setVideoURI(myVideiUri);
        videoView.setVideoPath("https://www.youtube.com/s/search/audio/failure.mp3");
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);

        bt1 = findViewById(R.id.playButton);
        bt2 = findViewById(R.id.pauseButton);
        bt3 = findViewById(R.id.stopButton);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.stopPlayback();
                videoView.resume();
            }
        });
    }
}