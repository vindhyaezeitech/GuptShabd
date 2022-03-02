package com.shabdamsdk;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.model.GetWordRequest;
import com.shabdamsdk.model.adduser.AddUserRequest;
import com.shabdamsdk.model.getwordresp.Datum;
import com.shabdamsdk.model.statistics.Data;
import com.shabdamsdk.pref.CommonPreference;
import com.shabdamsdk.ui.activity.LeaderBoardActivity;
import com.shabdamsdk.ui.activity.SettingsActivity;
import com.shabdamsdk.ui.activity.ShabdamActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Main Activity
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener, GameView {

    public static final int MAX_CHAR_LENGTH = 3;
    public static final int MAX_ATTEMPT = 5;
    private final int MAX_INDEX = 16;
    private final ArrayList<Integer> btnIdList = new ArrayList<>();
    private final int[] keyIdArray = {R.id.tv_ka, R.id.tv_kha, R.id.tv_ga, R.id.tv_gha, R.id.tv_anga,
            R.id.tv_cha, R.id.tv_chah, R.id.tv_ja, R.id.tv_jha, R.id.tv_ea,
            R.id.tv_ta, R.id.tdha, R.id.tv_da, R.id.tv_dha, R.id.tv_ada,
            R.id.tv_tea, R.id.tv_tha, R.id.tv_dea, R.id.tv_dhea, R.id.tv_na,
            R.id.tv_pa, R.id.tv_fa, R.id.tv_ba, R.id.tv_ma, R.id.tv_ya,
            R.id.tv_ra, R.id.tv_la, R.id.tv_va, R.id.tv_sha, R.id.tv_skha,
            R.id.tv_sa, R.id.tv_ha, R.id.tv_chota_a, R.id.tv_bada_a, R.id.tv_choti_e,
            R.id.tv_badi_e, R.id.tv_chota_u, R.id.tv_bada_u, R.id.tv_rishi, R.id.tv_lira,
            R.id.tv_chot_ae, R.id.tv_bada_ae, R.id.tv_chota_o, R.id.tv_bada_o};
    char[] word_array = new char[3];
    char[] entered_word_array = new char[3];
    StringBuilder[] matra = new StringBuilder[3];
    char[] charArray;
    Animation animBlink;
    int blinkCount;
    Animator.AnimatorListener animatorListener;
    private int index = 0;
    private int currentAttempt = 1;
    private String correctWord = "संदेश";
    private int hintCount = 0;
    private TextView tvKa;
    private TextView tvCross;
    private TextView tvEnter;
    private RelativeLayout rl_uttar_dekho_btn, continue_btn, agla_shabd_btn;
    private GamePresenter gamePresenter;
    private FrameLayout flLoading;
    private boolean mTimingRunning;
    private String userId, name, u_name, email, profile_image;
    private TextView tv_played, tv_win, tv_current_streak, tv_max_streak,tv_timer_counter_text;
    private Chronometer tv_timer_text;
    private long pauseOffset, minutes, seconds;
    private String minute, second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // load the animation
        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("user_id");
            name = getIntent().getStringExtra("name");
            u_name = getIntent().getStringExtra("uname");
            email = getIntent().getStringExtra("email");
            profile_image = getIntent().getStringExtra("profile_image");

            if (!TextUtils.isEmpty(userId)) {
                CommonPreference.getInstance(GameActivity.this).put(CommonPreference.Key.USER_ID, userId);
            }

            if (!TextUtils.isEmpty(name)) {
                CommonPreference.getInstance(GameActivity.this).put(CommonPreference.Key.NAME, name);
            }
            if (!TextUtils.isEmpty(u_name)) {
                CommonPreference.getInstance(GameActivity.this).put(CommonPreference.Key.UNAME, u_name);
            }
            if (!TextUtils.isEmpty(email)) {
                CommonPreference.getInstance(GameActivity.this).put(CommonPreference.Key.EMAIL, email);
            }
            if (!TextUtils.isEmpty(profile_image)) {
                CommonPreference.getInstance(GameActivity.this).put(CommonPreference.Key.PROFILE_IMAGE, profile_image);
            }
        } else {
            return;
        }

        animBlink = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        initViewClick();


        gamePresenter = new GamePresenter(this);

        if (!TextUtils.isEmpty(userId)) {
            AddUserRequest request = new AddUserRequest();
            request.setUserId(userId);
            request.setName(name);
            request.setUname(u_name);
            request.setEmail(email);
            request.setProfileimage(profile_image);
            gamePresenter.addUser(request);
        }

        if (!CommonPreference.getInstance(GameActivity.this).getBoolean(CommonPreference.Key.IS_FIRST_TIME)) {
            CommonPreference.getInstance(GameActivity.this).put(CommonPreference.Key.IS_FIRST_TIME, true);
            kaiseKhelePopup();
        }
    }

    private void gameTimer() {
        tv_timer_text.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - tv_timer_text.getBase() >= 3600000)) {
                    tv_timer_text.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(GameActivity.this, "Game Finished!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startTimer();
    }

    private void startTimer() {
        if (!mTimingRunning) {
            tv_timer_text.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            tv_timer_text.start();
            mTimingRunning = true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameTimer();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        super.onPause();
        if (mTimingRunning) {
            int time;
            tv_timer_text.stop();
            pauseOffset = SystemClock.elapsedRealtime() - tv_timer_text.getBase();
            mTimingRunning = false;
            time = Math.toIntExact(pauseOffset);
            minutes = TimeUnit.MILLISECONDS.toMinutes(time);
            seconds = TimeUnit.MILLISECONDS.toSeconds(time);
            minute = String.valueOf(minutes);
            second = String.valueOf(seconds);
            Log.d("game", String.valueOf(minute));
            Log.d("game", String.valueOf(second));
        }
    }

    private void initViewClick() {
        tvKa = findViewById(R.id.tv_ka);
        tvCross = findViewById(R.id.tv_cross);
        tvEnter = findViewById(R.id.tv_enter);
        flLoading = findViewById(R.id.fl_loading);
        agla_shabd_btn = findViewById(R.id.rl_agla_shabd_btn);
        tv_timer_text = findViewById(R.id.tv_timer_text);

        tvKa.setOnClickListener(this);
        tvCross.setOnClickListener(this);
        tvEnter.setOnClickListener(this);
        findViewById(R.id.rl_hint).setOnClickListener(this);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_ka || id == R.id.tv_kha || id == R.id.tv_ga || id == R.id.tv_gha || id == R.id.tv_anga || id == R.id.tv_cha || id == R.id.tv_chah || id == R.id.tv_ja || id == R.id.tv_jha || id == R.id.tv_ea || id == R.id.tv_ta || id == R.id.tdha || id == R.id.tv_da || id == R.id.tv_dha || id == R.id.tv_ada || id == R.id.tv_tea || id == R.id.tv_tha || id == R.id.tv_dea || id == R.id.tv_dhea || id == R.id.tv_na || id == R.id.tv_pa || id == R.id.tv_fa || id == R.id.tv_ba || id == R.id.tv_bha || id == R.id.tv_ma || id == R.id.tv_ya || id == R.id.tv_ra || id == R.id.tv_la || id == R.id.tv_va || id == R.id.tv_sha || id == R.id.tv_skha || id == R.id.tv_sa || id == R.id.tv_ha || id == R.id.tv_chota_a || id == R.id.tv_bada_a || id == R.id.tv_choti_e || id == R.id.tv_badi_e || id == R.id.tv_chota_u || id == R.id.tv_bada_u || id == R.id.tv_rishi || id == R.id.tv_lira || id == R.id.tv_chot_ae || id == R.id.tv_bada_ae || id == R.id.tv_chota_o || id == R.id.tv_bada_o) {
            if (index < currentAttempt * 3) {
                btnIdList.add(view.getId());
            }
            setText(((TextView) findViewById(view.getId())).getText().toString());
        } else if (id == R.id.tv_cross) {
            removeText();
        } else if (id == R.id.tv_enter) {
            submitText();
        } else if (id == R.id.rl_uttar_dekho_btn) {
            if (mTimingRunning) {
                int time;
                tv_timer_text.stop();
                pauseOffset = SystemClock.elapsedRealtime() - tv_timer_text.getBase();
                mTimingRunning = false;
                time = Math.toIntExact(pauseOffset);
                minutes = TimeUnit.MILLISECONDS.toMinutes(time);
                seconds = TimeUnit.MILLISECONDS.toSeconds(time);
                minute = String.valueOf(minutes);
                second = String.valueOf(seconds);
                Log.d("game", String.valueOf(minute));
                Log.d("game", String.valueOf(second));
            }
            if (!TextUtils.isEmpty(correctWord)) {
                Intent intent = new Intent(this, ShabdamActivity.class);
                intent.putExtra("word", correctWord);
                intent.putExtra("minute", minute);
                intent.putExtra("second", second);
                startActivity(intent);
            }
        } else if (id == R.id.iv_question_mark_btn) {
            kaiseKhelePopup();
        } else if (id == R.id.iv_trophy_btn) {
            startActivity(new Intent(this, LeaderBoardActivity.class));
        } else if (id == R.id.iv_statistics_btn) {
            statisticsPopup();
        } else if (id == R.id.iv_settings_btn) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.rl_hint) {
            showHint();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void statisticsPopup() {
        if (mTimingRunning) {
            int time;
            tv_timer_text.stop();
            pauseOffset = SystemClock.elapsedRealtime() - tv_timer_text.getBase();
            mTimingRunning = false;
            time = Math.toIntExact(pauseOffset);
            minutes = TimeUnit.MILLISECONDS.toMinutes(time);
            seconds = TimeUnit.MILLISECONDS.toSeconds(time);
            minute = String.valueOf(minutes);
            second = String.valueOf(seconds);
            Log.d("game", String.valueOf(minute));
            Log.d("game", String.valueOf(second));
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this, R.style.CustomAlertDialog);
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
                tv_timer_text.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                tv_timer_text.start();
                mTimingRunning = true;
                alertDialog.dismiss();
            }
        });

        agla_shabd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(userId)) {
                    AddUserRequest request = new AddUserRequest();
                    request.setUserId(userId);
                    request.setName(name);
                    request.setUname(u_name);
                    request.setEmail(email);
                    request.setProfileimage(profile_image);
                    gamePresenter.addUser(request);
                }
                alertDialog.dismiss();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this, R.style.CustomAlertDialog);
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
            if (gamePresenter != null) {
                GetWordRequest getWordRequest = new GetWordRequest();
                getWordRequest.setUserId("1");
                getWordRequest.setWordId(Arrays.asList("2", "3"));
                gamePresenter.fetchNewWord(getWordRequest);
            }
            e.printStackTrace();
        }
    }

    private boolean checkLetter(char c) {
        return ((int) c >= 2309 && (int) c <= 2316) || ((int) c >= 2325 && (int) c <= 2361)
                || (int) c == 2319 || (int) c == 2320 || (int) c == 2323 || (int) c == 2324;
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
                            findViewById(getId((currentAttempt - 1) * 3 + 1 + i)).setBackgroundResource(R.drawable.bg_green_box);
                            findViewById(btnIdList.get(i)).setBackgroundResource(R.drawable.bg_green_box);
                        } else {// yellow
                            findViewById(getId((currentAttempt - 1) * 3 + 1 + i)).setBackgroundResource(R.drawable.bg_yellow);
                            findViewById(btnIdList.get(i)).setBackgroundResource(R.drawable.bg_yellow);

                        }
                    }
                }
                if (!isExist) {//Not in word
                    findViewById(getId((currentAttempt - 1) * 3 + 1 + i)).setBackgroundResource(R.drawable.bg_grey);
                    findViewById(btnIdList.get(i)).setBackgroundResource(R.drawable.bg_grey);
                }
            }

        }
        return false;
    }

    private void updateGreenBoxes() {
        for (int i = (currentAttempt - 1) * 3 + 1; i < currentAttempt * 3 + 1; i++) {
            findViewById(getId(i)).setBackgroundResource(R.drawable.bg_green_box);
        }

        //update Key Board
        for (int j = 0; j < btnIdList.size(); j++) {
            findViewById(btnIdList.get(j)).setBackgroundResource(R.drawable.bg_green_box);
        }
    }

    private void animate() {
        try {


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
                    if (animator == flipOutAnimatorSet) {
                        flipInAnimatorSet.start();
                        flipOutAnimatorSet2.start();
                    } else if (animator == flipOutAnimatorSet2) {
                        flipInAnimatorSet2.start();
                        flipOutAnimatorSet3.start();
                    } else {
                        flipInAnimatorSet3.start();
                    }


                    findViewById(R.id.et_1).setBackgroundResource(R.drawable.bg_green_box);
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showProgress() {
        if (!isFinishing()) {
            flLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (!isFinishing()) {
            flLoading.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError(String errorMsg) {
        if (!isFinishing()) {
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
    public void onAddUser(com.shabdamsdk.model.adduser.Data data) {
        if (data != null) {
            if (data.getId() != 0) {
                CommonPreference.getInstance(this).put(CommonPreference.Key.GAME_USER_ID, String.valueOf(data.getId()));
                GetWordRequest getWordRequest = new GetWordRequest();
                getWordRequest.setUserId(String.valueOf(data.getId()));
                getWordRequest.setWordId(Arrays.asList("2", "3"));
                gamePresenter.fetchNewWord(getWordRequest);
            }
        }

    }


    void showHint() {
        if (hintCount < 2) {
            btnIdList.clear();
            hintCount++;
            int pos = ((currentAttempt - 1) * 3) + hintCount;

            if (hintCount == 2) {
                btnIdList.add(getKeyId(String.valueOf(word_array[0])));
                ((TextView) findViewById(getId(pos - 1))).setText(new StringBuilder().append(word_array[0]).append(matra[0]));
                entered_word_array[0] = word_array[0];

            }

            ((TextView) findViewById(getId(pos))).setText(new StringBuilder().append(word_array[hintCount - 1]).append(matra[hintCount - 1]));
            updateWordCharArray(String.valueOf(word_array[hintCount - 1]));
            int id = getKeyId(String.valueOf(word_array[hintCount - 1]));
            if (id != -1) {
                btnIdList.add(id);
                findViewById(id).setBackgroundResource(R.drawable.bg_green_box);
            }
            index = pos;


            clearNextBoxesAfterHint();
        }
    }

    private void clearNextBoxesAfterHint() {
        for (int i = 0; i < 3 - hintCount; i++) {
            ((TextView) findViewById(getId(index + i + 1))).setText("");
        }
    }


    public int getKeyId(String str) {
        int id = -1;

        for (int i = 0; i < keyIdArray.length; i++) {
            if (((TextView) findViewById(keyIdArray[i])).getText().toString().equalsIgnoreCase(str)) {
                id = keyIdArray[i];
            }
        }
        return id;
    }
}