package com.shabdamsdk;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.shabdamsdk.db.Task;
import com.shabdamsdk.model.GetWordRequest;
import com.shabdamsdk.model.SignupRequest;
import com.shabdamsdk.model.dictionary.CheckWordDicRequest;
import com.shabdamsdk.model.gamesubmit.SubmitGameRequest;
import com.shabdamsdk.model.getwordresp.Datum;
import com.shabdamsdk.model.statistics.Data;
import com.shabdamsdk.pref.CommonPreference;
import com.shabdamsdk.ui.activity.ShabdamLeaderBoardActivity;
import com.shabdamsdk.ui.activity.ShabdamSettingsActivity;
import com.shabdamsdk.ui.activity.ShabdamActivity;
import com.shabdamsdk.ui.activity.UserDetailActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Main Activity
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener, GameView {

    public static final int MAX_CHAR_LENGTH = 3;
    public static final int MAX_ATTEMPT = 5;
    private final int MAX_INDEX = 16;
    private final int UPDATE_REQUEST_CODE = 1612;
    char[] word_array = new char[3];
    char[] entered_word_array = new char[3];
    StringBuilder[] matra = new StringBuilder[3];
    char[] charArray;
    Animation animBlink;
    int blinkCount;
    Animator.AnimatorListener animatorListener;
    List<String> list = new ArrayList<>();
    private ArrayList<Integer> btnIdList = new ArrayList<>();
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    private String gameResult;

    private CLICK_ITEM click_item;
    private int[] keyIdArray = {R.id.tv_ka, R.id.tv_kha, R.id.tv_ga, R.id.tv_gha, R.id.tv_anga,
            R.id.tv_cha, R.id.tv_chah, R.id.tv_ja, R.id.tv_jha, R.id.tv_ea,
            R.id.tv_ta, R.id.tdha, R.id.tv_da, R.id.tv_dha, R.id.tv_ada,
            R.id.tv_tea, R.id.tv_tha, R.id.tv_dea, R.id.tv_dhea, R.id.tv_na,
            R.id.tv_pa, R.id.tv_fa, R.id.tv_ba, R.id.tv_ma, R.id.tv_ya,
            R.id.tv_ra, R.id.tv_la, R.id.tv_va, R.id.tv_sha, R.id.tv_skha,
            R.id.tv_sa, R.id.tv_ha, R.id.tv_chota_a, R.id.tv_bada_a, R.id.tv_choti_e,
            R.id.tv_badi_e, R.id.tv_chota_u, R.id.tv_bada_u, R.id.tv_rishi, R.id.tv_lira,
            R.id.tv_chot_ae, R.id.tv_bada_ae, R.id.tv_chota_o, R.id.tv_bada_o};
    private int index = 0;
    private int currentAttempt = 1;
    private String correctWord = "संदेश";
    private int hintCount = 0;
    private TextView tvKa;
    private TextView tvCross;
    private TextView tvEnter;
    private RelativeLayout rl_uttar_dekho_btn, continue_btn, agla_shabd_btn, rl_share_btn;
    private GamePresenter gamePresenter;
    private FrameLayout flLoading;
    private boolean mTimingRunning;
    private TextView tv_played, tv_win, tv_current_streak, tv_max_streak, tv_timer_counter_text;
    private Chronometer tv_timer_text;
    private long pauseOffset, minutes, seconds;
    private String minute, second;
    private Datum datumCorrectWord;
    private InterstitialAd mInterstitialAd;
    private Animation shakeAnimation;
    private boolean isUttarDekheClicked;
    private RewardedAd mRewardedAd;
    private Handler handler = new Handler();
    private boolean isHintPressed;
    private LinearLayout ll_google_sign_in;
    private TextView tv_google_sign_in;
    private AlertDialog alertDialog;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        adRequest = new AdRequest.Builder().build();


        interstitialAdd();
        initRewardAdd();
        callInAppupdate();
        googleSignIn();

        if (!TextUtils.isEmpty(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.NAME))) {
            ((TextView) findViewById(R.id.tv_uname)).setText(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.NAME));
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        animBlink = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        initViewClick();

        gamePresenter = new GamePresenter(this, GameActivity.this);

       /* if (!TextUtils.isEmpty(userId)) {
            AddUserRequest request = new AddUserRequest();
            request.setUserId(userId);
            request.setName(name);
            request.setUname(u_name);
            request.setEmail(email);
            request.setProfileimage(profile_image);
            gamePresenter.addUser(request);
        }*/

        if (gamePresenter != null) {
            GetWordRequest getWordRequest = new GetWordRequest();
            getWordRequest.setUserId(CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
            gamePresenter.fetchNewWord(GameActivity.this, getWordRequest);
        }
      /*  if (!CommonPreference.getInstance(GameActivity.this).getBoolean(CommonPreference.Key.IS_FIRST_TIME)) {
            CommonPreference.getInstance(GameActivity.this).put(CommonPreference.Key.IS_FIRST_TIME, true);
            kaiseKhelePopup();
        }*/
    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void callInAppupdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, UPDATE_REQUEST_CODE);

                } catch (IntentSender.SendIntentException exception) {
                    Log.d("Error_Message", "" + exception.getMessage());
                }
            }
        });
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            com.google.android.gms.tasks.Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }*/

    private void handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                // CommonPreference.getInstance(UserDetailActivity.this).put(CommonPreference.Key.EMAIL, personEmail);

                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail(personEmail);
                signupRequest.setProfileimage(String.valueOf(personPhoto));
                signupRequest.setName(personName);
                signupRequest.setUname(personName);
                signupRequest.setUserId("");

                Utils.saveUserData(GameActivity.this,personName, personName, personEmail, String.valueOf(personPhoto));

                if (gamePresenter != null) {
                    gamePresenter.signUpUser(signupRequest);
                }

                //Toast.makeText(this, ""+personEmail, Toast.LENGTH_SHORT).show();
            }
            //startActivity(new Intent(this, ShabdamSplashActivity.class));

        } catch (ApiException e) {
            Log.d("Message", e.toString());
        }
    }

    @Override
    public void onAddUser(com.shabdamsdk.model.adduser.Data data) {
        if (data != null) {
            if (data.getId() != 0) {
                CommonPreference.getInstance(this.getApplicationContext()).put(CommonPreference.Key.GAME_USER_ID, String.valueOf(data.getId()));
                ll_google_sign_in.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.NAME))) {
                    ((TextView) findViewById(R.id.tv_uname)).setText(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.NAME));
                }
                callgetStreakAPI();
            }
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
                    if (click_item == CLICK_ITEM.HINT) {
                        showHint();
                    } else if (click_item == CLICK_ITEM.AGLA_SHABD) {
                        openAglaShabd();
                    } else if (click_item == CLICK_ITEM.UTTAR_DEKHO) {
                        openUttarDekho();
                    }
                    interstitialAdd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (click_item == CLICK_ITEM.HINT) {
                        showHint();
                    } else if (click_item == CLICK_ITEM.AGLA_SHABD) {
                        openAglaShabd();
                    } else if (click_item == CLICK_ITEM.UTTAR_DEKHO) {
                        openUttarDekho();
                    }
                    interstitialAdd();
                }
            });
        } else {
            if (click_item == CLICK_ITEM.HINT) {
                showHint();
            } else if (click_item == CLICK_ITEM.AGLA_SHABD) {
                openAglaShabd();
            } else if (click_item == CLICK_ITEM.UTTAR_DEKHO) {
                openUttarDekho();
            }
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
           if(rl_uttar_dekho_btn != null){
               rl_uttar_dekho_btn.setEnabled(true);
           }

        }
    }

    private void openUttarDekho() {
        if (mTimingRunning) {
            tv_timer_text.stop();
            pauseOffset = SystemClock.elapsedRealtime() - tv_timer_text.getBase();
            mTimingRunning = false;
            minutes = TimeUnit.MILLISECONDS.toMinutes(pauseOffset);
            seconds = TimeUnit.MILLISECONDS.toSeconds(pauseOffset) % 60;
            minute = String.format("%02d", minutes);
            second = String.format("%02d", seconds);
        }

        Intent intent = new Intent(this, ShabdamActivity.class);
        intent.putExtra("word", correctWord);
        intent.putExtra("minute", minute);
        intent.putExtra("second", second);
        intent.putExtra("currentAttempt", String.valueOf(currentAttempt));
        startActivity(intent);
        finish();
    }

    private void openAglaShabd() {
        Intent intent = new Intent(GameActivity.this, GameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("user_id", CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
        intent.putExtra("name", CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.NAME));
        intent.putExtra("uname", CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.UNAME));
        intent.putExtra("email", CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.EMAIL));
        intent.putExtra("profile_image", CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.PROFILE_IMAGE));
        startActivity(intent);
        finish();
    }

    private void initRewardAdd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, Constants.REWARD_AD_ID,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        // Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                        initRewardAdd();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        // Log.d(TAG, "Ad was loaded.");
                    }
                });
    }

    private void showRewardAdd() {
        if (mRewardedAd != null) {
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    //  Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    showHint();
                    initRewardAdd();
                }
            });
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    // Log.d(TAG, "Ad was shown.");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    // Log.d(TAG, "Ad failed to show.");
                    showHint();
                    initRewardAdd();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    // Log.d(TAG, "Ad was dismissed.");
                    mRewardedAd = null;
                    initRewardAdd();
                }
            });

        } else {
            // Log.d(TAG, "The rewarded ad wasn't ready yet.");
            showHint();
            initRewardAdd();
        }

    }



    private void gameTimer() {
        tv_timer_text.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - tv_timer_text.getBase() >= 3600000)) {
                    tv_timer_text.setBase(SystemClock.elapsedRealtime());

                    endGame(Constants.LOSS,String.valueOf(3600000 / 1000),currentAttempt);

                    Toast.makeText(GameActivity.this, "Game Finished!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startTimer();
    }

    private void endGame(String gameStatus, String time, int attempt) {
        gameResult = gameStatus;
        if(!TextUtils.isEmpty(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID))){
            SubmitGameRequest submitGameRequest = new SubmitGameRequest();
            submitGameRequest.setGameUserId(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
            submitGameRequest.setGameStatus(gameStatus);
            submitGameRequest.setNoOfAttempt(attempt);
            submitGameRequest.setTime(time);
            gamePresenter.submitGame(submitGameRequest);
        }else {
            GameDataManager.getInstance().removeData();
            Intent intent = new Intent(GameActivity.this, ShabdamLeaderBoardActivity.class);
            intent.putExtra(Constants.NUMBER_OF_ATTEMPT, attempt);
            intent.putExtra(Constants.TIME, time);
            intent.putExtra(Constants.GAME_STATUS, gameStatus);
            intent.putExtra("type", "2");
            startActivity(intent);
            finish();
        }
        //
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
      if(click_item != CLICK_ITEM.HINT){
          gameTimer();
      }else {
          click_item = null;
      }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(click_item != CLICK_ITEM.HINT){
            if (mTimingRunning) {
                tv_timer_text.stop();
                pauseOffset = SystemClock.elapsedRealtime() - tv_timer_text.getBase();
                mTimingRunning = false;
                minutes = TimeUnit.MILLISECONDS.toMinutes(pauseOffset);
                seconds = TimeUnit.MILLISECONDS.toSeconds(pauseOffset) % 60;
                minute = String.format("%02d", minutes);
                second = String.format("%02d", seconds);
            }
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
            if (index != currentAttempt * MAX_CHAR_LENGTH) {
                ToastUtils.show(GameActivity.this, "Text is too short");
                return;
            }
            if (gamePresenter != null) {
                CheckWordDicRequest request = new CheckWordDicRequest();
                request.setWord(getEnteredText());
                gamePresenter.checkDictionary(request);
            }
            //submitText();
        } else if (id == R.id.rl_uttar_dekho_btn) {
            click_item = CLICK_ITEM.UTTAR_DEKHO;
            if(rl_uttar_dekho_btn != null){
                rl_uttar_dekho_btn.setEnabled(false);
            }
            if (!TextUtils.isEmpty(correctWord)) {
              //  endGame(Constants.LOSS,String.valueOf(pauseOffset / 1000),currentAttempt);
                if(TextUtils.isEmpty(CommonPreference.getInstance(GameActivity.this).getString(CommonPreference.Key.GAME_USER_ID))){
                    gameResult = Constants.LOSS;
                    GameDataManager.getInstance().removeData();
                    statisticsPopup(null);
                }else {
                    isUttarDekheClicked = true;
                    endGame(Constants.LOSS,String.valueOf(pauseOffset / 1000),currentAttempt);
                }



            }
        } else if (id == R.id.iv_question_mark_btn) {
            kaiseKhelePopup();
        } else if (id == R.id.iv_trophy_btn) {
            startActivity(new Intent(this, ShabdamLeaderBoardActivity.class));
            //finish();
            //onBackPressed();
        } else if (id == R.id.iv_statistics_btn) {
            if(!TextUtils.isEmpty(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID))){
                callgetStreakAPI();
            }else {
                statisticsPopup(null);
            }
        } else if (id == R.id.iv_settings_btn) {
         //   startActivityForResult(new Intent(this, ShabdamSettingsActivity.class), 200);
        Intent intent = new Intent(this, ShabdamSettingsActivity.class);
        someActivityResultLauncher.launch(intent);
        } else if (id == R.id.rl_hint) {
            // showHint();
            if (hintCount < 2) {
                // loadAdd();
                // isHintPressed = true;
                click_item = CLICK_ITEM.HINT;
                 showRewardAdd();
                //loadAdd();
            }
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        openLogin();
                    }
                }
            });

    private void openLogin(){
        Intent intent = new Intent(GameActivity.this, UserDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(new ComponentName(GameActivity.this, UserDetailActivity.class));
        startActivity(intent);
        Toast.makeText(GameActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getEnteredText() {
        String str = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < entered_word_array.length; i++) {
            builder.append(entered_word_array[i]).append(matra[i]).toString();
        }

        return builder.toString();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //startActivityForResult(signInIntent, RC_SIGN_IN);
        someActivityResultLauncherLogin.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncherLogin = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        com.google.android.gms.tasks.Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }
                }
            });



    private void statisticsPopup(Data data) {
        if(alertDialog != null){
            alertDialog.dismiss();
        }
        if (mTimingRunning) {
            tv_timer_text.stop();
            pauseOffset = SystemClock.elapsedRealtime() - tv_timer_text.getBase();
            mTimingRunning = false;
            minutes = TimeUnit.MILLISECONDS.toMinutes(pauseOffset);
            seconds = TimeUnit.MILLISECONDS.toSeconds(pauseOffset) % 60;
            minute = String.format("%02d", minutes);
            second = String.format("%02d", seconds);
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
        rl_share_btn = dialogView.findViewById(R.id.rl_share_btn);
        ll_google_sign_in = dialogView.findViewById(R.id.ll_google_sign_in);
        ll_google_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_google_sign_in = dialogView.findViewById(R.id.tv_google_sign_in);


        if(TextUtils.isEmpty(CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID))){
            ll_google_sign_in.setVisibility(View.VISIBLE);
            tv_google_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (GameActivity.this != null) {
                        if (ToastUtils.checkInternetConnection(GameActivity.this)) {
                            signIn();

                        } else {
                            Toast.makeText(GameActivity.this, getString(com.shabdamsdk.R.string.ensure_internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }


        builder.setView(dialogView);
        alertDialog = builder.create();

        String email = CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID);
        if (!TextUtils.isEmpty(email)) {
            ll_google_sign_in.setVisibility(View.GONE);
        } else {
            ll_google_sign_in.setVisibility(View.VISIBLE);
        }

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_timer_text.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                tv_timer_text.start();
                mTimingRunning = true;
                alertDialog.dismiss();
            }
        });
        rl_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot(dialogView);
            }
        });
        agla_shabd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_item = CLICK_ITEM.AGLA_SHABD;
                loadAdd();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        if(data != null){
            tv_played.setText(data.getPlayed());
            tv_current_streak.setText(data.getCurrentStreak());
            tv_max_streak.setText(data.getMaxStreak());

            if (data.getWin().equals("0") || data.getPlayed().equals("0")) {
                tv_win.setText("0");
            } else {
                float win = Integer.parseInt(data.getWin());
                float total_played = Integer.parseInt(data.getPlayed());
                int percent = (int) ((win / total_played) * 100f);
                tv_win.setText(String.valueOf(percent));
            }

            if (data != null && data.getNoOfAttempts() != null) {

                int progressOne = 0, progressTwo = 0, progressThree = 0, progressFour = 0, progressFive = 0, progressSix = 0;
                try {
                    progressOne = !TextUtils.isEmpty(data.getPlayed()) && !TextUtils.isEmpty(data.getNoOfAttempts().get1()) ? (Integer.parseInt(data.getNoOfAttempts().get1()) * 100) / Integer.parseInt(data.getPlayed()) : 0;
                    progressTwo = !TextUtils.isEmpty(data.getPlayed()) && !TextUtils.isEmpty(data.getNoOfAttempts().get2()) ? (Integer.parseInt(data.getNoOfAttempts().get2()) * 100) / Integer.parseInt(data.getPlayed()) : 0;
                    progressThree = !TextUtils.isEmpty(data.getPlayed()) && !TextUtils.isEmpty(data.getNoOfAttempts().get3()) ? (Integer.parseInt(data.getNoOfAttempts().get3()) * 100) / Integer.parseInt(data.getPlayed()) : 0;
                    progressFour = !TextUtils.isEmpty(data.getPlayed()) && !TextUtils.isEmpty(data.getNoOfAttempts().get4()) ? (Integer.parseInt(data.getNoOfAttempts().get4()) * 100) / Integer.parseInt(data.getPlayed()) : 0;
                    progressFive = !TextUtils.isEmpty(data.getPlayed()) && !TextUtils.isEmpty(data.getNoOfAttempts().get5()) ? (Integer.parseInt(data.getNoOfAttempts().get5()) * 100) / Integer.parseInt(data.getPlayed()) : 0;
                    progressSix = !TextUtils.isEmpty(data.getPlayed()) && !TextUtils.isEmpty(data.getNoOfAttempts().get6()) ? (Integer.parseInt(data.getNoOfAttempts().get6()) * 100) / Integer.parseInt(data.getPlayed()) : 0;


                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(data.getNoOfAttempts().get1())
                        && !data.getNoOfAttempts().get1().equalsIgnoreCase("0")) {
                    dialogView.findViewById(R.id.rl_layout1).setVisibility(View.GONE);
                    dialogView.findViewById(R.id.rl_layout_one).setVisibility(View.VISIBLE);
                    ((TextView) dialogView.findViewById(R.id.tv_pb_1)).setText(data.getNoOfAttempts().get1());
                    ((ProgressBar) dialogView.findViewById(R.id.p_bar_1)).setProgress(progressOne);
                } else {
                    dialogView.findViewById(R.id.rl_layout1).setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(data.getNoOfAttempts().get2())
                        && !data.getNoOfAttempts().get2().equalsIgnoreCase("0")) {
                    dialogView.findViewById(R.id.rl_layout2).setVisibility(View.GONE);
                    dialogView.findViewById(R.id.rl_layout_two).setVisibility(View.VISIBLE);
                    ((TextView) dialogView.findViewById(R.id.tv_pb_2)).setText(data.getNoOfAttempts().get2());
                    ((ProgressBar) dialogView.findViewById(R.id.p_bar_2)).setProgress(progressTwo);

                } else {
                    dialogView.findViewById(R.id.rl_layout2).setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(data.getNoOfAttempts().get3())
                        && !data.getNoOfAttempts().get3().equalsIgnoreCase("0")) {
                    dialogView.findViewById(R.id.rl_layout3).setVisibility(View.GONE);
                    dialogView.findViewById(R.id.rl_layout_three).setVisibility(View.VISIBLE);
                    ((TextView) dialogView.findViewById(R.id.tv_pb_3)).setText(data.getNoOfAttempts().get3());
                    ((ProgressBar) dialogView.findViewById(R.id.p_bar_3)).setProgress(progressThree);

                } else {
                    dialogView.findViewById(R.id.rl_layout3).setVisibility(View.VISIBLE);

                }

                if (!TextUtils.isEmpty(data.getNoOfAttempts().get4())
                        && !data.getNoOfAttempts().get4().equalsIgnoreCase("0")) {
                    dialogView.findViewById(R.id.rl_layout4).setVisibility(View.GONE);
                    dialogView.findViewById(R.id.rl_layout_four).setVisibility(View.VISIBLE);
                    ((TextView) dialogView.findViewById(R.id.tv_pb_4)).setText(data.getNoOfAttempts().get4());
                    ((ProgressBar) dialogView.findViewById(R.id.p_bar_4)).setProgress(progressFour);

                } else {
                    dialogView.findViewById(R.id.rl_layout4).setVisibility(View.VISIBLE);

                }

                if (!TextUtils.isEmpty(data.getNoOfAttempts().get5())
                        && !data.getNoOfAttempts().get5().equalsIgnoreCase("0")) {
                    dialogView.findViewById(R.id.rl_layout5).setVisibility(View.GONE);
                    dialogView.findViewById(R.id.rl_layout_five).setVisibility(View.VISIBLE);
                    ((TextView) dialogView.findViewById(R.id.tv_pb_5)).setText(data.getNoOfAttempts().get5());
                    ((ProgressBar) dialogView.findViewById(R.id.p_bar_5)).setProgress(progressFive);

                } else {
                    dialogView.findViewById(R.id.rl_layout5).setVisibility(View.VISIBLE);

                }

                if (!TextUtils.isEmpty(data.getNoOfAttempts().get6())
                        && !data.getNoOfAttempts().get6().equalsIgnoreCase("0")) {
                    dialogView.findViewById(R.id.rl_layout6).setVisibility(View.GONE);
                    dialogView.findViewById(R.id.rl_layout_six).setVisibility(View.VISIBLE);
                    ((TextView) dialogView.findViewById(R.id.tv_pb_6)).setText(data.getNoOfAttempts().get6());
                    ((ProgressBar) dialogView.findViewById(R.id.p_bar_6)).setProgress(progressSix);
                } else {

                }
            }
        }
        if(gameResult != null && gameResult.equalsIgnoreCase(Constants.LOSS)){
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            cancel_btn.setVisibility(View.GONE);
            TextView tvOne = dialogView.findViewById(R.id.tv_one);
            TextView tvTwo = dialogView.findViewById(R.id.tv_two);
            TextView tvThree = dialogView.findViewById(R.id.tv_three);

            try {
                tvOne.setText(new StringBuilder().append(word_array[0]).append(matra[0]));
                tvTwo.setText(new StringBuilder().append(word_array[1]).append(matra[1]));
                tvThree.setText(new StringBuilder().append(word_array[2]).append(matra[2]));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            dialogView.findViewById(R.id.ll_sahi_jawab).setVisibility(View.GONE);

        }
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
                CommonPreference.getInstance(GameActivity.this.getApplicationContext()).getPackageString("applicationId")+".GameActivity.provider",
                imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.STATISTICS_SHARE_MSG);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void callgetStreakAPI() {
        String game_id = CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID);
        gamePresenter = new GamePresenter(this, GameActivity.this);
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
                ((TextView) findViewById(getId(index))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.black));
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
            findViewById(getId(index)).setBackgroundResource(R.drawable.bg_answer);
            ((TextView) findViewById(getId(index))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.black));
            ((TextView) findViewById(getId(index))).setText(matra[index % MAX_CHAR_LENGTH == 0 ? MAX_CHAR_LENGTH - 1 : (index % MAX_CHAR_LENGTH) - 1]);

            index = index - 1;


        }
    }

    private void submitText() {
        animate();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index % MAX_CHAR_LENGTH == 0) {

                    //verifyText--> call API --> increment count
                    if (!verifyText()) {
                        //Increment current Attempt Count at Last
                        if (currentAttempt < MAX_ATTEMPT) {
                            currentAttempt = currentAttempt + 1;
                            updateCurrentAttempt();
                        }
                      //  btnIdList.clear();
                    }


                    if (index == MAX_ATTEMPT * MAX_CHAR_LENGTH) {
                        if (!Arrays.equals(word_array, entered_word_array)) {

                            endGame(Constants.LOSS,String.valueOf(pauseOffset / 1000), currentAttempt );
                            //openLeaderBoardOnGameEnd();
                        }

                    }

                }
            }
        }, 1600);
    }

    /**
     * hit API to check word in dictionary
     */
    private boolean verifyText() {
        return mapWord();
    }

    private void updateCurrentAttempt() {
        //Update Matra
        if (btnIdList != null) {
            btnIdList.clear();
        }
        for (int i = 1; i < MAX_CHAR_LENGTH + 1; i++) {
            ((TextView) findViewById(getId((currentAttempt - 1) * 3 + i))).setText(matra[i - 1].toString());
        }

        if (hintCount == 1) {
            hintOne();
        } else if (hintCount == 2) {
            hintTwo();
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
            boolean isDump = false;
            for (int i = 0; i < word_array.length; i++) {
                if((int)word_array[i] == 0){
                    isDump = true;
                }
            }
            if(isDump == true){
                Log.d("batxmen", correctWord);
                if (gamePresenter != null) {
                    btnIdList.clear();
                    GameDataManager.getInstance().removeData();
                    GetWordRequest getWordRequest = new GetWordRequest();
                    getWordRequest.setUserId(CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
                    getWordRequest.setWordId(list);
                    gamePresenter.fetchNewWord(GameActivity.this, getWordRequest);
                }
            }
            Log.d("", matra.toString());
        } catch (Exception e) {
            if (gamePresenter != null) {
                btnIdList.clear();
                GameDataManager.getInstance().removeData();
                GetWordRequest getWordRequest = new GetWordRequest();
                getWordRequest.setUserId(CommonPreference.getInstance(this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID));
                getWordRequest.setWordId(list);
                gamePresenter.fetchNewWord(GameActivity.this, getWordRequest);
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
        try {
            if (Arrays.equals(word_array, entered_word_array)) {
                updateGreenBoxes();
                if (gamePresenter != null) {
                    if (tv_timer_text != null) {
                        tv_timer_text.stop();
                        pauseOffset = SystemClock.elapsedRealtime() - tv_timer_text.getBase();
                        mTimingRunning = false;

                    }
                    if (!TextUtils.isEmpty(datumCorrectWord.getId())) {
                        saveID(datumCorrectWord.getId());
                    }

                    endGame(Constants.WIN,String.valueOf(pauseOffset / 1000), currentAttempt );
                    // save correct word id
                    //datumCorrectWord.getId()

                }
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
                                ((TextView) findViewById(getId((currentAttempt - 1) * 3 + 1 + i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
                                ((TextView) findViewById(btnIdList.get(i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));

                            } else {// yellow
                                findViewById(getId((currentAttempt - 1) * 3 + 1 + i)).setBackgroundResource(R.drawable.bg_yellow);
                                findViewById(btnIdList.get(i)).setBackgroundResource(R.drawable.bg_yellow);
                                ((TextView) findViewById(getId((currentAttempt - 1) * 3 + 1 + i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
                                ((TextView) findViewById(btnIdList.get(i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));

                            }
                        }
                    }
                    if (!isExist) {//Not in word
                        findViewById(getId((currentAttempt - 1) * 3 + 1 + i)).setBackgroundResource(R.drawable.bg_grey);
                        findViewById(btnIdList.get(i)).setBackgroundResource(R.drawable.bg_grey);
                        ((TextView) findViewById(getId((currentAttempt - 1) * 3 + 1 + i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
                        ((TextView) findViewById(btnIdList.get(i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveID(String id) {
        Task task = new Task();
        task.setWordId(id);

        gamePresenter.saveIDLocalDB(GameActivity.this, task);

    }

    private void openLeaderBoardOnGameEnd() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GameActivity.this, ShabdamLeaderBoardActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                finish();
                //finish();
            }
        }, 500);
    }

    private void updateGreenBoxes() {
        for (int i = (currentAttempt - 1) * 3 + 1; i < currentAttempt * 3 + 1; i++) {
            findViewById(getId(i)).setBackgroundResource(R.drawable.bg_green_box);
            ((TextView) findViewById(getId(i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
        }

        //update Key Board
        for (int j = 0; j < btnIdList.size(); j++) {
            findViewById(btnIdList.get(j)).setBackgroundResource(R.drawable.bg_green_box);
            ((TextView) findViewById(btnIdList.get(j))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));

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

            flipOutAnimatorSet.setTarget(findViewById(getId((currentAttempt - 1) * 3 + 1)));
            flipOutAnimatorSet2.setTarget(findViewById(getId((currentAttempt - 1) * 3 + 2)));
            flipOutAnimatorSet3.setTarget(findViewById(getId((currentAttempt - 1) * 3 + 3)));

            flipInAnimatorSet.setTarget(findViewById(getId((currentAttempt - 1) * 3 + 1)));
            flipInAnimatorSet2.setTarget(findViewById(getId((currentAttempt - 1) * 3 + 2)));
            flipInAnimatorSet3.setTarget(findViewById(getId((currentAttempt - 1) * 3 + 3)));

            animatorListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animator == flipOutAnimatorSet) {
                        flipInAnimatorSet.start();
                        // flipOutAnimatorSet2.start();
                    } else if (animator == flipOutAnimatorSet2) {
                        flipInAnimatorSet2.start();
                        // flipOutAnimatorSet3.start();
                    } else {
                        flipInAnimatorSet3.start();
                    }
                    /*flipInAnimatorSet.start();
                    flipInAnimatorSet2.start();
                    flipInAnimatorSet3.start();*/


                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            };


            flipOutAnimatorSet.start();
            flipOutAnimatorSet2.start();
            flipOutAnimatorSet3.start();


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

    /*private void flipCardAnimation(){
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageView.setImageResource(R.drawable.frontSide);
                oa2.start();
            }
        });
        oa1.start();
    }*/

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
        this.datumCorrectWord = datumCorrectWord;
        this.correctWord = datumCorrectWord.getWords();
       //  ToastUtils.show(GameActivity.this, correctWord);
        showMatraText();
        updateCurrentAttempt();
    }

    @Override
    public void onStatisticsDataFetched(Data data) {
        GameView.super.onStatisticsDataFetched(data);
        statisticsPopup(data);


    }

    /**
     * Hard coded log-- can make it dynamic but not in mood
     */
    void showHint() {
        if (hintCount < 2) {
            btnIdList.clear();
            if (hintCount == 0) {
                hintCount++;
                hintOne();

            } else {
                //First Text
                hintCount++;
                hintTwo();
            }
        }


      /*  if (hintCount < 2) {
            btnIdList.clear();
            entered_word_array[hintCount] = word_array[hintCount];
            hintCount++;
            int pos = ((currentAttempt - 1) * 3) + hintCount;

            if (hintCount == 2) {
                btnIdList.add(getKeyId(String.valueOf(word_array[0])));
                ((TextView) findViewById(getId(pos - 1))).setText(new StringBuilder().append(word_array[0]).append(matra[0]));
                findViewById(getId(pos-1)).setBackgroundResource(R.color.green);
                entered_word_array[0] = word_array[0];
                updateWordCharArray(String.valueOf(word_array[hintCount - 1]));

            }

            ((TextView) findViewById(getId(pos))).setText(new StringBuilder().append(word_array[hintCount - 1]).append(matra[hintCount - 1]));
            findViewById(getId(hintCount)).setBackgroundResource(R.color.green);
            updateWordCharArray(String.valueOf(word_array[hintCount - 1]));
            int id = getKeyId(String.valueOf(word_array[hintCount - 1]));
            if (id != -1) {
                btnIdList.add(id);
                findViewById(id).setBackgroundResource(R.drawable.bg_green_box);
            }
            index = pos;


            clearNextBoxesAfterHint();
        }*/
    }

    private void hintTwo() {
        // hintCount ++;
        entered_word_array[0] = word_array[0];
        int pos = ((currentAttempt - 1) * 3) + 1;
        ((TextView) findViewById(getId(pos))).setText(new StringBuilder().append(word_array[0]).append(matra[0]));
        findViewById(getId(pos)).setBackgroundResource(R.drawable.bg_green_box);
        ((TextView) findViewById(getId(pos))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));

        int id = getKeyId(String.valueOf(word_array[0]));
        if (id != -1) {
            btnIdList.add(id);
            findViewById(id).setBackgroundResource(R.drawable.bg_green_box);
            ((TextView) findViewById(id)).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
        }

        ((TextView) findViewById(getId(pos + 1))).setText(matra[1]);
        ((TextView) findViewById(getId(pos + 2))).setText(matra[2]);

        // Second Text

        entered_word_array[1] = word_array[1];
        int pos2 = ((currentAttempt - 1) * 3) + 2;
        ((TextView) findViewById(getId(pos2))).setText(new StringBuilder().append(word_array[1]).append(matra[1]));
        findViewById(getId(pos2)).setBackgroundResource(R.drawable.bg_green_box);
        ((TextView) findViewById(getId(pos2))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));

        int id2 = getKeyId(String.valueOf(word_array[1]));
        if (id2 != -1) {
            btnIdList.add(id2);
            findViewById(id2).setBackgroundResource(R.drawable.bg_green_box);
            ((TextView) findViewById(id2)).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
        }

        ((TextView) findViewById(getId(pos + 2))).setText(matra[2]);
        index = pos2;

    }

    private void hintOne() {
        // hintCount ++;
        entered_word_array[0] = word_array[0];
        int pos = ((currentAttempt - 1) * 3) + 1;
        ((TextView) findViewById(getId(pos))).setText(new StringBuilder().append(word_array[0]).append(matra[0]));
        findViewById(getId(pos)).setBackgroundResource(R.drawable.bg_green_box);
        ((TextView) findViewById(getId(pos))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));

        int id = getKeyId(String.valueOf(word_array[0]));
        if (id != -1) {
            btnIdList.add(id);
            findViewById(id).setBackgroundResource(R.drawable.bg_green_box);
            ((TextView) findViewById(id)).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
        }

        ((TextView) findViewById(getId(pos + 1))).setText(matra[1]);
        ((TextView) findViewById(getId(pos + 2))).setText(matra[2]);

        index = pos;
    }

    private void clearNextBoxesAfterHint() {
        for (int i = 0; i < 3 - hintCount; i++) {
            ((TextView) findViewById(getId(index + i + 1))).setText(matra[i + 1]);
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

    @Override
    public void onWordCheckDic(boolean isMatched) {
        if (isMatched) {
            // shake attempted layout
            submitText();
        } else {
            shakeAnimation();
            for (int i = 1; i < MAX_CHAR_LENGTH + 1; i++) {
                findViewById(getId((currentAttempt - 1) * 3 + i)).setBackgroundResource(R.drawable.bg_red);
                ((TextView) findViewById(getId((currentAttempt - 1) * 3 + i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
            }
            findViewById(R.id.fl_dic_error).setVisibility(View.VISIBLE);

            shakeAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    for (int i = 1; i < MAX_CHAR_LENGTH + 1; i++) {
                        findViewById(getId((currentAttempt - 1) * 3 + i)).setBackgroundResource(R.drawable.bg_answer);
                        ((TextView) findViewById(getId((currentAttempt - 1) * 3 + i))).setText("");
                        ((TextView) findViewById(getId((currentAttempt - 1) * 3 + i))).setTextColor(ContextCompat.getColor(GameActivity.this, R.color.black));
                    }
                    index = (currentAttempt - 1) * 3;
                    updateCurrentAttempt();
                    findViewById(R.id.fl_dic_error).setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // ToastUtils.show(GameActivity.this, "शब्द शब्दकोश में नहीं है। ");
        }
    }

    @Override
    protected void onDestroy() {
        if (gamePresenter != null) {
            gamePresenter.onDestroy();
        }
        tv_timer_text = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        alertDialog = null;
        keyIdArray = null;
        matra = null;
        btnIdList = null;
        entered_word_array = null;
        datumCorrectWord = null;
        word_array = null;
        charArray = null;
        gamePresenter = null;
        mGoogleSignInClient = null;
        adRequest = null;
        mRewardedAd = null;
        shakeAnimation = null;
        super.onDestroy();
    }

    @Override
    public void onGameSubmit() {
        if (isUttarDekheClicked) {
            isUttarDekheClicked = false;
            if(rl_uttar_dekho_btn != null){
                rl_uttar_dekho_btn.setEnabled(false);
            }

            loadAdd();

        } else {
            if(gameResult != null && gameResult.equalsIgnoreCase(Constants.LOSS)){
                callgetStreakAPI();
            }else {
                openLeaderBoardOnGameEnd();
            }
        }
    }

    private void shakeAnimation() {
        shakeAnimation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.shake);
        getGrid().setAnimation(shakeAnimation);
    }

    public View getGrid() {
        if (currentAttempt == 1) {
            return findViewById(R.id.ll_grid_one);
        } else if (currentAttempt == 2) {
            return findViewById(R.id.ll_grid_two);
        } else if (currentAttempt == 3) {
            return findViewById(R.id.ll_grid_three);
        } else if (currentAttempt == 4) {
            return findViewById(R.id.ll_grid_four);
        } else if (currentAttempt == 5) {
            return findViewById(R.id.ll_grid_five);
        }

        return null;
    }

    private enum CLICK_ITEM {HINT, UTTAR_DEKHO, AGLA_SHABD}

}