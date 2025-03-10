package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Score {

    private long scoreValue;
    private final Typeface robotoLight;
    private final int panelWidth;
    private final int panelHeight;
    private final Paint textPaint;

    public Score(Typeface robotoLight, int panelWidth, int panelHeight) {
        this.robotoLight = robotoLight;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        scoreValue = 0;
        textPaint = new Paint();
    }

    // Add end of stage score to running total scoreValue
    public void addToScore(long score) {
        scoreValue += score;
    }

    // Draw score text
    public void draw(Canvas canvas) {
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(45);
        canvas.drawText(scoreValue + "", (float) panelWidth / 2, (float) (panelHeight * 90) / 100, textPaint);
    }

    // Draw final score screen
    public void drawFinal(Canvas canvas) {
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(60);
        canvas.drawText("Final score: ", (float) panelWidth / 2, (float) (panelHeight * 45) / 100, textPaint);
        canvas.drawText(scoreValue + "", (float) panelWidth / 2, (float) (panelHeight * 55) / 100, textPaint);
    }
}
