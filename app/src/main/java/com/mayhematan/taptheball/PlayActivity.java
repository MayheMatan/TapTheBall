package com.mayhematan.taptheball;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;


public class PlayActivity extends AppCompatActivity {
    Ball ball;
    TextView currentXYTV;
    int counter = 0;
    FrameLayout frameLayout;
    StartFragment startfragment;
    SharedPreferences preference;
    int maxY;
    int maxX;
    int difficult;
    String diffToString;
    int ballDiff;
    String userName;
    Integer size;
    MediaPlayer kickSound, gameOverSound, mainSound;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_play);

        kickSound = MediaPlayer.create( PlayActivity.this, R.raw.kicking_ball_sound);
        gameOverSound = MediaPlayer.create( PlayActivity.this, R.raw.game_over);
        mainSound = MediaPlayer.create ( PlayActivity.this, R.raw.jungle );

        /////////get device display data
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;

        ////////////listening to Broadcast
        IntentFilter handlerFilter = new IntentFilter("com.mayhematan.taptheball.HANDLER_STOP");
        IntentFilter startFilter = new IntentFilter("com.mayhematan.taptheball.GAME_BEGIN");
        IntentFilter backToMainFilter = new IntentFilter ("com.mayhematan.taptheball.BACK_TO_MAIN");
        HandlerListener hendlerListener = new HandlerListener();
        LocalBroadcastManager.getInstance(this).registerReceiver(hendlerListener, handlerFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(hendlerListener, startFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(hendlerListener, backToMainFilter);
        ////////////get the high score from preference
        preference = PreferenceManager.getDefaultSharedPreferences( PlayActivity.this);
        startfragment = new StartFragment ();
        getFragmentManager().beginTransaction().replace( R.id.FullFrameLayout, startfragment).commit();
        currentXYTV = findViewById( R.id.currentXYTV);
        currentXYTV.setVisibility(View.INVISIBLE);

        difficult = getIntent ().getIntExtra ( "Level", 0 );
        if (difficult == 1) {
            diffToString = "Easy";
        }
        else if (difficult == 2) {
            diffToString = "Medium";
        }
        else
            diffToString = "Expert";
        ballDiff = getIntent ().getIntExtra ( "BallDiff",0 );
        userName = getIntent ().getStringExtra ( "Name" );
        frameLayout = findViewById( R.id.FullFrameLayout);
        /////////////initialize the random field
        Drawable fields[] = {getDrawable( R.drawable.noob_field),
                             getDrawable( R.drawable.med_field),
                             getDrawable( R.drawable.expert_field)};
        frameLayout.setBackground(fields[new Random().nextInt(fields.length)]);
        /////////////declare the custom ball view
        ball = new Ball ( PlayActivity.this, difficult, ballDiff);
        frameLayout.addView ( ball );
        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getY() >= (maxY / 2) && ball.downYping == ball.yPing) {


                        ////////////while the user touch the ball limits the view goes up
                        if (counter < 10/difficult) {
                            if ((event.getY() - ball.currentY) < ball.largeBallBmp.getHeight() && (event.getY() - ball.currentY) > 0) {
                                float a = event.getX() - ball.currentX;
                                float b = ball.largeBallBmp.getWidth();
                                float sum = b - a;
                                if ((event.getX() - ball.currentX) < ball.largeBallBmp.getWidth() && (event.getX() - ball.currentX) > 0) {
                                    if (sum >= ball.largeBallBmp.getWidth() / 2) {
                                        ball.xPing = ball.downXping;
                                    } else {
                                        ball.xPing = ball.upXping;
                                    }
                                    ball.yPing = ball.upYping;
                                    counter++;
                                    ball.counter++;
                                    currentXYTV.setText("" + counter);

                                    //play sound
                                    try { // also plays when ball drops
                                        if (kickSound.isPlaying()) {
                                            kickSound.stop();
                                            kickSound.release();
                                            kickSound = MediaPlayer.create( PlayActivity.this, R.raw.kicking_ball_sound);
                                        } kickSound.start();
                                    } catch(Exception e) { e.printStackTrace(); }

                                }
                            }

                            ///////////minimize the ball radius
                        } else if (counter >= 10/difficult && counter < 20/difficult) {
                            if ((event.getY() - ball.currentY) < ball.middleBallBmp.getHeight() && (event.getY() - ball.currentY) > 0) {
                                if ((event.getX() - ball.currentX) < ball.middleBallBmp.getWidth() && (event.getX() - ball.currentX) > 0) {
                                    if (ball.middleBallBmp.getWidth() / 2 > (event.getX() - ball.currentX)) {
                                        ball.xPing = ball.downXping;
                                    } else {
                                        ball.xPing = ball.upXping;
                                    }
                                    ball.yPing = ball.upYping;
                                    counter++;
                                    ball.counter++;
                                    currentXYTV.setText("" + counter);
                                }
                            }
                        } else if (counter >= 20/difficult) {
                            if ((event.getY() - ball.currentY) < ball.smallBallBmp.getHeight() && (event.getY() - ball.currentY) > 0) {
                                if ((event.getX() - ball.currentX) < ball.smallBallBmp.getWidth() && (event.getX() - ball.currentX) > 0) {
                                    if (ball.smallBallBmp.getWidth() / 2 >= (event.getX() - ball.currentX)) {
                                        ball.xPing = ball.downXping;
                                    } else {
                                        ball.xPing = ball.upXping;
                                    }
                                    ball.yPing = ball.upYping;
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
                //////////Playing game over sound
                try {
                    if (gameOverSound.isPlaying()) {
                        gameOverSound.stop();
                        gameOverSound.release();
                        gameOverSound = MediaPlayer.create( PlayActivity.this, R.raw.game_over);
                    } gameOverSound.start();
                } catch(Exception e) { e.printStackTrace(); }
                //////////if the current turn of game stop and the user fail
                size = preference.getInt ( "size", 0 );
                int top = preference.getInt("top score", counter);
                int oldCredit = preference.getInt ( "credit", counter );
                int newCredit = oldCredit + counter;
                preference.edit ().putInt("credit", newCredit).apply();
                if (counter >= top) {
                    preference.edit().putInt("top score", counter).apply();
                }
                SharedPreferences.Editor editor = preference.edit();
                switch (ballDiff) {
                    case 0: {
                        Player score = new Player ( BitmapFactory.decodeResource ( getResources (), R.drawable.ball_noob ),
                                getIntent ().getStringExtra ( "Name" ), counter, diffToString );
                        size++;
                        editor.putString ( size.toString (), score.toString () );
                        editor.putInt ( "size", size ).apply ();
                        editor.commit ();
                        break;
                    }
                    case 1: {
                        Player score = new Player ( BitmapFactory.decodeResource ( getResources (), R.drawable.ball_med ),
                                getIntent ().getStringExtra ( "Name" ), counter, diffToString );
                        size++;
                        editor.putString ( size.toString (), score.toString () );
                        editor.putInt ( "size", size ).apply ();
                        editor.commit ();
                        break;
                    }
                    case 2: {
                        Player score = new Player ( BitmapFactory.decodeResource ( getResources (), R.drawable.ball_expert ),
                                getIntent ().getStringExtra ( "Name" ), counter, diffToString );
                        size++;
                        editor.putString ( size.toString (), score.toString () );
                        editor.putInt ( "size", size ).apply ();
                        editor.commit ();
                        break;
                    }
                }
                currentXYTV.setText("0");
                counter = 0;
                ball = new Ball ( PlayActivity.this, difficult, ballDiff);
                frameLayout.addView(ball);
                startfragment = new StartFragment ();
                getFragmentManager().beginTransaction().replace( R.id.FullFrameLayout, startfragment).commit();
            }
            else if (intent.getAction().equals("com.mayhematan.taptheball.GAME_BEGIN")) {
                ////////////initialize the custom ball view to start another turn
                if (maxX < 700 && maxY < 1000) {
                    ball.downXping = 8;
                    ball.downYping = 8;
                    ball.upXping = -8;
                    ball.upYping = -8;
                }
                ball.maxX = maxX;
                ball.maxY = maxY;
                ball.InitializeBall();
                ball.InitializeLine();
                currentXYTV.setVisibility(View.VISIBLE);
            }
            else if (intent.getAction().equals("com.mayhematan.taptheball.BACK_TO_MAIN")) {
                Intent intent1 = new Intent ( PlayActivity.this, MainActivity.class );
                String name = getIntent ().getStringExtra ( "Name" );
                intent1.putExtra ( "Name", name );
                startActivity(intent1);
                finish ();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
