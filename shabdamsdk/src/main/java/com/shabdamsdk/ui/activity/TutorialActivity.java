package com.shabdamsdk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import com.shabdamsdk.R;
import com.shabdamsdk.pref.CommonPreference;
import com.shabdamsdk.ui.adapter.ImageSlideAdapter;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {
    private TextView next_btn, skip_btn, start_btn;
    private ImageSlideAdapter imageSlideAdapter;
    int currentPage;
    private ViewPager2 viewPager2;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewPager2 = findViewById(R.id.vp_skip_viewpager);
        imageSlideAdapter=new ImageSlideAdapter(this,getImages());
        viewPager2.setAdapter(imageSlideAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage=position;
                invalidateButton();
            }
        });

        next_btn = findViewById(R.id.tv_next_btn);
        skip_btn = findViewById(R.id.tv_skip_btn);
        start_btn = findViewById(R.id.tv_start_btn);

        currentPage=0;
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();

            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonPreference.getInstance(TutorialActivity.this.getApplicationContext()).put(CommonPreference.Key.IS_TUTORIAL_SHOWN, true);
                startActivity(new Intent(TutorialActivity.this, ShabdamPaheliActivity.class));
                finish();

            }
        });



        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonPreference.getInstance(TutorialActivity.this.getApplicationContext()).put(CommonPreference.Key.IS_TUTORIAL_SHOWN, true);
                startActivity(new Intent(TutorialActivity.this, ShabdamPaheliActivity.class));
                finish();
            }
        });

    }

    private void invalidateButton() {
        if (currentPage==imageSlideAdapter.getItemCount()-1)
        {
            skip_btn.setVisibility(View.GONE);
            next_btn.setVisibility(View.GONE);
            start_btn.setVisibility(View.VISIBLE);
        }else {
            skip_btn.setVisibility(View.VISIBLE);
            next_btn.setVisibility(View.VISIBLE);
            start_btn.setVisibility(View.GONE);
        }
    }

    private void next() {
        currentPage++;
        viewPager2.setCurrentItem(currentPage);
    }

    private List<Integer> getImages() {
        List<Integer>list=new ArrayList<>();
        list.add(R.drawable.first_img);
        list.add(R.drawable.second_img);
        list.add(R.drawable.third_img);
        list.add(R.drawable.fourth_img);

        return list;

    }
}