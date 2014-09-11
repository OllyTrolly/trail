package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import java.text.DecimalFormat;

/**
 * Created by Oliver on 13/07/2014.
 */
public class GameLoop extends Thread {

    //Desired fps
    private static final int MAX_FPS = 50;
    //Max frame skips
    private static final int MAX_FRAME_SKIPS = 5;
    //The frame period
    private static final int FRAME_PERIOD = 1000 / MAX_FPS;

    //Stuff for stats
    private DecimalFormat df = new DecimalFormat("0.##"); //2 dp
    //Reading stats every second (1000 ms)
    private static final int STAT_INTERVAL = 1000;
    //Average will be calculated from 'history' of NR sample size
    private static final int FPS_HISTORY_NR = 10;
    //Last time status was stored
    private long lastStatusStore = 0;
    //The status time counter
    private long statusIntervalTimer = 0l;
    //Number of frames skipped since the game started
    private long totalFramesSkipped = 0l;
    //Number of frames skipped in a store cycle (1 sec)
    private long framesSkippedPerStatCycle = 0l;

    //Number of rendered frames in an interval
    private int frameCountPerStatCycle = 0;
    private long totalFrameCount = 0l;
    //The last FPS values
    private double fpsStore[];
    //The number of times the stat has been read
    private long statsCount = 0;
    //The average FPS since the game started
    private double averageFps = 0.0;

    private static final String TAG = GameLoop.class.getSimpleName(); //Define the tag for logging
    //Surface holder that can access the physical surface
    private SurfaceHolder surfaceHolder;
    //The actual view that handles inputs and draws to the surface
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

        //Initialise timing elements for stat gathering
        initTimingElements();

        long beginTime; //The time when the cycle begun
        long timeDiff; //The time it took for the cycle to execute
        int sleepTime; //ms to sleep (<0 if we're behind)
        int framesSkipped; //Number of frames being skipped

        sleepTime = 0;

        while (running) {
            canvas = null;
            // Try locking the canvas for exclusive pixel editing on the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0; //Resetting frames skipped
                    // Update game state
                    this.gamePanel.update();
                    // Draws the canvas on the panel
                    if (canvas != null) this.gamePanel.render(canvas);
                    //Calculate how long did the cycle take
                    timeDiff = System.currentTimeMillis() - beginTime;
                    //Calculate sleep time
                    sleepTime = (int)(FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0) {
                        //If sleepTime > 0 we're OK
                        try {
                            //Send the thread to sleep for a short period
                            //Very useful for battery saving
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {}
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        //We need to catch up
                        this.gamePanel.update();
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }

                    if (framesSkipped > 0) {
                        //Log.d(TAG, "Skipped: " + framesSkipped);
                    }
                    //For statistics
                    framesSkippedPerStatCycle += framesSkipped;
                    //Calling the routine to store the gathered statistics
                    storeStats();
                }
            } finally {
                // In case of an exception the surface is not left in an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    /**
     * The statistics - it is called every cycle, it checks if time since last
     * store is greater than the statistics gathering period (1 sec) and if so
     * it calculates the FPS for the last period and stores it.
     *
     *  It tracks the number of frames per period. The number of frames since
     *  the start of the period are summed up and the calculation takes part
     *  only if the next period and the frame count is reset to 0.
     */
    private void storeStats() {
        frameCountPerStatCycle++;
        totalFrameCount++;

        // check the actual time
        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
            // calculate the actual frames per status check interval
            double actualFps = (double)(frameCountPerStatCycle / (STAT_INTERVAL / 1000));

            //stores the latest fps in the array
            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

            // increase the number of times statistics was calculated
            statsCount++;

            double totalFps = 0.0;
            // sum up the stored fps values
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            // obtain the average
            if (statsCount < FPS_HISTORY_NR) {
                // in case of the first 10 triggers
                averageFps = totalFps / statsCount;
            } else {
                averageFps = totalFps / FPS_HISTORY_NR;
            }
            // saving the number of total frames skipped
            totalFramesSkipped += framesSkippedPerStatCycle;
            // resetting the counters after a status record (1 sec)
            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;

            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;
//			Log.d(TAG, "Average FPS:" + df.format(averageFps));
            gamePanel.setAvgFps("FPS: " + df.format(averageFps));
        }
    }

    private void initTimingElements() {
        // initialise timing elements
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
            //Log.d(TAG, "i is " + i);
        }
        Log.d(TAG + ".initTimingElements()", "Timing elements for stats initialised");
    }

}
