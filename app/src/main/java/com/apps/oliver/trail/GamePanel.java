package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 */
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

public class GamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private GameLoop loop;
    private Graph graph;
    private int stageNo = 1;
    private int resetPos;
    private int backPos;
    private int vertSpace;
    private int h;
    private Bitmap reset;
    private Bitmap back;
    private Paint textPaint = new Paint();
    private int panelWidth;
    private int panelHeight;
    private boolean scoreScreen = false;

    public GamePanel(Context context, Typeface tf, int gameMode) {
        super(context);

        // Getting width and height of panel for scaling purposes
        panelWidth = context.getResources().getDisplayMetrics().widthPixels;
        panelHeight = context.getResources().getDisplayMetrics().heightPixels;

        resetPos = (panelWidth * 90) / 100;  // Horizontal position of reset button
        backPos = (panelWidth * 5) / 100; // Horizontal position of back button
        vertSpace = (panelHeight * 3) / 100; // vertSpace is the vertical co-ord for reset and back
        h = (panelWidth * 2) / 100; // The hitbox extension of an object's already existing dimensions

        // Setting relevant properties for the text's paint
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(tf);
        textPaint.setTextAlign(Paint.Align.CENTER);

        getHolder().addCallback(this);  // Adding the callback (this) to the surface holder to intercept events
        reset = BitmapFactory.decodeResource(getResources(), R.drawable.reset); // Adding/assigning reset button's bitmap
        back = BitmapFactory.decodeResource(getResources(), R.drawable.back); // Adding/assigning back button's bitmap
        graph = new Graph(gameMode, stageNo, panelWidth, panelHeight, tf);
        graph.constructStage();
        loop = new GameLoop(getHolder(), this);
        setFocusable(true); // Make the GamePanel focusable so it can handle events

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    // On surface creation, start loop, if resuming create new loop and resume stage's timer
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(loop.getState() != GameLoop.State.NEW) {
            loop = new GameLoop(getHolder(), this);
            graph.resumeTimer();
        }

        loop.setRunning(true);
        loop.start();
    }

    // On surface destruction, pause the stage's timer, tell loop to stop running
    // and wait for it to finish by itself.
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        graph.pauseTimer();
        loop.setRunning(false);

        boolean retry = true;
        while (retry) {
            try {
                loop.join(); // "Blocks the current Thread (Thread.currentThread()) until the receiver finishes its execution and dies."
                retry = false;
            } catch (InterruptedException e) {
                // Try shutting down the GameLoop thread again
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Check if touch event was at back button, if so start MenuActivity
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < backPos + (back.getWidth() / 2) + h && event.getX() > backPos - (back.getWidth() / 2) - h &&
                    event.getY() < vertSpace + (back.getHeight() / 2) + h && event.getY() > vertSpace - (back.getHeight() / 2) - h) {
                Intent i = new Intent();
                i.setClass(this.getContext(), MenuActivity.class);
                getContext().startActivity(i);
            }
        }

        // All other elements are not present on the final score screen
        if(!scoreScreen) {
            // Check if touch event was at reset button
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getX() < resetPos + (reset.getWidth() / 2) + h && event.getX() > resetPos - (reset.getWidth() / 2) - h &&
                        event.getY() < vertSpace + (reset.getHeight() / 2) + h && event.getY() > vertSpace - (reset.getHeight() / 2) - h) {
                    graph.reset();
                }
                // If not at back or reset, delegate event handling to the graph
                else graph.handleActionDown((int) event.getX(), (int) event.getY());
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // Delegate event handling to the graph
                graph.handleActionMove((int) event.getX(), (int) event.getY());
            }
            // Check if graph stage and/or mode has finished on action up
            // and take relevant action
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (graph.stageFinished()) {
                    if (graph.modeFinished()) {
                        //Exit to menu and/or call up scoring
                        scoreScreen = true;
                    }
                    graph.constructStage();
                }

            }
        }
        return true;
    }

    public void draw(Canvas canvas) {

        canvas.drawColor(Color.DKGRAY);
        if(scoreScreen) {
            // Draw final score screen after mode finish
            graph.score.drawFinal(canvas);
        }

        // Draw graph, icons and text during stages
        else {
            graph.draw(canvas);
            canvas.drawBitmap(reset, resetPos, vertSpace, null);
            textPaint.setTextSize(50);
            canvas.drawText("Stage " + graph.stageNo, panelWidth / 2, (panelHeight * 10) / 100, textPaint);
        }

        canvas.drawBitmap(back, backPos, vertSpace, null);
        textPaint.setTextSize(36);
        canvas.drawText("trail", panelWidth / 2, (panelHeight * 97) / 100, textPaint);
    }

}
