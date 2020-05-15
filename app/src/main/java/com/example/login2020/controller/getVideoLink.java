package com.example.login2020.controller;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TableLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login2020.controller.getVideoLink;
import com.example.login2020.widget.media.AndroidMediaController;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2020.R;
//import com.example.login2020.data.adapterListModel;
import com.example.login2020.widget.media.AndroidMediaController;
import com.example.login2020.widget.media.IjkVideoView;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import androidx.appcompat.app.ActionBar;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class getVideoLink extends AppCompatActivity {
    final String protocalName = "rtmp";
    final String IPaddress = "149.248.57.125";
    final int ffmpegPort = 1935;
    final String applicationName = "vod";
    private TextView textView;
    final int defaultSSHport = 22;
    final String password = "B2$nBo7!]UkEL1@w";
    final String defaultUserName = "root";
    public static List<String> videoNameList = new ArrayList<String>();
    final String command = "ls /mnt/mp4s";
    private ProgressBar progressBar;
    private IjkVideoView mVideoView;
    String path = "rtmp://149.248.57.125:1935/vod/ad286f25b01849e5b4832519b7d5ba76.mp4";
    public static Boolean flagOfAsync = false;
    private AndroidMediaController mMediaController;
    private TableLayout mHudView;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        final String host = "149.248.57.125";
//        LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();
//        ExecutorService exec = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, blockingQueue);
//        new VideoListS().executeOnExecutor(exec);


        new  VideoListS().execute(host);
        setContentView(R.layout.getlink);

        ListView listView = (ListView) findViewById(R.id.listView);
//        try {
//            wait(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        while (!flagOfAsync) {

            Log.d("video", "mainThread");
            Log.d("video", videoNameList.toString());
            if(flagOfAsync){
                break;
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,videoNameList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // TODO: shift to corresponding video videoNameList
            setContentView(R.layout.activity_main);
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            //定义IjkVideoView
            mVideoView = (IjkVideoView) findViewById(R.id.video_view);
            //定义的播放按钮的layout，用来加载定义好的播放界面
            mHudView = (TableLayout) findViewById(R.id.hud_view);
            //这里使用的是Demo中提供的AndroidMediaController类控制播放相关操作
            mMediaController = new AndroidMediaController(getVideoLink.this, false);
            ActionBar actionBar = getSupportActionBar();
            mMediaController.setSupportActionBar(actionBar);
            mVideoView = (IjkVideoView) findViewById(R.id.video_view);
            mVideoView.setMediaController(mMediaController);
            mVideoView.setHudView(mHudView);
            //设置videopath，开始播放
            mVideoView.setVideoPath(path);
            mVideoView.start();
        });
    }

    public static int totalVideos;

    public void convertStringToList(String s) {
        String str[] = s.split("\\s+");
        videoNameList = Arrays.asList(str);
    }
    public class VideoListS extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPostExecute(String host){
            Log.d("video","videoNameList.toString()");
            Log.d("video",videoNameList.toString());
//            Toast.makeText(getVideoLink.this,"连接成功", Toast.LENGTH_LONG).show();
            flagOfAsync = true;
        }
        protected String doInBackground(String... arg0) {
//            publishProgress(1);
            Log.d("video","aaaaaaaaaaaaaaaaaaaaaaaaaaa");
            JSch jsch = new JSch();
            // getVideoName
            try {
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                Session session = jsch.getSession(defaultUserName, IPaddress, defaultSSHport);
                session.setPassword(password);
                session.setConfig(config);
                session.setTimeout(1000);
                session.connect();
                Log.d("video","Connected");

                // Create and connect channel.
                Channel channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                channel.setInputStream(null);
                ((ChannelExec)channel).setErrStream(System.err);
                InputStream in=channel.getInputStream();
                channel.connect();

                byte[] tmp=new byte[1024];
                while(true){
                    while(in.available()>0){
                        int i=in.read(tmp, 0, 1024);
                        if(i<0)break;
                        convertStringToList(new String(tmp, 0, i));
                    }
                    if(channel.isClosed()){
                        Log.d("video","exit-status: "+channel.getExitStatus());
                        break;
                    }
                    try{Thread.sleep(1000);}catch(Exception ee){}
                }
                channel.disconnect();
                session.disconnect();
                Log.d("video","DONE");

            }catch (JSchException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                Log.d("video","bbbbbbbbbbbbbbbbbbbbbb");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("video","cccccccccccccccccccccc");
            }
//            Log.d("video",videoNameList.toString());

            return "Executed";
        }

//        public void executeOnExecutor() {
//            Log.d("video",videoNameList.toString());
//
//
//            flagOfAsync = true;
//        }
    }

    public String getVideoName(int index) {
        return videoNameList.get(index);
    }

//    public String jointLink(int index){
//        return protocol() + "://" + address() + ":" + ffmpegPort + "/" + application() + "/" +
//                getVideoName(index);
//    }




}

