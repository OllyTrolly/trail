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
    private int panelWidth;
    private int panelHeight;

    public Timer(int timerSecs, Typeface robotoLight, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
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
            paint.setColor(Color.RED);
            canvas.drawText("Too slow!", panelWidth / 2, (panelHeight * 23) / 100, paint);
            paint.setColor(Color.LTGRAY);
        }
        else if(secsLeft < 10) {
            canvas.drawText(minsLeft + ":0" + secsLeft, panelWidth / 2, (panelHeight * 23) / 100, paint);
        }
        else canvas.drawText(minsLeft + ":" + secsLeft, panelWidth / 2, (panelHeight * 23) / 100, paint);
    }
}
