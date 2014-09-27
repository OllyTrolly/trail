package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 * Credit goes to javacodegeeks for template code http://www.javacodegeeks.com/2011/07/android-game-development-basic-game_05.html
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();
    private GameLoop loop;
    private Graph graph;
    private int stageNo = 1;
    private int gameMode;
    private int resetPos;
    private int backPos;
    private int vertSpace;
    private int h;
    private Bitmap reset;
    private Bitmap back;
    public Paint textPaint = new Paint();
    private int panelWidth;
    private int panelHeight;
    private boolean scoreScreen = false;
    private Typeface tf;
    private GameActivity activity;

    public GamePanel(Context context, Typeface tf, int gameMode, GameActivity activity) {
        super(context);
        this.tf = tf;
        this.gameMode = gameMode;
        this.activity = activity;

        panelWidth = context.getResources().getDisplayMetrics().widthPixels;
        panelHeight = context.getResources().getDisplayMetrics().heightPixels;

        Log.d(TAG, "Panel width is: " + panelWidth);
        Log.d(TAG, "Panel height is: " + panelHeight);

        resetPos = (panelWidth * 85) / 100;
        backPos = (panelWidth * 5) / 100;
        //vertSpace is the vertical co-ord for reset and back
        vertSpace = (panelHeight * 3) / 100;
        h = (panelWidth * 4) / 100;

        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(tf);
        textPaint.setTextAlign(Paint.Align.CENTER);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        reset = BitmapFactory.decodeResource(getResources(), R.drawable.reset);
        back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        graph = new Graph(gameMode, panelWidth, panelHeight, tf, context, activity);
        graph.constructStage(stageNo);
        loop = new GameLoop(getHolder(), this); //Passes the SurfaceHolder and GamePanel class (this) to the loop instance of GameLoop
        setFocusable(true); // Make the GamePanel focusable so it can handle events
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(loop.getState() != GameLoop.State.NEW) {
            loop = new GameLoop(getHolder(), this);
            graph.resumeTimer();
        }

        loop.setRunning(true);
        loop.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Stopping thread on surface destruction");
        graph.pauseTimer();
        loop.setRunning(false);

        boolean retry = true;
        while (retry) {
            try {
                loop.join(); // "Blocks the current Thread (Thread.currentThread()) until the receiver finishes its execution and dies."
                retry = false;
                Log.d(TAG, "Successfully terminated thread");
            } catch (InterruptedException e) {
                //try shutting down the GameLoop thread again
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Finger touches down
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Delegating event handling to the graph
            if (event.getX() < resetPos + (reset.getWidth() / 2) + h && event.getX() > resetPos - (reset.getWidth() / 2) - h &&
                    event.getY() < vertSpace + (reset.getHeight() / 2) + h && event.getY() > vertSpace - (reset.getHeight() / 2) - h) {
                graph.reset();
            }
            else if (event.getX() < backPos + (back.getWidth() / 2) + h && event.getX() > backPos - (back.getWidth() / 2) - h &&
                    event.getY() < vertSpace + (back.getHeight() / 2) + h && event.getY() > vertSpace - (back.getHeight() / 2) - h) {
                activity.checkForAchievements((int) graph.score.getValue(),(int) graph.stageScore.getValue(), stageNo, graph.numEdges);
                activity.pushAccomplishments();
                Intent i = new Intent();
                i.setClass(this.getContext(), MenuActivity.class);
                getContext().startActivity(i);
            }
            else graph.handleActionDown((int) event.getX(), (int) event.getY());
        }
        // Finger moves
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            graph.handleActionMove((int) event.getX(), (int) event.getY());
        }
        // Finger up
        if (event.getAction() == MotionEvent.ACTION_UP) {
            graph.handleActionUp();
            if(graph.stageFinished()) {
                graph.finishStage();
                if (gameMode == 0 || gameMode == 1) {
                    activity.checkForAchievements((int) graph.score.getValue(),(int) graph.stageScore.getValue(), stageNo, graph.numEdges);
                    activity.updateLeaderboards(stageNo);
                    activity.pushAccomplishments();
                    stageNo++;
                    graph.constructStage(stageNo);
                }
                else {
                    activity.checkForAchievements((int) graph.score.getValue(),(int) graph.stageScore.getValue(), stageNo, graph.numEdges);
                    activity.pushAccomplishments();
                    stageNo++;
                    graph.constructStage(stageNo);
                }
            }
        }
        return true;
    }

    public void update() {

    }

    //The fps to be displayed
    private String avgFps;
    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.DKGRAY);
        graph.draw(canvas);
        //Display FPS
        //displayFps(canvas, avgFps);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawBitmap(reset, resetPos, vertSpace, paint);
        textPaint.setTextSize((panelHeight * 4) / 100);
        if (gameMode == 2) {
            canvas.drawText("Tutorial", panelWidth / 2, (panelHeight * 10) / 100, textPaint);
        }
        else {
            canvas.drawText("Stage " + graph.stageNo, panelWidth / 2, (panelHeight * 10) / 100, textPaint);
        }
        canvas.drawBitmap(back, backPos, vertSpace, paint);
        textPaint.setTextSize((panelWidth * 5) / 100);
        canvas.drawText("trail", panelWidth / 2, (panelHeight * 97) / 100, textPaint);
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255,255,255,255);
            paint.setTextSize(20);
            canvas.drawText(fps,panelWidth - 100, panelHeight - 20, paint);
        }
    }

}
