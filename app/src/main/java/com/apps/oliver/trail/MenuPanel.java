package com.apps.oliver.trail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Oliver on 15/08/2014.
 */
public class MenuPanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = MenuPanel.class.getSimpleName();
    private Typeface robotoLight;
    private SurfaceHolder surfaceHolder;

    int horizCentre;
    int circle1Vert;
    int circle2Vert;
    int circle3Vert;
    int rectRadius;
    int circleRadius;
    int panelWidth;
    int panelHeight;

    public MenuPanel(Context context, Typeface robotoLight) {
        super(context);

        panelWidth = context.getResources().getDisplayMetrics().widthPixels;
        panelHeight = context.getResources().getDisplayMetrics().heightPixels;

        horizCentre = panelWidth / 2;
        circle1Vert = (panelHeight * 30) / 100;
        circle2Vert = (panelHeight * 50) / 100;
        circle3Vert = (panelHeight * 70) / 100;
        rectRadius = panelWidth / 36;
        circleRadius = (panelHeight * 8) / 100;


        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this); // Adding the callback (this) to the surface holder to intercept events
        this.robotoLight = robotoLight;
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
        //Draw angled rectangles by rotating canvas each time
        Paint rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.LTGRAY);
        //rectPaint.setAlpha(150);
        //First line
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(40, horizCentre, circle1Vert);
        canvas.drawRect(horizCentre, circle1Vert - rectRadius, (float) (panelWidth + (0.75*horizCentre)), circle1Vert + rectRadius, rectPaint);
        canvas.restore();
        //Second line
        canvas.drawRect(horizCentre - rectRadius, circle2Vert, horizCentre + rectRadius, circle3Vert, rectPaint);
        //Third line
        rectPaint.setColor(Color.GRAY);
        rectPaint.setAlpha(150);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(320, horizCentre, circle2Vert);
        canvas.drawRect((float) (0 - (0.75*horizCentre)), circle2Vert - rectRadius, horizCentre, circle2Vert + rectRadius, rectPaint);
        canvas.restore();
        //Fourth line
        canvas.drawRect(0, circle1Vert - rectRadius, horizCentre, circle1Vert + rectRadius, rectPaint);
        //Set paint for drawing dots
        Paint dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
        //"Timed" - orange dot
        dotPaint.setColor(Color.rgb(237, 145, 33));
        canvas.drawCircle(horizCentre, circle1Vert, circleRadius, dotPaint);
        //"Endless" - green dot
        dotPaint.setColor(Color.rgb(115, 220, 90));
        canvas.drawCircle(horizCentre, circle2Vert, circleRadius, dotPaint);
        //"Scores" - purple dot
        dotPaint.setColor(Color.rgb(240, 120, 240));
        canvas.drawCircle(horizCentre, circle3Vert, circleRadius, dotPaint);
        //Set paint for text
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(36);
        //Draw text
        canvas.drawText("Timed", horizCentre, circle1Vert + (panelHeight * 1) / 100, textPaint);
        canvas.drawText("Endless", horizCentre, circle2Vert + (panelHeight * 1) / 100, textPaint);
        canvas.drawText("Scores", horizCentre, circle3Vert + (panelHeight * 1) / 100, textPaint);
        textPaint.setColor(Color.LTGRAY);
        canvas.drawText("trail", horizCentre, (panelHeight * 97) / 100, textPaint);
        textPaint.setTextSize(50);
        canvas.drawText("Menu", horizCentre, (panelHeight * 10) / 100, textPaint);
        //Unlock user interaction with the canvas
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if(dotSelection(circle1Vert, x, y)) {
                Intent i = new Intent();
                i.setClass(this.getContext(), GameActivity.class);
                i.putExtra("GAME_MODE", 0);
                getContext().startActivity(i);
            }
            else if(dotSelection(circle2Vert, x, y)) {
                Intent i = new Intent();
                i.setClass(this.getContext(), GameActivity.class);
                i.putExtra("GAME_MODE", 1);
                getContext().startActivity(i);
            }
            else if(dotSelection(circle3Vert, x, y)) {
                Intent i = new Intent();
                i.setClass(this.getContext(), ScoreActivity.class);
                i.putExtra("HIGH_SCORE", 10);
                getContext().startActivity(i);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

        }
        return true;
    }

    private boolean dotSelection(int vert, int eventX, int eventY) {
        if((eventX >= (horizCentre - circleRadius) &&
                (eventX <= (horizCentre + circleRadius)))
                && (eventY >= (vert - circleRadius) &&
                (eventY <= vert + circleRadius))) {
            return true;
        }
        return false;
    }

}
