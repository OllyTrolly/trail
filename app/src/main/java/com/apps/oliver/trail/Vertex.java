package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;

public class Vertex {

    private final int x; // The X coordinate
    private final int y; // The Y coordinate
    private final int r; // The radius
    private final int h; // The hit-box
    public int panelWidth;
    public int panelHeight;
    private final Paint paint = new Paint(); // Instantiate paint
    private final Paint paintBorder = new Paint();
    private boolean isActivated;
    private boolean isLocked;
    private final ArrayList<Vertex> conVertices = new ArrayList<>(); // Array of vertices connected to this vertex
    private final ArrayList<Edge> conEdges = new ArrayList<>(); // Array of edges connected to this vertex

    public Vertex(int x, int y, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.x = x;
        this.y = y;
        this.r = (panelWidth * 9) / 200;
        this.h = (panelWidth * 2) / 100;
        isActivated = false;
        paint.setColor(Color.rgb(237, 145, 33));
        paint.setAntiAlias(true);
        paintBorder.setColor(Color.WHITE);
        paintBorder.setAntiAlias(true);
    }

    // Get x co-ordinate of vertex
    public int getX() {
        return x;
    }

    // Get y co-ordinate of vertex
    public int getY() {
        return y;
    }

    // Get radius of vertex
    public int getR() {
        return r;
    }

    // Get hit-box extension of vertex
    public int getH() {
        return h;
    }

    // Toggle activation of vertex
    public void toggleActivation(boolean isActivated) {
        this.isActivated = isActivated;
    }

    // Toggle last selected status of vertex and set colour as appropriate
    public void lastSelected(boolean lastSelected) {
        if(lastSelected) paintBorder.setColor(Color.YELLOW);
        else paintBorder.setColor(Color.WHITE);
    }

    // Set a vertex as connected to this vertex, and the edge connecting them
    public void setConnected(Vertex vertex, Edge edge) {
        conVertices.add(vertex);
        conEdges.add(edge);
    }

    // Get the edge connecting this vertex to the given vertex
    public Edge getEdge(Vertex vertex) {
        for(int i = 0; i < conVertices.size(); i++) {
            if(vertex == conVertices.get(i)) {
                return conEdges.get(i);
            }
        }
        return conEdges.get(0);
    }

    // Check if there are any already activated edges connected to this vertex
    public boolean hasActiveEdge() {
        for (Edge edge : conEdges) {
            if(edge.isActivated()) return true;
        }
        return false;
    }

    // Check if this vertex is connected to the given vertex
    public boolean isConnectedTo(Vertex vertex) {
        for(int i = 0; i < conVertices.size(); i++) {
            if(vertex == conVertices.get(i)) {
                return true;
            }
        }
        return false;
    }

    // Get the number of edges connected to this vertex
    public int numConnected() {
        return conVertices.size();
    }

    // Set this vertex as locked
    public void setLocked() {
        isLocked = true;
    }

    // Check if this vertex is locked
    public boolean isLocked() {
        return isLocked;
    }

    public void draw(Canvas canvas) {
        if(isActivated) {
            //Draw circle border in background
            canvas.drawCircle((float) x, (float) y, (float) r, paintBorder);
            //Draw diminished circle over top
            canvas.drawCircle((float) x, (float) y, (float) r - ((float) panelWidth / 100), paint);
        }
        else {
            canvas.drawCircle((float) x, (float) y, (float) r, paint);
        }
    }

}