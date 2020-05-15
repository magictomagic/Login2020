package com.example.login2020.ui.user;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login2020.controller.getVideoLink;
import com.example.login2020.widget.media.AndroidMediaController;
import com.example.login2020.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import com.example.login2020.R;

import java.util.ArrayList;
import java.util.List;

public class videoInfList extends AppCompatActivity{
    private IjkVideoView mVideoView;

    private AndroidMediaController mMediaController;
    private TableLayout mHudView;
//    getVideoLink dynamicLinks = new getVideoLink();
//    List<String> dynamicLinks = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //定义的直播地址
        String path = "rtmp://149.248.57.125:1935/vod/ad286f25b01849e5b4832519b7d5ba76.mp4";

        //初始化IjkMediaPlayer
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        //定义IjkVideoView
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        //定义的播放按钮的layout，用来加载定义好的播放界面
        mHudView = (TableLayout) findViewById(R.id.hud_view);

        //这里使用的是Demo中提供的AndroidMediaController类控制播放相关操作
        mMediaController = new AndroidMediaController(this, false);
        ActionBar actionBar = getSupportActionBar();
        mMediaController.setSupportActionBar(actionBar);

        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);

        //设置videopath，开始播放
        mVideoView.setVideoPath(path);
        mVideoView.start();

}
}