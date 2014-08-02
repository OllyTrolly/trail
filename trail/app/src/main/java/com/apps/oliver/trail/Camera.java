package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Camera {

    private Bitmap bitmap; // The actual bitmap
    private int x; // The X coordinate
    private int y; // The Y coordinate
    private boolean touched; // If droid is touched/picked up

    public Camera(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

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
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null); // Because coordinates need to be CENTRE of bitmap rather than top left corner (paint is null)
    }

    public void handleActionDown(int eventX, int eventY) {
        if ( (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2)))
                && (eventY >= (y - bitmap.getHeight()/2) && eventY <= (y + bitmap.getWidth()/2)) ) {
            // Camera touched
            setTouched(true);
        }
        else {
            setTouched(false);
        }
    }
}
