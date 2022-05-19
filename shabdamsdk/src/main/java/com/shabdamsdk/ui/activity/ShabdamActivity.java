package com.shabdamsdk.ui.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import com.shabdamsdk.model.statistics.Data;
import com.shabdamsdk.pref.CommonPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ShabdamActivity extends AppCompatActivity implements GameView, View.OnClickListener {

    char[] word_array = new char[3];
    char[] entered_word_array = new char[3];
    StringBuilder[] matra = new StringBuilder[3];
    char[] charArray;
    private GamePresenter gamePresenter;
    private TextView tv_played, tv_win, tv_current_streak, tv_max_streak, tv_timer_counter_text;
    private String correctWord;
    private TextView tvOne, tvTwo, tvThree;
    private RelativeLayout agla_shabd_btn, rl_share_btn;
    private String minute, second, currentAttempt;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabdam);
        adRequest = new AdRequest.Builder().build();

        interstitialAdd();
        if (getIntent().getExtras() != null) {
            correctWord = getIntent().getStringExtra("word");
            minute = getIntent().getStringExtra("minute");
            second = getIntent().getStringExtra("second");
            currentAttempt = getIntent().getStringExtra("currentAttempt");
            Log.d("time_", minute);
            Log.d("time_", second);
            showMatraText();
        }
        inIt();
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);
        findViewById(R.id.iv_question_mark_btn).setOnClickListener(this);
        findViewById(R.id.iv_trophy_btn).setOnClickListener(this);
        findViewById(R.id.iv_statistics_btn).setOnClickListener(this);
        findViewById(R.id.iv_settings_btn).setOnClickListener(this);
        findViewById(R.id.rl_agla_shabd_btn).setOnClickListener(this);

        tvOne = findViewById(R.id.tv_one);
        tvTwo = findViewById(R.id.tv_two);
        tvThree = findViewById(R.id.tv_three);

        try {
            tvOne.setText(new StringBuilder().append(word_array[0]).append(matra[0]));
            tvTwo.setText(new StringBuilder().append(word_array[1]).append(matra[1]));
            tvThree.setText(new StringBuilder().append(word_array[2]).append(matra[2]));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showMatraText() {
        try {
            charArray = correctWord.toCharArray();
            matra[0] = new StringBuilder();
            matra[1] = new StringBuilder();
            matra[2] = new StringBuilder();
            int count = 0;
            boolean br = false;
            for (int i = 0; i < charArray.length; i++) {
                if (checkLetter(charArray[i])) {
                    word_array[count] = charArray[i];
                    count++;
                } else {
                    matra[count - 1].append(charArray[i]);
                }

            }
            Log.d("", matra.toString());
        } catch (Exception e) {

        }
    }

    private boolean checkLetter(char c) {
        return ((int) c >= 2309 && (int) c <= 2316) || ((int) c >= 2325 && (int) c <= 2361)
                || (int) c == 2319 || (int) c == 2320 || (int) c == 2323 || (int) c == 2324;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back_btn) {
            Intent intent = new Intent(this, GameActivity.class);
           // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_id", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
            intent.putExtra("name", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.NAME));
            intent.putExtra("uname", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.UNAME));
            intent.putExtra("email", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.EMAIL));
            intent.putExtra("profile_image", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.PROFILE_IMAGE));
            startActivity(intent);
            finish();
        } else if (id == R.id.iv_question_mark_btn) {
            kaiseKhelePopup();
        } else if (id == R.id.iv_trophy_btn) {
            Intent intent = new Intent(this, ShabdamLeaderBoardActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
            finish();
        } else if (id == R.id.iv_statistics_btn) {
            statisticsPopup();
        } else if (id == R.id.iv_settings_btn) {
            Intent intent1 = new Intent(this, ShabdamSettingsActivity.class);
            intent1.putExtra("type", "1");
            startActivity(intent1);
        } else if (id == R.id.rl_agla_shabd_btn) {
            findViewById(R.id.rl_agla_shabd_btn).setClickable(false);

            loadAdd();
        }
    }


    private void statisticsPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShabdamActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.statistics_popup_layout, viewGroup, false);
        ImageView cancel_btn = dialogView.findViewById(R.id.iv_cancel_btn);
        tv_played = dialogView.findViewById(R.id.tv_played);
        tv_win = dialogView.findViewById(R.id.tv_win);
        tv_current_streak = dialogView.findViewById(R.id.tv_current_streak);
        tv_max_streak = dialogView.findViewById(R.id.tv_max_streak);
        agla_shabd_btn = dialogView.findViewById(R.id.rl_agla_shabd_btn);
        rl_share_btn = dialogView.findViewById(R.id.rl_share_btn);


        if(currentAttempt.equals("1")){
            dialogView.findViewById(R.id.ll_one).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_two).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ll_three).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ll_four).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ll_five).setVisibility(View.GONE);
        }else if(currentAttempt.equals("2")){
            dialogView.findViewById(R.id.ll_one).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_two).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_three).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ll_four).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ll_five).setVisibility(View.GONE);
        }else if(currentAttempt.equals("3")){
            dialogView.findViewById(R.id.ll_one).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_two).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_three).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_four).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ll_five).setVisibility(View.GONE);
        }else if( currentAttempt.equals("4")){
            dialogView.findViewById(R.id.ll_one).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_two).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_three).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_four).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.ll_five).setVisibility(View.GONE);
        }

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        rl_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot(dialogView);
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        callgetStreakAPI();
        alertDialog.show();
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
                CommonPreference.getInstance(ShabdamActivity.this.getApplicationContext()).getPackageString("applicationId")+".ShabdamActivity.provider",
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

    private void callgetStreakAPI() {
        String game_id = CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID);
        gamePresenter = new GamePresenter(this, ShabdamActivity.this);
        gamePresenter.fetchStatisticsData(game_id);
    }

    private void kaiseKhelePopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShabdamActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.kaise_khele_popup_layout, viewGroup, false);
        RelativeLayout continue_btn = dialogView.findViewById(R.id.rl_continue_btn);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
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
    public void onStatisticsDataFetched(Data data) {
        GameView.super.onStatisticsDataFetched(data);
        tv_played.setText(data.getPlayed());
        tv_current_streak.setText(data.getCurrentStreak());
        tv_max_streak.setText(data.getMaxStreak());

        if (data.getWin().equals("0") || data.getPlayed().equals("0")) {
            tv_win.setText("0");
        } else {
            int win = Integer.parseInt(data.getWin());
            int total_played = Integer.parseInt(data.getPlayed());
            float percent = (win / total_played) * 100f;
            tv_win.setText(String.valueOf(percent));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, GameActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("user_id", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
        intent.putExtra("name", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.NAME));
        intent.putExtra("uname", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.UNAME));
        intent.putExtra("email", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.EMAIL));
        intent.putExtra("profile_image", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.PROFILE_IMAGE));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        word_array = null;
        matra = null;
        charArray = null;
        adRequest = null;
        if(gamePresenter != null){
            gamePresenter.onDestroy();
        }
        super.onDestroy();

    }

    private void interstitialAdd() {

       /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });*/

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
            findViewById(R.id.rl_agla_shabd_btn).setClickable(true);

            startGame();
           // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
      //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("user_id", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
        intent.putExtra("name", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.NAME));
        intent.putExtra("uname", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.UNAME));
        intent.putExtra("email", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.EMAIL));
        intent.putExtra("profile_image", CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.PROFILE_IMAGE));
        startActivity(intent);
        finish();
    }


}