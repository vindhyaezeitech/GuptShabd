package com.shabdamsdk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.model.GetWordRequest;
import com.shabdamsdk.model.adduser.AddUserRequest;
import com.shabdamsdk.pref.CommonPreference;

public  class MainActivity extends AppCompatActivity implements GameView {
    private GamePresenter gamePresenter;
    private String userId, name, u_name, email, profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gamePresenter = new GamePresenter(this, MainActivity.this);

        // load the animation
        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("user_id");
            name = getIntent().getStringExtra("name");
            u_name = getIntent().getStringExtra("uname");
            email = getIntent().getStringExtra("email");
            profile_image = getIntent().getStringExtra("profile_image");

            if (!TextUtils.isEmpty(userId)) {
                CommonPreference.getInstance(MainActivity.this).put(CommonPreference.Key.USER_ID, userId);
            }

            if (!TextUtils.isEmpty(name)) {
                CommonPreference.getInstance(MainActivity.this).put(CommonPreference.Key.NAME, name);
            }
            if (!TextUtils.isEmpty(u_name)) {
                CommonPreference.getInstance(MainActivity.this).put(CommonPreference.Key.UNAME, u_name);
            }
            if (!TextUtils.isEmpty(email)) {
                CommonPreference.getInstance(MainActivity.this).put(CommonPreference.Key.EMAIL, email);
            }
            if (!TextUtils.isEmpty(profile_image)) {
                CommonPreference.getInstance(MainActivity.this).put(CommonPreference.Key.PROFILE_IMAGE, profile_image);
            }
        } else {
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
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
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
