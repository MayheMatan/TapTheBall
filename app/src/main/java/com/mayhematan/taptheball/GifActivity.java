package com.mayhematan.taptheball;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GifActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.gif_activity );

        ImageView gifIv = findViewById ( R.id.gif );
        Glide.with ( GifActivity.this ).asGif ().load ( R.raw.kicking_mov).into ( gifIv );

        Handler handler = new Handler (  );
        handler.postDelayed ( new Runnable () {
            @Override
            public void run() {
                Intent intent = new Intent ( GifActivity.this, FirstActivity.class );
                startActivity(intent);
            }
        } ,2000);
    }
}

