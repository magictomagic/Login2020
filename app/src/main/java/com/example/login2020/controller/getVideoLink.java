package com.example.login2020.controller;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login2020.widget.media.AndroidMediaController;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.login2020.R;
import com.example.login2020.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import static com.example.login2020.controller.conGetLinkKu.videoNameList;
public class getVideoLink extends AppCompatActivity {
    AndroidMediaController mMediaController;
    IjkVideoView mVideoView;
    TableLayout mHudView;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.getlink);
        while (videoNameList.size() == 0){
            Log.d("video", "is 0");
        }
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoNameList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            setContentView(R.layout.activity_main);
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            //定义IjkVideoView
            mVideoView = findViewById(R.id.video_view);
            //定义的播放按钮的layout，用来加载定义好的播放界面
            mHudView = findViewById(R.id.hud_view);
            //这里使用的是Demo中提供的AndroidMediaController类控制播放相关操作
            mMediaController = new AndroidMediaController(getVideoLink.this, false);
            ActionBar actionBar = getSupportActionBar();
            mMediaController.setSupportActionBar(actionBar);
            mVideoView = findViewById(R.id.video_view);
            mVideoView.setMediaController(mMediaController);
            mVideoView.setHudView(mHudView);
            //设置videopath，开始播放
            Log.d("video1", String.valueOf((int) id));
            Log.d("video1", videoNameList.get((int) id));
            mVideoView.setVideoPath(conGetLinkKu.jointLink((int) id));
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
//            Toast.makeText(getVideoLink.this,"刷新成功",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}

