package com.apps.oliver.trail;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Created by Oliver on 13/07/2014.
 */

public class Timer{

    private int timerSecs;
    public long startTime;
    public int timeLeft;
    private Paint paint;

    public Timer(int timerSecs, Typeface robotoLight) {
        startTime = System.nanoTime();
        this.timerSecs = timerSecs;
        paint = new Paint();
        paint.setTypeface(robotoLight);
        paint.setColor(Color.LTGRAY);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
    }

    public boolean draw(Canvas canvas) {
        int timeElapsed = (int) ((System.nanoTime() - startTime)/1000000000);
        timeLeft = timerSecs - timeElapsed;
        if(timeLeft <= 0) {
            canvas.drawText("Too slow!", 360, 850, paint);
            return false;
        }
        canvas.drawText(timeLeft + "", 360, 850, paint);
        return true;
    }
}
