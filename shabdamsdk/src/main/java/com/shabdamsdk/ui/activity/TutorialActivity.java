package com.shabdamsdk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.shabdamsdk.R;

public class TutorialActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private TextView next_btn, skip_btn, start_btn;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewFlipper = findViewById(R.id.view_flipper);
        next_btn = findViewById(R.id.tv_next_btn);
        skip_btn = findViewById(R.id.tv_skip_btn);
        start_btn = findViewById(R.id.tv_start_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count >= 0 && count <= 2) {
                    if (count == 2) {
                        viewFlipper.showNext();
                        next_btn.setVisibility(View.GONE);
                        skip_btn.setVisibility(View.GONE);
                        start_btn.setVisibility(View.VISIBLE);
                    } else {
                        viewFlipper.showNext();
                        count++;
                    }
                }
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TutorialActivity.this, ShabdamPaheliActivity.class));
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TutorialActivity.this, ShabdamPaheliActivity.class));
            }
        });
    }
}