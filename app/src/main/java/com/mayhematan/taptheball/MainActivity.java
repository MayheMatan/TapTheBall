package com.mayhematan.taptheball;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_in);

        EditText nameEt = findViewById(R.id.nameEt);
        userName = nameEt.getText().toString();
        Button difficulty[] = {
                findViewById(R.id.easy_btn),
                findViewById(R.id.medium_btn),
                findViewById(R.id.hard_btn)
        };
        for(int i=0;i<3;i++) {
            difficulty[i].setAnimation(slideUp);
        }

        for(int i=0;i<3;i++){
            difficulty[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String diff = "";
                    if((v).getId() == R.id.easy_btn){
                        diff = "Easy";
                    }
                    else if((v).getId() == R.id.medium_btn){
                        diff = "Medium";
                    }
                    else if((v).getId() == R.id.hard_btn){
                        diff = "Hard";
                    }

                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    intent.putExtra("Level", diff);
                    intent.putExtra("Name", userName);
                    Bundle extras = new Bundle();
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        }

    }
}
