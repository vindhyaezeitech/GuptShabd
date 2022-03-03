package com.guptshabd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shabdamsdk.GameActivity;

public class UserDetailActivity extends AppCompatActivity {

    private EditText etUserName, etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        etUserName = findViewById(R.id.etUserName);
        etId = findViewById(R.id.etID);

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etUserName.getText().toString())){
                    etUserName.setError("Invalid User Name");
                }
                if(TextUtils.isEmpty(etId.getText().toString())){
                    etUserName.setError("Invalid User Id");
                }
                Intent intent = new Intent(UserDetailActivity.this, MainActivity.class);
                intent.putExtra("user_id", etId.getText().toString());
                intent.putExtra("name",etUserName.getText().toString());
                intent.putExtra("uname","vikash");
                intent.putExtra("email","vikash@mailinator.com");
                intent.putExtra("profile_image","");
                startActivity(intent);
                finish();
            }
        });


    }
}