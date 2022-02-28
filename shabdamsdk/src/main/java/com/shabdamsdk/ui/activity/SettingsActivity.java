package com.shabdamsdk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.GameActivity;
import com.shabdamsdk.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inIt();
    }

    private void inIt() {
        findViewById(R.id.iv_back_btn).setOnClickListener(this);

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
                Intent intent = new Intent(this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }
}