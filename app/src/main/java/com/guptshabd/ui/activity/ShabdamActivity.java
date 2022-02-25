package com.guptshabd.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guptshabd.GameActivity;
import com.guptshabd.GamePresenter;
import com.guptshabd.GameView;
import com.guptshabd.R;
import com.guptshabd.model.statistics.Data;

public class ShabdamActivity extends AppCompatActivity implements GameView, View.OnClickListener {
    private GamePresenter gamePresenter;
    private TextView tv_played, tv_win, tv_current_streak, tv_max_streak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabdam);
        inIt();
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);
        findViewById(R.id.iv_question_mark_btn).setOnClickListener(this);
        findViewById(R.id.iv_trophy_btn).setOnClickListener(this);
        findViewById(R.id.iv_statistics_btn).setOnClickListener(this);
        findViewById(R.id.iv_settings_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_btn: {
                Intent intent = new Intent(this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            }

            case R.id.iv_question_mark_btn:
                kaiseKhelePopup();
                break;

            case R.id.iv_trophy_btn:
                Intent intent = new Intent(this, LeaderBoardActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;

            case R.id.iv_statistics_btn:
                statisticsPopup();
                break;

            case R.id.iv_settings_btn:
                Intent intent1 = new Intent(this, SettingsActivity.class);
                intent1.putExtra("type", "1");
                startActivity(intent1);
                break;
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
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        callgetStreakAPI();
        alertDialog.show();
    }

    private void callgetStreakAPI() {
        gamePresenter = new GamePresenter(this);
        gamePresenter.fetchStatisticsData();
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

        int win = Integer.parseInt(data.getWin());
        int total_played = Integer.parseInt(data.getPlayed());
        int percent = (win / total_played) * 100;
        tv_win.setText(percent);
        Log.d("Percentage", "" + percent);
    }
}