package com.mayhematan.taptheball;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.instructions_dialog );

        Button playBtn = findViewById( R.id.play_btn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent( InstructionsActivity.this, MainActivity.class);

                startActivity(mainIntent);
                finish();
            }
        });
    }
}
