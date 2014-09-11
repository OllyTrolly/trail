package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Score implements Parcelable {

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

    public Score(Parcel in) {
        readFromParcel(in);
        scoreName = "Player";
        textPaint = new Paint();
    }

    public void setTypeface(Typeface tf) {
        textPaint.setTypeface(tf);
    }

    public void addToScore(long score) {
        scoreValue += score;
    }

    public void nameScore(String inputName) {
        scoreName = inputName;
    }

    public void draw(Canvas canvas) {

        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(45);
        //Draw text
        canvas.drawText(scoreValue + "", panelWidth / 2, (panelHeight * 90) / 100, textPaint);
    }

    public void drawFinal(Canvas canvas) {
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(60);
        //Draw text
        canvas.drawText("Final score: ", panelWidth / 2, (panelHeight * 45) / 100, textPaint);
        canvas.drawText(scoreValue + "", panelWidth / 2, (panelHeight * 55) / 100, textPaint);

    }

    public void addToBoard() {
        //Write the score with its name to SQLite
        //Learn to use SQLite of course
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(panelWidth);
        dest.writeInt(panelHeight);
        dest.writeLong(scoreValue);
    }

    private void readFromParcel(Parcel in) {
        panelWidth = in.readInt();
        panelHeight = in.readInt();
        scoreValue = in.readLong();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}
