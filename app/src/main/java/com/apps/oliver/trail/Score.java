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
    private String scoreName;
    private Typeface robotoLight;
    private int panelWidth;
    private int panelHeight;
    private Paint textPaint;

    public Score(Typeface robotoLight, int panelWidth, int panelHeight) {
        this.robotoLight = robotoLight;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        scoreValue = 0;
        scoreName = "Player";
        textPaint = new Paint();
    }

    // Add end of stage score to running total scoreValue
    public void addToScore(long score) {
        scoreValue += score;
    }

    // Give a name to the score (was not used in the end)
    public void nameScore(String inputName) {
        scoreName = inputName;
    }

    // Draw score text
    public void draw(Canvas canvas) {
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(45);
        canvas.drawText(scoreValue + "", panelWidth / 2, (panelHeight * 90) / 100, textPaint);
    }

    // Draw final score screen
    public void drawFinal(Canvas canvas) {
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(60);
        canvas.drawText("Final score: ", panelWidth / 2, (panelHeight * 45) / 100, textPaint);
        canvas.drawText(scoreValue + "", panelWidth / 2, (panelHeight * 55) / 100, textPaint);
    }
}
