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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guptshabd.GameActivity;
import com.guptshabd.R;

public class ShabdamActivity extends AppCompatActivity implements View.OnClickListener {

    private String correctWord;
    private TextView tvOne, tvTwo, tvThree;

    char[] word_array = new char[3];
    char[] entered_word_array = new char[3];
    StringBuilder[] matra = new StringBuilder[3];
    char[] charArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabdam);
        if(getIntent().getExtras() != null){
            correctWord = getIntent().getStringExtra("word");
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

        tvOne = findViewById(R.id.tv_one);
        tvTwo = findViewById(R.id.tv_two);
        tvThree = findViewById(R.id.tv_three);

        try{
            tvOne.setText(new StringBuilder().append(word_array[0]).append(matra[0]));
            tvTwo.setText(new StringBuilder().append(word_array[1]).append(matra[1]));
            tvThree.setText(new StringBuilder().append(word_array[2]).append(matra[2]));

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void showMatraText() {
        try{
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
        }catch (Exception e){

        }
    }

    private boolean checkLetter(char c) {
        if (((int) c >= 2309 && (int) c <= 2316) || ((int) c >= 2325 && (int) c <= 2361)
                || (int) c == 2319 || (int) c == 2320 || (int) c == 2323 || (int) c == 2324) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_back_btn:
            {
                /*Intent intent = new Intent(this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                finish();
                break;
            }

            case R.id.iv_question_mark_btn:
                kaiseKhelePopup();
                break;

            case R.id.iv_trophy_btn:
                Intent intent=new Intent(this, LeaderBoardActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;

            case R.id.iv_statistics_btn:
                statisticsPopup();
                break;

            case R.id.iv_settings_btn:
                Intent intent1=new Intent(this, SettingsActivity.class);
                intent1.putExtra("type","1");
                startActivity(intent1);
                break;
        }

    }
    private void statisticsPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShabdamActivity.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.statistics_popup_layout, viewGroup, false);
        ImageView cancel_btn=dialogView.findViewById(R.id.iv_cancel_btn);
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
        alertDialog.show();
    }

    private void kaiseKhelePopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShabdamActivity.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.kaise_khele_popup_layout, viewGroup, false);
        RelativeLayout continue_btn=dialogView.findViewById(R.id.rl_continue_btn);
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
}