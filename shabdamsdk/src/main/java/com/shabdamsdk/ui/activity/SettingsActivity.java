package com.shabdamsdk.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.shabdamsdk.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private String type;
    private RelativeLayout rl_feedback_btn;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inIt();
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);
        findViewById(R.id.rl_feedback_btn).setOnClickListener(this);
        findViewById(R.id.iv_logout_btn).setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back_btn) {
            if (!TextUtils.isEmpty(type) && type.equals("1")) {
                Intent intent = new Intent(this, ShabdamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        if (view.getId() == R.id.iv_logout_btn)
        {
            signOut();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(SettingsActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }
                });
    }
}