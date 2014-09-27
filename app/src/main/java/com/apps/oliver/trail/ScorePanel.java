package com.apps.oliver.trail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Oliver on 16/08/2014.
 */
public class ScorePanel extends SurfaceView implements
            SurfaceHolder.Callback {
    private Typeface robotoLight;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private ArrayList<ScoreBoardXmlParser.Score> scores = new ArrayList<ScoreBoardXmlParser.Score>();
    private int panelWidth;
    private int panelHeight;
    private int backPos;
    private int vertSpace;
    private int h;
    private int stageNumber;
    private Bitmap back;
    private boolean highScore;

    public ScorePanel(Context context, Typeface robotoLight, int stageNumber, boolean highScore) {
        super(context);
        this.context = context;
        this.stageNumber = stageNumber;
        this.robotoLight = robotoLight;
        this.highScore = highScore;

        panelWidth = context.getResources().getDisplayMetrics().widthPixels;
        panelHeight = context.getResources().getDisplayMetrics().heightPixels;

        backPos = (panelWidth * 5) / 100;
        vertSpace = (panelHeight * 3) / 100;
        h = (panelWidth * 2) / 100;
        back = BitmapFactory.decodeResource(getResources(), R.drawable.back);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this); // Adding the callback (this) to the surface holder to intercept events
        setFocusable(true); // Make the MenuPanel focusable so it can handle events
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Lock user interaction with the canvas
        Canvas canvas = surfaceHolder.lockCanvas();
        //Set background colour
        canvas.drawColor(Color.DKGRAY);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(50);
        canvas.drawText("Game Over", panelWidth / 2, (panelHeight * 45) / 100, textPaint);
        textPaint.setTextSize(40);
        canvas.drawText("You completed " + stageNumber + " stages", panelWidth / 2, (panelHeight * 55) / 100, textPaint);

        if(highScore) {
            textPaint.setColor(Color.GREEN);
            canvas.drawText("You set a high score!", panelWidth / 2, (panelHeight * 65) / 100, textPaint);
        }

        textPaint.setTextSize(36);
        textPaint.setColor(Color.LTGRAY);
        canvas.drawText("trail", panelWidth / 2, (panelHeight * 97) / 100, textPaint);

        canvas.drawBitmap(back, backPos, (panelHeight * 3) / 100, null);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < backPos + (back.getWidth() / 2) + h && event.getX() > backPos - (back.getWidth() / 2) - h &&
                    event.getY() < vertSpace + (back.getHeight() / 2) + h && event.getY() > vertSpace - (back.getHeight() / 2) - h) {
                Intent i = new Intent();
                i.setClass(this.getContext(), MenuActivity.class);
                getContext().startActivity(i);
            }
        }
        return true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
