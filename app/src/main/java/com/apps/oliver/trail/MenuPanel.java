package com.apps.oliver.trail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
    private MenuActivity activity;

    int horizCentre;
    int firstColumn;
    int secondColumn;
    int circle1Vert;
    int circle2Vert;
    int circle3Vert;
    int rectRadius;
    int circleRadius;
    int panelWidth;
    int panelHeight;

    public MenuPanel(Context context, Typeface robotoLight, MenuActivity activity) {
        super(context);
        this.activity = activity;

        panelWidth = context.getResources().getDisplayMetrics().widthPixels;
        panelHeight = context.getResources().getDisplayMetrics().heightPixels;

        horizCentre = panelWidth / 2;
        firstColumn = (panelWidth * 30) / 100;
        secondColumn = (panelWidth * 70) / 100;
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
        //First line
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(60, horizCentre, circle1Vert);
        canvas.drawRect(horizCentre, circle1Vert - rectRadius, horizCentre + ((panelHeight * 22) / 100), circle1Vert + rectRadius, rectPaint);
        canvas.restore();
        //Second line
        canvas.drawRect(firstColumn - rectRadius, circle2Vert, firstColumn + rectRadius, circle3Vert, rectPaint);
        canvas.drawRect(horizCentre, circle1Vert - rectRadius, panelWidth, circle1Vert + rectRadius, rectPaint);
        //Third line
        rectPaint.setColor(Color.GRAY);
        rectPaint.setAlpha(150);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(320, secondColumn, circle2Vert);
        canvas.drawRect((float) (0 - (0.75*horizCentre)), circle2Vert - rectRadius, secondColumn, circle2Vert + rectRadius, rectPaint);
        canvas.restore();
        //Fourth line
        canvas.drawRect(firstColumn, circle3Vert - rectRadius, secondColumn, circle3Vert + rectRadius, rectPaint);
        //Set paint for drawing dots
        Paint dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
        //"Tutorial" - yellow dot
        dotPaint.setColor(Color.rgb(246, 240, 67));
        canvas.drawCircle(horizCentre, circle1Vert, circleRadius, dotPaint);
        //"Timed" - orange dot
        dotPaint.setColor(Color.rgb(237, 145, 33));
        canvas.drawCircle(firstColumn, circle2Vert, circleRadius, dotPaint);
        //"Endless" - green dot
        dotPaint.setColor(Color.rgb(115, 220, 90));
        canvas.drawCircle(secondColumn, circle2Vert, circleRadius, dotPaint);
        //"Scores" - purple dot
        dotPaint.setColor(Color.rgb(240, 120, 240));
        canvas.drawCircle(firstColumn, circle3Vert, circleRadius, dotPaint);
        //"Trophies" - turquoise dot
        dotPaint.setColor(Color.rgb(66, 228, 222));
        canvas.drawCircle(secondColumn, circle3Vert, circleRadius, dotPaint);
        //Set paint for text
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize((panelWidth * 5) / 100);
        //Draw text
        canvas.drawText("Tutorial", horizCentre, circle1Vert + (panelHeight * 1) / 100, textPaint);
        canvas.drawText("Timed", firstColumn, circle2Vert + (panelHeight * 1) / 100, textPaint);
        canvas.drawText("Endless", secondColumn, circle2Vert + (panelHeight * 1) / 100, textPaint);
        canvas.drawText("Scores", firstColumn, circle3Vert + (panelHeight * 1) / 100, textPaint);
        canvas.drawText("Trophies", secondColumn, circle3Vert + (panelHeight * 1) / 100, textPaint);
        textPaint.setColor(Color.LTGRAY);
        canvas.drawText("trail", horizCentre, (panelHeight * 97) / 100, textPaint);
        textPaint.setTextSize((panelHeight * 4) / 100);
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
            //Tutorial
            if(dotSelection(horizCentre, circle1Vert, x, y)) {
                Intent i = new Intent();
                i.setClass(this.getContext(), GameActivity.class);
                i.putExtra("GAME_MODE", 2);
                getContext().startActivity(i);
            }
            //Timed
            else if(dotSelection(firstColumn, circle2Vert, x, y)) {
                Intent i = new Intent();
                i.setClass(this.getContext(), GameActivity.class);
                i.putExtra("GAME_MODE", 0);
                getContext().startActivity(i);
            }
            //Endless
            else if(dotSelection(secondColumn, circle2Vert, x, y)) {
                Intent i = new Intent();
                i.setClass(this.getContext(), GameActivity.class);
                i.putExtra("GAME_MODE", 1);
                getContext().startActivity(i);
            }
            //Scores
            else if(dotSelection(firstColumn, circle3Vert, x, y)) {
                activity.onSignInButtonClicked();
                activity.onShowLeaderboardsRequested();
                /*
                Intent i = new Intent();
                i.setClass(this.getContext(), ScoreActivity.class);
                i.putExtra("HIGH_SCORE", 10);
                getContext().startActivity(i);
                */
            }
            //Trophies
            else if(dotSelection(secondColumn, circle3Vert, x, y)) {
                activity.onSignInButtonClicked();
                activity.onShowAchievementsRequested();
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

        }
        return true;
    }

    private boolean dotSelection(int horiz, int vert, int eventX, int eventY) {
        if((eventX >= (horiz - circleRadius) &&
                (eventX <= (horiz + circleRadius)))
                && (eventY >= (vert - circleRadius) &&
                (eventY <= vert + circleRadius))) {
            return true;
        }
        return false;
    }

}
