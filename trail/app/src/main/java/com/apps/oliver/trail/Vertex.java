package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;

public class Vertex implements Parcelable {

    private static final String TAG = Vertex.class.getSimpleName(); //Define the tag for logging
    private int x; // The X coordinate
    private int y; // The Y coordinate
    private int r; // The radius
    private int h; // The hitbox
    public int panelWidth;
    public int panelHeight;
    private Paint paint = new Paint(); // Instantiate paint
    private Paint paintBorder = new Paint();
    private boolean isActivated;
    private boolean isLocked;
    private ArrayList<Vertex> conVertices = new ArrayList<Vertex>();
    private ArrayList<Edge> conEdges = new ArrayList<Edge>();
    private boolean[] bArray = new boolean[2];

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

    public Vertex(Parcel in) {
        readFromParcel(in);
        this.r = (panelWidth * 9) / 200;
        this.h = (panelWidth * 2) / 100;
        paint.setColor(Color.rgb(237, 145, 33)); //Take color as input later on (can change colour scheme this way)
        paint.setAntiAlias(true);
        paintBorder.setColor(Color.WHITE);
        paintBorder.setAntiAlias(true);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(panelWidth);
        dest.writeInt(panelHeight);
        bArray[0] = isActivated;
        bArray[1] = isLocked;
        dest.writeBooleanArray(bArray);
        dest.writeList(conVertices);
        dest.writeList(conEdges);
    }

    private void readFromParcel(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        panelWidth = in.readInt();
        panelHeight = in.readInt();
        in.readBooleanArray(bArray);
        isActivated = bArray[0];
        isLocked = bArray[1];
        in.readList(conVertices, Vertex.class.getClassLoader());
        in.readList(conEdges, Edge.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Vertex createFromParcel(Parcel in) {
            return new Vertex(in);
        }
        public Vertex[] newArray(int size) {
            return new Vertex[size];
        }
    };
}