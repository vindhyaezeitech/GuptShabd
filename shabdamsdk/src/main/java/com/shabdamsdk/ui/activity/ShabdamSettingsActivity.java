package com.shabdamsdk.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.shabdamsdk.R;
import com.shabdamsdk.event.CleverTapEvent;
import com.shabdamsdk.event.CleverTapEventConstants;
import com.shabdamsdk.pref.CommonPreference;

public class ShabdamSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private String type;
    private RelativeLayout rl_feedback_btn;
    private GoogleSignInClient mGoogleSignInClient;
    private Switch swithc_sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabdam_settings);
        inIt();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);
        findViewById(R.id.rl_feedback_btn).setOnClickListener(this);
        findViewById(R.id.rl_logout_btn).setOnClickListener(this);
        findViewById(R.id.rl_submit_word).setOnClickListener(this);
        findViewById(R.id.rl_PrivacyPolicy).setOnClickListener(this);
        findViewById(R.id.rl_Term).setOnClickListener(this);
        findViewById(R.id.rl_faq_btn).setOnClickListener(this);
        Switch sw = findViewById(R.id.switch_btn);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.NOTIFICATION_ON);

                } else {
                    // The toggle is disabled
                    CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.NOTIFICATION_OFF);
                }
            }
        });

        swithc_sound = findViewById(R.id.switch_music);
        swithc_sound.setChecked(CommonPreference.getInstance(ShabdamSettingsActivity.this).getSoundState());
        swithc_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CommonPreference.getInstance(ShabdamSettingsActivity.this).saveSoundState(isChecked);
                if (isChecked) {
                    // The toggle is enabled

                    CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.NOTIFICATION_ON);

                } else {
                    // The toggle is disabled
                    CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.NOTIFICATION_OFF);
                }
            }
        });

        if (!TextUtils.isEmpty(CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID))) {
            findViewById(R.id.rl_logout_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rl_logout_btn).setVisibility(View.GONE);
            findViewById(R.id.view_log_out).setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back_btn) {
            if (!TextUtils.isEmpty(type) && type.equals("1")) {
                Intent intent = new Intent(this, ShabdamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                /*Intent intent = new Intent(this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                finish();
            }
        }
        if (view.getId() == R.id.rl_feedback_btn) {
            CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.FEEDBACK);
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"tak@gmail.com"});
            //email.putExtra(Intent.EXTRA_SUBJECT, subject);
            //email.putExtra(Intent.EXTRA_TEXT, message);
            email.setType("message/rfc822");
            if (email.resolveActivity(getPackageManager()) != null) {
                startActivity(email);
            }
            //startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }

        if (view.getId() == R.id.rl_submit_word) {
            CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.SUBMIT_WORD);
            String url = "https://docs.google.com/forms/d/e/1FAIpQLSdrlK8uTrvqsWAy5Vok3tKNiFJqS2el8Ah1NqmXIsYO0_PF-w/viewform?vc=0&c=0&w=1&flr=0";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            //startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        if (view.getId() == R.id.rl_logout_btn) {
            if (!TextUtils.isEmpty(CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).getString(CommonPreference.Key.GAME_USER_ID))) {
                signOut();
            }
        }
        if (view.getId() == R.id.rl_faq_btn) {
            CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.FAQ);
        }
        if(view.getId() == R.id.rl_Term){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1efJN1iZrt9r_hd4hK8_Kct0tUsnVNptSXSm_26DcE6Q/edit?usp=sharing"));
            startActivity(browserIntent);
        }
        if(view.getId() == R.id.rl_PrivacyPolicy){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1efJN1iZrt9r_hd4hK8_Kct0tUsnVNptSXSm_26DcE6Q/edit?usp=sharing"));
            startActivity(browserIntent);
        }
    }

    private void signOut() {


        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                        CleverTapEvent.getCleverTapEvents(ShabdamSettingsActivity.this).createOnlyEvent(CleverTapEventConstants.LOGOUT_ICON);

                        boolean isTutShown = CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).getBoolean(CommonPreference.Key.IS_TUTORIAL_SHOWN, false);
                        boolean isRuleShown = CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).getBoolean(CommonPreference.Key.IS_RULE_SHOWN, false);

                        String applicationId = CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).getPackageString("applicationId");
                        String appUniqueId = CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).getUniqueAppId();

                        CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).clear();

                        CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).put(CommonPreference.Key.IS_TUTORIAL_SHOWN, isTutShown);
                        CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).put(CommonPreference.Key.IS_RULE_SHOWN, isRuleShown);
                        CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).put("applicationId", applicationId);
                        CommonPreference.getInstance(ShabdamSettingsActivity.this.getApplicationContext()).put("appUniqueId", appUniqueId);

                        setResult(RESULT_OK);
                        finish();

                    }
                });
    }
}