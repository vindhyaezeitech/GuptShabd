package com.guptshabd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shabdamsdk.ShabdamSplashActivity;
import com.shabdamsdk.pref.CommonPreference;
import com.shabdamsdk.ui.activity.TutorialActivity;

public class UserDetailActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    private TextView tv_google_sign_in;
    private Button play_guest_btn;
    private LinearLayout ll_welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ll_welcome = findViewById(R.id.ll_welcome_layout);
        Animation bottomDown = AnimationUtils.loadAnimation(this,
                com.shabdamsdk.R.anim.bottom_to_up_slide);
        ll_welcome.startAnimation(bottomDown);

        String email = CommonPreference.getInstance(UserDetailActivity.this).getString(CommonPreference.Key.EMAIL);
        if (!TextUtils.isEmpty(email)) {
            Intent intent = new Intent(UserDetailActivity.this, ShabdamSplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            inIt();
            googleSignIn();
        }
       /* Intent intent = new Intent(UserDetailActivity.this, ShabdamSplashActivity.class);
        intent.putExtra("user_id", "1123444");

        startActivity(intent);
        finish();*/

    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void inIt() {
        play_guest_btn = findViewById(R.id.play_guest_btn);

        play_guest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDetailActivity.this, TutorialActivity.class));
            }
        });
        //tv_google_sign_in = findViewById(R.id.tv_google_sign_in);

/*
        tv_google_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserDetailActivity.this != null) {
                    if (ToastUtils.checkInternetConnection(UserDetailActivity.this)) {
                        signIn();

                    } else {
                        Toast.makeText(UserDetailActivity.this, getString(com.shabdamsdk.R.string.ensure_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

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
                CommonPreference.getInstance(UserDetailActivity.this).put(CommonPreference.Key.EMAIL, personEmail);

                Intent intent = new Intent(UserDetailActivity.this, ShabdamSplashActivity.class);
                intent.putExtra("user_id", "1123444");
                intent.putExtra("name", acct.getDisplayName());
                intent.putExtra("uname", personName);
                intent.putExtra("email", personEmail);
                intent.putExtra("profile_image", personPhoto);
                startActivity(intent);
                finish();

                //Toast.makeText(this, ""+personEmail, Toast.LENGTH_SHORT).show();
            }
            //startActivity(new Intent(this, ShabdamSplashActivity.class));

        } catch (ApiException e) {
            Log.d("Message", e.toString());
        }
    }
}