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
        paint.setTextSize(70);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
    }

    public int getTimeLeft() {
        int timeElapsed = (int) ((System.nanoTime() - startTime)/1000000000);
        timeLeft = timerSecs - timeElapsed;
        return timeLeft;
    }

    public void draw(Canvas canvas) {
        int timeElapsed = (int) ((System.nanoTime() - startTime)/1000000000);
        timeLeft = timerSecs - timeElapsed;
        int minsLeft = timeLeft / 60;
        int secsLeft = timeLeft % 60;
        if(timeLeft <= 0) {
            canvas.drawText("Too slow!", 360, 850, paint);
        }
        else if(secsLeft < 10) {
            canvas.drawText(minsLeft + ":0" + secsLeft, 360, 850, paint);
        }
        else canvas.drawText(minsLeft + ":" + secsLeft, 360, 850, paint);
    }
}
