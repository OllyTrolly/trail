package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Oliver on 13/07/2014.
 * Credit goes to javacodegeeks for template code http://www.javacodegeeks.com/2011/07/android-game-development-basic-game_05.html
 */
public class GameLoop extends Thread {

    private static final String TAG = GameLoop.class.getSimpleName(); //Define the tag for logging
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running; // Flag to hold game state

    public GameLoop(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super(); //Calls constructor for new instance of superclass (i.e. public Thread() )
        this.surfaceHolder = surfaceHolder; //Assigns constructor argument to class variable - surfaceHolder allows the app to lock the surface as it draws
        this.gamePanel = gamePanel; //Ditto
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        //long tickCount = 0L;
        Log.d(TAG, "Starting game loop"); //logCat output
        while (running) {
            canvas = null;
            // Try locking the canvas for exclusive pixel editing on the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // Update game state
                    // Draws the canvas on the panel
                    this.gamePanel.onDraw(canvas);
                }
            } finally {
                // In case of an exception the surface is not left in an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        //Log.d(TAG, "Game loop executed " + tickCount + " times");
    }
}
