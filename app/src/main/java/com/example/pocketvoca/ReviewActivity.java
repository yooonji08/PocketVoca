package com.example.pocketvoca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    TextView review_correct_tv;
    TextView review_incorrect_tv;
    Button review_gostudy_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        review_correct_tv = (TextView) findViewById(R.id.review_correct_tv);
        review_incorrect_tv = (TextView) findViewById(R.id.review_incorrect_tv);
        review_gostudy_btn = (Button) findViewById(R.id.review_gostudy_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // flashcard 액티비티에서 보낸 데이터 받기
        Intent intent = getIntent();
        int result = intent.getIntExtra("result", 0);

        if (result == 0) { // 문제를 틀렸다면
            review_correct_tv.setVisibility(View.INVISIBLE);
            review_incorrect_tv.setVisibility(View.VISIBLE);
        } else if (result == 1) { // 문제를 맞췄다면
            review_correct_tv.setVisibility(View.VISIBLE);
            review_incorrect_tv.setVisibility(View.INVISIBLE);
        }

        // 돌아가기 버튼을 눌렀다면
        review_gostudy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}