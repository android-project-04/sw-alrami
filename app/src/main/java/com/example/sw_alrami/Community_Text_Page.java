package com.example.sw_alrami;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Community_Text_Page extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_text_page);

        TextView titleText = findViewById(R.id.communityTitle);
        TextView mainText = findViewById(R.id.mainText);

        Intent intent = getIntent();
        titleText.setText(intent.getStringExtra("titleText"));
        mainText.setText(intent.getStringExtra("mainText"));
    }
}