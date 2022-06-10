package com.example.pocketvoca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SetFlashcardActivity extends AppCompatActivity {

    // 앱바 관련 변수
    ImageView sub_close_iv;

    EditText set_word_number_edt;
    Button set_play_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_flashcard);

        sub_close_iv = (ImageView) findViewById(R.id.sub_close_iv);

        set_word_number_edt = (EditText) findViewById(R.id.set_word_number_edt);
        set_play_btn = (Button) findViewById(R.id.set_play_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 메인 액티비티에서 보낸 단어와 뜻 데이터 받기
        Intent intent = getIntent();
        ArrayList<Word> wordArrayList = (ArrayList<Word>) intent.getSerializableExtra("words");

        // 닫기 버튼을 클릭했을 경우
        // 현재 액티비티 종료
        sub_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 시작하기 버튼을 클릭했을 경우
        set_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // 문제 수 값을 정수 값으로 변환
                    int wordNum = Integer.parseInt(set_word_number_edt.getText().toString());

                    // 올바른 값을 입력했다면
                    if (wordNum > 0 && wordNum <= wordArrayList.size()) {
                        // arrylist 값과 문제 수 정보를 넘겨주고 화면 전환
                        Intent intent = new Intent(getApplicationContext(), FlashCardActivity.class);
                        intent.putExtra("flashWords", wordArrayList);
                        intent.putExtra("flashWordsNum", wordNum);
                        finish();
                        startActivity(intent);
                    }
                    else if (wordNum <= 0 || wordNum > wordArrayList.size()) { // 문제 수 값이 0이하 이거나 arryList값보다 크다면
                        Toast.makeText(getApplicationContext(), "문제 수를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception exception) { // editText에 숫자가 아닌 다른 값을 입력했다면
                    Toast.makeText(getApplicationContext(), "올바른 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}