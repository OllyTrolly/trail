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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();
    private GameLoop loop;
    private Graph graph;
    //private int gameMode; //Placeholder value
    private int stageNo = 1;
    private int resetPos = 650;
    private int backPos = 20;
    private int h = 15;
    private Bitmap reset;
    private Bitmap back;
    public Paint textPaint = new Paint();

    public GamePanel(Context context, Typeface robotoLight, int gameMode) {
        super(context);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextAlign(Paint.Align.CENTER);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        reset = BitmapFactory.decodeResource(getResources(), R.drawable.reset);
        back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        graph = new Graph(gameMode,stageNo, robotoLight);
        loop = new GameLoop(getHolder(), this); //Passes the SurfaceHolder and GamePanel class (this) to the loop instance of GameLoop
        setFocusable(true); // Make the GamePanel focusable so it can handle events
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        loop.setRunning(true);
        loop.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                loop.join(); // "Blocks the current Thread (Thread.currentThread()) until the receiver finishes its execution and dies."
                retry = false;
            } catch (InterruptedException e) {
                //try shutting down the GameLoop thread again
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            // Delegating event handling to the graph
            if(event.getX() < resetPos + (reset.getWidth() / 2) + h && event.getX() > resetPos - (reset.getWidth() / 2) - h &&
                    event.getY() < 20 + (reset.getHeight() / 2) + h && event.getY() > 20 - (reset.getHeight() / 2) - h) {
                graph.reset();
            }
            if(event.getX() < backPos + (back.getWidth() / 2) + h && event.getX() > backPos - (back.getWidth() / 2) - h &&
                    event.getY() < 20 + (back.getHeight() / 2) + h && event.getY() > 20 - (back.getHeight() / 2) - h) {
                Intent i = new Intent();
                i.setClass(this.getContext(), MenuActivity.class);
                getContext().startActivity(i);
            }
            else graph.handleActionDown((int) event.getX(), (int) event.getY());
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            graph.handleActionMove((int) event.getX(), (int) event.getY());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            graph.handleActionUp((int) event.getX(), (int) event.getY());
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
        canvas.drawBitmap(reset, resetPos, 20, null);
        canvas.drawBitmap(back, backPos, 20, null);
        textPaint.setTextSize(36);
        canvas.drawText("trail", 360, 1150, textPaint);
        textPaint.setTextSize(50);
        canvas.drawText("Stage " + graph.stageNo, 360, 120, textPaint);
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255,255,255,255);
            paint.setTextSize(20);
            canvas.drawText(fps,this.getWidth() - 100, 20, paint);
        }
    }
}
