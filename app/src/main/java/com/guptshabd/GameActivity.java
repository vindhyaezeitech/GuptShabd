package com.guptshabd;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guptshabd.model.GetWordRequest;
import com.guptshabd.model.getwordresp.Datum;
import com.guptshabd.model.statistics.Data;
import com.guptshabd.ui.activity.LeaderBoardActivity;
import com.guptshabd.ui.activity.SettingsActivity;
import com.guptshabd.ui.activity.ShabdamActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main Activity
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener, GameView {

    private int index = 0;
    private int MAX_INDEX = 16;
    private int currentAttempt = 1;
    public static final int MAX_CHAR_LENGTH = 3;
    public static final int MAX_ATTEMPT = 5;
    private String correctWord = "संदेश";

    private TextView tvKa;
    private TextView tvCross;
    private TextView tvEnter;
    char[] word_array = new char[3];
    char[] entered_word_array = new char[3];
    private ArrayList<Integer> btnIdList = new ArrayList<>();

    StringBuilder[] matra = new StringBuilder[3];
    char[] charArray;
    private RelativeLayout rl_uttar_dekho_btn, continue_btn;

    private GamePresenter gamePresenter;
    private FrameLayout flLoading;

    Animation animBlink;
    int blinkCount;

    private TextView tv_played,tv_win,tv_current_streak,tv_max_streak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // load the animation
        animBlink = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        initViewClick();

        gamePresenter = new GamePresenter(this);
        GetWordRequest getWordRequest = new GetWordRequest();
        getWordRequest.setUserId("1");
        getWordRequest.setWordId(Arrays.asList("2","3"));
        gamePresenter.fetchNewWord(getWordRequest);

    }

    private void initViewClick() {
        tvKa = findViewById(R.id.tv_ka);
        tvCross = findViewById(R.id.tv_cross);
        tvEnter = findViewById(R.id.tv_enter);
        flLoading = findViewById(R.id.fl_loading);

        tvKa.setOnClickListener(this);
        tvCross.setOnClickListener(this);
        tvEnter.setOnClickListener(this);





        findViewById(R.id.tv_kha).setOnClickListener(this);
        findViewById(R.id.tv_ga).setOnClickListener(this);
        findViewById(R.id.tv_gha).setOnClickListener(this);
        findViewById(R.id.tv_anga).setOnClickListener(this);
        findViewById(R.id.tv_cha).setOnClickListener(this);
        findViewById(R.id.tv_chah).setOnClickListener(this);
        findViewById(R.id.tv_ja).setOnClickListener(this);
        findViewById(R.id.tv_jha).setOnClickListener(this);
        findViewById(R.id.tv_ea).setOnClickListener(this);
        findViewById(R.id.tv_ta).setOnClickListener(this);
        findViewById(R.id.tdha).setOnClickListener(this);
        findViewById(R.id.tv_da).setOnClickListener(this);
        findViewById(R.id.tv_dha).setOnClickListener(this);
        findViewById(R.id.tv_ada).setOnClickListener(this);
        findViewById(R.id.tv_tea).setOnClickListener(this);
        findViewById(R.id.tv_tha).setOnClickListener(this);
        findViewById(R.id.tv_dea).setOnClickListener(this);
        findViewById(R.id.tv_dhea).setOnClickListener(this);
        findViewById(R.id.tv_na).setOnClickListener(this);
        findViewById(R.id.tv_pa).setOnClickListener(this);
        findViewById(R.id.tv_fa).setOnClickListener(this);
        findViewById(R.id.tv_ba).setOnClickListener(this);
        findViewById(R.id.tv_bha).setOnClickListener(this);
        findViewById(R.id.tv_ma).setOnClickListener(this);
        findViewById(R.id.tv_ya).setOnClickListener(this);
        findViewById(R.id.tv_ra).setOnClickListener(this);
        findViewById(R.id.tv_la).setOnClickListener(this);
        findViewById(R.id.tv_va).setOnClickListener(this);
        findViewById(R.id.tv_sha).setOnClickListener(this);
        findViewById(R.id.tv_skha).setOnClickListener(this);
        findViewById(R.id.tv_sa).setOnClickListener(this);
        findViewById(R.id.tv_ha).setOnClickListener(this);

        findViewById(R.id.tv_chota_a).setOnClickListener(this);
        findViewById(R.id.tv_bada_a).setOnClickListener(this);
        findViewById(R.id.tv_choti_e).setOnClickListener(this);
        findViewById(R.id.tv_badi_e).setOnClickListener(this);
        findViewById(R.id.tv_chota_u).setOnClickListener(this);
        findViewById(R.id.tv_bada_u).setOnClickListener(this);
        findViewById(R.id.tv_rishi).setOnClickListener(this);
        findViewById(R.id.tv_lira).setOnClickListener(this);
        findViewById(R.id.tv_chot_ae).setOnClickListener(this);
        findViewById(R.id.tv_bada_ae).setOnClickListener(this);
        findViewById(R.id.tv_chota_o).setOnClickListener(this);
        findViewById(R.id.tv_bada_o).setOnClickListener(this);
        findViewById(R.id.rl_uttar_dekho_btn).setOnClickListener(this);
        findViewById(R.id.iv_question_mark_btn).setOnClickListener(this);
        findViewById(R.id.iv_trophy_btn).setOnClickListener(this);
        findViewById(R.id.iv_statistics_btn).setOnClickListener(this);
        findViewById(R.id.iv_settings_btn).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_ka:
            case R.id.tv_kha:
            case R.id.tv_ga:
            case R.id.tv_gha:
            case R.id.tv_anga:
            case R.id.tv_cha:
            case R.id.tv_chah:
            case R.id.tv_ja:
            case R.id.tv_jha:
            case R.id.tv_ea:
            case R.id.tv_ta:
            case R.id.tdha:
            case R.id.tv_da:
            case R.id.tv_dha:
            case R.id.tv_ada:
            case R.id.tv_tea:
            case R.id.tv_tha:
            case R.id.tv_dea:
            case R.id.tv_dhea:
            case R.id.tv_na:
            case R.id.tv_pa:
            case R.id.tv_fa:
            case R.id.tv_ba:
            case R.id.tv_bha:
            case R.id.tv_ma:
            case R.id.tv_ya:
            case R.id.tv_ra:
            case R.id.tv_la:
            case R.id.tv_va:
            case R.id.tv_sha:
            case R.id.tv_skha:
            case R.id.tv_sa:
            case R.id.tv_ha:
            case R.id.tv_chota_a:
            case R.id.tv_bada_a:
            case R.id.tv_choti_e:
            case R.id.tv_badi_e:
            case R.id.tv_chota_u:
            case R.id.tv_bada_u:
            case R.id.tv_rishi:
            case R.id.tv_lira:
            case R.id.tv_chot_ae:
            case R.id.tv_bada_ae:
            case R.id.tv_chota_o:
            case R.id.tv_bada_o:
                if (index < currentAttempt * 3) {
                    btnIdList.add(view.getId());
                }
                setText(((TextView) findViewById(view.getId())).getText().toString());
                break;

            case R.id.tv_cross:
                removeText();
                break;

            case R.id.tv_enter:
                submitText();
                break;

            case R.id.rl_uttar_dekho_btn:
                if(!TextUtils.isEmpty(correctWord)){
                    Intent intent = new Intent(this, ShabdamActivity.class);
                    intent.putExtra("word", correctWord);
                    startActivity(intent);
                }
                break;

            case R.id.iv_question_mark_btn:
                kaiseKhelePopup();
                break;

            case R.id.iv_trophy_btn:
                startActivity(new Intent(this, LeaderBoardActivity.class));
                break;

            case R.id.iv_statistics_btn:
                statisticsPopup();
                break;

            case R.id.iv_settings_btn:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

        }
    }

    private void statisticsPopup() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GameActivity.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.statistics_popup_layout, viewGroup, false);
        ImageView cancel_btn=dialogView.findViewById(R.id.iv_cancel_btn);
        tv_played=dialogView.findViewById(R.id.tv_played);
        tv_win=dialogView.findViewById(R.id.tv_win);
        tv_current_streak=dialogView.findViewById(R.id.tv_current_streak);
        tv_max_streak=dialogView.findViewById(R.id.tv_max_streak);
        builder.setView(dialogView);
        final android.app.AlertDialog alertDialog = builder.create();
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
        gamePresenter=new GamePresenter(this);
        gamePresenter.fetchStatisticsData();
    }

    private void kaiseKhelePopup() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GameActivity.this,R.style.CustomAlertDialog);
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

    /**
     * check which cell to set text
     * If already have text accept MATRA_TEXT then remove text
     * and add new text
     *
     * @param s
     */
    private void setText(String s) {
        if (index < currentAttempt * 3) {
            index = index + 1;
            if (getId(index) != 0) {
                updateWordCharArray(s);
                ((TextView) findViewById(getId(index))).setText(new StringBuilder().append(s).append(getTextIndex(index)));
            }
        }

    }

    private void updateWordCharArray(String s) {
        entered_word_array[index % MAX_CHAR_LENGTH == 0 ? MAX_CHAR_LENGTH - 1 : (index % MAX_CHAR_LENGTH) - 1] = s.toCharArray()[0];
    }

    private String getTextIndex(int index) {
        return ((TextView) findViewById(getId(index))).getText().toString();
    }

    private void removeText() {
        if (index > (currentAttempt - 1) * 3) {
            ((TextView) findViewById(getId(index))).setText("");
            if (btnIdList != null && btnIdList.size() > 0) {
                btnIdList.remove(btnIdList.size() - 1);
            }
            updateWordCharArray("x");
            ((TextView) findViewById(getId(index))).setText(matra[index % MAX_CHAR_LENGTH == 0 ? MAX_CHAR_LENGTH - 1 : (index % MAX_CHAR_LENGTH) - 1]);

            index = index - 1;
        }
    }

    private void submitText() {
        // animate();
        if (index == 0 || index % MAX_CHAR_LENGTH != 0) {
            ToastUtils.show(GameActivity.this, "Text is too short");
            return;
        }

        if (index % MAX_CHAR_LENGTH == 0) {
            //verifyText--> call API --> increment count
            verifyText();
            //Increment current Attempt Count at Last
            if (currentAttempt < MAX_ATTEMPT) {
                currentAttempt = currentAttempt + 1;
                updateCurrentAttempt();
            }
            btnIdList.clear();
        }
    }

    /**
     * hit API to check word in dictionary
     */
    private void verifyText() {
        mapWord();
    }

    private void updateCurrentAttempt() {
        //Update Matra
        for (int i = 1; i < MAX_CHAR_LENGTH + 1; i++) {
            ((TextView) findViewById(getId((currentAttempt - 1) * 3 + i))).setText(matra[i - 1].toString());
        }

    }

    private int getId(int pos) {
        switch (pos) {
            case 1:
                return R.id.et_1;

            case 2:
                return R.id.et_2;

            case 3:
                return R.id.et_3;

            case 4:
                return R.id.et_4;

            case 5:
                return R.id.et_5;

            case 6:
                return R.id.et_6;

            case 7:
                return R.id.et_7;

            case 8:
                return R.id.et_8;

            case 9:
                return R.id.et_9;

            case 10:
                return R.id.et_10;

            case 11:
                return R.id.et_11;

            case 12:
                return R.id.et_12;

            case 13:
                return R.id.et_13;

            case 14:
                return R.id.et_14;

            case 15:
                return R.id.et_15;


        }

        return 0;
    }

    private void showMatraText() {
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
    }

    private boolean checkLetter(char c) {
        if (((int) c >= 2309 && (int) c <= 2316) || ((int) c >= 2325 && (int) c <= 2361)
                || (int) c == 2319 || (int) c == 2320 || (int) c == 2323 || (int) c == 2324) {
            return true;
        }
        return false;
    }

    /**
     * if word array same and entered_word_array
     * Correct Word
     * hit api, stop timer, disable keyboard, color all the answer boxes with green
     * Else check word in dictionary
     * yes then check is letter are in word_array
     * yes check if it is right place
     * yes color answer box of that letter with green
     * color keyboard box of that letter with green
     * no color answer box of that letter with yellow
     * color keyboard box of that letter with yellow
     * no color answer box with yellow
     * color keyborad box of that letter with yellow
     * no re-attempt or color it with grey
     *
     * @return
     */
    private boolean mapWord() {
        if (Arrays.equals(word_array, entered_word_array)) {
            updateGreenBoxes();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(GameActivity.this, LeaderBoardActivity.class);
                    startActivity(intent);
                }
            }, 1000);
            return true;
        }// dictionary check is pending
        else {//check is letter are in word_array
            for (int i = 0; i < entered_word_array.length; i++) {
                boolean isExist = false;
                for (int j = 0; j < word_array.length; j++) {
                    if (entered_word_array[i] == word_array[j]) {
                        isExist = true;
                        if (i == j) {//same postion green
                            ((TextView) findViewById(getId((currentAttempt - 1) * 3 + 1 + i))).setBackgroundResource(R.drawable.bg_green_box);
                            ((TextView) findViewById(btnIdList.get(i))).setBackgroundResource(R.drawable.bg_green_box);
                        } else {// yellow
                            ((TextView) findViewById(getId((currentAttempt - 1) * 3 + 1 + i))).setBackgroundResource(R.drawable.bg_yellow);
                            ((TextView) findViewById(btnIdList.get(i))).setBackgroundResource(R.drawable.bg_yellow);

                        }
                    }
                }
                if (!isExist) {//Not in word
                    ((TextView) findViewById(getId((currentAttempt - 1) * 3 + 1 + i))).setBackgroundResource(R.drawable.bg_grey);
                    ((TextView) findViewById(btnIdList.get(i))).setBackgroundResource(R.drawable.bg_grey);
                }
            }

        }
        return false;
    }

    private void updateGreenBoxes() {
        for (int i = (currentAttempt - 1) * 3 + 1; i < currentAttempt * 3 + 1; i++) {
            ((TextView) findViewById(getId(i))).setBackgroundResource(R.drawable.bg_green_box);
        }

        //update Key Board
        for (int j = 0; j < btnIdList.size(); j++) {
            ((TextView) findViewById(btnIdList.get(j))).setBackgroundResource(R.drawable.bg_green_box);
        }
    }

    Animator.AnimatorListener animatorListener;
    private void animate(){
        try{


            //visibleView.visible()
            AnimatorSet flipOutAnimatorSet =
                    (AnimatorSet) AnimatorInflater.loadAnimator(
                            GameActivity.this,
                            R.animator.flip_out
                    );

            AnimatorSet flipInAnimatorSet =
                    (AnimatorSet) AnimatorInflater.loadAnimator(
                            GameActivity.this,
                            R.animator.flip_in
                    );

            //visibleView.visible()
            AnimatorSet flipOutAnimatorSet2 =
                    (AnimatorSet) AnimatorInflater.loadAnimator(
                            GameActivity.this,
                            R.animator.flip_out
                    );

            AnimatorSet flipInAnimatorSet2 =
                    (AnimatorSet) AnimatorInflater.loadAnimator(
                            GameActivity.this,
                            R.animator.flip_in
                    );

            //visibleView.visible()
            AnimatorSet flipOutAnimatorSet3 =
                    (AnimatorSet) AnimatorInflater.loadAnimator(
                            GameActivity.this,
                            R.animator.flip_out
                    );

            AnimatorSet flipInAnimatorSet3 =
                    (AnimatorSet) AnimatorInflater.loadAnimator(
                            GameActivity.this,
                            R.animator.flip_in
                    );

            flipOutAnimatorSet.setTarget(findViewById(R.id.et_1));
            flipOutAnimatorSet2.setTarget(findViewById(R.id.et_2));
            flipOutAnimatorSet3.setTarget(findViewById(R.id.et_3));

            flipInAnimatorSet.setTarget(findViewById(R.id.et_1));
            flipInAnimatorSet2.setTarget(findViewById(R.id.et_2));
            flipInAnimatorSet3.setTarget(findViewById(R.id.et_3));

            animatorListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(animator == flipOutAnimatorSet){
                        flipInAnimatorSet.start();
                        flipOutAnimatorSet2.start();
                    }else if( animator == flipOutAnimatorSet2){
                        flipInAnimatorSet2.start();
                        flipOutAnimatorSet3.start();
                    }else {
                        flipInAnimatorSet3.start();
                    }


                    ((TextView)findViewById(R.id.et_1)).setBackgroundResource(R.drawable.bg_green_box);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            };


            flipOutAnimatorSet.start();


            flipOutAnimatorSet.addListener(animatorListener);
            flipOutAnimatorSet2.addListener(animatorListener);
            flipOutAnimatorSet3.addListener(animatorListener);




        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void showProgress() {
        if(!isFinishing()){
            flLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if(!isFinishing()){
            flLoading.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError(String errorMsg) {
        if(!isFinishing()){
            flLoading.setVisibility(View.GONE);
            ToastUtils.show(GameActivity.this, errorMsg);
        }
    }

    @Override
    public void onWordFetched(Datum datumCorrectWord) {
        this.correctWord = datumCorrectWord.getWords();
        showMatraText();
        updateCurrentAttempt();
    }

    @Override
    public void onStatisticsDataFetched(Data data) {
        GameView.super.onStatisticsDataFetched(data);
        tv_played.setText(data.getPlayed());
        tv_current_streak.setText(data.getCurrentStreak());
        tv_max_streak.setText(data.getMaxStreak());

        int win= Integer.parseInt(data.getWin());
        int total_played= Integer.parseInt(data.getPlayed());
        int percent=(win/total_played)*100;
        tv_win.setText(percent);
        Log.d("Percentage",""+percent);
    }
}