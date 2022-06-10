package com.example.pocketvoca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FlashCardActivity extends AppCompatActivity {

    // 앱바 관련 변수
    ImageView sub_close_iv;
    TextView sub_title_tv;

    TextView flashcard_num_tv;
    TextView flashcard_word_tv;
    TextView flashcard_meaning_tv;

    Button flashcard_incorrect_btn;
    Button flashcard_correct_btn;

    ArrayList<Word> wordArrayList; // 총 단어 리스트
    ArrayList<Word> studyArrayList = new ArrayList<Word>();; // 학습할 단어 리스트
    ArrayList<Word> inCorrectArrayList = new ArrayList<Word>();; // 틀린 단어 리스트
    ArrayList<Word> correctArrayList = new ArrayList<Word>();; // 맞은 단어 리스트

    int cardIndex = 0; // 현재 보여주고있는 단어 순서
    int wordNum = 0; // 학습할 총 단어 개수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        sub_title_tv = (TextView) findViewById(R.id.sub_title_tv);
        sub_title_tv.setVisibility(View.INVISIBLE);
        sub_close_iv = (ImageView) findViewById(R.id.sub_close_iv);

        flashcard_num_tv = (TextView) findViewById(R.id.flashcard_num_tv);
        flashcard_word_tv = (TextView) findViewById(R.id.flashcard_word_tv);
        flashcard_meaning_tv = (TextView) findViewById(R.id.flashcard_meaning_tv);

        flashcard_incorrect_btn = (Button) findViewById(R.id.flashcard_incorrect_btn);
        flashcard_correct_btn = (Button) findViewById(R.id.flashcard_correct_btn);

        // setFlash 액티비티에서 보낸 데이터 받기
        Intent intent = getIntent();
        wordArrayList = (ArrayList<Word>) intent.getSerializableExtra("flashWords");
        wordNum = intent.getIntExtra("flashWordsNum", 0);

        // 학습할 단어 설정
        for (int i = 0; i < wordNum; i++) {
            studyArrayList.add(new Word(wordArrayList.get(i).word, wordArrayList.get(i).meaning));
        }

        // 단어와 뜻 설정
        flashcard_word_tv.setText(studyArrayList.get(cardIndex).word);
        flashcard_meaning_tv.setText(studyArrayList.get(cardIndex).meaning);
        // 현재 보이는 인덱스 설정
        flashcard_num_tv.setText((cardIndex+1) + " / " + Integer.toString(wordNum));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 단어와 뜻 설정
        flashcard_word_tv.setText(studyArrayList.get(cardIndex).word);
        flashcard_meaning_tv.setText(studyArrayList.get(cardIndex).meaning);
        // 현재 보이는 인덱스 설정
        flashcard_num_tv.setText((cardIndex+1) + " / " + Integer.toString(wordNum));

        // 모르겠어요 버튼을 눌렀을 경우
        flashcard_incorrect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inCorrectArrayList.add(new Word(studyArrayList.get(cardIndex).word, studyArrayList.get(cardIndex).meaning)); // 틀린 단어 목록 추가

                // 화면 전환
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra("result", 0);
                startActivity(intent);
            }
        });

        // 알아요 버튼을 눌렀을 경우
        flashcard_correct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctArrayList.add(new Word(studyArrayList.get(cardIndex).word, studyArrayList.get(cardIndex).meaning)); // 맞은 단어 목록 추가

                // 화면 전환
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra("result", 1);
                startActivity(intent);
            }
        });

        // 닫기 버튼을 눌렀을 경우
        sub_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        cardIndex += 1; // 인덱스 증가
        
        // 모든 단어 학습을 완료했다면
        if (cardIndex == wordNum) {
            // 화면 전환
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("inCorrectWords", inCorrectArrayList);
            intent.putExtra("correctWords", correctArrayList);
            intent.putExtra("studyWords", studyArrayList);
            finish();
            startActivity(intent);
        }
    }
}