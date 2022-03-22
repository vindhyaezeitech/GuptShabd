package com.shabdamsdk.ui.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.shabdamsdk.Constants;
import com.shabdamsdk.GameActivity;
import com.shabdamsdk.GamePresenter;
import com.shabdamsdk.GameView;
import com.shabdamsdk.R;
import com.shabdamsdk.model.leaderboard.LeaderboardListModel;
import com.shabdamsdk.pref.CommonPreference;
import com.shabdamsdk.ui.adapter.GetLeaderboardListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity implements GameView, View.OnClickListener {

    private String type;
    private GamePresenter gamePresenter;
    private GetLeaderboardListAdapter adapter;
    private RecyclerView recyclerView;
    private RelativeLayout rl_one, rl_two, rl_three, rl_share_btn;
    private TextView tv_name_one, tv_name_two, tv_name_three;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        interstitialAdd();
        inIt();
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);
        recyclerView = findViewById(R.id.rv_getLeaderboard_List);
        rl_one = findViewById(R.id.rl_one);
        rl_two = findViewById(R.id.rl_two);
        rl_three = findViewById(R.id.rl_three);
        tv_name_one = findViewById(R.id.tv_name_one);
        tv_name_two = findViewById(R.id.tv_name_two);
        tv_name_three = findViewById(R.id.tv_name_three);
        rl_share_btn = findViewById(R.id.rl_share_btn);
        findViewById(R.id.rl_agla_shabd_btn).setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        rl_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot(getWindow().getDecorView());
            }
        });

        callGetLeaderBoardListAPI();

    }

    private void callGetLeaderBoardListAPI() {
        String game_id = CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID);

        gamePresenter = new GamePresenter(this, LeaderBoardActivity.this);
        //  compositeDisposable = interestPresenter.loadCategoryData();
        gamePresenter.fetchLeaderBoardList(game_id);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back_btn) {
            if (!TextUtils.isEmpty(type) && type.equals("1")) {
                Intent intent = new Intent(this, ShabdamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (!TextUtils.isEmpty(type) && type.equals("2")) {
                Intent intent = new Intent(this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("user_id", CommonPreference.getInstance(this).getString(CommonPreference.Key.USER_ID));
                intent.putExtra("name", CommonPreference.getInstance(this).getString(CommonPreference.Key.NAME));
                intent.putExtra("uname", CommonPreference.getInstance(this).getString(CommonPreference.Key.UNAME));
                intent.putExtra("email", CommonPreference.getInstance(this).getString(CommonPreference.Key.EMAIL));
                intent.putExtra("profile_image", CommonPreference.getInstance(this).getString(CommonPreference.Key.PROFILE_IMAGE));
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        }else if (view.getId() == R.id.rl_agla_shabd_btn) {
            loadAdd();
        }
    }

    private void takeScreenShot(View view) {
        Date date = new Date();
        CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);

        try {
            File mainDir = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
            if (!mainDir.exists()) {
                boolean mkdir = mainDir.mkdir();
            }
            String path = mainDir + "/" + "TrendOceans" + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            shareScreenShot(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareScreenShot(File imageFile) {
        Uri uri = FileProvider.getUriForFile(
                this,
                "com.guptshabd.LeaderBoardActivity.provider",
                imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        //intent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Application from Instagram");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
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
    public void onGetLeaderBoardListFetched(List<LeaderboardListModel> list) {
        if (list.size() == 1) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.GONE);
            rl_three.setVisibility(View.GONE);
            tv_name_one.setText(list.get(0).getName());
        } else if (list.size() == 2) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.VISIBLE);
            rl_three.setVisibility(View.GONE);
            tv_name_one.setText(list.get(0).getName());
            tv_name_two.setText(list.get(1).getName());

        } else if (list.size() == 3) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.VISIBLE);
            rl_three.setVisibility(View.VISIBLE);
            tv_name_one.setText(list.get(0).getName());
            tv_name_two.setText(list.get(1).getName());
            tv_name_three.setText(list.get(2).getName());
        } else if (list.size() == 4) {
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.VISIBLE);
            rl_three.setVisibility(View.VISIBLE);
            tv_name_one.setText(list.get(0).getName());
            tv_name_two.setText(list.get(1).getName());
            tv_name_three.setText(list.get(2).getName());
        }

        adapter = new GetLeaderboardListAdapter(this, list);
        RecyclerView.LayoutManager layoutManagerLive = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerLive);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!TextUtils.isEmpty(type) && type.equals("2")) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_id", CommonPreference.getInstance(this).getString(CommonPreference.Key.USER_ID));
            intent.putExtra("name", CommonPreference.getInstance(this).getString(CommonPreference.Key.NAME));
            intent.putExtra("uname", CommonPreference.getInstance(this).getString(CommonPreference.Key.UNAME));
            intent.putExtra("email", CommonPreference.getInstance(this).getString(CommonPreference.Key.EMAIL));
            intent.putExtra("profile_image", CommonPreference.getInstance(this).getString(CommonPreference.Key.PROFILE_IMAGE));
            startActivity(intent);
            finish();
        }
    }

    private void interstitialAdd() {

       /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });*/

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, Constants.INTRESTITIAL_AD_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //Log.i(TAG, "onAdLoaded");
                        // loadAdd();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }


                });

    }



    private void loadAdd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    startGame();
                    interstitialAdd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    startGame();
                    interstitialAdd();
                }
            });
        } else {
            startGame();
           // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("user_id", CommonPreference.getInstance(this).getString(CommonPreference.Key.USER_ID));
        intent.putExtra("name", CommonPreference.getInstance(this).getString(CommonPreference.Key.NAME));
        intent.putExtra("uname", CommonPreference.getInstance(this).getString(CommonPreference.Key.UNAME));
        intent.putExtra("email", CommonPreference.getInstance(this).getString(CommonPreference.Key.EMAIL));
        intent.putExtra("profile_image", CommonPreference.getInstance(this).getString(CommonPreference.Key.PROFILE_IMAGE));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gamePresenter != null){
            gamePresenter.onDestroy();
        }
    }
}