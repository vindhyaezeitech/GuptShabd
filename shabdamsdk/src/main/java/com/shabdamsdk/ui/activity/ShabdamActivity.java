package com.shabdamsdk.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.GameActivity;
import com.shabdamsdk.GamePresenter;
import com.shabdamsdk.GameView;
import com.shabdamsdk.R;
import com.shabdamsdk.model.statistics.Data;
import com.shabdamsdk.pref.CommonPreference;

public class ShabdamActivity extends AppCompatActivity implements GameView, View.OnClickListener {
    char[] word_array = new char[3];
    char[] entered_word_array = new char[3];
    StringBuilder[] matra = new StringBuilder[3];
    char[] charArray;
    private GamePresenter gamePresenter;
    private TextView tv_played, tv_win, tv_current_streak, tv_max_streak, tv_timer_counter_text;
    private String correctWord;
    private TextView tvOne, tvTwo, tvThree;
    private RelativeLayout agla_shabd_btn;
    private String minute, second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabdam);
        if (getIntent().getExtras() != null) {
            correctWord = getIntent().getStringExtra("word");
            minute = getIntent().getStringExtra("minute");
            second = getIntent().getStringExtra("second");
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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_id", CommonPreference.getInstance(this).getString(CommonPreference.Key.USER_ID));
            intent.putExtra("name", CommonPreference.getInstance(this).getString(CommonPreference.Key.NAME));
            intent.putExtra("uname", CommonPreference.getInstance(this).getString(CommonPreference.Key.UNAME));
            intent.putExtra("email", CommonPreference.getInstance(this).getString(CommonPreference.Key.EMAIL));
            intent.putExtra("profile_image", CommonPreference.getInstance(this).getString(CommonPreference.Key.PROFILE_IMAGE));
            startActivity(intent);
            finish();
        } else if (id == R.id.iv_question_mark_btn) {
            kaiseKhelePopup();
        } else if (id == R.id.iv_trophy_btn) {
            Intent intent = new Intent(this, LeaderBoardActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
        } else if (id == R.id.iv_statistics_btn) {
            statisticsPopup();
        } else if (id == R.id.iv_settings_btn) {
            Intent intent1 = new Intent(this, SettingsActivity.class);
            intent1.putExtra("type", "1");
            startActivity(intent1);
        } else if (id == R.id.rl_agla_shabd_btn) {
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
        tv_timer_counter_text = dialogView.findViewById(R.id.tv_time_counter_text);
        tv_timer_counter_text.setText(minute + " " + ":" + " " + second);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        agla_shabd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShabdamActivity.this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("user_id", CommonPreference.getInstance(ShabdamActivity.this).getString(CommonPreference.Key.USER_ID));
                intent.putExtra("name", CommonPreference.getInstance(ShabdamActivity.this).getString(CommonPreference.Key.NAME));
                intent.putExtra("uname", CommonPreference.getInstance(ShabdamActivity.this).getString(CommonPreference.Key.UNAME));
                intent.putExtra("email", CommonPreference.getInstance(ShabdamActivity.this).getString(CommonPreference.Key.EMAIL));
                intent.putExtra("profile_image", CommonPreference.getInstance(ShabdamActivity.this).getString(CommonPreference.Key.PROFILE_IMAGE));
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        callgetStreakAPI();
        alertDialog.show();
    }

    private void callgetStreakAPI() {
        String game_id = CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID);
        gamePresenter = new GamePresenter(this);
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
}