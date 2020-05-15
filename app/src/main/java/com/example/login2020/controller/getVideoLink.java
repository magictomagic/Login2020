package com.example.login2020.controller;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login2020.widget.media.AndroidMediaController;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.login2020.R;
import com.example.login2020.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import static com.example.login2020.controller.conGetLinkKu.videoNameList;

public class getVideoLink extends AppCompatActivity {
    AndroidMediaController mMediaController;
    IjkVideoView mVideoView;
    TableLayout mHudView;
    String path = "rtmp://149.248.57.125:1935/vod/ad286f25b01849e5b4832519b7d5ba76.mp4";
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.getlink);
        // TODO: 创建一个线程更新 videoNameList
//        ThreadGetLink tgl = new ThreadGetLink();
//        tgl.setName("backThread");
//        tgl.start();
//        Log.d("video", "tgl.start()");
        while (videoNameList.size() == 0){

            Log.d("video", "is 0");
        }
        // videoNameList 同步锁？


        // TODO: 当 videoNameList.size() != 0时执行下面的线程
        ListView listView = (ListView) findViewById(R.id.listView);
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
    public static class ThreadGetLink extends Thread{
        @Override
        public void run(){
            conGetLinkKu cg = new conGetLinkKu();
            cg.getVideoNameList();
            Log.d("video", "String.valueOf(videoNameList)");
            Log.d("video", String.valueOf(videoNameList));
        }
    }


}

