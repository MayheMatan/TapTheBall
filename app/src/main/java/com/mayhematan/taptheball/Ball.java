package com.mayhematan.taptheball;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;


public class Ball extends View {

    public Bitmap largeBallBmp;
    public float currentY = 0;
    public float currentX = 290;
    Handler handler;
    public float downYping = 18;
    public float downXping = 18;
    public float upXping = -18;
    public float upYping = -18;
    float yPing = downYping;
    float xPing = downXping;
    Context myContext;
    public Bitmap middleBallBmp;
    public Bitmap smallBallBmp;
    public Canvas myCanvas;
    public int counter =0 ;
    Paint paint;
    Path path;
    boolean isInitLine = false;
    public  int maxX = 0;
    public int maxY = 0;
    public int diff;

    public Ball(Context context, int difficult, int ballDiff) {
        super(context);
        diff = difficult;
        //////////initialize the all 3 kind of ball by size
        myContext = context;
        if (ballDiff == 0) {
                largeBallBmp = BitmapFactory.decodeResource ( getResources (), R.drawable.ball_noob );
        }
        else if (ballDiff == 1){
            largeBallBmp = BitmapFactory.decodeResource ( getResources (), R.drawable.ball_med );
        }
        else
            largeBallBmp = BitmapFactory.decodeResource ( getResources (), R.drawable.ball_expert );
        middleBallBmp = Bitmap.createScaledBitmap ( largeBallBmp, largeBallBmp.getWidth () * 3 / 4, largeBallBmp.getHeight () * 3 / 4, false );
        smallBallBmp = Bitmap.createScaledBitmap ( middleBallBmp, middleBallBmp.getWidth () * 3 / 4, middleBallBmp.getHeight () * 3 / 4, false );
    }

    public Ball(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitializeBall();
        InitializeLine();

    }
    public void InitializeLine(){
        ////////////set the limit line that the user can touch the ball
        isInitLine =true;
        paint= new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        path = new Path();
    }
    public void InitializeBall()
    {
        ///////////start the ball movement
        handler = new Handler();
        final Runnable BallFall = new Runnable() {
            @Override
            public void run() {
                currentY += yPing;
                currentX += xPing;
                if (counter < 10){
                    if (currentX > (maxX - largeBallBmp.getWidth())) {
                        xPing = upXping;
                    }
                }
                else if (counter >= 10 && counter < 20){
                    if (currentX > (maxX- middleBallBmp.getWidth())) {
                        xPing = upXping;
                    }
                }
                else if (counter >= 20) {
                    if (currentX > (maxX - smallBallBmp.getWidth())) {
                        xPing = upXping;
                    }
                }
                if (currentY < 0) {
                    yPing = downYping;
                }
                if (currentX < 0) {
                    xPing = downXping;
                }
                if (currentY < maxY) {
                    handler.postDelayed(this,1);
                }else if (maxY < currentY){
                    ////////////if the ball falls from the ended Y the handler is stop and send broadcast
                    Intent intent = new Intent("com.mayhematan.taptheball.HANDLER_STOP");
                    LocalBroadcastManager.getInstance( myContext ).sendBroadcast(intent);
                    handler.removeCallbacks(this);
                    isInitLine = false;
                }
                //TODO check why on game exist tocuh back press send broadcast
                 invalidate();
            }
        };
        xPing = downXping;
        yPing = downYping;
        handler.postDelayed(BallFall,1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ////////////draw the custom ball view all the time that the handler is running
        if (isInitLine) {
            if (diff == 1) {
                path.moveTo ( 0, (float) (maxY / 2) );
                path.lineTo ( maxX, (float) (maxY / 2) );
                canvas.drawPath ( path, paint );
            }
            if (diff == 2) {
                path.moveTo ( 0, (float) (maxY / 1.75) );
                path.lineTo ( maxX, (float) (maxY / 1.75) );
                canvas.drawPath ( path, paint );
            }
            if (diff == 3) {
                path.moveTo ( 0, (float) (maxY / 1.5) );
                path.lineTo ( maxX, (float) (maxY / 1.5) );
                canvas.drawPath ( path, paint );
            }
        }
        myCanvas = canvas;
        if (counter<10/diff) {
            canvas.drawBitmap( largeBallBmp, currentX, currentY, null);
        }else if (counter < 20)
        {
            canvas.drawBitmap( middleBallBmp, currentX, currentY, null);
        }else {
            canvas.drawBitmap( smallBallBmp, currentX, currentY, null);
        }
    }
}
