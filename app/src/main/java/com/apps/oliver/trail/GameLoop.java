package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Oliver on 13/07/2014.
 */
public class GameLoop extends Thread {

    private SurfaceHolder surfaceHolder; // Surface holder that can access the physical surface
    private GamePanel gamePanel; // The actual view that handles inputs and draws to the surface
    private boolean running; // Flag to hold game state

    public GameLoop(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super(); // Calls constructor for new instance of superclass (i.e. public Thread() )
        this.surfaceHolder = surfaceHolder; // Assigns constructor argument to class variable - surfaceHolder allows the app to lock the surface as it draws
        this.gamePanel = gamePanel;
    }

    // Set thread as running or not running
    public void setRunning(boolean running) {
        this.running = running;
    }

    // The body of the thread
    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = null;
            // Try locking the canvas for exclusive pixel editing on the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // Draws the canvas on the panel
                    if (canvas != null) gamePanel.draw(canvas);
                }
            } finally {
                // In case of an exception the surface is not left in an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
