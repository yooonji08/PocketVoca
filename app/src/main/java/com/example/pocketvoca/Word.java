package com.example.pocketvoca;

import java.io.Serializable;

// arrayList를 위한 단어 데이터 클래스
public class Word implements Serializable {
    String word;
    String meaning;

    // 생성자
    Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }
}
