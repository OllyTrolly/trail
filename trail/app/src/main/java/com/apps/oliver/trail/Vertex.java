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
    public int panelWidth;
    public int panelHeight;
    private boolean touched; // If vertex is touched
    Paint paint = new Paint(); // Instantiate paint
    Paint paintBorder = new Paint();
    private boolean isActivated;
    private boolean isLocked;
    ArrayList<Vertex> conVertices = new ArrayList<Vertex>();
    ArrayList<Edge> conEdges = new ArrayList<Edge>();

    public Vertex(int x, int y, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.x = x;
        this.y = y;
        this.r = (panelWidth * 9) / 200;
        this.h = (panelWidth * 2) / 100;
        isActivated = false;
        paint.setColor(Color.rgb(237, 145, 33)); //Take color as input later on (can change colour scheme this way)
        paint.setAntiAlias(true);
        paintBorder.setColor(Color.WHITE);
        paintBorder.setAntiAlias(true);
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

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getH() {
        return h;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void toggleActivation(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public void lastSelected(boolean lastSelected) {
        if(lastSelected) paintBorder.setColor(Color.YELLOW);
        else paintBorder.setColor(Color.WHITE);
    }

    public void draw(Canvas canvas) {
        //Log.d(TAG, "Drawing vertex at x = " + x + ", y = " + y + ", r = " + r);
        if(isActivated) {
            //Draw circle border in background
            canvas.drawCircle((float) x, (float) y, (float) r, paintBorder);
            //Draw diminished circle over top
            canvas.drawCircle((float) x, (float) y, (float) r - ((panelWidth * 1) / 100), paint);
        }
        else {
            canvas.drawCircle((float) x, (float) y, (float) r, paint); // Because coordinates need to be CENTRE of circle rather than top left corner
        }
    }

    public void setConnected(Vertex vertex, Edge edge) {
        conVertices.add(vertex);
        conEdges.add(edge);
    }

    public Edge getEdge(Vertex vertex) {
        for(int i = 0; i < conVertices.size(); i++) {
            if(vertex == conVertices.get(i)) {
                return conEdges.get(i);
            }
        }
        Log.d(TAG, "No connected edge, returning first edge");
        return conEdges.get(0);
    }

    public boolean hasActiveEdge() {
        for (Edge edge : conEdges) {
            if(edge.isActivated()) return true;
        }
        return false;
    }

    public boolean isConnectedTo(Vertex vertex) {
        for(int i = 0; i < conVertices.size(); i++) {
            if(vertex == conVertices.get(i)) {
                return true;
            }
        }
        return false;
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