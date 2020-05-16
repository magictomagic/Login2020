package com.example.login2020.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login2020.R;
import com.example.login2020.controller.getVideoLink;
import com.example.login2020.ui.login.LoginActivity;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class UserActivity extends AppCompatActivity {
//    private Button mReturnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        final Button mReturnButton = findViewById(R.id.returnback);
        final Button gotoVideoList = findViewById(R.id.goto_video_inf_list);
        final Button reflash = findViewById(R.id.reflash);
        reflash.setEnabled(true);
        reflash.setOnClickListener(v -> {
            getVideoLink.ThreadGetLink tgl = new getVideoLink.ThreadGetLink();
            tgl.setName("backThread");
            tgl.start();
            Toast.makeText(this,"刷新成功", Toast.LENGTH_SHORT).show();
        });

        gotoVideoList.setEnabled(true);

        gotoVideoList.setOnClickListener(v -> {
            Intent intentL = new Intent(UserActivity.this, getVideoLink.class);
            startActivity(intentL);

        });
    }

    public void back_to_login(View view) {
        Intent intent3 = new Intent(UserActivity.this, LoginActivity.class) ;
        startActivity(intent3);

        // add
        finish();
    }



}
