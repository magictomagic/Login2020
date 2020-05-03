package com.example.login2020.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login2020.R;
import com.example.login2020.ui.login.LoginActivity;

public class UserActivity extends AppCompatActivity {
//    private Button mReturnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        final Button mReturnButton  = findViewById(R.id.returnback);
    }
    public void back_to_login(View view) {
        Intent intent3 = new Intent(UserActivity.this, LoginActivity.class) ;
        startActivity(intent3);
//        finish();
    }

}
