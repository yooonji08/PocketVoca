package com.example.pocketvoca;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    // 앱바 관련 변수
    ImageView home_add_iv;
    ImageView home_flashcard_iv;

    LinearLayout dialogView; // 커스텀 대화상자
    EditText dialog_word_edt, dialog_meaning_edt; // 대화상자에 입력한 값
    Button dialog_cancel_btn, dialog_confirm_btn; // 확인, 취소 버튼
    Button dialog_delete_btn, dialog_no_btn; // 삭제, 취소 버튼

    LinearLayout main_ingWordBox_LinearLayout; // 레이아웃
    int layoutCount = 0; // 레이아웃 개수, 레이아웃 아이디

    ArrayList<Word> wordArray = new ArrayList<Word>(); // <단어, 뜻> 직렬화된 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_add_iv = (ImageView) findViewById(R.id.home_add_iv); // 앱바 단어 추가 버튼
        home_flashcard_iv = (ImageView) findViewById(R.id.home_flashcard_iv); // 앱바 플래시카드 버튼

        // 레이아웃
        main_ingWordBox_LinearLayout = (LinearLayout) findViewById(R.id.main_ingWordBox_LinearLayout);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("테스트 ", "onStart()");
        // + 아이콘을 클릭한 경우, 단어 추가 대화상자 띄우기기
        home_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWordDialog();
            }
        });

        // 플래시 카드 아이콘을 클릭한 경우, 데이터 전달(단어, 뜻)과 함께 화면 이동
        home_flashcard_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < wordArray.size(); i++) {
                    Log.d("메인화면 리스트 word: ", wordArray.get(i).word);
                    Log.d("메인화면 리스트 meaning: ", wordArray.get(i).meaning);
                }
                Intent intent = new Intent(getApplicationContext(), SetFlashcardActivity.class);
                intent.putExtra("words", wordArray);
                startActivity(intent);
            }
        });
    }

    // 대화상자 함수
    public void addWordDialog() {
        // 대화상자 레이아웃 연결
        dialogView = (LinearLayout) View.inflate(MainActivity.this, R.layout.dialog_add_word, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setView(dialogView);

        AlertDialog adlg = dlg.create();
        adlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 모서리 둥글게

        adlg.show(); // 대화 상자 출력

        // 확인 버튼
        dialog_confirm_btn = (Button) dialogView.findViewById(R.id.dialog_confirm_btn);
        dialog_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_word_edt = (EditText) dialogView.findViewById(R.id.dialog_word_edt);
                dialog_meaning_edt = (EditText) dialogView.findViewById(R.id.dialog_meaning_edt);
                String word = dialog_word_edt.getText().toString();
                String meaning = dialog_meaning_edt.getText().toString();

                // 중복값을 입력했는지 확인하기 위한 boolean값
                boolean isSame = false;

                // 만약 중복 값을 입력했다면
                for (int i = 0; i < wordArray.size(); i++) {
                    if (wordArray.get(i).word.equals(word)) {
                        Toast.makeText(getApplicationContext(), "이미 추가한 단어입니다", Toast.LENGTH_SHORT).show();
                        isSame = true;
                        break;
                    }
                }

                // 값이 전부 입력되어 있다면 대화 상자 닫기
                if (!isSame) {
                    if (word != null && !word.trim().isEmpty()) { // 널값, 빈값체크
                        if (meaning != null && !meaning.trim().isEmpty()) {
                            adlg.dismiss(); // 대화상자 닫기

                            // 화면에 단어 추가
                            createWordBox(word, meaning);

                            // arraylist에 <단어, 뜻> 저장
                            wordArray.add(new Word(word, meaning));

                            Toast.makeText(getApplicationContext(), "단어를 추가했습니다", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "값을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 취소 버튼
        dialog_cancel_btn = (Button) dialogView.findViewById(R.id.dialog_cancel_btn);
        dialog_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adlg.dismiss(); // 대화상자 닫기
            }
        });
    }

    // 동적으로 단어 박스 생성
    public void createWordBox(@NonNull String word, @NonNull String meaning) {
        // 동적 레이아웃 생성
        ConstraintLayout newLayout; // 레이아웃 변수
        LayoutInflater inflater = getLayoutInflater();
        newLayout = (ConstraintLayout) inflater.inflate(R.layout.item_wordbox, null); // 레이아웃 연결
        newLayout.setId(layoutCount);

        // 객체 연결
        TextView wordTV = newLayout.findViewById(R.id.item_wordbox_word_tv);
        TextView meaningTV = newLayout.findViewById(R.id.item_wordbox_meaning_tv);
        ImageView moreIV = newLayout.findViewById(R.id.item_wordbox_more_iv);

        // 텍스트뷰에 들어갈 문자 설정
        wordTV.setText(word);
        meaningTV.setText(meaning);

        // 레이아웃 클릭 이벤트 -> 대화상자 -> 삭제
        moreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 대화상자 레이아웃 연결
                dialogView = (LinearLayout) View.inflate(MainActivity.this, R.layout.dialog_delete_wordbox, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setView(dialogView);

                AlertDialog adlg = dlg.create();
                adlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 모서리 둥글게

                adlg.show(); // 대화 상자 출력
                dialog_delete_btn = (Button) dialogView.findViewById(R.id.dialog_delete_btn);
                dialog_no_btn = (Button) dialogView.findViewById(R.id.dialog_no_btn);

                // 삭제 버튼을 눌렀다면
                dialog_delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < wordArray.size(); i++) {
                            // 리스트 정보 삭제
                            TextView dataTV = newLayout.findViewById(R.id.item_wordbox_word_tv);
                            String data = dataTV.getText().toString();
                            if (wordArray.get(i).word.equals(data)) {
                                wordArray.remove(i);
                            }
                        }
                        // 현재 리스트 출력
                        for (int i = 0; i < wordArray.size(); i++) {
                            Log.i("삭제한 후 리스트 ", wordArray.get(i).word + " " + wordArray.get(i).meaning);
                        }
                        newLayout.removeAllViews();
                        main_ingWordBox_LinearLayout.removeView(newLayout);

                        adlg.dismiss(); // 대화상자 닫기

                        Toast.makeText(getApplicationContext(), "단어를 삭제했습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                // 취소 버튼을 눌렀다면
                dialog_no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adlg.dismiss(); // 대화 상자 닫기
                    }
                });
            }
        });

        // 동적 레이아웃 설정
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 24;
        newLayout.setLayoutParams(params);

        // 리니어 레이아웃에 동적 레이아웃 추가
        main_ingWordBox_LinearLayout.addView(newLayout);
        layoutCount++; // 레이아웃 아이디(개수) 증가
    }
}