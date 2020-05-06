package com.example.login2020.ui.user;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login2020.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.login2020.ui.login.LoginActivity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import static android.app.ProgressDialog.show;

public class videoInfList extends AppCompatActivity {
    // test display on view
    static List<String> t = new ArrayList<String>();

    // videoInfList a = new videoInfList();
    // System.out.println(a.t);
    public videoInfList() {
        t.add("0a2bcec0876a4d62b33fc0fcea6b5f04.mp4");
        t.add("30cdaacc47bb4aaab3ad91b1e8600184.mp4");
        t.add("ad286f25b01849e5b4832519b7d5ba76.mp4");
    }
    private String path;
    private VideoView mVideoView;
    //rtmp://58.200.131.2:1935/livetv/hunantv
//        rtmp://149.248.57.125:1935/vod/ad286f25b01849e5b4832519b7d5ba76.mp4
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.video_inf_list);
        path = "rtmp://58.200.131.2:1935/livetv/hunantv";
        mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
        mVideoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
        mVideoView.setVideoPath(path);

        mVideoView.setMediaController(new MediaController(this));

        mVideoView.requestFocus();
//        HashMap<String, String> options = new HashMap<String, String>();

//        mediaPlayer.setDataSource(path, options);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });

    }

}
