package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Edge {

    private static final String TAG = Edge.class.getSimpleName(); //Define the tag for logging
    private Vertex vertexA;
    private Vertex vertexB;
    private float smallX;
    private float smallY;
    private float bigX;
    private float bigY;
    private float r;
    RectF rect;
    Paint paint = new Paint(); // Instantiate paint
    private boolean isActivated;
    private boolean lastSelected;

    public Edge(Vertex vertexA, Vertex vertexB) {
        this.vertexA = vertexA;
        vertexA.setConnected(vertexB, this);
        this.vertexB = vertexB;
        vertexB.setConnected(vertexA, this);
        isActivated = false;
        //constructRectangle();
        paint.setColor(Color.WHITE); //Take color as input later on (can change colour scheme this way)
        paint.setAntiAlias(true);
        r = 45;
    }

    private void constructRectangle() {
        if(vertexA.getX() <= vertexB.getX()) {
            smallX = vertexA.getX();
            bigX = vertexB.getX();
        }
        else {
            smallX = vertexB.getX();
            bigX = vertexA.getX();
        }

        if(vertexA.getY() <= vertexB.getY()) {
            smallY = vertexA.getY();
            bigY = vertexB.getY();
        }
        else {
            smallY = vertexB.getY();
            bigY = vertexA.getY();
        }
        rect = new RectF(smallX, smallY, bigX, bigY);
    }

    public void toggleActivation(boolean isActivated) {
        this.isActivated = isActivated;
        if(isActivated) {
            paint.setColor(Color.RED);
            Log.d(TAG, "Setting edge as activated (red)");
        }
        else {
            paint.setColor(Color.WHITE);
            Log.d(TAG, "Setting edge as deactivated (white)");
        }
    }

    public void lastSelected(boolean lastSelected) {
        this.lastSelected = lastSelected;
        if(lastSelected) {
            paint.setColor(Color.YELLOW);
            Log.d(TAG, "Setting edge as last selected (yellow)");
        }
        else if(isActivated) paint.setColor(Color.RED);
        else paint.setColor(Color.WHITE);
    }

    public boolean isLastSelected() {
        return lastSelected;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(vertexA.getX(), vertexA.getY(), vertexB.getX(), vertexB.getY(), paint);
    }
}
