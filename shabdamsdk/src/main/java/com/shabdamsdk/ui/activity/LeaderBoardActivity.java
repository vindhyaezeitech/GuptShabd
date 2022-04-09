package com.shabdamsdk.ui.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shabdamsdk.Constants;
import com.shabdamsdk.GameActivity;
import com.shabdamsdk.GamePresenter;
import com.shabdamsdk.GameView;
import com.shabdamsdk.R;
import com.shabdamsdk.ToastUtils;
import com.shabdamsdk.Utils;
import com.shabdamsdk.model.SignupRequest;
import com.shabdamsdk.model.gamesubmit.SubmitGameRequest;
import com.shabdamsdk.model.leaderboard.LeaderboardListModel;
import com.shabdamsdk.pref.CommonPreference;
import com.shabdamsdk.ui.adapter.GetLeaderboardListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity implements GameView, View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    private String type;
    private GamePresenter gamePresenter;
    private GetLeaderboardListAdapter adapter;
    private RecyclerView recyclerView;
    private RelativeLayout rl_one, rl_two, rl_three, rl_share_btn;
    private TextView tv_name_one, tv_name_two, tv_name_three;
    private InterstitialAd mInterstitialAd;
    private LinearLayout ll_google_sign_in;
    private TextView tv_google_sign_in;
    private String gameStatus, gameTime;
    private int noOfattempt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        gamePresenter = new GamePresenter(this, LeaderBoardActivity.this);
        interstitialAdd();
        inIt();
        googleSignIn();

    }

    @SuppressLint("WrongViewCast")
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
        ll_google_sign_in = findViewById(R.id.ll_google_sign_in);
        ll_google_sign_in.setOnClickListener(this);
        tv_google_sign_in = findViewById(R.id.tv_google_sign_in);
        findViewById(R.id.rl_agla_shabd_btn).setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if(intent != null && intent.getExtras() != null){
            if(!TextUtils.isEmpty(getIntent().getStringExtra(Constants.GAME_STATUS))){
                gameStatus = getIntent().getStringExtra(Constants.GAME_STATUS);
            }

            if(!TextUtils.isEmpty(getIntent().getStringExtra(Constants.TIME))){
                gameTime = getIntent().getStringExtra(Constants.TIME);
            }

            if(getIntent().getIntExtra(Constants.NUMBER_OF_ATTEMPT, 0) != 0){
                noOfattempt = getIntent().getIntExtra(Constants.NUMBER_OF_ATTEMPT, 1);
            }
        }

        rl_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot(getWindow().getDecorView());
            }
        });

        String gameId = CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID);
        if (!TextUtils.isEmpty(gameId)) {
            ll_google_sign_in.setVisibility(View.GONE);
            callGetLeaderBoardListAPI();

        } else {
            ll_google_sign_in.setVisibility(View.VISIBLE);
            tv_google_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LeaderBoardActivity.this != null) {
                        if (ToastUtils.checkInternetConnection(LeaderBoardActivity.this)) {
                            signIn();

                        } else {
                            Toast.makeText(LeaderBoardActivity.this, getString(com.shabdamsdk.R.string.ensure_internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }



    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
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

                Utils.saveUserData(LeaderBoardActivity.this,personName, personName, personEmail, String.valueOf(personPhoto));

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
                CommonPreference.getInstance(this).put(CommonPreference.Key.GAME_USER_ID, String.valueOf(data.getId()));
                ll_google_sign_in.setVisibility(View.GONE);

                if(!TextUtils.isEmpty(gameTime)){
                    SubmitGameRequest submitGameRequest = new SubmitGameRequest();
                    submitGameRequest.setGameUserId(CommonPreference.getInstance(LeaderBoardActivity.this).getString(CommonPreference.Key.GAME_USER_ID));
                    submitGameRequest.setGameStatus(gameStatus);
                    submitGameRequest.setNoOfAttempt(noOfattempt);
                    submitGameRequest.setTime(gameTime);
                    gamePresenter.submitGame(submitGameRequest);
                }
            }
        }

    }

    @Override
    public void onGameSubmit() {
        GameView.super.onGameSubmit();
        String game_id = CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID);


        //  compositeDisposable = interestPresenter.loadCategoryData();
        if(gamePresenter != null)
            gamePresenter.fetchLeaderBoardList(game_id);
    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void callGetLeaderBoardListAPI() {
        String game_id = CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID);


        //  compositeDisposable = interestPresenter.loadCategoryData();
        if(gamePresenter != null)
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
                intent.putExtra("user_id", CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID));
                intent.putExtra("name", CommonPreference.getInstance(this).getString(CommonPreference.Key.NAME));
                intent.putExtra("uname", CommonPreference.getInstance(this).getString(CommonPreference.Key.UNAME));
                intent.putExtra("email", CommonPreference.getInstance(this).getString(CommonPreference.Key.EMAIL));
                intent.putExtra("profile_image", CommonPreference.getInstance(this).getString(CommonPreference.Key.PROFILE_IMAGE));
                startActivity(intent);
                finish();
            } else {
                onBackPressed();
            }
        } else if (view.getId() == R.id.rl_agla_shabd_btn) {
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
            intent.putExtra("user_id", CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID));
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
        intent.putExtra("user_id", CommonPreference.getInstance(this).getString(CommonPreference.Key.GAME_USER_ID));
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
        if (gamePresenter != null) {
            gamePresenter.onDestroy();
        }
    }
}