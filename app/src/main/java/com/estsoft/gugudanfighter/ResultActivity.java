package com.estsoft.gugudanfighter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        int correctAnswerCount = intent.getIntExtra( "correctAnswerCount", 0 );
        int questionCount = intent.getIntExtra( "questionCount", 0 );

        ((TextView)findViewById( R.id.textViewResultCorrectAnswerCount )).setText( "" + correctAnswerCount );
        ((TextView)findViewById( R.id.textViewResultQuestionCount )).setText( "" + questionCount );

        findViewById( R.id.buttonResultYes ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ResultActivity.this, GameActivity.class );
                startActivity( intent );
                finish();
            }
        });

        findViewById( R.id.buttonResultNo ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
