package com.example.pocketvoca;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class ResultActivity extends TabActivity {

    // 앱바 관련 변수
    ImageView sub_close_iv;
    TextView sub_title_tv;

    LinearLayout result_allWord_LinearLayout; // 모든 단어 레이아웃
    LinearLayout result_correct_LinearLayout; // 정답 레이아웃
    LinearLayout result_incorrect_LinearLayout; // 오답 레이아웃

    ArrayList<Word> studyArrayList = new ArrayList<Word>(); // 학습한 단어 리스트
    ArrayList<Word> inCorrectArrayList = new ArrayList<Word>(); // 틀린 단어 리스트
    ArrayList<Word> correctArrayList = new ArrayList<Word>(); // 맞은 단어 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        sub_title_tv = (TextView) findViewById(R.id.sub_title_tv);
        sub_title_tv.setText("학습 결과");
        sub_close_iv = (ImageView) findViewById(R.id.sub_close_iv);

        result_allWord_LinearLayout = (LinearLayout) findViewById(R.id.result_allWord_LinearLayout);
        result_correct_LinearLayout = (LinearLayout) findViewById(R.id.result_correct_LinearLayout);
        result_incorrect_LinearLayout = (LinearLayout) findViewById(R.id.result_incorrect_LinearLayout);

        // flashcard 액티비티에서 보낸 데이터 받기
        Intent intent = getIntent();
        studyArrayList = (ArrayList<Word>) intent.getSerializableExtra("studyWords");
        correctArrayList = (ArrayList<Word>) intent.getSerializableExtra("correctWords");
        inCorrectArrayList = (ArrayList<Word>) intent.getSerializableExtra("inCorrectWords");

        TabHost tabHost = getTabHost(); // 탭 호스트 호출

        // 모든 단어 탭
        TabSpec tabAllWord = tabHost.newTabSpec("ALL").setIndicator("모든 단어 " + studyArrayList.size());
        tabAllWord.setContent(R.id.result_allWord_sv);
        tabHost.addTab(tabAllWord);

        // 정답 탭
        TabSpec tabCorrectWord = tabHost.newTabSpec("CORRECT").setIndicator("정답 " + correctArrayList.size());
        tabCorrectWord.setContent(R.id.result_correct_sv);
        tabHost.addTab(tabCorrectWord);

        // 오답 탭
        TabSpec tabInCorrectWord = tabHost.newTabSpec("INCORRECT").setIndicator("오답 " + inCorrectArrayList.size());
        tabInCorrectWord.setContent(R.id.result_incorrect_sv);
        tabHost.addTab(tabInCorrectWord);

        // 처음으로 시작할 tab
        tabHost.setCurrentTab(0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 닫기버튼을 눌렀을 경우
        sub_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 모든 단어 레이아웃에 동적 레이아웃 추가
        for (int i = 0; i < studyArrayList.size(); i++) {
            for (int k = 0; k < studyArrayList.size(); k++) {

                // 만약 맞춘 단어라면
                if (k < correctArrayList.size()) {
                    if (studyArrayList.get(i).word.equals(correctArrayList.get(k).word)) {

                        LinearLayout newLayout = returnResult("purple_60", correctArrayList.get(k).word, correctArrayList.get(k).meaning);

                        result_allWord_LinearLayout.addView(newLayout);
                        break;
                    }
                }

                // 만약 틀린 단어라면
                if (k < inCorrectArrayList.size()) {
                    if (studyArrayList.get(i).word.equals(inCorrectArrayList.get(k).word)){

                        LinearLayout newLayout = returnResult("black_40", inCorrectArrayList.get(k).word, inCorrectArrayList.get(k).meaning);

                        result_allWord_LinearLayout.addView(newLayout);
                        break;
                    }
                }
            }
        }

        // 정답 레이아웃에 동적 레이아웃 추가
        for (int i = 0; i < correctArrayList.size(); i++) {

            LinearLayout newLayout = returnResult("purple_60", correctArrayList.get(i).word, correctArrayList.get(i).meaning);

            // 레이아웃 추가
            result_correct_LinearLayout.addView(newLayout);
        }

        // 오답 레이아웃에 동적 레이아웃 추가
        for (int i = 0; i < inCorrectArrayList.size(); i++) {

            LinearLayout newLayout = returnResult("black_40", inCorrectArrayList.get(i).word, inCorrectArrayList.get(i).meaning);

            // 레이아웃 추가
            result_incorrect_LinearLayout.addView(newLayout);
        }
    }

    // 객체 생성 후 전달받은 값으로 동적 객체에 값 넣는 함수
    public LinearLayout returnResult(String color, String word, String meaning) {

        // 동적으로 단어 박스 생성
        LinearLayout newLayout;
        LayoutInflater inflater = getLayoutInflater();
        newLayout = (LinearLayout) inflater.inflate(R.layout.item_resultbox, null);

        // 동적 레이아웃 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 24;
        newLayout.setLayoutParams(params);

        // 객체 연결
        ImageView circleIV = newLayout.findViewById(R.id.item_resultbox_circle_iv);
        TextView wordTV = newLayout.findViewById(R.id.item_resultbox_word_tv);
        TextView meaningTV = newLayout.findViewById(R.id.item_resultbox_meaning_tv);

        // 객체에 값 넣기
        if (color.equals("black_40")) {
            circleIV.setImageResource(R.drawable.ic_circle2);
        } else if (color.equals("purple_60")) {
            circleIV.setImageResource(R.drawable.ic_circle1);
        }
        wordTV.setText(word);
        meaningTV.setText(meaning);

        return newLayout;
    }
}