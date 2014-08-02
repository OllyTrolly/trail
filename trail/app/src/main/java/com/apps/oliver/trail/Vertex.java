package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import java.util.ArrayList;

public class Vertex {

    private static final String TAG = Vertex.class.getSimpleName(); //Define the tag for logging
    private int x; // The X coordinate
    private int y; // The Y coordinate
    private int r; // The radius
    private int h; // The hitbox
    private boolean touched; // If vertex is touched
    Paint paint = new Paint(); // Instantiate paint
    private boolean isActivated;
    private boolean isLocked;
    ArrayList<Vertex> conVertices = new ArrayList<Vertex>();

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
        this.r = 25;
        this.h = 15;
        isActivated = false;
        paint.setColor(Color.rgb(237, 145, 33)); //Take color as input later on (can change colour scheme this way)
        paint.setAntiAlias(true);
    }
/*
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
*/
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void draw(Canvas canvas) {
        //Log.d(TAG, "Drawing vertex at x = " + x + ", y = " + y + ", r = " + r);
        canvas.drawCircle((float)(x),(float)(y),(float) r, paint); // Because coordinates need to be CENTRE of circle rather than top left corner
    }

    public void handleActionDown(int eventX, int eventY) {
        if ( (eventX >= (x - r - h) && (eventX <= (x + r + h)))
                && (eventY >= (y - r - h) && eventY <= (y + r + h)) ) {
            // Vertex touched
            setTouched(true);
        }
        else {
            setTouched(false);
        }
    }

    public void setConnected(Vertex vertex) {
        conVertices.add(vertex);
    }

    public boolean isNotConnected(Vertex vertex) {
        for(int i = 0; i < conVertices.size(); i++) {
            if(vertex == conVertices.get(i)) {
                return false;
            }
        }
        return true;
    }

    public int numConnected() {
        return conVertices.size();
    }

    public void setLocked() {
        isLocked = true;
    }

    public boolean isLocked() {
        return isLocked;
    }
}