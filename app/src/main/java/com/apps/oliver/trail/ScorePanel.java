package com.apps.oliver.trail;

import android.content.Context;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Oliver on 16/08/2014.
 */
public class ScorePanel extends SurfaceView implements
            SurfaceHolder.Callback {
        private static final String TAG = MenuPanel.class.getSimpleName();
        private Typeface robotoLight;
        private SurfaceHolder surfaceHolder;

    public ScorePanel(Context context, Typeface robotoLight) {
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

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
