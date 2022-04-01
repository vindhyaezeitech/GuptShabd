package com.shabdamsdk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.model.SignupRequest;
import com.shabdamsdk.model.adduser.AddUserRequest;
import com.shabdamsdk.pref.CommonPreference;

public  class ShabdamSplashActivity extends AppCompatActivity implements GameView {
    private GamePresenter gamePresenter;
    private String userId, name, u_name, email, profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_shabdam_main);
        gamePresenter = new GamePresenter(this, ShabdamSplashActivity.this);

        // load the animation
        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("user_id");
            name = !TextUtils.isEmpty(getIntent().getStringExtra("name"))? getIntent().getStringExtra("name") : " ";
            u_name = !TextUtils.isEmpty(getIntent().getStringExtra("uname")) ? getIntent().getStringExtra("uname") : " ";
            email = !TextUtils.isEmpty(getIntent().getStringExtra("email"))? getIntent().getStringExtra("email") : " ";
            profile_image = !TextUtils.isEmpty(getIntent().getStringExtra("profile_image")) ? getIntent().getStringExtra("profile_image") :" ";

            if (!TextUtils.isEmpty(userId)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this).put(CommonPreference.Key.USER_ID, userId);
            }

            if (!TextUtils.isEmpty(name)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this).put(CommonPreference.Key.NAME, name);
            }
            if (!TextUtils.isEmpty(u_name)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this).put(CommonPreference.Key.UNAME, u_name);
            }
            if (!TextUtils.isEmpty(email)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this).put(CommonPreference.Key.EMAIL, email);
            }
            if (!TextUtils.isEmpty(profile_image)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this).put(CommonPreference.Key.PROFILE_IMAGE, profile_image);
            }
        }else if(!TextUtils.isEmpty(CommonPreference.getInstance(ShabdamSplashActivity.this).getString(CommonPreference.Key.GAME_USER_ID))){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ShabdamSplashActivity.this, GameActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
        else {
            return;
        }

        if (!TextUtils.isEmpty(userId)) {
            AddUserRequest request = new AddUserRequest();
            request.setUserId(userId);
            request.setName(name);
            request.setUname(u_name);
            request.setEmail(email);
            request.setProfileimage(profile_image);
            gamePresenter.addUser(request);
        }else if(email != null){
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail(email);
            signupRequest.setName(name);
            signupRequest.setUname(name);
            signupRequest.setProfileimage(profile_image);
            gamePresenter.signUpUser(signupRequest);
        }
       /* Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("user_id",getIntent().getStringExtra("user_id"));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("uname","vikash");
                intent.putExtra("email","vikash@mailinator.com");
                intent.putExtra("profile_image","");
                startActivity(intent);
                finish();
            }
        },2000);*/
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onAddUser(com.shabdamsdk.model.adduser.Data data) {
        if (data != null) {
            if (data.getId() != 0) {
                CommonPreference.getInstance(this).put(CommonPreference.Key.GAME_USER_ID, String.valueOf(data.getId()));
                Intent intent = new Intent(ShabdamSplashActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gamePresenter != null){
            gamePresenter.onDestroy();
        }
    }
}
