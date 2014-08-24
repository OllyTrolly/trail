package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RatingBar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Graph {

    private static final String TAG = Graph.class.getSimpleName(); //Define the tag for logging
    public boolean generatingGraph;
    private int difficultyLevel; //Minimum value 1, maximum value 5
    private int vRows = 0; //Minimum value 3, maximum value provisionally 6
    private int vColumns = 0; //Minimum value 3, maximum value provisionally 6
    private boolean eulCircuit = true; //True if Euler circuit, false if non-circuit Euler trail
    private ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();
    private Vertex[] vertexArray;
    private int edgeCount;
    public Score score;
    private Timer timer;
    public int stageNo;
    public int gameMode;
    private Typeface tf;
    private int randNum;
    private int origin;
    private int limit;
    private int locked;
    private int panelWidth;
    private int panelHeight;
    private int centreHoriz;
    private int centreVert;
    private int vertexSpacing;
    private int initHoriz;
    private int initVert;
    private Vertex selectedVertex;
    private Stack<Edge> selectedEdges = new Stack<Edge>();
    private boolean vertexSelected = false;
    private int activated = 0;
    public int timerSecs;
    public boolean constructComplete = false;
    private int penalty = 0; //Number of penalties incurred, for use in endless mode's scoring

    public Graph(int gameMode, int stageNo, int panelWidth, int panelHeight, Typeface tf) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.tf = tf;
        this.stageNo = stageNo;
        this.gameMode = gameMode;

        centreHoriz = panelWidth / 2;
        centreVert = panelHeight / 2;
        vertexSpacing = (panelWidth * 18) / 100;

        score = new Score(tf, panelWidth, panelHeight);
    }

    public void pauseTimer() {
        if (gameMode == 0) {
            timer.pauseTimer();
        }
    }

    public void resumeTimer() {
        if (gameMode == 0) {
            timer.resumeTimer();
        }
    }

    public void constructStage() {

        int tries = 0;
        constructComplete = false;
        vertexSelected = false;
        generatingGraph = true;
        activated = 0;
        penalty = 0;

        while(!constructComplete) {
            edgeArrayList.clear();
            if (gameMode == 0) {
                Log.d(TAG, "Creating new timed stage");
                timedMode();
                tries++;
            }
            if (gameMode == 1) {
                Log.d(TAG, "Creating new endless stage");
                endlessMode();
                tries++;
            }
        }
        generatingGraph = false;
        Log.d(TAG, "Created new stage after " + tries + " tries");
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
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 3;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 3;
                }
                eulCircuit = true;
                break;
            case 3:
                vRows = 4;
                vColumns = 4;
                eulCircuit = true;
                break;
            case 4:
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 5;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 5;
                }
                eulCircuit = true;
                break;
            case 5:
                vRows = 5;
                vColumns = 5;
                eulCircuit = true;
                break;
        }

        Log.d(TAG, "Number of rows: " + vRows  + ", number of columns: " + vColumns + ", is an Euler circuit: " + eulCircuit);

        constructVertices();
        constructCornerEdges();

        if(!constructSideEdges()) {
            return;
        }

        if(!constructInnerEdges()) {
            return;
        }

        if(!eulCircuit) {
            addNonCircuitEdge();
        }

        edgeCount = edgeArrayList.size();
        timerSecs = vRows * vColumns * 3;

        if(!checkConnectedness()) {
            return;
        }

        timer = new Timer(timerSecs, tf, panelWidth, panelHeight);
        constructComplete = true;
    }

    public void endlessMode() {

        if(stageNo <= 10)
            difficultyLevel = 1;
        else if (stageNo <= 20)
            difficultyLevel = 2;
        else if (stageNo <= 30)
            difficultyLevel = 3;
        else if (stageNo <= 40)
            difficultyLevel = 4;
        else
            difficultyLevel = 5;

        switch (difficultyLevel) {
            case 1:
                vRows = 3;
                vColumns = 3;
                eulCircuit = true;
                break;
            case 2:
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 3;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 3;
                }
                if(randInt(1, 100) >= 5) eulCircuit = false;
                else eulCircuit = true;
                break;
            case 3:
                vRows = 4;
                vColumns = 4;
                if(randInt(1, 100) >= 10) eulCircuit = false;
                else eulCircuit = true;
                break;
            case 4:
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 5;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 5;
                }
                if(randInt(1, 100) >= 15) eulCircuit = false;
                else eulCircuit = true;
                break;
            case 5:
                vRows = 5;
                vColumns = 5;
                eulCircuit = true;
                if(randInt(1, 100) >= 20) eulCircuit = false;
                else eulCircuit = true;
                break;
        }

        //eulCircuit = false;

        constructVertices();
        constructCornerEdges();

        if(!constructSideEdges()) {
            return;
        }

        if(!constructInnerEdges()) {
            return;
        }

        if(!eulCircuit) {
            addNonCircuitEdge();
        }

        edgeCount = edgeArrayList.size();

        if(!checkConnectedness()) {
            return;
        }

        constructComplete = true;
    }

    public void draw(Canvas canvas) {

        //Draw edges
        if(constructComplete) {
            try {
                for (Edge edge : edgeArrayList) {
                    //Log.d(TAG, "Drawing an edge");
                    edge.draw(canvas);
                }

                //Draw vertices
                for (Vertex vertex : vertexArray) {
                    vertex.draw(canvas);
                }
            }

            catch(ConcurrentModificationException e) {
                Log.d(TAG, "CONCURRENCY EXCEPTION CAUGHT");
            }

            if(gameMode == 0) {
                timer.draw(canvas);

                if (timer.timeLeft <= 0) {
                    //QUIT TO MENU
                }
            }

            score.draw(canvas);
        }
    }

    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private void constructVertices() {

        if(vColumns % 2 != 0) initHoriz = centreHoriz - (vertexSpacing * (vColumns / 2));
        else initHoriz = centreHoriz + (vertexSpacing / 2) - (vertexSpacing * (vColumns / 2));
        if(vRows % 2 != 0) initVert = centreVert + ((panelHeight * 5) / 100) - (vertexSpacing * (vRows / 2));
        else initVert = centreVert + (vertexSpacing / 2) + ((panelHeight * 5) / 100) - (vertexSpacing * (vRows / 2));

        vertexArray = new Vertex[vRows*vColumns];
        for (int i = 0; i < vRows; i++) {
            for (int j = 0; j < vColumns; j++) {
                int vertexNo = (i * vColumns) + j;
                vertexArray[vertexNo] = new Vertex(initHoriz+(j * vertexSpacing),initVert+(i * vertexSpacing), panelWidth, panelHeight);
            }
        }
    }

    private void constructCornerEdges() {
        //First corner
        randNum = randInt(0, 2);
        origin = 0;
        switch (randNum) {
            case 0:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Second corner
        randNum = randInt(0, 2);
        origin = vColumns-1;
        switch (randNum) {
            case 0:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin*2));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + ((origin*2)+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin*2));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + ((origin*2)+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Third corner
        randNum = randInt(0, 2);
        origin = (vRows-1)*vColumns;
        switch (randNum) {
            case 0:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Fourth corner
        randNum = randInt(0, 2);
        origin = (vRows*vColumns)-1;
        switch (randNum) {
            case 0:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                //Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                //Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }
    }

    private boolean constructSideEdges() {
        int origin;

        //Top sides
        for (int i = 1; i < vColumns - 1; i++) {
            origin = i;
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1], vertexArray[origin + 1]};
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            //Either 2 or 4 edges wanted for Euler path
            while(true) {
                randNum = (randInt(1,2)) * 2;
                if((5-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            //Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            //Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            //Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                //Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            //Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            //Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        //Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            //Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Left sides
        for (int i = 1; i < vRows - 1; i++) {
            origin = vColumns*i;
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin + 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1]};
            //Either 2 or 4 edges wanted for Euler path
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            //Either 2 or 4 edges wanted for Euler path
            while(true) {
                randNum = (randInt(1,2)) * 2;
                if((5-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            //Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            //Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            //Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                //Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            //Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            //Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        //Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            //Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Right sides
        for (int i = 1; i < vRows - 1; i++) {
            origin = (vColumns*(i+1))-1;
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns - 1], vertexArray[origin - 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns]};
            //Either 2 or 4 edges wanted for Euler path
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            //Either 2 or 4 edges wanted for Euler path
            while(true) {
                randNum = (randInt(1,2)) * 2;
                if((5-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            //Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            //Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            //Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                //Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            //Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            //Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        //Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            //Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Bottom sides
        for (int i = (vColumns*(vRows - 1)) + 1; i < (vColumns*vRows) - 1; i++) {
            //Log.d(TAG, "i is " + i);
            origin = i;
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin - vColumns - 1], vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin + 1]};
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if (adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            if (vertexArray[origin].numConnected() == 0) {
                Log.d(TAG, "Regenerating graph due to no connections to bottom vertex");
                return false;
            }
            //Log.d(TAG, "Origin is " + origin);
            while (true) {
                randNum = (randInt(1, 2)) * 2;
                //Log.d(TAG, "Attempted randNum is " + randNum);
                if ((5 - locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            //Log.d(TAG, "Final randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            //Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            //Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                //Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if (!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            //Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        } else {
                            //Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    } else {
                        //Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            //Log.d(TAG, "Locking vertex " + origin);
            vertexArray[origin].setLocked();
        }

        return true;
    }

    private boolean constructInnerEdges() {
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

                Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
                if(vertexArray[origin].numConnected() == 0 &&  locked > 6) {
                    return false;
                }

                while(true) {
                    randNum = (randInt(1,4)) * 2;
                    if((8-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                        break;
                }

                limit = randNum - vertexArray[origin].numConnected();
                Log.d(TAG, "Limit is " + limit);
                for (int k = 0; k < limit; k++) {
                    Log.d(TAG, "On iteration " + k);
                    while (true) {
                        //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                        int randNum2 = randInt(0, 7);
                        if (!(adjVertices[randNum2].isLocked())) {
                            if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                                //Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
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

        return true;
    }

    private void addNonCircuitEdge() {

        Log.d(TAG, "Adding non-circuit edge");
        outerloop:
        while(true) {
            //Find random vertex
            randNum = randInt(0, (vRows * vColumns) - 1);
            //If there aren't already 8 connections to that random vertex, proceed to attempt drawing an edge to random adjacent vertex
            if(vertexArray[randNum].numConnected() != 8) {
                while (true) {
                    //Top left corner
                    if(randNum == 0) {
                        int[] adjVertices = new int[] {
                                1,
                                vColumns,
                                vColumns + 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                Log.d(TAG, "Drawing edge from vertex " + randNum + " to vertex " + (randNum + adjVertices[randNum2]));
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Top right corner
                    else if(randNum == vColumns - 1) {
                        int[] adjVertices = new int[] {
                                - 1,
                                vColumns - 1,
                                vColumns
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                Log.d(TAG, "Drawing edge from vertex " + randNum + " to vertex " + (randNum + adjVertices[randNum2]));
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Bottom left corner
                    else if(randNum == vColumns * (vRows - 1)) {
                        int[] adjVertices = new int[] {
                                - vColumns,
                                - vColumns + 1,
                                1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                Log.d(TAG, "Drawing edge from vertex " + randNum + " to vertex " + (randNum + adjVertices[randNum2]));
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Bottom right corner
                    else if(randNum == (vColumns * vRows) - 1) {
                        int[] adjVertices = new int[] {
                                - vColumns - 1,
                                - vColumns,
                                - 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                Log.d(TAG, "Drawing edge from vertex " + randNum + " to vertex " + (randNum + adjVertices[randNum2]));
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Left side vertex
                    else if(randNum % vColumns == 0) {
                        int[] adjVertices = new int[] {
                                - vColumns,
                                - vColumns + 1,
                                1,
                                vColumns,
                                vColumns + 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 4);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                Log.d(TAG, "Drawing edge from vertex " + randNum + " to vertex " + (randNum + adjVertices[randNum2]));
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Right side vertex
                    else if((randNum + 1) % vColumns == 0) {
                        int[] adjVertices = new int[] {
                                - vColumns - 1,
                                - vColumns,
                                - 1,
                                vColumns - 1,
                                vColumns
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 4);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                Log.d(TAG, "Drawing edge from vertex " + randNum + " to vertex " + (randNum + adjVertices[randNum2]));
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    else {
                        int[] adjVertices = new int[] {
                                - vColumns - 1,
                                - vColumns,
                                - vColumns + 1,
                                - 1,
                                1,
                                vColumns - 1,
                                vColumns,
                                vColumns + 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 7);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                Log.d(TAG, "Drawing edge from vertex " + randNum + " to vertex " + (randNum + adjVertices[randNum2]));
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }

                }
            }
        }
    }

    private boolean traverseGraph(int root, int dest) {
        //ArrayList for all the visited vertices
        ArrayList<Integer> visited = new ArrayList<Integer>();
        visited.add(new Integer(root));
        //Stack for parents to get children of
        Stack<Integer> parents = new Stack<Integer>();
        parents.add(new Integer(root));
        //An array of the integer modifiers for adjacent vertices
        int[] adjVertices = new int[] {
                - vColumns - 1,
                - vColumns,
                - vColumns + 1,
                - 1,
                1,
                vColumns - 1,
                vColumns,
                vColumns + 1
        };

        //While the parents stack still has children to check, carry on
        while(!parents.empty()) {
            //Pop parent off the stack(in int form)
            origin = parents.pop().intValue();
            //For all adjacent vertices (always size 8)
            for(int i = 0; i < 8; i++) {
                //If the parent is connected to the adjacent vertex, proceed
                if(connectedTo(origin + adjVertices[i])) {
                    //If the child is our destination, return true
                    if(origin + adjVertices[i] == dest) {
                        return true;
                    }
                    //Else if child has not yet been visited, add it to parents stack
                    else if(!visited.contains(new Integer(origin + adjVertices[i]))) {
                        parents.add(new Integer(origin + adjVertices[i]));
                        visited.add(new Integer(origin + adjVertices[i]));
                    }
                }
            }
        }

        return false;
    }

    private boolean checkConnectedness() {
        //Top left to bottom right
        if(!traverseGraph(0, (vColumns * vRows) - 1)) return false;
        //Top right to bottom left
        if(!traverseGraph(vColumns - 1, vColumns * (vRows - 1))) return false;
        //Top to bottom 1
        if(!traverseGraph(vColumns - 2, (vColumns * vRows) - 2)) return false;
        //Top to bottom 2
        if(!traverseGraph(1, (vColumns * (vRows - 1)) + 1)) return false;
        //Left to right 1
        if(!traverseGraph(vColumns, (vColumns * 2) - 1)) return false;
        //Left to right 2
        if(!traverseGraph(vColumns * (vRows - 2), (vColumns * (vRows - 1) - 1))) return false;

        return true;
    }

    private boolean connectedTo(int vertInt) {
        try {
            if(vertexArray[origin].isConnectedTo(vertexArray[vertInt]))
                return true;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    private boolean vertexSelection(Vertex vertex1, Vertex vertex2, int eventX, int eventY) {
        if((eventX >= (vertex1.getX() - vertex1.getR() - vertex1.getH()) &&
                (eventX <= (vertex2.getX() + vertex2.getR() + vertex2.getH())))
                && (eventY >= (vertex1.getY() - vertex1.getR() - vertex1.getH()) &&
                (eventY <= vertex2.getY() + vertex2.getR() + vertex2.getH()))) {
            return true;
        }
        return false;
    }

    private boolean vertexSelection(Vertex vertex, int eventX, int eventY) {
        if((eventX >= (vertex.getX() - vertex.getR() - vertex.getH()) &&
                (eventX <= (vertex.getX() + vertex.getR() + vertex.getH())))
                && (eventY >= (vertex.getY() - vertex.getR() - vertex.getH()) &&
                (eventY <= vertex.getY() + vertex.getR() + vertex.getH()))) {
            return true;
        }
        return false;
    }

    public void reset() {
        for (Edge edge : edgeArrayList) {
            edge.toggleActivation(false);
            edge.lastSelected(false);
        }

        for (Vertex vertex : vertexArray) {
            vertex.toggleActivation(false);
            vertex.lastSelected(false);
        }

        activated = 0;
        vertexSelected = false;
        selectedEdges.clear();
        edgeCount = edgeArrayList.size();
        penalty += 5;
    }

    public boolean modeFinished() {
        if(gameMode == 0)
            if(stageNo > 10)
                return true;

        return false;
    }

    public void handleActionDown(int eventX, int eventY) {
        //Check if touch is in the bounds of the graph
        if(activated == 0) {
            if (vertexSelection(vertexArray[0], vertexArray[(vRows*vColumns)-1], eventX, eventY)) {
                //Check the row that has been touched
                for (int i = 0; i < vColumns; i++) {
                    if (eventX >= (initHoriz + (i * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH()) && eventX <= (initHoriz + (i * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                        //Log.d(TAG, "Touched vertex in column " + (i + 1));
                        for (int j = 0; j < vRows; j++) {
                            if (eventY >= (initVert + (j * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH()) && eventY <= (initVert + (j * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                                //Log.d(TAG, "Touched vertex in row " + (j + 1));
                                //Log.d(TAG, "Selecting vertex");
                                vertexArray[(j * vColumns) + i].toggleActivation(true);
                                vertexArray[(j * vColumns) + i].lastSelected(true);
                                selectedVertex = vertexArray[(j * vColumns) + i];
                                vertexSelected = true;
                                activated++;
                                return;
                            }
                        }
                        return;
                    }
                }
                return;
            }
        }

        else if(vertexSelection(selectedVertex, eventX, eventY)) {
            vertexSelected = true;
        }
    }

    public void handleActionMove(int eventX, int eventY) {
        if (edgeCount > 0) {
            if (vertexSelected) {
                for (int i = 0; i < vColumns; i++) {
                    if (eventX >= (initHoriz + (i * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH()) && eventX <= (initHoriz + (i * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                        //Log.d(TAG, "Touched vertex in column " + (i + 1));
                        for (int j = 0; j < vRows; j++) {
                            if (eventY >= (initVert + (j * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH()) && eventY <= (initVert + (j * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                                //Check for adjacency to currently selected vertex
                                if (selectedVertex.isConnectedTo(vertexArray[(j * vColumns) + i])) {
                                    if (!selectedVertex.getEdge(vertexArray[(j * vColumns) + i]).isActivated()) {
                                        //Log.d(TAG, "Touched vertex in row " + (j + 1));
                                        //Log.d(TAG, "Selecting vertex and previous edge");
                                        selectedVertex.lastSelected(false);
                                        if(!selectedEdges.empty()) {selectedEdges.peek().lastSelected(false);}
                                        selectedEdges.push(selectedVertex.getEdge(vertexArray[(j * vColumns) + i]));
                                        selectedVertex = vertexArray[(j * vColumns) + i];
                                        selectedEdges.peek().toggleActivation(true);
                                        selectedEdges.peek().lastSelected(true);
                                        selectedVertex.toggleActivation(true);
                                        selectedVertex.lastSelected(true);
                                        vertexSelected = true;
                                        edgeCount--;
                                    }

                                    else if(selectedEdges.peek() == selectedVertex.getEdge(vertexArray[(j * vColumns) + i])) {
                                        //Log.d(TAG, "Deselecting vertex and previous edge");
                                        selectedEdges.peek().toggleActivation(false);
                                        selectedEdges.pop().lastSelected(false);
                                        if(!selectedEdges.empty()) {
                                            selectedEdges.peek().toggleActivation(true);
                                            selectedEdges.peek().lastSelected(true);
                                        }
                                        selectedVertex.lastSelected(false);
                                        if(!selectedVertex.hasActiveEdge()) selectedVertex.toggleActivation(false);
                                        selectedVertex = vertexArray[(j * vColumns) + i];
                                        selectedVertex.lastSelected(true);
                                        selectedVertex.toggleActivation(true);
                                        vertexSelected = true;
                                        edgeCount++;
                                        penalty++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean stageFinished() {
        if(selectedEdges.empty()) {
            if(vertexSelected) {
                selectedVertex.lastSelected(false);
                selectedVertex.toggleActivation(false);
                activated = 0;
            }
        }

        vertexSelected = false;

        if(edgeCount == 0) {
            stageNo++;

            if(gameMode == 0) {
                if(timer.getTimeLeft() > 0) {
                    score.addToScore((long) timer.getTimeLeft() * 100);
                }
            }

            else if(gameMode == 1) {
                if((vRows*vColumns) - penalty > 0) {
                    score.addToScore((long) ((vRows*vColumns) - penalty)*100);
                }
            }

            constructComplete = false;
            return true;
        }

        return false;
    }

}


