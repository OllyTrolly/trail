package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Timer {

    private final int timerSecs;
    public long startTime;
    private long pauseTime;
    public int timeLeft;
    private final Paint paint;
    private final int panelWidth;
    private final int panelHeight;
    private long offset = 0;

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

    // Calculate the time left on the timer in seconds
    public int getTimeLeft() {
        int timeElapsed = (int) ((System.nanoTime() - startTime - offset)/1000000000);
        timeLeft = timerSecs - timeElapsed;
        return timeLeft;
    }

    // Pause the timer by recording the time
    public void pauseTimer() {
        pauseTime = System.nanoTime();
    }

    // Resume the timer by calculating time difference since pause and offsetting it
    public void resumeTimer() {
        offset = System.nanoTime() - pauseTime;
    }

    public void draw(Canvas canvas) {
        // Get time left and changes into minutes and seconds to display
        timeLeft = getTimeLeft();
        int minsLeft = timeLeft / 60;
        int secsLeft = timeLeft % 60;
        // Change display to 'Too slow!' if all the time has elapsed
        if(timeLeft <= 0) {
            paint.setColor(Color.RED);
            canvas.drawText("Too slow!", (float) panelWidth / 2, (float) (panelHeight * 23) / 100, paint);
            paint.setColor(Color.LTGRAY);
        }
        else if(secsLeft < 10) {
            canvas.drawText(minsLeft + ":0" + secsLeft, (float) panelWidth / 2, (float) (panelHeight * 23) / 100, paint);
        }
        else canvas.drawText(minsLeft + ":" + secsLeft, (float) panelWidth / 2, (float) (panelHeight * 23) / 100, paint);
    }
}
