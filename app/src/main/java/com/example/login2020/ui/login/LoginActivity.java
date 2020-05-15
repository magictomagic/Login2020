package com.example.login2020.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login2020.R;
import com.example.login2020.controller.conGetLinkKu;
import com.example.login2020.controller.getVideoLink;
import com.example.login2020.ui.register.RegisterActivity;
import com.example.login2020.ui.user.UserActivity;

import static com.example.login2020.controller.conGetLinkKu.videoNameList;
import static com.example.login2020.controller.conGetLinkKu.videoNameList;
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private UserDataManager mUserDataManager;         //用户数据管理类
    private SharedPreferences login_sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //加载 res.layout/ 下 activity_login.xml
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.login_register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView logininSuccessShow = findViewById(R.id.login_success_show);
        final View loginSuccessView = findViewById(R.id.login_success_view);
        final CheckBox mRememberCheck = findViewById(R.id.Login_Remember);

        getVideoLink.ThreadGetLink tgl = new getVideoLink.ThreadGetLink();
        tgl.setName("backThread");
        tgl.start();
        Log.d("video", "tgl.start()");

        // 如果不加这句，按钮就是灰色的，点不了。
        registerButton.setEnabled(true);
        // 注意区别 .setClickable 定时让按键自己按
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            usernameEditText.setText(name);
            passwordEditText.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                String userName = usernameEditText.getText().toString().trim();     // trim means 修剪，去除字符串两边的空白字符
                String userPwd = passwordEditText.getText().toString().trim();
                SharedPreferences.Editor editor =login_sp.edit();
                int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
                if(result==1){                                             //返回1说明用户名和密码均正确
                    //保存用户名和密码
                    editor.putString("USER_NAME", userName);
                    editor.putString("PASSWORD", userPwd);

                    //是否记住密码
                    if(mRememberCheck.isChecked()){
                        editor.putBoolean("mRememberCheck", true);
                    }else{
                        editor.putBoolean("mRememberCheck", false);
                    }
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, UserActivity.class) ;    //切换Login Activity至User Activity
                    startActivity(intent);
//                    finish();
                    Toast.makeText(LoginActivity.this, getString(R.string.login_success),Toast.LENGTH_SHORT).show();//登录成功提示
                }else if(result==0){
                    Toast.makeText(LoginActivity.this, getString(R.string.login_fail),Toast.LENGTH_SHORT).show();  //登录失败提示
//                    System.out.println("login fail");
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 切换到 register.xml
//                System.out.println("registerButton");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                System.out.println("registerButton");
                // 老子加的，看看对改善崩溃有没有好处
                finish();

            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    public class ThreadGetLink extends Thread{
        @Override
        public void run(){
            conGetLinkKu cg = new conGetLinkKu();
            cg.getVideoNameList();
            Log.d("video", "String.valueOf(videoNameList)");
            Log.d("video", String.valueOf(videoNameList));
        }
    }
}
