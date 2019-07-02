package com.mayhematan.taptheball;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseActivity extends AppCompatActivity {
    Button startBtn;
    SharedPreferences preference;
    TextView creditTv, amountMedBall, amountExpBall;
    int credit;
    int ballDiff;
    int isMedBall;
    int isExpBall;
    MediaPlayer mainSound;
    boolean isMusicOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_choose );
        mainSound = MediaPlayer.create ( ChooseActivity.this, R.raw.jungle );
        preference = PreferenceManager.getDefaultSharedPreferences ( ChooseActivity.this );
        ballDiff = 0;

        final String userName = getIntent ().getStringExtra ( "Name" );
        final int difficult = getIntent ().getIntExtra ( "Level", 0 );

        final Animation buttonAnim = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.button_anim );
        final Animation slideIn = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.slide_in_left );
        final Animation slideUp = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.slide_up_in );

        final ImageView ball[] = {
                findViewById ( R.id.noob_ball ),
                findViewById ( R.id.med_ball ),
                findViewById ( R.id.expert_ball )
        };
        startBtn = findViewById ( R.id.start_btn );
        for (int i = 0; i < ball.length; i++)
            ball[i].startAnimation ( slideIn );
        startBtn.startAnimation ( slideUp );

        Handler handler = new Handler ();
        handler.postDelayed ( new Runnable () {
            @Override
            public void run() {
                for (int i = 0; i < ball.length; i++) {
                    ball[i].startAnimation ( buttonAnim );
                }
                startBtn.startAnimation ( buttonAnim );
            }
        }, 1500 );

        creditTv = findViewById ( R.id.credit );
        credit = preference.getInt ( "credit", 0 );
        creditTv.setText ( "" + getResources ().getString ( R.string.credit ) + " " + credit );

        isMedBall = preference.getInt("isMedBall",0);
        isExpBall = preference.getInt ( "isExpBall", 0 );

        amountMedBall = findViewById ( R.id.ball_med_amount );
        amountExpBall = findViewById ( R.id.ball_expert_amount );

        if (isMedBall == 1) {
            amountMedBall.setText ( getResources ().getString ( R.string.purchased ) );
            amountMedBall.setTextSize ( 20 );
        }
        if (isExpBall == 1) {
            amountExpBall.setText ( getResources ().getString ( R.string.purchased ) );
            amountExpBall.setTextSize ( 20 );
        }

        ball[1].setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (amountMedBall.getText ().toString ().equals ( "100" )) {
                    if (credit >= 100) {
                        AlertDialog.Builder builder = new AlertDialog.Builder ( ChooseActivity.this, R.style.CustomAlertDialog );
                        View dialogView = getLayoutInflater ().inflate ( R.layout.purchese_dialog, null );
                        builder.setView ( dialogView ).setCancelable ( false );
                        final AlertDialog dialog = builder.show ();
                        Button yesBtn = dialogView.findViewById ( R.id.yes );
                        yesBtn.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View v) {
                                isMedBall = 1;
                                credit -= 100;
                                amountMedBall.setTextSize ( 20 );
                                amountMedBall.setText (getResources ().getString ( R.string.purchased ));
                                creditTv.setText ( "" + getResources ().getString ( R.string.credit ) + " " + credit );
                                preference.edit ().putInt ( "credit", credit ).apply ();
                                preference.edit ().putInt ( "isMedBall", isMedBall ).apply ();
                                ballDiff = 1;
                                dialog.dismiss ();
                            }
                        } );
                        Button noBtn = dialogView.findViewById ( R.id.no );
                        noBtn.startAnimation ( buttonAnim );
                        noBtn.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss ();
                            }
                        } );
                        Animation animation = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.button_anim );
                        yesBtn.startAnimation ( animation );
                        noBtn.startAnimation ( animation );

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder ( ChooseActivity.this, R.style.CustomAlertDialog );
                        View dialogView = getLayoutInflater ().inflate ( R.layout.no_funds_dialog, null );
                        builder.setView ( dialogView ).setCancelable ( false );
                        final AlertDialog dialog = builder.show ();
                        Button backBtn = dialogView.findViewById ( R.id.back );
                        backBtn.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss ();
                            }
                        } );
                    }
                } else {
                    ballDiff = 1;
                }
            }
        } );

        ball[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountExpBall.getText().toString().equals ("250")) {
                    if(credit >= 250) {
                        AlertDialog.Builder builder = new AlertDialog.Builder( ChooseActivity.this, R.style.CustomAlertDialog);
                        View dialogView = getLayoutInflater().inflate( R.layout.purchese_dialog, null);
                        builder.setView(dialogView).setCancelable(false);
                        final AlertDialog dialog = builder.show();
                        Button yesBtn = dialogView.findViewById ( R.id.yes );
                        yesBtn.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View v) {
                                isExpBall = 1;
                                credit -= 250;
                                amountExpBall.setText(getResources ().getString ( R.string.purchased ));
                                amountExpBall.setTextSize ( 20 );
                                creditTv.setText(""+getResources().getString( R.string.credit) + " "+ credit);
                                preference.edit ().putInt("credit", credit).apply();
                                preference.edit ().putInt ( "isExpBall", isExpBall ).apply ();
                                ballDiff = 2;
                                dialog.dismiss ();
                            }
                        } );
                        Button noBtn = dialogView.findViewById ( R.id.no );
                        noBtn.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss ();
                            }
                        } );
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
                        yesBtn.startAnimation ( animation );
                        noBtn.startAnimation ( animation );
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder( ChooseActivity.this, R.style.CustomAlertDialog);
                        View dialogView = getLayoutInflater().inflate( R.layout.no_funds_dialog, null);
                        builder.setView(dialogView).setCancelable(false);
                        final AlertDialog dialog = builder.show();
                        Button backBtn = dialogView.findViewById ( R.id.back );
                        backBtn.startAnimation ( buttonAnim );
                        backBtn.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss ();
                            }
                        } );
                    }
                }
                else
                    ballDiff = 2;
            }
        } );

        startBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( ChooseActivity.this, PlayActivity.class);
                intent.putExtra ("Level", difficult);
                intent.putExtra ("Name", userName);
                intent.putExtra ("BallDiff", ballDiff);
                startActivity (intent);
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.choose_menu, menu );
        preference = PreferenceManager.getDefaultSharedPreferences( ChooseActivity.this);
        isMusicOn = preference.getBoolean ( "sound", false );
        if (isMusicOn) {
            MenuItem item = menu.findItem ( R.id.sound );
            item.setTitle ( getResources ().getString ( R.string.sound_on ) );
        }
        else {
            MenuItem item = menu.findItem ( R.id.sound );
            item.setTitle ( getResources ().getString ( R.string.sound_off ) );
        }
        return super.onCreateOptionsMenu ( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        switch (id) {
            case R.id.sound: {
                if (item.getTitle ().toString () == getResources ().getString ( R.string.sound_on )) {
                    item.setTitle ( getResources ().getString ( R.string.sound_off ) );
                    mainSound.stop ();
                    preference.edit ().putBoolean ( "sound", false ).apply ();
                } else {
                    item.setTitle ( getResources ().getString ( R.string.sound_on ) );
                    mainSound.release ();
                    mainSound = MediaPlayer.create ( ChooseActivity.this, R.raw.jungle );
                    mainSound.start ();
                    preference.edit ().putBoolean ( "sound", true ).apply ();
                }
                break;
            }
            case R.id.instructions: {
                AlertDialog.Builder builder = new AlertDialog.Builder (  ChooseActivity.this, R.style.CustomAlertDialog );
                View dialogView = getLayoutInflater ().inflate ( R.layout.instructions_dialog, null );
                builder.setView ( dialogView ).setCancelable ( false );
                final AlertDialog dialog = builder.show ();
                Button backBtn = dialogView.findViewById ( R.id.back_btn );
                backBtn.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss ();
                    }
                } );
                break;
            }
            case R.id.switch_player: {
                Intent intent = new Intent ( ChooseActivity.this, MainActivity.class );
                intent.putExtra ( "Name", "" );
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected ( item );
    }
}
