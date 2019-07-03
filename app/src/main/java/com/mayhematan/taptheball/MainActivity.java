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
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String userName;
    int diff;
    SharedPreferences preference;
    boolean isMusicOn;
    MusicManager musicManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView ( R.layout.activity_main);
        musicManager = MusicManager.getInstance ();

        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_in);
        final Animation buttonAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        final Animation slideRight = AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.slide_in_right );


        final EditText nameEt = findViewById( R.id.nameEt);
        final Button leaderBoard = findViewById ( R.id.leader );
        final TextView appName = findViewById ( R.id.app_nameTv );
        appName.startAnimation(slideRight);
        appName.animate().rotationY(360).setDuration(2500);
        userName = getIntent ().getStringExtra ( "Name");
        nameEt.setText ( userName );
        final Button difficulty[] = {
                findViewById( R.id.easy_btn),
                findViewById( R.id.medium_btn),
                findViewById( R.id.hard_btn)
        };

        for (int i = 0; i < 3; i++) {
            difficulty[i].startAnimation(slideUp);
            leaderBoard.startAnimation(slideUp);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    difficulty[i].startAnimation(buttonAnim);
                }
                nameEt.startAnimation(buttonAnim);
                leaderBoard.startAnimation ( buttonAnim );
            }
        },1500);

        leaderBoard.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( MainActivity.this, LeaderBoardActivity.class );
                startActivity(intent);
            }
        } );


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
                        userName = nameEt.getText().toString();
                        if (!userName.isEmpty()) {
                            Intent intent = new Intent ( MainActivity.this, ChooseActivity.class );
                            intent.putExtra ( "Level", diff );
                            intent.putExtra ( "Name", userName );
                            Bundle extras = new Bundle ();
                            intent.putExtras ( extras );
                            startActivity ( intent );
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity.this, R.style.CustomAlertDialog );
                            View dialogView = getLayoutInflater ().inflate ( R.layout.name_missing_dialog, null );
                            builder.setView ( dialogView ).setCancelable ( false );
                            final AlertDialog dialog = builder.show ();
                            Button backBtn = dialogView.findViewById ( R.id.back_missing );
                            backBtn.setOnClickListener ( new View.OnClickListener () {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss ();
                                }
                            } );
                        }
                    }
                });
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.main_menu, menu );
        preference = PreferenceManager.getDefaultSharedPreferences( MainActivity.this);
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
                    musicManager.stopMusic ();
                    preference.edit ().putBoolean ( "sound", false ).apply ();
                } else {
                    item.setTitle ( getResources ().getString ( R.string.sound_on ) );
                    musicManager.initalizeMediaPlayer ( MainActivity.this, R.raw.jungle );
                    musicManager.startMusic ();
                    preference.edit ().putBoolean ( "sound", true ).apply ();
                }
                break;
            }
            case R.id.instructions: {
                AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity.this, R.style.CustomAlertDialog );
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
        }
        return super.onOptionsItemSelected ( item );
    }
}
