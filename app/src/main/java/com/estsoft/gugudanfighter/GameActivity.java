package com.estsoft.gugudanfighter;

import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[] answerButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // 게임 초기화
        initGame();

    }

    @Override
    public void onClick(View v) {
        newQuestion();
    }

    // 게임 초기화 하는 메서드
    private void initGame() {
        // 답 버튼 위젯 ID 저장 배열
        final int[] answerButtonIds = {
                R.id.button_0_0, R.id.button_0_1, R.id.button_0_2,
                R.id.button_1_0, R.id.button_1_1, R.id.button_1_2,
                R.id.button_2_0, R.id.button_2_1, R.id.button_2_2
        };

        // 답 버튼의 개수
        final int answerButtonCount = answerButtonIds.length;

        // 답 버튼 위젯 객체 저장 배열 생성
        answerButtons = new Button[ answerButtonCount ];

        // 답 버튼 위젯 저장 / 클릭 이벤트 리스너 설정
        for (int i = 0; i < answerButtonCount; i++) {
            answerButtons[i] = (Button) findViewById(answerButtonIds[i]);
            answerButtons[i].setOnClickListener(this);
        }

        // 첫 문제 제출
        newQuestion();
    }

    // 새문제 만드는 메서드
    private void newQuestion() {
        // 답의 개수
        final int answerCount = answerButtons.length;

        // 세팅된 답 배열 ( 중복 체크을 위해서 )
        int[] answers = new int[ answerCount ];

        // 세팅 시작
        for( int i = 0; i <  answerCount; i++ ) {
            final int operandLeft = randomize( 1, 9 );
            final int operandRight  = randomize( 1, 9 );
            final int result = operandLeft * operandRight;

            // 중복 검사
            boolean isExist = intArrayContains( answers, result );
            if( isExist ) {
                continue;
            }

            answers[ i ] = result;
            answerButtons[ i ].setText( "" + result );
        }

        // 답 세팅을 위한 랜덤 순서
        int randomOrderAnswer = randomize( 0, answerCount );
    }

    // int 배열에 값이 있는 검사하는 메서드
    private boolean intArrayContains( int[] array, int val ) {
        for( final int i : array ) {
            if( i == val ) {
                return true;
            }
        }

        return false;
    }

    // 랜덤 정수 만드는 메세드
    private int randomize( int from, int to ) {
        return (int)( Math.random() * to ) + from;
    }


}
