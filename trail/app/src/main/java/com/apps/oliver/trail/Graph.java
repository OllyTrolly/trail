package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.util.Log;
import android.widget.RatingBar;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Graph {

    private int difficultyLevel; //Minimum value 1, maximum value 5
    private int vRows; //Minimum value 3, maximum value provisionally 6
    private int vColumns; //Minimum value 3, maximum value provisionally 6
    private boolean eulCircuit; //True if Euler circuit, false if non-circuit Euler trail
    private boolean incomplete;
    private ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();
    private Vertex[] vertexArray;
    private Score score;
    private Timer timer;
    private int stageNo;
    private int midPoint;
    private int spacing;
    private int randNum;
    private int origin;
    private int limit;
    private static final String TAG = Graph.class.getSimpleName(); //Define the tag for logging

    public Graph(int gameMode, int stageNo) {
        this.stageNo = stageNo;
        //score.addToScore(0); //Add as display element later
        switch (gameMode){ //Switch statement
            case 0: timedMode();
                break;
            case 1: endlessMode();
                break;
        }
    }

    public void timedMode() {
        switch (stageNo) { //difficultyLevel switch should make it easy to modify difficulty curve later on and add or take away number of stages
            case 1:
                difficultyLevel = 1;
                break;
            case 2:
                difficultyLevel = 1;
                break;
            case 3:
                difficultyLevel = 2;
                break;
            case 4:
                difficultyLevel = 2;
                break;
            case 5:
                difficultyLevel = 3;
                break;
            case 6:
                difficultyLevel = 3;
                break;
            case 7:
                difficultyLevel = 4;
                break;
            case 8:
                difficultyLevel = 4;
                break;
            case 9:
                difficultyLevel = 5;
                break;
            case 10:
                difficultyLevel = 5;
                break;
        }

        switch (difficultyLevel) {
            case 1:
                vRows = 3;
                vColumns = 3;
                eulCircuit = true;
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }

        constructVertices();

        constructCornerEdges();

        constructSideEdges();

    }

    private void endlessMode() {

    }

    public Vertex[] getVertices() {
        return vertexArray;
    }

    public void draw(Canvas canvas) {

        //Draw edges
        for (Edge edge : edgeArrayList) {
            //Log.d(TAG, "Drawing edge " + edge);
            edge.draw(canvas);
        }

        //Draw vertices
        for (Vertex vertex : vertexArray) {
            //Log.d(TAG, "Drawing vertex " + vertex);
            vertex.draw(canvas);
        }
    }

    private boolean isEulCircuit(int weighting) {
        return true;
    }

    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private void constructVertices() {
        int initHoriz;
        int initVert;

        if(vColumns % 2 != 0) initHoriz = 360-(100*(vColumns/2));
        else initHoriz = 360+50-(100*(vColumns/2));
        if(vRows % 2 != 0) initVert = 500-(100*(vRows/2));
        else initVert = 500+50-(100*(vRows/2));

        vertexArray = new Vertex[vRows*vColumns];
        for (int i = 0; i < vRows; i++) {
            for (int j = 0; j < vColumns; j++) {
                int vertexNo = (i * vColumns) + j;
                vertexArray[vertexNo] = new Vertex(initHoriz+(j*100),initVert+(i*100));
            }
        }
    }

    private void constructCornerEdges() {
        //First corner
        randNum = randInt(0, 2);
        origin = 0;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Second corner
        randNum = randInt(0, 2);
        origin = vColumns-1;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin*2));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + ((origin*2)+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin*2));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + ((origin*2)+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Third corner
        randNum = randInt(0, 2);
        origin = (vRows-1)*vColumns;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Fourth corner
        randNum = randInt(0, 2);
        origin = (vRows*vColumns)-1;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }
    }

    private void constructSideEdges() {
        //THIS IS DISGUSTING CODE
        int origin;

        //Top sides
        for (int i = 1; i < vColumns - 1; i++) {
            origin = i;
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1], vertexArray[origin + 1]};
            //Either 2 or 4 edges wanted for Euler path
            if (vertexArray[origin].numConnected() > 2)
                randNum = 4;
            else randNum = (randInt(1, 2)) * 2;
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(adjVertices[randNum2].isNotConnected(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Left sides
        for (int i = 1; i < vRows - 1; i++) {
            origin = vColumns*i;
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin + 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1]};
            //Either 2 or 4 edges wanted for Euler path
            if (vertexArray[origin].numConnected() > 2)
                randNum = 4;
            else randNum = (randInt(1, 2)) * 2;
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(adjVertices[randNum2].isNotConnected(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Right sides
        for (int i = 1; i < vRows - 1; i++) {
            origin = (vColumns*i)+(vRows-1);
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns - 1], vertexArray[origin - 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns]};
            //Either 2 or 4 edges wanted for Euler path
            if (vertexArray[origin].numConnected() > 2)
                randNum = 4;
            else randNum = (randInt(1, 2)) * 2;
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(adjVertices[randNum2].isNotConnected(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Bottom sides
        for (int i = (vColumns*(vRows - 1)) + 1; i < (vColumns*vRows) - 1; i++) {
            origin = i;
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin - vColumns - 1], vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin + 1]};


            int locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            if(vertexArray[origin].numConnected() == 0)
                if(5 - locked < 2)
                    randNum = 0;
                else randNum = 2;
            else if(vertexArray[origin].numConnected() % 2 == 1)
                    randNum = vertexArray[origin].numConnected() + 1;
            else if (locked < 4)
                randNum = 4;
            else randNum = 2;
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(adjVertices[randNum2].isNotConnected(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }

                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

    }

    private void constructInnerEdges() {
        int origin;
        int locked;
        for (int i = 1; i < vRows-1; i++) {
            for (int j = 1; j < vColumns-1; j++) {
                origin = (i*vColumns) + j;
                Vertex[] adjVertices = {vertexArray[origin - vColumns - 1], vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin - 1],
                                        vertexArray[origin + 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1]};
                locked = 0;
                for (int v = 0; v < 8; v++) {
                    if(adjVertices[v].isLocked()) {
                        locked++;
                    }
                }
                if(locked == 8 && !eulCircuit) {
                    //Draw completely random line (just make sure it doesn't already exist)
                }
            }
        }
    }
}


