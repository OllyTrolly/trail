package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 * Credit goes to javacodegeeks for template code http://www.javacodegeeks.com/2011/07/android-game-development-basic-game_05.html
 */
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
    public boolean isHighScore;
    public int highScore;

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
        vertSpace = (panelHeight * 4) / 100;
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
        if (loop.getState() != GameLoop.State.NEW) {
            if (gameMode == 0) {
                if (graph.timer.getTimeLeft() <= 0) {
                    Intent i = new Intent();
                    i.setClass(this.getContext(), MenuActivity.class);
                    getContext().startActivity(i);
                }
            }
            else if (gameMode == 1) {
                if (graph.mistakesLeft <= 0) {
                    Intent i = new Intent();
                    i.setClass(this.getContext(), MenuActivity.class);
                    getContext().startActivity(i);
                }
            }
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
            if (event.getX() < panelWidth && event.getX() > (panelWidth * 80) / 100 &&
                    event.getY() < (panelHeight * 10) / 100 && event.getY() > 0) {
                graph.reset();
            }
            else if (event.getX() < (panelWidth * 20) / 100 && event.getX() > 0 &&
                    event.getY() < (panelHeight * 10) / 100 && event.getY() > 0) {
                activity.checkForAchievements(graph.timer.getTimeLeft(), stageNo, graph.numEdges);
                activity.pushAccomplishments();
                if (gameMode == 0 || gameMode == 1) {
                    graph.score.addToBoard();
                }
                activity.finish();
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
                    activity.checkForAchievements(graph.timer.getTimeLeft(), stageNo, graph.numEdges);
                    activity.updateLeaderboards(stageNo);
                    activity.pushAccomplishments();
                    graph.score.setScoreValue(stageNo);
                    stageNo++;
                    graph.constructStage(stageNo);
                }
                else {
                    activity.checkForAchievements(graph.timer.getTimeLeft(), stageNo, graph.numEdges);
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

    public boolean failedGame(Canvas canvas) {
        if(gameMode == 0) {
            graph.timer.draw(canvas);
            if(!graph.launchingActivity) {
                if (graph.timer.timeLeft <= 0) {
                    graph.launchingActivity = true;
                    activity.checkForAchievements(graph.timer.getTimeLeft(), stageNo, graph.numEdges);
                    activity.updateLeaderboards(stageNo - 1);
                    activity.pushAccomplishments();
                    Intent i = new Intent();
                    if (graph.score.isHighScore()) {
                        graph.score.addToBoard();
                        isHighScore = true;
                        highScore = stageNo - 1;
                        //Add high score to intent as extra so it can be highlighted
                    }
                    else {
                        isHighScore = false;
                        highScore = (int) graph.score.getHighScore();
                    }
                    return true;
                }
            }
        }

        else if (gameMode == 1) {
            if (!graph.launchingActivity) {
                graph.mistakesLeft = 10 - graph.penalty;
                if (graph.mistakesLeft <= 0) {
                    graph.launchingActivity = true;
                    activity.checkForAchievements(graph.timer.getTimeLeft(), stageNo, graph.numEdges);
                    activity.updateLeaderboards(stageNo - 1);
                    activity.pushAccomplishments();
                    graph.mistakesLeft = 0;
                    if (graph.score.isHighScore()) {
                        graph.score.addToBoard();
                        isHighScore = true;
                        highScore = stageNo - 1;
                        //Add high score to intent as extra so it can be highlighted
                    }
                    else {
                        isHighScore = false;
                        highScore = (int) graph.score.getHighScore();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    //The fps to be displayed
    private String avgFps;
    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }

    public void render(Canvas canvas) {

        if(!scoreScreen) {
            scoreScreen = failedGame(canvas);
            canvas.drawColor(Color.DKGRAY);
            graph.draw(canvas);
            //Display FPS
            //displayFps(canvas, avgFps);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawBitmap(reset, resetPos, vertSpace, paint);
            textPaint.setTextSize((panelHeight * 5) / 100);
            if (gameMode == 2) {
                canvas.drawText("Tutorial", panelWidth / 2, (panelHeight * 9) / 100, textPaint);
            } else {
                canvas.drawText("Stage " + graph.stageNo, panelWidth / 2, (panelHeight * 9) / 100, textPaint);
            }
            canvas.drawBitmap(back, backPos, vertSpace, paint);
            textPaint.setTextSize((panelWidth * 5) / 100);
            canvas.drawText("trail", panelWidth / 2, (panelHeight * 97) / 100, textPaint);
        }
        else {
            canvas.drawColor(Color.DKGRAY);

            textPaint.setAntiAlias(true);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.LTGRAY);
            textPaint.setTextSize((panelHeight * 5) / 100);
            canvas.drawText("Game Over", panelWidth / 2, (panelHeight * 40) / 100, textPaint);
            textPaint.setTextSize((panelHeight * 4) / 100);
            canvas.drawText("You completed " + stageNo + " stages", panelWidth / 2, (panelHeight * 50) / 100, textPaint);

            if(isHighScore) {
                textPaint.setColor(Color.GREEN);
                canvas.drawText("You set a high score!", panelWidth / 2, (panelHeight * 60) / 100, textPaint);
                textPaint.setColor(Color.LTGRAY);
            }

            else {
                textPaint.setColor(Color.RED);
                canvas.drawText("Previous high score: " + highScore, panelWidth / 2, (panelHeight * 60) / 100, textPaint);
                textPaint.setColor(Color.LTGRAY);
            }

            textPaint.setTextSize(36);
            textPaint.setColor(Color.LTGRAY);
            canvas.drawText("trail", panelWidth / 2, (panelHeight * 97) / 100, textPaint);

            canvas.drawBitmap(back, backPos, (panelHeight * 4) / 100, null);
        }
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
