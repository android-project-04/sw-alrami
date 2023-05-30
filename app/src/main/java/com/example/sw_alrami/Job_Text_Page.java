package com.example.sw_alrami;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Job_Text_Page extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_text_page);

        TextView titleText = findViewById(R.id.jobTitle);

        Intent intent = getIntent();
        titleText.setText(intent.getStringExtra("titleText"));
    }
}
