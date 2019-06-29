package com.mayhematan.taptheball;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseActivity extends AppCompatActivity {
    Button startBtn;
    SharedPreferences preference;
    TextView creditTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        preference = PreferenceManager.getDefaultSharedPreferences(ChooseActivity.this);

        final String userName = getIntent ().getStringExtra ( "Name" );
        final int difficult = getIntent ().getIntExtra ( "Level",0 );

        final Animation buttonAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        final Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_in);

        final ImageView ball[] = {
                findViewById(R.id.noob_ball),
                findViewById(R.id.med_ball),
                findViewById(R.id.expert_ball)
        };
        startBtn = findViewById(R.id.start_btn);
        for (int i = 0; i<ball.length; i++)
            ball[i].startAnimation(slideIn);
        startBtn.startAnimation(slideUp);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < ball.length; i++) {
                    ball[i].startAnimation(buttonAnim);
                }
                startBtn.startAnimation(buttonAnim);
            }
        },1500);

        creditTv = findViewById(R.id.credit);
        int credit = preference.getInt("credit", 0);
        creditTv.setText(""+getResources().getString(R.string.credit) + " " + credit);

        TextView amountMedBall = findViewById (R.id.ball_med_amount);
        TextView amountExpBall = findViewById (R.id.ball_expert_amount);

        ball[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //  if ()
            }
        } );

        startBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ChooseActivity.this, PlayActivity.class);
                intent.putExtra ("Level", difficult);
                intent.putExtra ("Name", userName);
                Bundle extras = new Bundle ();
                intent.putExtras (extras);
                startActivity (intent);
            }
        } );

    }
}
