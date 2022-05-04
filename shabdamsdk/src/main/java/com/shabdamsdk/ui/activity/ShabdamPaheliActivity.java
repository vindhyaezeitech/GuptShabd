package com.shabdamsdk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.GameActivity;
import com.shabdamsdk.R;
import com.shabdamsdk.pref.CommonPreference;

public class ShabdamPaheliActivity extends AppCompatActivity {

    private Button aage_bade_btn;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabdam_paheli);
        aage_bade_btn = findViewById(R.id.aage_bade_btn);
        checkBox = findViewById(R.id.check_box_btn);

        aage_bade_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShabdamPaheliActivity.this, GameActivity.class));
                finish();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    CommonPreference.getInstance(ShabdamPaheliActivity.this.getApplicationContext()).put(CommonPreference.Key.IS_RULE_SHOWN, true);

                } else {
                    CommonPreference.getInstance(ShabdamPaheliActivity.this.getApplicationContext()).put(CommonPreference.Key.IS_RULE_SHOWN, false);

                }
            }
        });
    }
}