package com.mayhematan.taptheball;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class FirstActivity extends AppCompatActivity {

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_first);

        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_in);
        final Animation buttonAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        final Animation slideRight = AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.slide_in_right );

        Button playBtn = findViewById( R.id.play_btn);
        Button highScoresBtn = findViewById( R.id.high_scores_btn);
        Button instructionsBtn = findViewById( R.id.instructions_btn);
        ImageView logoIv = findViewById ( R.id.logo );
        logoIv.setAnimation ( slideRight );
        logoIv.animate().rotationY(360).setDuration(2500);

        final Button mainMenu[] = {
                playBtn,
                highScoresBtn,
                instructionsBtn,
        };

        for (int i = 0; i < mainMenu.length; i++) {
            mainMenu[i].startAnimation(slideUp);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mainMenu.length; i++) {
                    mainMenu[i].startAnimation(buttonAnim);
                }
            }
        },1500);

        //start the game
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent( FirstActivity.this, MainActivity.class);

                startActivity(mainIntent);
            }
        });

        //open high scores window
        highScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LeaderboardIntent = new Intent( FirstActivity.this, LeaderBoardActivity.class);

                startActivity(LeaderboardIntent);
            }
        });


        //open instructions window
        instructionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder ( FirstActivity.this, R.style.CustomAlertDialog );
                View dialogView = getLayoutInflater ().inflate ( R.layout.instructions_dialog, null );
                builder.setView ( dialogView ).setCancelable ( false );
                dialog = builder.show ();
                Button playBtn = dialog.findViewById ( R.id.play_btn );
                playBtn.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent instructionsIntent = new Intent( FirstActivity.this, MainActivity.class);
                        startActivity(instructionsIntent);
                        dialog.dismiss ();
                    }
                });
            }
        });
    }
}
