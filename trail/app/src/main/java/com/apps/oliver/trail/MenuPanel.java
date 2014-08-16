package com.apps.oliver.trail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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

    //Set common co-ordinates
    int horizCentre = 360;
    int circle1Vert = 350;
    int circle2Vert = circle1Vert + 250;
    int circle3Vert = circle2Vert + 250;
    int rectRadius = 20;
    int circleRadius = 90;

    public MenuPanel(Context context, Typeface robotoLight) {
        super(context);
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
        canvas.save();
        canvas.rotate(-45);
        canvas.drawRect(horizCentre, circle1Vert - rectRadius, 750, circle1Vert + rectRadius, rectPaint);
        canvas.restore();
        //Second line
        canvas.drawRect(horizCentre - rectRadius, circle2Vert, horizCentre + rectRadius, circle3Vert, rectPaint);
        //Third line
        rectPaint.setColor(Color.GRAY);
        canvas.save();
        canvas.rotate(45);
        canvas.drawRect(0, circle2Vert - rectRadius, horizCentre, circle2Vert + rectRadius, rectPaint);
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
        canvas.drawText("Timed", horizCentre, circle1Vert + 15, textPaint);
        canvas.drawText("Endless", horizCentre, circle2Vert + 15, textPaint);
        canvas.drawText("Scores", horizCentre, circle3Vert + 15, textPaint);
        textPaint.setColor(Color.LTGRAY);
        canvas.drawText("trail", horizCentre, 1150, textPaint);
        textPaint.setTextSize(50);
        canvas.drawText("Menu", horizCentre, 150, textPaint);
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
