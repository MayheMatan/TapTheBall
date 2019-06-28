package com.mayhematan.taptheball;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class PlayActivity extends AppCompatActivity {
    Ball ball;
    TextView currentXYTV;
    int counter = 0;
    FrameLayout FullFrameLauout;
    StartFragment startfragment;
    SharedPreferences preference;
    int maxY;
    int maxX;
    int difficult;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_play );

        /////////get device dis[lay data
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;
        ////////////listening to Broadcast
        IntentFilter Handlerfilter = new IntentFilter("com.mayhematan.taptheball.HANDLER_STOP");
        IntentFilter Startfilter = new IntentFilter("com.mayhematan.taptheball.GAME_BEGIN");
        HandlerListener hendlerlistener = new HandlerListener();
        LocalBroadcastManager.getInstance(this).registerReceiver(hendlerlistener, Handlerfilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(hendlerlistener, Startfilter);
        ////////////get the high score from preference
        preference = PreferenceManager.getDefaultSharedPreferences(PlayActivity.this);
        startfragment = new StartFragment();
        getFragmentManager().beginTransaction().replace(R.id.FullFrameLauout, startfragment).commit();
        currentXYTV = (TextView) findViewById(R.id.CurrentXYTV);
        currentXYTV.setVisibility(View.INVISIBLE);
        difficult = getIntent ().getIntExtra ( "Level", 0 );
        FullFrameLauout = (FrameLayout) findViewById(R.id.FullFrameLauout);


        /////////////declare the custom ball view
        ball = new Ball(PlayActivity.this, difficult);
        FullFrameLauout.addView ( ball );
        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getY() >= (maxY / 2) && ball.DownYping == ball.Yping) {
                        ////////////while the user touch the ball limits the view goes up
                        if (counter < 10/difficult) {
                            if ((event.getY() - ball.currentY) < ball.LargeBallBmp.getHeight() && (event.getY() - ball.currentY) > 0) {
                                float a = event.getX() - ball.currentX;
                                float b = ball.LargeBallBmp.getWidth();
                                float sum = b - a;
                                if ((event.getX() - ball.currentX) < ball.LargeBallBmp.getWidth() && (event.getX() - ball.currentX) > 0) {
                                    if (sum >= ball.LargeBallBmp.getWidth() / 2) {
                                        ball.Xping = ball.DownXping;
                                    } else {
                                        ball.Xping = ball.UpXping;
                                    }
                                    ball.Yping = ball.UpYping;
                                    counter++;
                                    ball.counter++;
                                    currentXYTV.setText("" + counter);
                                }
                            }
                            ///////////minimize the ball radius
                        } else if (counter >= 10/difficult && counter < 20/difficult) {
                            if ((event.getY() - ball.currentY) < ball.MiddleBallBmp.getHeight() && (event.getY() - ball.currentY) > 0) {
                                if ((event.getX() - ball.currentX) < ball.MiddleBallBmp.getWidth() && (event.getX() - ball.currentX) > 0) {
                                    if (ball.MiddleBallBmp.getWidth() / 2 > (event.getX() - ball.currentX)) {
                                        ball.Xping = ball.DownXping;
                                    } else {
                                        ball.Xping = ball.UpXping;
                                    }
                                    ball.Yping = ball.UpYping;
                                    counter++;
                                    ball.counter++;
                                    currentXYTV.setText("" + counter);
                                }
                            }
                        } else if (counter >= 20/difficult) {
                            if ((event.getY() - ball.currentY) < ball.SmallBallBmp.getHeight() && (event.getY() - ball.currentY) > 0) {
                                if ((event.getX() - ball.currentX) < ball.SmallBallBmp.getWidth() && (event.getX() - ball.currentX) > 0) {
                                    if (ball.SmallBallBmp.getWidth() / 2 >= (event.getX() - ball.currentX)) {
                                        ball.Xping = ball.DownXping;
                                    } else {
                                        ball.Xping = ball.UpXping;
                                    }
                                    ball.Yping = ball.UpYping;
                                    counter++;
                                    ball.counter++;
                                    currentXYTV.setText("" + counter);
                                }
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    class HandlerListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.mayhematan.taptheball.HANDLER_STOP")) {
                //////////if the current turn of game stop and the user fail
                int top = preference.getInt("top score", counter);
                if (counter >= top) {
                    preference.edit().putInt("top score", counter).apply();
                }
                currentXYTV.setText("0");
                counter = 0;
                ball = new Ball(PlayActivity.this, difficult);
                FullFrameLauout.addView(ball);
                getFragmentManager().beginTransaction().replace(R.id.FullFrameLauout, startfragment).commit();
            }
            else if (intent.getAction().equals("com.mayhematan.taptheball.GAME_BEGIN")) {
                ////////////initialize the custom ball view to start another turn
                if (maxX < 700 && maxY < 1000) {
                    ball.DownXping = 8;
                    ball.DownYping = 8;
                    ball.UpXping = -8;
                    ball.UpYping = -8;
                }
                ball.maxX = maxX;
                ball.maxY = maxY;
                ball.InitializeBall();
                ball.InitializeLine();
                currentXYTV.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
