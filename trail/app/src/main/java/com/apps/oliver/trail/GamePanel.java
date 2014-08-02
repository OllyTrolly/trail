package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 * Credit goes to javacodegeeks for template code http://www.javacodegeeks.com/2011/07/android-game-development-basic-game_05.html
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();
    private GameLoop loop;
    private Vertex[] vertexArray;
    private Canvas canvas;
    private Graph graph;
    private int gameMode = 0; //Placeholder value
    private int stageNo = 1;
    private int difficultyLevel = 1;
    private Score gameScore;


    public GamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        //camera = new Camera(BitmapFactory.decodeResource(getResources(), R.drawable.android_camera), 50, 50);
        graph = new Graph(gameMode,stageNo);
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

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            // Delegating event handling to the camera
            camera.handleActionDown((int)event.getX(), (int)event.getY());

            // Check if in the lower part of the screen we exit
            if(event.getY() > getHeight() - 300) { //Checking press was in lowest 300 pixels, (0,0) is at top left corner
                loop.setRunning(false);
                ((Activity)getContext()).finish(); //Effectively exit application by telling the main activity to finish.
            }
            else {
                Log.d(TAG, "Co-ords: x=" + event.getX() + ",y=" + event.getY());
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // The gestures
            if (camera.isTouched()) {
                // The droid was picked up and is being dragged
                camera.setX((int) event.getX());
                camera.setY((int) event.getY());
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // Touch was released
            if (camera.isTouched()) {
                camera.setTouched(false);
            }
        }
        return true;
    }
    */
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            // Delegating event handling to the vertex
            vertex.handleActionDown((int)event.getX(), (int)event.getY());
            Log.d(TAG, "Co-ords: x=" + event.getX() + ",y=" + event.getY());
            /*
            // Check if in the lower part of the screen we exit
            if(event.getY() > getHeight() - 300) { //Checking press was in lowest 300 pixels, (0,0) is at top left corner
                loop.setRunning(false);
                ((Activity)getContext()).finish(); //Effectively exit application by telling the main activity to finish.
            }
            else {
                Log.d(TAG, "Co-ords: x=" + event.getX() + ",y=" + event.getY());
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // The gestures
            if (vertex.isTouched()) {
                // The dot was picked up and is being dragged
                vertex.setX((int) event.getX());
                vertex.setY((int) event.getY());
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // Touch was released
            if (vertex.isTouched()) {
                vertex.setTouched(false);
            }
        }
        return true;
    }
    */

    @Override
    protected void onDraw(Canvas canvas) {
        // Fills the canvas with black
        canvas.drawColor(Color.DKGRAY);
        graph.draw(canvas);
    }
}
