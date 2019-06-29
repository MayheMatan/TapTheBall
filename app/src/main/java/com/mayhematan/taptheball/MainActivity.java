package com.mayhematan.taptheball;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    String userName;
    int diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_in);
        final Animation buttonAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);


        final EditText nameEt = findViewById(R.id.nameEt);
        userName = nameEt.getText().toString();

        final Button difficulty[] = {
                findViewById(R.id.easy_btn),
                findViewById(R.id.medium_btn),
                findViewById(R.id.hard_btn)
        };

        for (int i = 0; i < 3; i++) {
            difficulty[i].startAnimation(slideUp);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    difficulty[i].startAnimation(buttonAnim);
                }
                nameEt.startAnimation(buttonAnim);
            }
        },1500);


            for (int i = 0; i < 3; i++) {
                difficulty[i].setOnClickListener(new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        if ((v).getId () == R.id.easy_btn) {
                            diff = 1;
                        } else if ((v).getId () == R.id.medium_btn) {
                            diff = 2;
                        } else if ((v).getId () == R.id.hard_btn) {
                            diff = 3;
                        }

                        Intent intent = new Intent (MainActivity.this, ChooseActivity.class);
                        intent.putExtra ("Level", diff);
                        intent.putExtra ("Name", userName);
                        Bundle extras = new Bundle ();
                        intent.putExtras (extras);
                        startActivity (intent);
                    }
                });
            }
        }
    }
