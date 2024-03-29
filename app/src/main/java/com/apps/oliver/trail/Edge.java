package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Edge {

    // Connected vertices
    private Vertex vertexA;
    private Vertex vertexB;
    private float aX;
    private float aY;
    private float bX;
    private float bY;
    private float smallX;
    private float smallY;
    private float bigX;
    private float bigY;
    private float rectRadius;
    private int rotation;
    private Paint paint = new Paint(); // Instantiate paint
    private boolean isActivated;
    private boolean lastSelected;
    private float diagEdgeLength;

    public Edge(Vertex vertexA, Vertex vertexB) {
        // Setting up properties of deactivated vertices as connected to each other
        this.vertexA = vertexA;
        vertexA.setConnected(vertexB, this);
        this.vertexB = vertexB;
        vertexB.setConnected(vertexA, this);
        isActivated = false;

        rectRadius = (vertexA.panelWidth * 2) / 100;
        paint.setColor(Color.GRAY); // Take color as input later on (can change colour scheme this way)
        paint.setAlpha(100);
        paint.setAntiAlias(true);
        diagEdgeLength = (vertexA.panelWidth * 51) / 200;  // Pre-calculated length of a diagonal edge

        // Shorthands for x and y co-ordinates of each vertex
        aX = vertexA.getX();
        aY = vertexA.getY();
        bX = vertexB.getX();
        bY = vertexB.getY();

        // Finding shortest x and y co-ordinates for rectangle creation
        if (aX < bX) {smallX = aX; bigX = bX;}
        else {smallX = bX; bigX = aX;}
        if (aY < bY) {smallY = aY; bigY = bY;}
        else {smallY = bY; bigY = aY;}

        // Finding needed canvas angle of rotation to draw rectangle
        if(aY < bY) {
            if(aX < bX) rotation = 45;
            else rotation = 135;
        }
        else {
            if (bX < aX) rotation = 360 - 135;
            else rotation = 360 - 45;
        }
    }

    // Toggle whether edge is activated or not
    public void toggleActivation(boolean isActivated) {
        this.isActivated = isActivated;
        updatePaint();
    }

    // Toggle whether edge is last selected or not
    public void lastSelected(boolean lastSelected) {
        this.lastSelected = lastSelected;
        updatePaint();
    }

    // Update paint color and alpha to reflect current edge state
    public void updatePaint() {
        if(lastSelected) {
            paint.setColor(Color.YELLOW);
            paint.setAlpha(150);
        }
        else if(isActivated) {
            paint.setColor(Color.LTGRAY);
            paint.setAlpha(255);
        }
        else {
            paint.setColor(Color.GRAY);
            paint.setAlpha(100);
        }
    }

    // Return state of activation
    public boolean isActivated() {
        return isActivated;
    }

    // Draw edge, depends on angle between the two vertices
    public void draw(Canvas canvas) {
        //Horizontal
        if(vertexA.getY() == vertexB.getY()) {
            canvas.drawRect(smallX, smallY - rectRadius, bigX, smallY + rectRadius, paint);
        }

        //Vertical
        else if(vertexA.getX() == vertexB.getX()) {
            canvas.drawRect(smallX - rectRadius, smallY, smallX + rectRadius, bigY, paint);
        }

        //Diagonal
        else {
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate(rotation, aX, aY);
            canvas.drawRect(aX, aY - rectRadius, aX + diagEdgeLength, aY + rectRadius, paint);
            canvas.restore();
        }
    }
}
