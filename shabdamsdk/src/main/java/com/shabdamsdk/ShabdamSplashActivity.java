package com.shabdamsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.model.SignupRequest;
import com.shabdamsdk.model.adduser.AddUserRequest;
import com.shabdamsdk.pref.CommonPreference;
import com.shabdamsdk.ui.activity.TutorialActivity;
import com.shabdamsdk.ui.activity.UserDetailActivity;

import org.jetbrains.annotations.NotNull;

public  class ShabdamSplashActivity extends AppCompatActivity implements GameView {
    private GamePresenter gamePresenter;
    private String userId, name, u_name, email, profile_image;

   /* public static void startShabdam(@NonNull Context context, @NonNull String userId, @NonNull String name, String uName, String email, String profile_image, String rewardAdId, String interstitialsAdId, String bannerAdId){
        if(context == null){
            Log.e(Constants.SHABDAM_TAG, "context cannot be null");
            return;
        }

        if(!TextUtils.isEmpty(userId)){
            Log.e(Constants.SHABDAM_TAG, "User Id cannot be null");
            return;
        }

        if(!TextUtils.isEmpty(name)){
            Log.e(Constants.SHABDAM_TAG, "Name cannot be null");
            return;
        }

        Intent intent = new Intent(context, ShabdamSplashActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("name", name);
        intent.putExtra("uname", uName);
        intent.putExtra("email", email);
        intent.putExtra("profile_image", profile_image);
        context.startActivity(intent);
    }*/

    public static void startShabdam(@NotNull Context context, @NotNull String appPackageName,@NotNull String appUniqueId){
        Intent intent = new Intent(context, ShabdamSplashActivity.class);
        intent.putExtra("applicationId", appPackageName);
        intent.putExtra("appUniqueId", appUniqueId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_shabdam_main);
        GameDataManager.getInstance().shuffleList();
        gamePresenter = new GamePresenter(this, ShabdamSplashActivity.this);


        if(getIntent() != null && getIntent().getExtras() != null && !TextUtils.isEmpty(getIntent().getStringExtra("applicationId"))){
            CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).put("applicationId", getIntent().getStringExtra("applicationId"));
        }

        if(getIntent() != null && getIntent().getExtras() != null && !TextUtils.isEmpty(getIntent().getStringExtra("appUniqueId"))){
            CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).put("appUniqueId", getIntent().getStringExtra("appUniqueId"));
        }

        // load the animation
        if (getIntent().getExtras() != null && !TextUtils.isEmpty(getIntent().getStringExtra("user_id"))) {
            userId = getIntent().getStringExtra("user_id");
            name = !TextUtils.isEmpty(getIntent().getStringExtra("name"))? getIntent().getStringExtra("name") : " ";
            u_name = !TextUtils.isEmpty(getIntent().getStringExtra("uname")) ? getIntent().getStringExtra("uname") : " ";
            email = !TextUtils.isEmpty(getIntent().getStringExtra("email"))? getIntent().getStringExtra("email") : " ";
            profile_image = !TextUtils.isEmpty(getIntent().getStringExtra("profile_image")) ? getIntent().getStringExtra("profile_image") :" ";

            if (!TextUtils.isEmpty(userId)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).put(CommonPreference.Key.USER_ID, userId);
            }

            if (!TextUtils.isEmpty(name)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).put(CommonPreference.Key.NAME, name);
            }

            if (!TextUtils.isEmpty(u_name)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).put(CommonPreference.Key.UNAME, u_name);
            }

            if (!TextUtils.isEmpty(email)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).put(CommonPreference.Key.EMAIL, email);
            }

            if (!TextUtils.isEmpty(profile_image)) {
                CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).put(CommonPreference.Key.PROFILE_IMAGE, profile_image);
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

        }else if(!TextUtils.isEmpty(CommonPreference.getInstance(ShabdamSplashActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID))){
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ShabdamSplashActivity.this, UserDetailActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }

        /*if (!TextUtils.isEmpty(userId)) {
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
        }*/
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
                CommonPreference.getInstance(this.getApplicationContext()).put(CommonPreference.Key.GAME_USER_ID, String.valueOf(data.getId()));
                Intent intent = new Intent(ShabdamSplashActivity.this, TutorialActivity.class);
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
