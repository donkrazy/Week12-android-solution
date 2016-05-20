package com.estsoft.gugudanfighter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int correctAnswer;
    private int correctAnswerCount;
    private int questionCount;

    private Button[] answerButtons;
    private TextView tvScore;
    private TextView tvLastTime;

    private Timer timer = new Timer();
    private static final int TIME_LIMITS = 30; //seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // 게임 초기화
        initGame();
    }

    @Override
    public void onClick(View v) {
        // Button 타입이 아닐 수 없지만, 변경 될 수 있으니깐...
        if( v instanceof Button == false ) {
            return;
        }

        String textButton = ((Button) v).getText().toString();

        // 0 또는 양의 정수가 아니면...
        if( textButton.matches("\\d+") == false ) {
            return;
        }

        int answer = Integer.parseInt( textButton );
        if( answer == correctAnswer ) {
            correctAnswerCount++;
        }
        updateScore();
        newQuestion();
    }

    // 게임 중 back button 를 누른 경우
    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
        //super.onBackPressed();
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
        answerButtons = new Button[answerButtonCount];

        // 답 버튼 위젯 저장 / 클릭 이벤트 리스너 설정
        for (int i = 0; i < answerButtonCount; i++) {
            answerButtons[i] = (Button) findViewById(answerButtonIds[i]);
            answerButtons[i].setOnClickListener(this);
        }

        // TextView 위젯 세팅 / 초기화
        tvScore = (TextView) findViewById(R.id.textViewScore);
        tvLastTime = (TextView) findViewById(R.id.textViewLastTime);

        updateScore();
        updateLastTime(TIME_LIMITS);

        // 첫 문제 제출
        newQuestion();

        // 타이머 시작
        timer.schedule(new PlayGameTimerTask(), 1000, 1000);
    }

    // 스코어를 화면에 엡데이트 하는 메서드
    private void updateScore() {
        tvScore.setText(correctAnswerCount + "/" + questionCount);
    }

    // 남은 시간을 화면에 업데이트 하는 메서드
    private void updateLastTime(int lastTime) {
        tvLastTime.setText("" + lastTime);
    }

    // 새 문제 만드는 메서드
    private void newQuestion() {
        // 답의 개수
        final int answerCount = answerButtons.length;

        // 답 과 문제 세팅을 위한 랜덤 순서
        int randomOrderAnswer = randomize(0, answerCount );

        // 세팅된 답 배열 ( 중복 체크을 위해서 )
        int[] answers = new int[answerCount];

        // 세팅 시작
        int index = 0;
        while ( index < answerCount ) {
            final int operandLeft = randomize(1, 9);
            final int operandRight = randomize(1, 9);
            final int result = operandLeft * operandRight;

            // 중복 검사
            boolean isExist = intArrayContains(answers, result);
            if (isExist) {
                continue;
            }

            answers[index] = result;
            answerButtons[index].setText("" + result);

            // 답과 문제 세팅
            if( randomOrderAnswer == index ) {
                correctAnswer = result;
                ( (TextView) findViewById(R.id.textViewLeftOperand ) ).setText( "" + operandLeft );
                ( (TextView) findViewById(R.id.textViewRightOperand ) ).setText( "" + operandRight );
            }
            index++;
        }
        // 문제 수 증가
        questionCount++;
    }

    // int 배열에 값이 있는 검사하는 메서드
    private boolean intArrayContains(int[] array, int val) {
        for (final int i : array) {
            if (i == val) {
                return true;
            }
        }
        return false;
    }

    // 랜덤 정수 만드는 메세드
    private int randomize(int from, int to) {
        return (int) (Math.random() * to) + from;
    }

    private class PlayGameTimerTask extends TimerTask {
        private int seconds;

        @Override
        public void run() {
            if (seconds >= TIME_LIMITS) {
                // 게임 종료
                timer.cancel();

                // ResultActivity로 이동
                Intent intent = new Intent( GameActivity.this, ResultActivity.class );
                intent.putExtra( "correctAnswerCount",  correctAnswerCount );
                intent.putExtra( "questionCount",  questionCount );
                startActivity( intent );

                // Activity 종료
                finish();
                return;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateLastTime(TIME_LIMITS - ++seconds);
                }
            });
        }
    }
}
